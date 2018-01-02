package cn.zinus.warehouse.Fragment.inventorymgnt;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Timer;
import java.util.TimerTask;

import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Adapter.StockLotCheckDetailListViewAdapter;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.JaveBean.StockCheckData;
import cn.zinus.warehouse.JaveBean.StockLotCheckDeatilData;
import cn.zinus.warehouse.JaveBean.TagInfoData;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;

import static cn.zinus.warehouse.util.Constant.RFIDSCAN;
import static cn.zinus.warehouse.util.Constant.UPDATEUI;
import static cn.zinus.warehouse.util.Utils.showToast;
import static com.micube.control.util.Server.hexStringToString;

/**
 * Developer:Spring
 * DataTime :2017/9/25 15:26
 * Main Change:
 */

public class StockLotCheckDetailFragment extends KeyDownFragment implements View.OnClickListener {

    //region ◆ 변수(Variables)
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式
    //上下文
    private MainNaviActivity mContext;
    //popwindow
    private ImageView mivchoose;
    private PopupWindow mpopChooseBorR;
    private View mViewChooseBorR;
    //popwindow
    private PopupWindow mpopFixQty;
    private View mViewFixQty;
    //ListView
    private ListView mlvStockLotCheckDetail;
    private StockLotCheckDetailListViewAdapter mStockLotCheckDetailListViewAdapter;
    private ArrayList<StockLotCheckDeatilData> mStockLotCheckDeatilDataArrayList;
    private ArrayList<String> IDlist;
    private TextView tvtagqty;
    private TextView tvCheckMonth;
    //
    private EditText etTagID;
    private int BRFlag = 1;
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
    SQLiteDatabase db;
    boolean savePlanFlag = false;
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
        return inflater.inflate(R.layout.fragment_stocklotcheckdetail, container, false);
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
                    case UPDATEUI:
                        int position = (Integer) msg.obj;
                        if (position == -1) {
                            //-1代表第一次搜索
                            mlvStockLotCheckDetail.setSelection(0);
                        } else {
                            mlvStockLotCheckDetail.setSelection(position);
                            //修改好数字以后，更新数据库的数据
                            Log.e("保存了", "kaishi");
                            UpdateSF_STOCKLOTCHECKDETAIL(mStockLotCheckDeatilDataArrayList.get(position));
                        }
                        break;
                    case RFIDSCAN:
                        String tagid = msg.obj + "";
                        String tagID = hexStringToString(tagid);
                        if (!tagID.equals("")) {
                            checkAndSearchWeb(tagID);
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
                showToast(mContext, "RFID", 0);
                mivchoose.setImageResource(R.drawable.rfidicon);
                if (BaecodeReader != null) {
                    BaecodeReader.stopScan();
                }
                mpopChooseBorR.dismiss();
                break;
            case R.id.btnbarcode:
                //选择R&B的popupwindow里面的Barcode按钮
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
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //endregion

    //region ◆ Function

    //region initData
    private void initData() {
        mStockLotCheckDeatilDataArrayList = new ArrayList<>();
        IDlist = new ArrayList<>();
    }
    //endregion

    //region initView()
    private void initview() {
        etTagID = (EditText) getView().findViewById(R.id.etTagID);
        tvtagqty = (TextView) getView().findViewById(R.id.tvtagqty);
        tvCheckMonth = (TextView) getView().findViewById(R.id.CheckMonth);
        mivchoose = (ImageView) getView().findViewById(R.id.iv_choose);
        mivchoose.setOnClickListener(this);
        //Choose RFID or Barcode popupwindow
        mViewChooseBorR = mContext.getLayoutInflater().inflate(R.layout.chooseborr, null);
        //finqty popupwindow
        mViewFixQty = mContext.getLayoutInflater().inflate(R.layout.fixqty, null);
        //listViewDataList   **
        mlvStockLotCheckDetail = (ListView) getView().findViewById(R.id.lv_StockLotCheckDetail);
        mStockLotCheckDetailListViewAdapter = new StockLotCheckDetailListViewAdapter(mContext, mStockLotCheckDeatilDataArrayList);
        mlvStockLotCheckDetail.setAdapter(mStockLotCheckDetailListViewAdapter);
        mlvStockLotCheckDetail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fixQty(view, position);
                return false;
            }
        });
    }
    //endregion

    //region fixQty
    @SuppressLint("WrongConstant")
    private void fixQty(View view, final int position) {
        final StockLotCheckDeatilData tempdata = mStockLotCheckDeatilDataArrayList.get(position);
        Button btnConfirm = (Button) mViewFixQty.findViewById(R.id.btnfqty);
        final EditText etFixQty = (EditText) mViewFixQty.findViewById(R.id.etfqty);
        etFixQty.setText(tempdata.getCHECKQTY() + "");
        etFixQty.setSelection(etFixQty.length());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempdata.setCHECKQTY(etFixQty.getText().toString());
                if (Float.parseFloat(tempdata.getCHECKQTY()) > Float.parseFloat(tempdata.getQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(tempdata.getCHECKQTY()) == Float.parseFloat(tempdata.getQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymatch);
                } else {
                    tempdata.setBackgroundColor(R.color.qtyless);
                }
                mStockLotCheckDeatilDataArrayList.set(position, tempdata);
                mStockLotCheckDetailListViewAdapter.notifyDataSetChanged();
                Message message = new Message();
                message.what = UPDATEUI;
                message.obj = position;
                handler.sendMessage(message);
                mpopFixQty.dismiss();
            }
        });
        mpopFixQty = new PopupWindow(mViewFixQty, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mpopFixQty.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mpopFixQty.setTouchable(true);
        mpopFixQty.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mpopFixQty.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mpopFixQty.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        mpopFixQty.showAtLocation(view, Gravity.CENTER, 0, 0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) etFixQty.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etFixQty, 0);
            }
        }, 500);
    }
    //endregion

    //region getStockLotCheckDetailbyCheckMonth
    public void getStockLotCheckDetailbyCheckMonth(StockCheckData stockCheck) {
        String selectDataListsql = String.format(getString(R.string.GetStockLotCheckDetailQuery), stockCheck.getCHECKMONTH(), stockCheck.getWAREHOUSEID());
        Log.e("StockLotCheckDetail", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mStockLotCheckDeatilDataArrayList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                StockLotCheckDeatilData stockLotCheckDeatilData = new StockLotCheckDeatilData();
                stockLotCheckDeatilData.setCONSUMEABLDEFNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFNAME)).trim());
                stockLotCheckDeatilData.setWAREHOUSEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.WAREHOUSEID)));
                stockLotCheckDeatilData.setCONSUMABLELOTID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLELOTID)));
                stockLotCheckDeatilData.setUNIT(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.UNIT)));
                stockLotCheckDeatilData.setQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.QTY)));
                stockLotCheckDeatilData.setTAGID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.TAGID)));
                stockLotCheckDeatilData.setTAGQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.TAGQTY)));
                stockLotCheckDeatilData.setCONSUMEABLDEFID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFID)));
                stockLotCheckDeatilData.setCONSUMEABLDEFVERSION(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFVERSION)));
                stockLotCheckDeatilData.setCHECKMONTH(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKMONTH)));
                if (cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)).equals("null") ||
                        cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)).equals("")) {
                    stockLotCheckDeatilData.setCHECKQTY("0");
                } else {
                    stockLotCheckDeatilData.setCHECKQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)));
                }
                if (Float.parseFloat(stockLotCheckDeatilData.getCHECKQTY()) > Float.parseFloat(stockLotCheckDeatilData.getQTY())) {
                    stockLotCheckDeatilData.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(stockLotCheckDeatilData.getCHECKQTY()) == Float.parseFloat(stockLotCheckDeatilData.getQTY())) {
                    stockLotCheckDeatilData.setBackgroundColor(R.color.qtymatch);
                } else {
                    stockLotCheckDeatilData.setBackgroundColor(R.color.qtyless);
                }
                Log.e("stockLotCheckDeatilData", stockLotCheckDeatilData.toString());
                mStockLotCheckDeatilDataArrayList.add(stockLotCheckDeatilData);
            }
        }
        tvtagqty.setText(mStockLotCheckDeatilDataArrayList.size() + "");
        tvCheckMonth.setText(stockCheck.getCHECKMONTH());
        savePlanFlag = false;
        mStockLotCheckDetailListViewAdapter.notifyDataSetChanged();
        Message message = new Message();
        message.what = UPDATEUI;
        message.obj = -1;
        handler.sendMessage(message);
    }
    //endregion

    //region checkIsExist
    private int checkIsExist(String tagid) {
        int returnint = -1;
        for (int i = 0; i < IDlist.size(); i++)
            if (IDlist.get(i).equals(tagid)) {
                returnint = i;
            }
        return returnint;
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
                    mContext.mRFIDWithUHF.stopInventory();
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
    private void reaRFIDTag() {
        String strUII = mContext.mRFIDWithUHF.inventorySingleTag();
        //Log.e("tagtagtag", "读到" + strUII);
        if (!TextUtils.isEmpty(strUII)) {
            String tagID = hexStringToString(strUII);
            if (!tagID.equals("")) {
                checkAndSearchWeb(tagID);
            }
        }
    }

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

    //region checkAndSearchWeb
    private void checkAndSearchWeb(String tagID) {
        int tagposition = findTagInList(tagID);
        if (tagposition == -2) {
            Log.e("indexhand", tagID + "重复扫描了");
        } else if (tagposition == -1) {
            Log.e("indexhand", tagID + "不在这个list里面");
            showToast(mContext, tagID+"不是这个列表里面的标签", 0);
        } else {
            Log.e("indexhand", tagID + "在列表里，替换Inqty的值");
            final StockLotCheckDeatilData tempdata = mStockLotCheckDeatilDataArrayList.get(tagposition);
            tempdata.setCHECKQTY(tempdata.getTAGQTY().toString());
            if (Float.parseFloat(tempdata.getCHECKQTY()) > Float.parseFloat(tempdata.getQTY())) {
                tempdata.setBackgroundColor(R.color.qtymore);
            } else if (Float.parseFloat(tempdata.getCHECKQTY()) == Float.parseFloat(tempdata.getQTY())) {
                tempdata.setBackgroundColor(R.color.qtymatch);
            } else {
                tempdata.setBackgroundColor(R.color.qtyless);
            }
            mStockLotCheckDeatilDataArrayList.set(tagposition, tempdata);
            mStockLotCheckDetailListViewAdapter.notifyDataSetChanged();
            Message message = new Message();
            message.what = UPDATEUI;
            message.obj = tagposition;
            handler.sendMessage(message);
        }
    }
    //endregion

    //region findTagInList
    private int findTagInList(String tagid) {
        int returnint = -1;
        for (int i = 0; i < mStockLotCheckDeatilDataArrayList.size(); i++) {
            StockLotCheckDeatilData data = mStockLotCheckDeatilDataArrayList.get(i);
            if (data.getTAGID().equals(tagid)) {
                if (data.getCHECKQTY().equals(data.getTAGQTY())) {
                    //一样的话就不必要修改ui了
                    returnint = -2;
                } else {
                    //不一样的时候修改ui的数量
                    returnint = i;
                }
            }
        }
        return returnint;
    }
    //endregion

    //region actionSearch
    protected void actionSearch() {

    }
    //endregion

    //region actionClearAll
    public void actionClearAll() {
        if (mStockLotCheckDeatilDataArrayList.size() > 0) {
            mStockLotCheckDeatilDataArrayList.clear();
        }
        mStockLotCheckDetailListViewAdapter.notifyDataSetChanged();
        IDlist.clear();
        etTagID.setText("");
        tvtagqty.setText("0");
        tvCheckMonth.setText("");
        TagInfoData data = new TagInfoData();
        data.setEnableFlag(true);
        data.setClearFlag(true);
    }
    //endregion

    //region UpdateSF_STOCKLOTCHECKDETAIL
    private void UpdateSF_STOCKLOTCHECKDETAIL(StockLotCheckDeatilData data) {
        ContentValues values = new ContentValues();
        Log.e("保存了", data.toString());
        values.put(Constant.CHECKQTY, data.getCHECKQTY());
        db.update(Constant.SF_STOCKLOTCHECKDETAIL, values, "WAREHOUSEID = ? AND CHECKMONTH = ?" +
                        " AND CONSUMABLEDEFID = ? AND CONSUMABLEDEFVERSION = ? AND CONSUMABLELOTID = ?"
                        , new String[]{data.getWAREHOUSEID(), data.getCHECKMONTH()
                        , data.getCONSUMEABLDEFID(), data.getCONSUMEABLDEFVERSION(), data.getCONSUMABLELOTID()});
        if (!savePlanFlag){
            ContentValues values1 = new ContentValues();
            values1.put(Constant.ISPDASAVE, "Y");
            savePlanFlag = true;
            db.update(Constant.SF_STOCKCHECK, values1, "WAREHOUSEID = ? AND CHECKMONTH = ?"
                    , new String[]{data.getWAREHOUSEID(), data.getCHECKMONTH()});
        }
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
            }

            if (mContext.mRFIDWithUHF.stopInventory()) {
                //BtnReadTag.setText(mContext.getString(R.string.title_start_Inventory));
            } else {

            }
        }
    }
    //endregion

    //endregion

}