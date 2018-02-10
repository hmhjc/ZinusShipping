package cn.zinus.shipping.Fragment.lotshipping;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Adapter.LotShippingListViewAdapter;
import cn.zinus.shipping.Fragment.Event;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.JaveBean.LotData;
import cn.zinus.shipping.JaveBean.LotShippingListData;
import cn.zinus.shipping.JaveBean.ShippingCommonData;
import cn.zinus.shipping.JaveBean.ShippingPlanData;
import cn.zinus.shipping.JaveBean.ShippingPlanSeqListData;
import cn.zinus.shipping.JaveBean.TagInfoData;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;
import cn.zinus.shipping.util.SoundUtil;

import static cn.zinus.shipping.util.Constant.INVALID;
import static cn.zinus.shipping.util.Constant.UPDATEUI;
import static cn.zinus.shipping.util.Constant.VALID;
import static cn.zinus.shipping.util.Constant.VALIDSTATE;
import static cn.zinus.shipping.util.DBManger.getCursorData;
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
    //ListView
    private ListView mlvLotShhipping;
    private LotShippingListViewAdapter mLotShippingListViewAdapter;
    //listview的数据源
    private ArrayList<LotShippingListData> mLotShippingDataList;
    //保存一个扫描期间listview增加的数据
    private ArrayList<String> _tempLotShippingDataList;
    //这个画面的计划信息
    private ShippingPlanData _plandata;
    //这个画面的货柜序号
    private String _ContainerSeq;
    //这个记录了这个货柜下面所有符合条件的lotId,用来判断扫到的lot是不是符合条件
    private HashSet<String> _mContainerLotHashSet;
    //这个记录了这个货柜下面已经上车的lotId,用来判断扫到的lot是不是已经上车了
    private HashSet<String> _mlistLotHashSet;
    //这个记录了这个货柜下面所有符合条件的lotId,和它对应的计划序号
    private HashMap<String, String> _mplanseqLotHashMap;
    //按照planseq,存放个计划顺序下的符合条件的lot(已经上车的为valid,没有上车的为Invalid)
    private HashMap<String, HashSet<LotData>> _mLotDataHashMap;
    //按照计划顺序,存放这个顺序的计划数量,只在进入这个tab的时候修改数量
    private HashMap<String, Integer> _mPlanSeqPlanQtyHashMap;
    //按照计划顺序,存放这个顺序已经装车的数量
    private HashMap<String, Integer> _mPlanSeqShippedQtyHashMap;
    public static final int RFIDSCAN = 111;
    //condition
    //扫描标签个数
    private TextView tvtagqty;
    //已经上车总数
    private TextView tvlotshippingqty;
    //计划上车总数
    private TextView tvlotshippingPlanqty;
    //提单号
    private TextView tvBookingNo;
    //截进场ShippingEndPlanDate
    private TextView tvShippingEndPlanDate;
    //货柜编号ContainerNo
    private TextView tvContainerNo;
    //封箱号SealNo
    private TextView tvSealNo;
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
                        setQTYInfo();
                        int position = (Integer) msg.obj;
                        if (position == -1) {
                            //-1代表第一次搜索
                            mlvLotShhipping.setSelection((Integer) msg.obj);
                        } else {
                            mlvLotShhipping.setSelection((Integer) msg.obj);

                        }
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
                //saveShipping();
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
        _mplanseqLotHashMap = new HashMap<>();
        _mContainerLotHashSet = new HashSet<>();
        _mLotDataHashMap = new HashMap<>();
        _mlistLotHashSet = new HashSet<>();
        _mPlanSeqPlanQtyHashMap = new HashMap<>();
        _mPlanSeqShippedQtyHashMap = new HashMap<>();
        mLotShippingDataList = new ArrayList<>();
        _plandata = new ShippingPlanData();
        removeData = new ArrayList<>();
        _tempLotShippingDataList = new ArrayList<>();
    }
    //endregion

    //region initView()
    private void initview() {
        etTagID = (EditText) getView().findViewById(R.id.etTagID);
        tvtagqty = (TextView) getView().findViewById(R.id.tvtagqty);
        tvlotshippingqty = (TextView) getView().findViewById(R.id.tvshippedqty);
        tvlotshippingPlanqty = (TextView) getView().findViewById(R.id.tvshipplanqty);
        tvBookingNo = (TextView) getView().findViewById(R.id.tv_BookingNo);
        tvSealNo = (TextView) getView().findViewById(R.id.tv_SealNo);
        tvShippingEndPlanDate = (TextView) getView().findViewById(R.id.tv_ShippingEndPlanDate);
        tvContainerNo = (TextView) getView().findViewById(R.id.tv_ContainerNo);
        mivchoose = (ImageView) getView().findViewById(R.id.iv_choose);
        mivchoose.setOnClickListener(this);
        mViewChooseBorR = mContext.getLayoutInflater().inflate(R.layout.chooseborr, null);
        //listViewDataList   **
        mlvLotShhipping = (ListView) getView().findViewById(R.id.lv_consumeLotInbound);
        mLotShippingListViewAdapter = new LotShippingListViewAdapter(mContext, mLotShippingDataList);
        mlvLotShhipping.setAdapter(mLotShippingListViewAdapter);
        mlvLotShhipping.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                removeShipping(position);

//                if (mLotShippingDataList.get(position).getLOTID() != null) {
//                    AlertDialog prompt = new AlertDialog.Builder(mContext).
//                            setTitle(getString(R.string.prompt)).
//                            setMessage(getString(R.string.removeshipping)).
//                            setCancelable(true).
//                            setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            }).
//                            setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    /**
//                                     * 移除装车
//                                     */
//                                    //给移除tag的list增加项目
//                                    removeData.add(mLotShippingDataList.get(position));
//                                    //修改记录lot的list相应的项，validstate改成invalid
//                                    String removelotid = mLotShippingDataList.get(position).getLOTID();
//                                    for (int i = 0; i < _mLotDataList.size(); i++) {
//                                        LotData data = _mLotDataList.get(i);
//                                        if (removelotid.equals(data.getLOTID())) {
//                                            data.setVALIDSTATE(INVALID);
//                                            _mLotDataList.set(i, data);
//                                            break;
//                                        }
//                                    }
//                                    saveRemovedShipping(mLotShippingDataList.get(position));
//                                    //画面listview的list中移除一项
//                                    mLotShippingDataList.remove(position);
//                                    mLotShippingListViewAdapter.notifyDataSetChanged();
//                                    Message message = new Message();
//                                    message.what = UPDATEUI;
//                                    message.obj = position;
//                                    handler.sendMessage(message);
//                                }
//                            }).create();
//                    prompt.show();
//                }
                return false;
            }
        });
    }

    private void removeShipping(final int position) {
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
//                            for (int i = 0; i < _mLotDataList.size(); i++) {
//                                LotData data = _mLotDataList.get(i);
//                                if (removelotid.equals(data.getLOTID())) {
//                                    data.setVALIDSTATE(INVALID);
//                                    _mLotDataList.set(i, data);
//                                    break;
//                                }
//                            }
                            saveRemovedShipping(mLotShippingDataList.get(position));
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
    }

    //endregion

    //region getLotShippingByContainer,进入这个tab的方法(按照货柜扫描)

    public void getLotShippingByContainer(ShippingPlanData shippingPlanData, ArrayList<ShippingPlanSeqListData> mPlanSeqListData, ShippingCommonData mShippingCommonData) {
        /**
         * 先把lot表里符合条件的lot搜出来放到一个全局变量中,包含po,productid,数量(搜索出来的就是可以装到这个柜里面的lot)
         * 再搜shippinglot，找出对应货柜已经装车的lot，显示在画面中
         * 把提单号,截进场,货柜编号,封箱号显示到下面的列表里面
         * 查到的没有装车的lot按计划序号分类,放进一个Map中
         */
        //这个货柜的计划信息
        _plandata = shippingPlanData;
        tvBookingNo.setText(mShippingCommonData.getBOOKINGNO());
        tvShippingEndPlanDate.setText(mShippingCommonData.getShippingEndPlanDate());
        tvContainerNo.setText(mShippingCommonData.getContainerNo());
        tvSealNo.setText(mShippingCommonData.getSEALNO());
        //搜索符合条件的lot,并放入_mLotDataHashSet，已经保存装车的为valid，没有的为invalid,在把这个hashset
        //放到_mLotDataHashMap,key是计划序号
        _mplanseqLotHashMap = new HashMap<>();
        _mPlanSeqPlanQtyHashMap = new HashMap<>();
        _mPlanSeqShippedQtyHashMap = new HashMap<>();
        _mLotDataHashMap = new HashMap<>();
        _mlistLotHashSet = new HashSet<>();
        _mContainerLotHashSet = new HashSet<>();
        int totalplanqty = 0;
        for (ShippingPlanSeqListData data : mPlanSeqListData) {
            totalplanqty += Integer.parseInt(data.getQTY());
            _ContainerSeq = data.getCONTAINERSEQ();
        }
        tvlotshippingPlanqty.setText(totalplanqty + "");
//            HashSet<LotData> tempHashSet = new HashSet<>();
//            int shippedqty = 0;
        //搜索这个柜可以装的lot的信息,已经装车的为Valid,未装车的为Invalid
        String selectNoShippinglOTsql = String.format(getString(R.string.GetlOTShippingQuery)
                , _plandata.getSHIPPINGPLANNO(), _ContainerSeq);
        Log.e("sql语句查这个货柜的lot", selectNoShippinglOTsql);
        Cursor cursorNoShippingLotDatalist = DBManger.selectDatBySql(db, selectNoShippinglOTsql, null);
        if (cursorNoShippingLotDatalist.getCount() != 0) {
            //有LOT的时候
            while (cursorNoShippingLotDatalist.moveToNext()) {
                LotData mLotData = new LotData();
                mLotData.setPURCHASEORDERID(getCursorData(cursorNoShippingLotDatalist, Constant.PURCHASEORDERID).trim());
                mLotData.setLOTID(getCursorData(cursorNoShippingLotDatalist, Constant.LOTID).trim());
                mLotData.setRFID(getCursorData(cursorNoShippingLotDatalist, Constant.RFID).trim());
                mLotData.setQTY(getCursorData(cursorNoShippingLotDatalist, Constant.QTY).trim());
                mLotData.setTRACKOUTTIME(getCursorData(cursorNoShippingLotDatalist, Constant.TRACKOUTTIME).trim());
                mLotData.setPRODUCTDEFID(getCursorData(cursorNoShippingLotDatalist, Constant.PRODUCTDEFID).trim());
                mLotData.setPRODUCTDEFNAME(getCursorData(cursorNoShippingLotDatalist, Constant.PRODUCTDEFNAME).trim());
                mLotData.setVALIDSTATE(getCursorData(cursorNoShippingLotDatalist, Constant.VALIDSTATE).trim());
                Log.e("货柜的lot数据", mLotData.toString());
                //已经上车的加入列表
                if (mLotData.getVALIDSTATE().equals(VALID)) {
                    _mlistLotHashSet.add(mLotData.getRFID());
                    addToList(mLotData);
                } else {
                    _mContainerLotHashSet.add(mLotData.getRFID());
                }
            }
        } else {
            //没有lot的时候
            showToast(mContext, "这个货柜没有相应的LOT信息，请确认", 0);
            SoundUtil.play(R.raw.waring, 0);
        }
    }

    //endregion

    //region addToList(根据LotData的数据,插入到list中)
    private void addToList(LotData mLotData) {
        LotShippingListData lotShippingListData = new LotShippingListData();
        lotShippingListData.setLOTID(mLotData.getLOTID());
        lotShippingListData.setINQTY(mLotData.getQTY());
        lotShippingListData.setPRODUCTDEFID(mLotData.getPRODUCTDEFID());
        lotShippingListData.setPRODUCTDEFNAME(mLotData.getPRODUCTDEFNAME());
        lotShippingListData.setTRACKOUTTIME(mLotData.getTRACKOUTTIME());
        lotShippingListData.setTAGID(mLotData.getRFID());
        lotShippingListData.setVALIDSTATE(VALID);
        mLotShippingDataList.add(lotShippingListData);
        Log.e("已经装车的lot数据", lotShippingListData.toString());
        tvtagqty.setText(mLotShippingDataList.size() + "");
        mLotShippingListViewAdapter.notifyDataSetChanged();
        Message message = new Message();
        message.what = UPDATEUI;
        message.obj = -1;
        handler.sendMessage(message);
    }
    //endregion

    //region TagScan
    private void TagScan() {
        if (BRFlag == 1) {
            if (tvlotshippingqty.getText().toString().equals("")) {
                // SoundUtil.play(R.raw.waring, 0);
                showToast(mContext, "请先在上一个tab中选择po信息!", 0);
                return;
            }
            if (Float.parseFloat(tvlotshippingqty.getText().toString()) == Float.parseFloat(tvlotshippingPlanqty.getText().toString())) {
                // SoundUtil.play(R.raw.waring, 0);
                showToast(mContext, "所有lot已经完成上车!", 0);
                return;
            }
            if (Float.parseFloat(tvlotshippingqty.getText().toString()) > Float.parseFloat(tvlotshippingPlanqty.getText().toString())) {
                // SoundUtil.play(R.raw.waring, 0);
                showToast(mContext, "上车数量超过计划，请移除多余的部分", 0);
            } else if (!scanFlag) {
                scanFlag = true;
                _tempLotShippingDataList.clear();
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
                    SoundUtil.play(R.raw.waring, 0);
                    showToast(mContext, "扫描模块异常，请再按一次", 1);
                    mContext.freeUHF();
                    mContext.initUHF();
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
            threadStop = false;

        } else {
            if (BaecodeReader != null) {
                BaecodeReader.stopScan();
            }
            threadStop = true;
        }
    }

    //endregion

    //region checkIsExistInlist

    private int checkIsExistInlist(String tagid) {
        //初始化返回值为-1，代表着这个id不在这个list里面
        int TagLocation = -1;
        for (int i = 0; i < mLotShippingDataList.size(); i++) {
            if (mLotShippingDataList.get(i).getTAGID().equals(tagid)) {
                TagLocation = i;
            }
        }
        return TagLocation;
    }

    //endregion

    //region 扫描到以后的处理流程
    private void checkAndSearchWeb(String tagID) {
        if (Float.parseFloat(tvlotshippingqty.getText().toString()) == Float.parseFloat(tvlotshippingPlanqty.getText().toString())) {
            Log.e("全部完成", "全部完成");
            if (dismessDialogFlag) {
                dismessDialogFlag = false;
                stopInventory();
                SoundUtil.play(R.raw.success, 0);
                showToast(mContext, "所有lot已经完成上车,请保存作业结果!", 0);
            } else {
                dismessDialogFlag = true;
            }
            return;
        }
        if (Float.parseFloat(tvlotshippingqty.getText().toString()) > Float.parseFloat(tvlotshippingPlanqty.getText().toString())) {
            Log.e("全部完成", "全部完成");
            if (dismessDialogFlag) {
                dismessDialogFlag = false;
                stopInventory();
                SoundUtil.play(R.raw.waring, 0);
                showToast(mContext, "上车数量超过计划，请移除多余的部分", 0);
            } else {
                dismessDialogFlag = true;
            }
        }
        //1:先确定这个tag有没有被扫过或者已经装车,存在则返回,判断下一个标签
        if (!_mlistLotHashSet.add(tagID)) return;
        //2:是否在可以扫描的tagid的列表里面
        if (!_mContainerLotHashSet.contains(tagID)) {
            //不存在,报警,停止扫描
            showToast(mContext, tagID + "这个lot不属于这个货柜，请确认", 0);
            SoundUtil.play(R.raw.waring, 0);
            stopInventory();
        } else {
            //3:说明这个lot是可以进入这个货柜的,把它放到扫描的临时变量里面
            _tempLotShippingDataList.add(tagID);
            SoundUtil.play(R.raw.success, 0);
        }
    }

    private void checkAndRemoveShipping(String tagID) {
        int lotflag = checkIsExistInlist(tagID);
        if (lotflag == -1) {
            //Log.e("indexhand", tagID + "不在这个list里面");
            showToast(mContext, tagID + "这个Lot还没有上车", 0);
        } else {
            removeShipping(lotflag);
        }
    }

    private void searchToUpdateUI(int lotflag) {
        //修改lot数据的validstate为valid，再扫描的时候就会把它归为已经存放到列表上的数据

        //设置listview的数据
        LotShippingListData listData = new LotShippingListData();
//        listData.setLOTID(data.getLOTID());
//        listData.setINQTY(data.getQTY());
//        listData.setPRODUCTDEFID(data.getPRODUCTDEFID());
//        listData.setPRODUCTDEFNAME(data.getPRODUCTDEFNAME());
//        listData.setTRACKOUTTIME(data.getTRACKOUTTIME());
//        listData.setTAGID(data.getRFID());
        listData.setVALIDSTATE(VALID);
        mLotShippingDataList.add(listData);
        //_tempLotShippingDataList.add(listData);
        mLotShippingListViewAdapter.notifyDataSetChanged();
        setQTYInfo();
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
        tvBookingNo.setText("");
        tvlotshippingPlanqty.setText("0");
        tvSealNo.setText("");
        tvShippingEndPlanDate.setText("");
        tvContainerNo.setText("");
        _plandata = new ShippingPlanData();
        removeData = new ArrayList<>();
        TagInfoData data = new TagInfoData();
        data.setEnableFlag(true);
        data.setClearFlag(true);
    }
    //endregion

    //region ActionSave

    public void saveNewShipping() {
        db = mHelper.getWritableDatabase();
        if (db.isOpen()) {
            try {
                db.beginTransaction();
                for (String tagid : _tempLotShippingDataList) {
                    String selectDataListsql = String.format(getString(R.string.GetShippingByTagIDQuery), tagid,_ContainerSeq);
                    Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
                    if (cursorDatalist.getCount() != 0) {
                        while (cursorDatalist.moveToNext()) {
                            //可以插入的lot,插入shippinglot表
                            LotData mLotData = new LotData();
                            mLotData.setPURCHASEORDERID(getCursorData(cursorDatalist, Constant.PURCHASEORDERID).trim());
                            mLotData.setLOTID(getCursorData(cursorDatalist, Constant.LOTID).trim());
                            mLotData.setRFID(getCursorData(cursorDatalist, Constant.RFID).trim());
                            mLotData.setQTY(getCursorData(cursorDatalist, Constant.QTY).trim());
                            mLotData.setTRACKOUTTIME(getCursorData(cursorDatalist, Constant.TRACKOUTTIME).trim());
                            mLotData.setPRODUCTDEFID(getCursorData(cursorDatalist, Constant.PRODUCTDEFID).trim());
                            mLotData.setPRODUCTDEFNAME(getCursorData(cursorDatalist, Constant.PRODUCTDEFNAME).trim());
                            mLotData.setVALIDSTATE(getCursorData(cursorDatalist, Constant.VALIDSTATE).trim());
                            addToList(mLotData);
                            String insertShippinglot = String.format(getString(R.string.InsertIntoShippingLotQuery), tagid);
                            db.execSQL(insertShippinglot);
                            //再修改一下这个lot对应的计划顺序的标志位
                            String UpdateShippingPlanDetailQuery = String.format(getString(R.string.UpdateShippingPlanDetailQuery)
                                    , _plandata.getSHIPPINGPLANNO(),_ContainerSeq,tagid);
                            db.execSQL(UpdateShippingPlanDetailQuery);
                           // Log.e("update",UpdateShippingPlanDetailQuery);
                        }
                    } else {
                        //说明已经装满了,报警
                        SoundUtil.play(R.raw.waring, 0);
                    }
                }
                tvtagqty.setText(mLotShippingDataList.size() + "");
                mLotShippingListViewAdapter.notifyDataSetChanged();
                Message message = new Message();
                message.what = UPDATEUI;
                message.obj = -1;
                handler.sendMessage(message);
            } catch (Exception e) {
                showToast(mContext, e.getMessage(), 0);
            } finally {
                db.setTransactionSuccessful();
                db.endTransaction();
                //保存好以后,前面那个tab重新搜索一下
                EventBus.getDefault().post(new Event.ReSearchShippingPlanDetail(_ContainerSeq));
            }
        }
    }

    public void saveRemovedShipping(LotShippingListData removeData) {
        Log.e("保存", "保存");
        //把移除的东西从SF_LOTSHIPPING表中删除
        ContentValues lotshippingValues = new ContentValues();
        lotshippingValues.put(VALIDSTATE, INVALID);
        db.update(Constant.SF_SHIPPINGLOT, lotshippingValues, "LOTID = ?", new String[]{removeData.getLOTID()});
        //修改plan表,说明pda已经操作过了
        ContentValues shippingplanValues = new ContentValues();
        shippingplanValues.put(Constant.ISPDASHIPPING, "Y");
        if (tvlotshippingqty.getText().toString().equals(tvlotshippingPlanqty.getText().toString())) {
            shippingplanValues.put(Constant.STATE, "Finished");
        } else {
            shippingplanValues.put(Constant.STATE, "Run");
        }
        db.update(Constant.SF_SHIPPINGPLAN, shippingplanValues, "SHIPPINGPLANNO = ?", new String[]{tvBookingNo.getText().toString()});
        Log.e("保存", _ContainerSeq);
        EventBus.getDefault().post(new Event.ReSearchShippingPlanDetail(_ContainerSeq));
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

    //region setQTYInfo
    private void setQTYInfo() {
        tvlotshippingqty.setText(getShippingQty() + "");
        tvtagqty.setText(mLotShippingDataList.size() + "");
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
            if (!TextUtils.isEmpty(barcode)) {
                checkAndRemoveShipping(barcode);
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
            saveNewShipping();
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
