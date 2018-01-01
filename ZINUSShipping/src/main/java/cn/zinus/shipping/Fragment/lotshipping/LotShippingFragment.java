package cn.zinus.shipping.Fragment.lotshipping;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Adapter.LotShippingListViewAdapter;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.JaveBean.LotData;
import cn.zinus.shipping.JaveBean.LotShippingListData;
import cn.zinus.shipping.JaveBean.PODate;
import cn.zinus.shipping.JaveBean.ShippingPlanData;
import cn.zinus.shipping.JaveBean.TagInfoData;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;
import cn.zinus.shipping.util.SoundUtil;

import static cn.zinus.shipping.util.Constant.INVALID;
import static cn.zinus.shipping.util.Constant.RFIDSCAN;
import static cn.zinus.shipping.util.Constant.UPDATEUI;
import static cn.zinus.shipping.util.Constant.VALID;
import static cn.zinus.shipping.util.Constant.VALIDSTATE;
import static cn.zinus.shipping.util.Utils.showToast;
import static com.micube.control.util.Server.hexStringToString;

/**
 * Created by Spring on 2017/2/18.
 */

public class LotShippingFragment extends KeyDownFragment implements View.OnClickListener {

    //region ◆ 변수(Variables)
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式

    //上下文
    private MainNaviActivity mContext;
    //popwindow
    private ImageView mivchoose;
    private PopupWindow mpopChooseBorR;
    private View mViewChooseBorR;
    private PopupWindow mpopFixQty;
    private View mViewFixQty;
    //ListView
    private ListView mlvLotShhipping;
    private LotShippingListViewAdapter mLotShippingListViewAdapter;
    private ArrayList<LotShippingListData> mLotShippingDataList;
    //这个画面对应的PO信息
    private PODate mPODate;
    //这个画面的计划信息
    private ShippingPlanData plandata;
    //这个记录了这个po下面所有的lot,已经在数据库里保存装车的validstate为valid，没有的为invalid
    private ArrayList<LotData> mLotDataList;
    //condition
    //扫描标签个数
    private TextView tvtagqty;
    //已经上车总数
    private TextView tvlotshippingqty;
    //计划上车总数
    private TextView tvlotshippingPlanqty;
    //上车计划号
    private TextView tvshippingPlanNo;
    //计划序号
    private TextView tvShippingPlanSeq;
    //货柜序号
    private TextView tvContainerSeq;
    //PO号
    private TextView tvPoNo;
    //记录移除的tag
    private ArrayList<LotShippingListData> removeData = new ArrayList<>();
    //
    private EditText etTagID;
    private int BRFlag = 1;
    private boolean threadStop = true;
    private Thread thread;
    //Auto Read
    Handler handler = null;
    boolean scanFlag = false;
    public Barcode2DWithSoft BaecodeReader;
    private ProgressDialog myDialog;
    private boolean dismessDialogFlag = false;
    //数据库
    MyDateBaseHelper mHelper;
    private SQLiteDatabase db;
    //endregion

    //region ◆ 생성자(Creator)

