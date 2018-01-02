package cn.zinus.warehouse.Fragment.materialstockin;

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
import cn.zinus.warehouse.Adapter.ConsumeLotInboundListViewAdapter;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.JaveBean.ConsumeLotInboundData;
import cn.zinus.warehouse.JaveBean.TagInfoData;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;
import cn.zinus.warehouse.util.Utils;

import static cn.zinus.warehouse.util.Constant.RFIDSCAN;
import static cn.zinus.warehouse.util.Constant.UPDATEUI;
import static cn.zinus.warehouse.util.Utils.showToast;
import static com.micube.control.util.Server.hexStringToString;

/**
 * Created by Spring on 2017/2/18.
 */

public class ConsumeLotInboundFragment extends KeyDownFragment implements View.OnClickListener {

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
    private ListView mlvComsumeLotInbound;
    private ConsumeLotInboundListViewAdapter mConsumeLotInboundListViewAdapter;
    private ArrayList<ConsumeLotInboundData> mcomsumeLotInboundDataList;
    private TextView tvtagqty;
    private TextView tvInboundOrderNo;
    private String InboundOrderNo;
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
        return inflater.inflate(R.layout.fragment_consumelotinbound, container, false);
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
                        if (!tagID.equals("")) {
                            checkAndSearchWeb(tagID);
                        }
                        break;
                    case UPDATEUI:
                        mlvComsumeLotInbound.setSelection((Integer) msg.obj);
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
                //actionSearch();
             //   getConsumeLotInboundByInboundOrder("INB-2017090600001");
                break;

            case  R.id.action_save:
                actionSave();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //endregion

    //region ◆ Function

    //region initData
    private void initData() {
        mcomsumeLotInboundDataList = new ArrayList<>();
    }
    //endregion

