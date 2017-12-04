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

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Adapter.LotShippingListViewAdapter;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.JaveBean.LotShippingListData;
import cn.zinus.shipping.JaveBean.ShippingPlanData;
import cn.zinus.shipping.JaveBean.TagInfoData;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;
import cn.zinus.shipping.util.SoundUtil;

import static cn.zinus.shipping.util.Constant.RFIDSCAN;
import static cn.zinus.shipping.util.Constant.UPDATEUI;
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
    //condition
    private TextView tvtagqty;
    private TextView tvlotshippingqty;
    private TextView tvshippingPlanNo;
    private TextView tvPoNo;
    private TextView tvCustomer;
    private TextView tvProduct;
    private String PoNo;
    //
    private EditText etTagID;
    private EditText etContainerNo;
    private EditText etSealNo;
    private int BRFlag = 1;
    //RadioGroup
//    private RadioGroup RgInventory;
//    private RadioButton rbSingle;
//    private RadioButton rbAuto;
    // private Handler handler;
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

    //region onDestroy
    @Override
    public void onDestroy() {
        super.onDestroy();
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
                //actionSearch();
                // getLotShippingByShippingPlan("INB-2017090600001");
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
        mLotShippingDataList = new ArrayList<>();
        PoNo = "";
    }
    //endregion

    //region initView()
    private void initview() {
        etTagID = (EditText) getView().findViewById(R.id.etTagID);
        etContainerNo = (EditText) getView().findViewById(R.id.etContainerNo);
        etSealNo = (EditText) getView().findViewById(R.id.etSealNo);
        tvtagqty = (TextView) getView().findViewById(R.id.tvtagqty);
        tvlotshippingqty = (TextView) getView().findViewById(R.id.tvlotqty);
        tvshippingPlanNo = (TextView) getView().findViewById(R.id.tv_shippingPlanNo);
        tvPoNo = (TextView) getView().findViewById(R.id.tv_PoNo);
        tvCustomer = (TextView) getView().findViewById(R.id.tv_Customer);
        tvProduct = (TextView) getView().findViewById(R.id.tv_Product);
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
                if (mLotShippingDataList.get(position).getCONTAINER() != null) {
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
                                    LotShippingListData data = new LotShippingListData();
                                    data.setLOTID(mLotShippingDataList.get(position).getLOTID());
                                    data.setTAGID(mLotShippingDataList.get(position).getTAGID());
                                    data.setVALIDSTATE(Constant.INVALID);
                                    mLotShippingDataList.set(position, data);
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

    //region getLotShippingByShippingPlan
    public void getLotShippingByShippingPlan(ShippingPlanData shippingPlanData) {
        tvshippingPlanNo.setText(shippingPlanData.getSHIPPINGPLANNO());
        tvCustomer.setText(shippingPlanData.getCUSTOMERID());
        PoNo = shippingPlanData.getPRODUCTIONORDERNAME();
        tvPoNo.setText(shippingPlanData.getPRODUCTIONORDERNAME());
        tvProduct.setText(shippingPlanData.getPRODUCTDEFID());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetlOTShippingQuery), shippingPlanData.getPRODUCTIONORDERNAME());
        Log.e("sql语句lotShipping", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mLotShippingDataList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                LotShippingListData lotShippingListData = new LotShippingListData();
                lotShippingListData.setLOTID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.LOTID)));
                lotShippingListData.setTAGID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.RFID)));
                lotShippingListData.setVALIDSTATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.VALIDSTATE)));
                if (lotShippingListData.getVALIDSTATE() != null && lotShippingListData.getVALIDSTATE().equals(Constant.VALID)) {
                    lotShippingListData.setINQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.QTY)));
                    lotShippingListData.setCONTAINER(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONTAINERNO)));
                    lotShippingListData.setSEALNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SEALNO)));
                }
                Log.e("lotShipping数据", lotShippingListData.toString());
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

    //region checkIsExist
    private int checkIsExist(String tagid) {
        int TagLocation = -1;
        for (int i = 0; i < mLotShippingDataList.size(); i++)
            if (mLotShippingDataList.get(i).getTAGID().equals(tagid) &&
                    mLotShippingDataList.get(i).getCONTAINER()!=null &&
                    mLotShippingDataList.get(i).getSEALNO()!=null) {
                TagLocation = i;
            }
        return TagLocation;
    }
    //endregion

    //region TagScan
    private void TagScan() {
        if (etContainerNo.getText().toString().equals("") ||
                etSealNo.getText().toString().equals("")) {
            showToast(mContext, getString(R.string.noContainerAndSealNo), 0);
            return;
        }
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

    //region ReadTag

    private void readBarcodeTag() {
        if (etContainerNo.getText().toString().equals("") &&
                etSealNo.getText().toString().equals("")) {
            showToast(mContext, getString(R.string.noContainerAndSealNo), 0);
            return;
        }

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

    //region checkAndSearchWeb
    private void checkAndSearchWeb(String tagID) {
        if (tvlotshippingqty.getText().toString().equals(tvtagqty.getText().toString())) {
            Log.e("全部完成", "全部完成");
            if (dismessDialogFlag) {
                dismessDialogFlag = false;
                stopInventory();
                showToast(mContext, "所有lot已经完成上车,请保存作业结果!", 0);
            } else {
                dismessDialogFlag = true;
            }
        }
        if (etContainerNo.getText().toString().equals("") &&
                etSealNo.getText().toString().equals("")) {
            showToast(mContext, getString(R.string.noContainerAndSealNo), 0);
            return;
        }
        if (checkIsExist(tagID)==-1) {
            Log.e("indexhand", "列表里存在" + tagID + "修改货柜号和封箱号");
        } else {
            Log.e("indexhand", "列表里不存在" + tagID + "查询数据库");
            SoundUtil.play(R.raw.pegconn, 0);
        }
        searchSqlite(tagID);
    }

    private void searchSqlite(String tagID) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetlOTQtyQuery), PoNo, tagID);
        Log.e("sql语句GetlOTQtyQuery", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("查到了", "111111111");
            while (cursorDatalist.moveToNext()) {
                LotShippingListData lotShippingListData = new LotShippingListData();
                lotShippingListData.setLOTID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.LOTID)));
                lotShippingListData.setINQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.QTY)));
                for (int i = 0; i < mLotShippingDataList.size(); i++) {
                    LotShippingListData data = mLotShippingDataList.get(i);
                    if (data.equals(lotShippingListData)) {
                        data.setINQTY(lotShippingListData.getINQTY());
                        data.setCONTAINER(etContainerNo.getText().toString());
                        data.setSEALNO(etSealNo.getText().toString());
                        data.setVALIDSTATE(Constant.VALID);
                        mLotShippingDataList.set(i, data);
                        mLotShippingListViewAdapter.notifyDataSetChanged();
                        tvlotshippingqty.setText(getShippingQty() + "");
                        // mlvLotShhipping.setSelection(0);
//                        Message message = new Message();
//                        message.what = UPDATEUI;
//                        message.obj = 0;
//                        handler.sendMessage(message);
                    }
                }
            }
        }
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
        tvCustomer.setText("");
        tvPoNo.setText("");
        tvProduct.setText("");
        PoNo = "";
        TagInfoData data = new TagInfoData();
        data.setEnableFlag(true);
        data.setClearFlag(true);
    }
    //endregion

    //region getShippingQty
    private int getShippingQty() {
        int i = 0;
        for (LotShippingListData data : mLotShippingDataList) {
            if (data.getCONTAINER() != null) {
                i++;
            }
        }
        return i;
    }
    //endregion

    public void saveShipping() {
        Log.e("保存", "保存");
        StringBuffer Shipping_insert = new StringBuffer();
        Shipping_insert.append("INSERT OR REPLACE INTO " + Constant.SF_LOTSHIPPING + "("
                + Constant.LOTID + ","
                + Constant.SHIPPINGPLANNO + ","
                + Constant.CONTAINERNO + ","
                + Constant.SEALNO + ","
                + Constant.VALIDSTATE + ")");
        Shipping_insert.append(" VALUES( ?, ?, ?, ? , ? )");
        for (int i = 0; i < mLotShippingDataList.size(); i++) {
            if (mLotShippingDataList.get(i).getVALIDSTATE() != null) {
                SQLiteStatement statement = db.compileStatement(Shipping_insert.toString());
                statement.bindString(1, mLotShippingDataList.get(i).getLOTID());
                statement.bindString(2, tvshippingPlanNo.getText().toString());
                statement.bindString(5, mLotShippingDataList.get(i).getVALIDSTATE());
                if (mLotShippingDataList.get(i).getVALIDSTATE().equals(Constant.VALID)) {
                    statement.bindString(3, mLotShippingDataList.get(i).getCONTAINER());
                    statement.bindString(4, mLotShippingDataList.get(i).getSEALNO());
                } else {
                    statement.bindString(3, "");
                    statement.bindString(4, "");
                }
                try {
                    statement.executeInsert();
                } catch (Exception e) {
                    Log.e("LOTSHIPPING保存出错", e.getMessage().toString());
                }
            }
        }
        //插入plan表,说明pda已经操作过了

        ContentValues values = new ContentValues();
        values.put(Constant.ISPDASHIPPING, "Y");
        if (tvlotshippingqty.getText().toString().equals(tvtagqty.getText().toString())) {
            values.put(Constant.STATE, "Finished");
        } else {
            values.put(Constant.STATE, "Run");
        }

        db.update(Constant.SF_SHIPPINGPLAN, values, "SHIPPINGPLANNO = ?", new String[]{tvshippingPlanNo.getText().toString()});
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