    //region onCreate
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //用来响应toolbar的单击事件的,配合onOptionsItemSelected
        mContext = (MainNaviActivity) getActivity();
        setHasOptionsMenu(true);
        mHelper = DBManger.getIntance(mContext);
        db = mHelper.getWritableDatabase();
    }
    //endregion

    //region onCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lotshipping, container, false);
    }
    //endregion

    //region onActivityCreated
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initview();
        BaecodeReader = mContext.mBarcode2DWithSoft;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case RFIDSCAN:
                        String tagid = msg.obj + "";
                        String tagID = hexStringToString(tagid);
                        Log.e("转化为字符串", tagID);
                        if (!tagID.equals("")) {
                            checkAndSearchWeb(tagID);
                        }
                        break;
                    case UPDATEUI:
                        tvlotshippingqty.setText(getShippingQty() + "");
                        Log.e("TAGgeshu ", mLotShippingDataList.size() + "");
                        tvtagqty.setText(mLotShippingDataList.size() + "");
                        mlvLotShhipping.setSelection((Integer) msg.obj);
                        break;
                }
            }
        };
    }

    //endregion

    //region onPause
    @Override
    public void onPause() {
        super.onPause();
        if (BaecodeReader != null) {
            BaecodeReader.stopScan();
        }
    }
    //endregion

    //region onHiddenChanged(切换Fragment时触发的方法)
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (BaecodeReader != null) {
            BaecodeReader.stopScan();
        }
    }
    //endregion

    //region onClick Event

    //region 按钮单击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_choose:
                //选择是RFID还是Barcode的imageView
                Button btnRFID = (Button) mViewChooseBorR.findViewById(R.id.btnrfid);
                Button btnBarCode = (Button) mViewChooseBorR.findViewById(R.id.btnbarcode);
                btnRFID.setOnClickListener(this);
                btnBarCode.setOnClickListener(this);
                mpopChooseBorR = new PopupWindow(mViewChooseBorR, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                mpopChooseBorR.setBackgroundDrawable(new ColorDrawable(0x00000000));
                mpopChooseBorR.setTouchable(true);
                mpopChooseBorR.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                mpopChooseBorR.showAsDropDown(v);
                break;
            case R.id.btnrfid:
                //选择R&B的popupwindow里面的RFID按钮
                BRFlag = 1;
                //  RgInventory.setVisibility(View.VISIBLE);
                showToast(mContext, "RFID", 0);
                mivchoose.setImageResource(R.drawable.rfidicon);
                if (BaecodeReader != null) {
                    BaecodeReader.stopScan();
                }
                mpopChooseBorR.dismiss();
                break;
            case R.id.btnbarcode:
                //选择R&B的popupwindow里面的Barcode按钮
                //   RgInventory.setVisibility(View.GONE);
                showToast(mContext, "Barcode", 0);
                BRFlag = 2;
                mivchoose.setImageResource(R.drawable.barcodeicon);
                mpopChooseBorR.dismiss();
                break;
        }
    }
    //endregion

    //endregion

    //region onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                actionSearch();
                break;
            case R.id.action_save:
                //save的业务代码
                saveShipping();
                break;
            case R.id.action_ClearAll:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //endregion

    //region ◆ Function

    //region initData
    private void initData() {
        mLotDataList = new ArrayList<>();
        mLotShippingDataList = new ArrayList<>();
        plandata = new ShippingPlanData();
        mPODate = new PODate();
        removeData = new ArrayList<>();
    }
    //endregion

    //region initView()
    private void initview() {
        etTagID = (EditText) getView().findViewById(R.id.etTagID);
        tvtagqty = (TextView) getView().findViewById(R.id.tvtagqty);
        tvlotshippingqty = (TextView) getView().findViewById(R.id.tvshippedqty);
        tvlotshippingPlanqty = (TextView) getView().findViewById(R.id.tvshipplanqty);
        tvshippingPlanNo = (TextView) getView().findViewById(R.id.tv_shippingPlanNo);
        tvPoNo = (TextView) getView().findViewById(R.id.tv_PoNo);
        tvShippingPlanSeq = (TextView) getView().findViewById(R.id.tv_ShippingPlanSeq);
        tvContainerSeq = (TextView) getView().findViewById(R.id.tv_ContainerSeq);
        mivchoose = (ImageView) getView().findViewById(R.id.iv_choose);
        mivchoose.setOnClickListener(this);
        mViewChooseBorR = mContext.getLayoutInflater().inflate(R.layout.chooseborr, null);
        //finqty popupwindow
        mViewFixQty = mContext.getLayoutInflater().inflate(R.layout.fixqty, null);
        //listViewDataList   **
        mlvLotShhipping = (ListView) getView().findViewById(R.id.lv_consumeLotInbound);
        mLotShippingListViewAdapter = new LotShippingListViewAdapter(mContext, mLotShippingDataList);
        mlvLotShhipping.setAdapter(mLotShippingListViewAdapter);
        mlvLotShhipping.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (mLotShippingDataList.get(position).getLOTID() != null) {
                    AlertDialog prompt = new AlertDialog.Builder(mContext).
                            setTitle(getString(R.string.prompt)).
                            setMessage(getString(R.string.removeshipping)).
                            setCancelable(true).
                            setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).
                            setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    /**
                                     * 移除装车
                                     */
                                    //给移除tag的list增加项目
                                    removeData.add(mLotShippingDataList.get(position));
                                    //修改记录lot的list相应的项，validstate改成invalid
                                    String removelotid = mLotShippingDataList.get(position).getLOTID();
                                    for (int i = 0; i < mLotDataList.size(); i++) {
                                        LotData data = mLotDataList.get(i);
                                        if (removelotid.equals(data.getLOTID())) {
                                            data.setVALIDSTATE(INVALID);
                                            mLotDataList.set(i, data);
                                            break;
                                        }
                                    }
                                    //画面listview的list中移除一项
                                    mLotShippingDataList.remove(position);
                                    mLotShippingListViewAdapter.notifyDataSetChanged();
                                    Message message = new Message();
                                    message.what = UPDATEUI;
                                    message.obj = position;
                                    handler.sendMessage(message);
                                }
                            }).create();
                    prompt.show();
                }
                return false;
            }
        });
    }

    //endregion

    //region getLotShippingByShippingPlan,点击上一个TAB的listview修改这个tab的方法

    public void getLotShippingByPO(ShippingPlanData shippingPlanData, PODate poDate) {
        //先把lot表里符合条件的lot搜出来放到一个全局变量中
        //再搜shippinglot，找出对应货柜顺序已经装车的lot，显示在画面中
        //把公共的数据显示到画面的下方shippingPlanData
        plandata = shippingPlanData;
        mPODate = poDate;
        tvshippingPlanNo.setText(shippingPlanData.getSHIPPINGPLANNO());
        tvPoNo.setText(poDate.getPOID());
        tvlotshippingPlanqty.setText(poDate.getPOQTY());
        tvShippingPlanSeq.setText(poDate.getSHIPPINGPLANSEQ());
        tvContainerSeq.setText(poDate.getCONTAINERSEQ());
        //搜索符合条件的lot,并放入mLotDataList，已经保存装车的为valid，没有的为invalid
        mLotDataList = new ArrayList<>();
        String selectNoShippinglOTsql = String.format(getString(R.string.GetlOTNoShippingQuery), mPODate.getPOID());
        Log.e("sql语句查po对应的没有装车的lot", selectNoShippinglOTsql);
        Cursor cursorNoShippingLotDatalist = DBManger.selectDatBySql(db, selectNoShippinglOTsql, null);
        if (cursorNoShippingLotDatalist.getCount() != 0) {
            while (cursorNoShippingLotDatalist.moveToNext()) {
                LotData mLotData = new LotData();
                mLotData.setLOTID(cursorNoShippingLotDatalist.getString(cursorNoShippingLotDatalist.getColumnIndex(Constant.LOTID)));
                mLotData.setRFID(cursorNoShippingLotDatalist.getString(cursorNoShippingLotDatalist.getColumnIndex(Constant.RFID)));
                mLotData.setQTY(cursorNoShippingLotDatalist.getString(cursorNoShippingLotDatalist.getColumnIndex(Constant.QTY)));
                mLotData.setTRACKOUTTIME(cursorNoShippingLotDatalist.getString(cursorNoShippingLotDatalist.getColumnIndex(Constant.TRACKOUTTIME)));
                mLotData.setPRODUCTDEFID(mPODate.getPRODUCTDEFID());
                mLotData.setPRODUCTDEFNAME(mPODate.getPRODUCTDEFNAME());
                mLotData.setPURCHASEORDERID(mPODate.getPOID());
                mLotData.setVALIDSTATE(INVALID);
                Log.e("没有装车的lot数据", mLotData.toString());
                mLotDataList.add(mLotData);
            }
        }

        String selectlOTsql = String.format(getString(R.string.GetlOTShippingQuery), mPODate.getPOID());
        Log.e("sql语句查po对应的已经装车的lot", selectlOTsql);
        Cursor cursorLotDatalist = DBManger.selectDatBySql(db, selectlOTsql, null);
        if (cursorLotDatalist.getCount() != 0) {
            while (cursorLotDatalist.moveToNext()) {
                LotData mLotData = new LotData();
                mLotData.setLOTID(cursorLotDatalist.getString(cursorLotDatalist.getColumnIndex(Constant.LOTID)));
                mLotData.setRFID(cursorLotDatalist.getString(cursorLotDatalist.getColumnIndex(Constant.RFID)));
                mLotData.setQTY(cursorLotDatalist.getString(cursorLotDatalist.getColumnIndex(Constant.QTY)));
                mLotData.setTRACKOUTTIME(cursorLotDatalist.getString(cursorLotDatalist.getColumnIndex(Constant.TRACKOUTTIME)));
                mLotData.setPRODUCTDEFID(mPODate.getPRODUCTDEFID());
                mLotData.setPRODUCTDEFNAME(mPODate.getPRODUCTDEFNAME());
                mLotData.setPURCHASEORDERID(mPODate.getPOID());
                mLotData.setVALIDSTATE(VALID);
                Log.e("已经装车的lot数据", mLotData.toString());
                mLotDataList.add(mLotData);
            }
        }
        //搜索已经装车的lot，并显示在画面上
        String selectDataListsql = String.format(getString(R.string.GetlOTShipedQuery),
                shippingPlanData.getSHIPPINGPLANNO(), mPODate.getSHIPPINGPLANSEQ(), mPODate.getCONTAINERSEQ(),mPODate.getPOID());
        Log.e("搜索已经装车的lot", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mLotShippingDataList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                LotShippingListData lotShippingListData = new LotShippingListData();
                lotShippingListData.setLOTID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.LOTID)));
                lotShippingListData.setINQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.QTY)));
                lotShippingListData.setPRODUCTDEFID(mPODate.getPRODUCTDEFID());
                lotShippingListData.setPRODUCTDEFNAME(mPODate.getPRODUCTDEFNAME());
                lotShippingListData.setTRACKOUTTIME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.TRACKOUTTIME)));
                lotShippingListData.setVALIDSTATE(VALID);
                Log.e("搜索已经装车的lot数据", lotShippingListData.toString());
                mLotShippingDataList.add(lotShippingListData);
            }
        }
        tvtagqty.setText(mLotShippingDataList.size() + "");
        mLotShippingListViewAdapter.notifyDataSetChanged();
        Message message = new Message();
        message.what = UPDATEUI;
        message.obj = 0;
        handler.sendMessage(message);
    }
    //endregion

    //region TagScan
    private void TagScan() {
        if (BRFlag == 1) {
            if (!scanFlag) {
                scanFlag = true;
                if (mContext.mRFIDWithUHF.startInventoryTag((byte) 0, (byte) 0)) {
                    myDialog = new ProgressDialog(mContext);
                    myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    myDialog.setMessage(getString(R.string.Scaning));
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.setCancelable(false);
                    myDialog.show();
                    myDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == 139) {
                                if (dismessDialogFlag) {
                                    dismessDialogFlag = false;
                                    stopInventory();
                                } else {
                                    dismessDialogFlag = true;
                                }
                            }
                            return false;
                        }
                    });
                    new TagThread(10).start();
                } else {
                    Log.e("扫描出问题了", "扫描出问题了404");
                    mContext.freeUHF();
                    mContext.initUHF();
                    // EventBus.getDefault().post(new Event.LotShippingByShippingPlanEvent(mSelectShippingPlandata));
                    // mContext.mRFIDWithUHF.stopInventory();
                }
            } else {
                stopInventory();
            }
        } else if (BRFlag == 2) {
            readBarcodeTag();
        }
    }
    //endregion

    //region readBarcodeTag

    private void readBarcodeTag() {
        if (BaecodeReader != null) {
            BaecodeReader.setScanCallback(mScanCallback);
        }

        if (threadStop) {
            boolean bContinuous = false;
            thread = new DecodeThread(bContinuous, 100);
            thread.start();

        } else {
            threadStop = true;
        }
    }

    //endregion

    //region checkIsExistInSF_LOT
    //判断扫到的lot是不是这个po所对应的lot
    //mLotDataList存的是
    private int checkIsExistInSF_LOT(String tagid) {
        //初始化返回值为-2，代表着这个id不在这个po里面
        int TagLocation = -2;
        for (int i = 0; i < mLotDataList.size(); i++) {
            if (mLotDataList.get(i).getRFID().equals(tagid)) {
                //没有加入列表的是invalid
                if ((mLotDataList.get(i).getVALIDSTATE().equals(INVALID))) {
                    //返回在list中的位置
                    TagLocation = i;
                } else {
                    TagLocation = -1;
                }
            }
        }
        return TagLocation;
    }
    //endregion

    //region checkAndSearchWeb
    private void checkAndSearchWeb(String tagID) {
        if (Integer.parseInt(tvlotshippingqty.getText().toString()) == Integer.parseInt(tvlotshippingPlanqty.getText().toString())) {
            Log.e("全部完成", "全部完成");
            if (dismessDialogFlag) {
                dismessDialogFlag = false;
                stopInventory();
                showToast(mContext, "所有lot已经完成上车,请保存作业结果!", 0);
            } else {
                dismessDialogFlag = true;
            }
        }
        int lotflag = checkIsExistInSF_LOT(tagID);
        switch (lotflag) {
            //-2是没有找到的，-1是已经加入列表的
            case -2:
                showToast(mContext, tagID + "不是这个计划的lot，请确认", 0);
                break;
            case -1:
                //已经存在了.
                showToast(mContext, tagID + "这个LOT已经装过车了", 0);
                Log.e("已经存在了", "已经存在了");
                break;
            default:
                //没有扫到过这个lot，且lot表里有这个数据
                SoundUtil.play(R.raw.pegconn, 0);
                searchSqlite(lotflag);
                break;
        }
    }

    private void searchSqlite(int lotflag) {
        //修改lot数据的validstate为valid，再扫描的时候就会把它归为已经存放到列表上的数据
        LotData data = mLotDataList.get(lotflag);
        data.setVALIDSTATE(VALID);
        mLotDataList.set(lotflag, data);
        //设置listview的数据
        LotShippingListData listData = new LotShippingListData();
        listData.setLOTID(data.getLOTID());
        listData.setINQTY(data.getQTY());
        listData.setPRODUCTDEFID(data.getPRODUCTDEFID());
        listData.setPRODUCTDEFNAME(data.getPRODUCTDEFNAME());
        listData.setTRACKOUTTIME(data.getTRACKOUTTIME());
        listData.setVALIDSTATE(VALID);
        boolean flag = true;
        for (int i = 0; i < mLotShippingDataList.size(); i++) {
            if (mLotShippingDataList.get(i).getLOTID().equals(listData.getLOTID())) {
                flag = false;
            }
        }
        if (flag) {
            mLotShippingDataList.add(listData);
            mLotShippingListViewAdapter.notifyDataSetChanged();
        }
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        String selectDataListsql = String.format(getString(R.string.GetlOTQtyQuery), PoNo, tagID);
//        Log.e("sql语句GetlOTQtyQuery", selectDataListsql);
//        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
//        if (cursorDatalist.getCount() != 0) {
//            Log.e("查到了", "111111111");
//            while (cursorDatalist.moveToNext()) {
//                LotShippingListData lotShippingListData = new LotShippingListData();
//                lotShippingListData.setLOTID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.LOTID)));
//                lotShippingListData.setINQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.QTY)));
//                for (int i = 0; i < mLotShippingDataList.size(); i++) {
//                    LotShippingListData data = mLotShippingDataList.get(i);
//                    if (data.equals(lotShippingListData)) {
//                        data.setINQTY(lotShippingListData.getINQTY());
//                        data.setCONTAINER(etContainerNo.getText().toString());
//                        data.setSEALNO(etSealNo.getText().toString());
//                        data.setVALIDSTATE(Constant.VALID);
//                        mLotShippingDataList.set(i, data);
//                        mLotShippingListViewAdapter.notifyDataSetChanged();
//                        tvlotshippingqty.setText(getShippingQty() + "");
//                        // mlvLotShhipping.setSelection(0);
////                        Message message = new Message();
////                        message.what = UPDATEUI;
////                        message.obj = 0;
////                        handler.sendMessage(message);
//                    }
//                }
//            }
//        }
    }
    //endregion

    //region actionSearch
    protected void actionSearch() {
        checkAndSearchWeb(etTagID.getText().toString());
    }
    //endregion

    //region actionClearAll
    public void actionClearAll() {
        if (mLotShippingDataList.size() > 0) {
            mLotShippingDataList.clear();
        }
        mLotShippingListViewAdapter.notifyDataSetChanged();
        etTagID.setText("");
        tvtagqty.setText("0");
        tvlotshippingqty.setText("0");
        tvshippingPlanNo.setText("");
        tvlotshippingPlanqty.setText("0");
        tvPoNo.setText("");
        tvShippingPlanSeq.setText("");
        tvContainerSeq.setText("");
        plandata = new ShippingPlanData();
        mPODate = new PODate();
        removeData = new ArrayList<>();
        TagInfoData data = new TagInfoData();
        data.setEnableFlag(true);
        data.setClearFlag(true);
    }
    //endregion

    //region ActionSave
    public void saveShipping() {
        Log.e("保存", "保存");
        //插入SF_SHIPPINGLOT表
        StringBuffer Shipping_insert = new StringBuffer();
        Shipping_insert.append("INSERT OR REPLACE INTO " + Constant.SF_SHIPPINGLOT + "("
                + Constant.SHIPPINGPLANNO + ","
                + Constant.SHIPPINGPLANSEQ + ","
                + Constant.ORDERTYPE + ","
                + Constant.ORDERNO + ","
                + Constant.LINENO + ","
                + Constant.PRODUCTDEFID + ","
                + Constant.PRODUCTDEFVERSION + ","
                + Constant.CONTAINERSEQ + ","
                + Constant.POID + ","
                + Constant.LOTID + ","
                + Constant.QTY + ","
                + Constant.VALIDSTATE + ","
                + Constant.SHIPPINGDATE + ")");
        Shipping_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
        Date now = new Date();
        for (int i = 0; i < mLotShippingDataList.size(); i++) {
            if (mLotShippingDataList.get(i).getVALIDSTATE() != null) {
                SQLiteStatement statement = db.compileStatement(Shipping_insert.toString());
                statement.bindString(1, plandata.getSHIPPINGPLANNO());
                statement.bindString(2, mPODate.getSHIPPINGPLANSEQ());
                statement.bindString(3, mPODate.getORDERTYPE());
                statement.bindString(4, mPODate.getORDERNO());
                statement.bindString(5, mPODate.getLINENO());
                statement.bindString(6, mPODate.getPRODUCTDEFID());
                statement.bindString(7, mPODate.getPRODUCTDEFVERSION());
                statement.bindString(8, mPODate.getCONTAINERSEQ());
                statement.bindString(9, mPODate.getPOID());
                statement.bindString(10, mLotShippingDataList.get(i).getLOTID());
                statement.bindString(11, mLotShippingDataList.get(i).getINQTY());
                statement.bindString(12, VALID);
                statement.bindString(13, df.format(now));
                try {
                    statement.executeInsert();
                } catch (Exception e) {
                    Log.e("LOTSHIPPING保存出错", e.getMessage().toString());
                }
            }
        }
        //把移除的东西从SF_LOTSHIPPING表中删除
        ContentValues lotshippingValues = new ContentValues();
        lotshippingValues.put(VALIDSTATE, INVALID);
        for (int i = 0; i < removeData.size(); i++) {
            Log.e("移除保存", removeData.get(i).getLOTID());
            db.update(Constant.SF_SHIPPINGLOT, lotshippingValues, "LOTID = ?", new String[]{removeData.get(i).getLOTID()});
        }
        //修改plan表,说明pda已经操作过了
        ContentValues shippingplanValues = new ContentValues();
        shippingplanValues.put(Constant.ISPDASHIPPING, "Y");
        if (tvlotshippingqty.getText().toString().equals(tvlotshippingPlanqty.getText().toString())) {
            shippingplanValues.put(Constant.STATE, "Finished");
        } else {
            shippingplanValues.put(Constant.STATE, "Run");
        }
        db.update(Constant.SF_SHIPPINGPLAN, shippingplanValues, "SHIPPINGPLANNO = ?", new String[]{tvshippingPlanNo.getText().toString()});
//        StringBuffer Shipplan_insert = new StringBuffer();
//
//        //SET column1 = value1, column2 = value2...., columnN = valueN
//       // WHERE [condition];
//        Shipplan_insert.append("UPDATE " + Constant.SF_SHIPPINGPLAN +
//                "set " + Constant.ISPDASHIPPING +" = ?"+
//                "("
//                + Constant.SHIPPINGPLANNO + ","
//                + Constant.ISPDASHIPPING + ")");
//        Shipplan_insert.append(" VALUES( ?, ?)");
//        SQLiteStatement statement = db.compileStatement(Shipplan_insert.toString());
//        statement.bindString(1, tvshippingPlanNo.getText().toString());
//        statement.bindString(2, "Y");
//        try {
//            statement.executeInsert();
//        } catch (Exception e) {
//            Log.e("SHIPPLAN保存出错", e.getMessage().toString());
//        }
    }
    //endregion

    //region getShippingQty
    private int getShippingQty() {
        int i = 0;
        for (LotShippingListData data : mLotShippingDataList) {
            i = i + Integer.parseInt(data.getINQTY());
        }
        return i;
    }
    //endregion

    //endregion

    //region ◆ Listener&extends

    //region PAD KEY Event(extends KeyDownFragmemt)
    @Override
    public void myOnKeyDown() {
        TagScan();
    }

    //endregion

    //endregion

    //region ◆ Barcode相关

    //region Barcode Thread Class
    private class DecodeThread extends Thread {
        private boolean isContinuous = false;
        private long sleepTime = 1000;

        public DecodeThread(boolean isContinuous, int sleep) {
            this.isContinuous = isContinuous;
            this.sleepTime = sleep;
        }

        @Override
        public void run() {
            super.run();
            do {
                BaecodeReader.scan();
                if (isContinuous) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (isContinuous && !threadStop);
        }
    }
    //endregion

    //region BarCode CallBack(扫描到Barcode时的回调事件)
    public Barcode2DWithSoft.ScanCallback mScanCallback = new Barcode2DWithSoft.ScanCallback() {
        @Override
        public void onScanComplete(int i, int length, byte[] data) {
            if (length < 1) {
                return;
            }
            BaecodeReader.stopScan();
            String barcode = new String(data).trim();
            //Log.e("barcode", barcode);
            if (!TextUtils.isEmpty(barcode)) {
                checkAndSearchWeb(barcode);
            }
        }
    };
    //endregion

    //endregion

    //region ◆ RFID循环识别相关

    //region RFID Thread Class
    class TagThread extends Thread {

        private int mBetween = 80;

        public TagThread(int iBetween) {
            mBetween = iBetween;
        }

        public void run() {

            String[] res = null;

            while (scanFlag) {

                res = mContext.mRFIDWithUHF.readTagFromBuffer();

                if (res != null) {
                    Message msg = handler.obtainMessage();
                    msg.what = RFIDSCAN;
                    msg.obj = res[1];
                    handler.sendMessage(msg);
                }
                try {
                    sleep(mBetween);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    //endregion

    //region stopInventory :停止识别
    private void stopInventory() {

        if (scanFlag) {

            scanFlag = false;
            if (myDialog != null) {
                myDialog.dismiss();
                Message message = new Message();
                message.what = UPDATEUI;
                message.obj = 0;
                handler.sendMessage(message);
            }
            if (mContext.mRFIDWithUHF.stopInventory()) {

            } else {
                Log.e("扫描出问题了", "扫描出问题了668");
            }
        }
    }
    //endregion

    //endregion

}