    //region initView()
    private void initview() {
        etTagID = (EditText) getView().findViewById(R.id.etTagID);
        tvtagqty = (TextView) getView().findViewById(R.id.tvtagqty);
        tvInboundOrderNo = (TextView) getView().findViewById(R.id.InboundOrderNo);
        mivchoose = (ImageView) getView().findViewById(R.id.iv_choose);
        mivchoose.setOnClickListener(this);
        //Choose RFID or Barcode popupwindow
        mViewChooseBorR = mContext.getLayoutInflater().inflate(R.layout.chooseborr, null);
        //finqty popupwindow
        mViewFixQty = mContext.getLayoutInflater().inflate(R.layout.fixqty, null);
        //listViewDataList   **
        mlvComsumeLotInbound = (ListView) getView().findViewById(R.id.lv_consumeLotInbound);
        mConsumeLotInboundListViewAdapter = new ConsumeLotInboundListViewAdapter(mContext, mcomsumeLotInboundDataList);
        mlvComsumeLotInbound.setAdapter(mConsumeLotInboundListViewAdapter);
        mlvComsumeLotInbound.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fixQty(view, position);
                return false;
            }
        });
    }
    //endregion

    @SuppressLint("WrongConstant")
    private void fixQty(View view, final int position) {
        final ConsumeLotInboundData tempdata = mcomsumeLotInboundDataList.get(position);
        Button btnConfirm = (Button) mViewFixQty.findViewById(R.id.btnfqty);
        final EditText etFixQty = (EditText) mViewFixQty.findViewById(R.id.etfqty);
        etFixQty.setText(tempdata.getINQTY() + "");
        etFixQty.setSelection(etFixQty.length());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempdata.setINQTY(etFixQty.getText().toString());
                if (Float.parseFloat(tempdata.getINQTY()) > Float.parseFloat(tempdata.getPLANQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(tempdata.getINQTY()) == Float.parseFloat(tempdata.getPLANQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymatch);
                } else {
                    tempdata.setBackgroundColor(R.color.qtyless);
                }
                mcomsumeLotInboundDataList.set(position, tempdata);
                mConsumeLotInboundListViewAdapter.notifyDataSetChanged();
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

    //region getConsumeLotInboundByInboundOrder
    public void getConsumeLotInboundByInboundOrder(String inboundNo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetConsumeLotInboundQuery), inboundNo);
        Log.e("sql语句lotInbound", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mcomsumeLotInboundDataList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                ConsumeLotInboundData consumeLotInboundData = new ConsumeLotInboundData();
                consumeLotInboundData.setCONSUMABLEDEFID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFID)).trim());
                consumeLotInboundData.setCONSUMABLELOTID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLELOTID)));
                consumeLotInboundData.setUNIT(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.UNIT)));
                consumeLotInboundData.setORDERNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.ORDERNO)));
                consumeLotInboundData.setORDERTYPE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.ORDERTYPE)));
                consumeLotInboundData.setLINENO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.LINENO)));
                consumeLotInboundData.setWAREHOUSEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.WAREHOUSEID)));
                consumeLotInboundData.setCONSUMABLEDEFNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFID)));
                consumeLotInboundData.setCONSUMABLEDEFVERSION(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFVERSION)));
                consumeLotInboundData.setTAGID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.TAGID)));
                consumeLotInboundData.setTAGQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.TAGQTY)));
                if (cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.INQTY)).equals("null") ||
                        cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.INQTY)).equals("")) {
                    consumeLotInboundData.setINQTY("0");
                } else {
                    consumeLotInboundData.setINQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.INQTY)));
                }
                consumeLotInboundData.setPLANQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PLANQTY)));
                if (Float.parseFloat(consumeLotInboundData.getINQTY()) > Float.parseFloat(consumeLotInboundData.getPLANQTY())) {
                    consumeLotInboundData.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(consumeLotInboundData.getINQTY()) == Float.parseFloat(consumeLotInboundData.getPLANQTY())) {
                    consumeLotInboundData.setBackgroundColor(R.color.qtymatch);
                } else {
                    consumeLotInboundData.setBackgroundColor(R.color.qtyless);
                }
                Log.e("consumeLotInboundData",consumeLotInboundData.toString());
                mcomsumeLotInboundDataList.add(consumeLotInboundData);
            }
        }
        tvtagqty.setText(mcomsumeLotInboundDataList.size() + "");
        tvInboundOrderNo.setText(inboundNo);
        InboundOrderNo = inboundNo;
        mConsumeLotInboundListViewAdapter.notifyDataSetChanged();
        Message message = new Message();
        message.what = UPDATEUI;
        message.obj = 0;
        handler.sendMessage(message);
    }
    //endregion

    //region findTagInList
    private int findTagInList(String tagid) {
        int returnint = -1;
        for (int i = 0; i < mcomsumeLotInboundDataList.size(); i++)
            if (mcomsumeLotInboundDataList.get(i).getTAGID().equals(tagid)) {
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
        if (tagposition == -1) {
            Log.e("indexhand",  tagID + "不在这个list里面");
        } else {
            Log.e("indexhand", tagID  + "在列表里，替换Inqty的值");
            final ConsumeLotInboundData tempdata = mcomsumeLotInboundDataList.get(tagposition);
            tempdata.setINQTY(tempdata.getTAGQTY().toString());
            if (Float.parseFloat(tempdata.getINQTY()) > Float.parseFloat(tempdata.getPLANQTY())) {
                tempdata.setBackgroundColor(R.color.qtymore);
            } else if (Float.parseFloat(tempdata.getINQTY()) == Float.parseFloat(tempdata.getPLANQTY())) {
                tempdata.setBackgroundColor(R.color.qtymatch);
            } else {
                tempdata.setBackgroundColor(R.color.qtyless);
            }
            mcomsumeLotInboundDataList.set(tagposition, tempdata);
            mConsumeLotInboundListViewAdapter.notifyDataSetChanged();
            Message message = new Message();
            message.what = UPDATEUI;
            message.obj = tagposition;
            handler.sendMessage(message);
        }
    }
    //endregion

    //region actionSearch
    protected void actionSearch() {

    }
    //endregion

    private void  actionSave(){
        Log.e("保存","保存ConsumeLotInbound");
        try {
            for (int i = 0; i < mcomsumeLotInboundDataList.size(); i++) {
                ConsumeLotInboundData data = mcomsumeLotInboundDataList.get(i);
                ContentValues values = new ContentValues();
                values.put(Constant.INQTY,data.getINQTY());
                db.update(Constant.SF_CONSUMELOTINBOUND,values,"INBOUNDNO = ? AND " +
                                "CONSUMABLEDEFID =? AND CONSUMABLEDEFVERSION = ? AND WAREHOUSEID = ?" +
                                "AND ORDERNO = ? AND ORDERTYPE = ? AND LINENO = ? AND CONSUMABLELOTID = ?",
                        new String[]{InboundOrderNo,data.getCONSUMABLEDEFID(), data.getCONSUMABLEDEFVERSION(),
                                data.getWAREHOUSEID(),data.getORDERNO(),data.getORDERTYPE(),
                                data.getLINENO(),data.getCONSUMABLELOTID()});
            }
            ContentValues values = new ContentValues();
            values.put(Constant.ISPDASAVE, "Y");
            db.update(Constant.SF_INBOUNDORDER, values, "INBOUNDNO =?", new String[]{InboundOrderNo});
            Utils.showToast(mContext,"保存成功",0);
        }catch (Exception e){
            Utils.showToast(mContext,e.getMessage(),0);
        }


    }

    //region actionClearAll
    public void actionClearAll() {
        if (mcomsumeLotInboundDataList.size() > 0) {
            mcomsumeLotInboundDataList.clear();
        }
        mConsumeLotInboundListViewAdapter.notifyDataSetChanged();
        etTagID.setText("");
        tvtagqty.setText("0");
        tvInboundOrderNo.setText("");
        InboundOrderNo = "";
        TagInfoData data = new TagInfoData();
        data.setEnableFlag(true);
        data.setClearFlag(true);
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
                Message msg = handler.obtainMessage();
                msg.what = RFIDSCAN;
                msg.obj = barcode;
                handler.sendMessage(msg);
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