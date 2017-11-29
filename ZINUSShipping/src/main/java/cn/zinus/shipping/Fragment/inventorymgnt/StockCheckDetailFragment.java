package cn.zinus.shipping.Fragment.inventorymgnt;

import android.app.ProgressDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Adapter.StockCheckDetailListViewAdapter;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.JaveBean.StockCheckData;
import cn.zinus.shipping.JaveBean.StockCheckDeatilData;
import cn.zinus.shipping.JaveBean.TagInfoData;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;

import static cn.zinus.shipping.util.Utils.showToast;
import static com.micube.control.util.Server.hexStringToString;

/**
 * Developer:Spring
 * DataTime :2017/9/25 13:40
 * Main Change:
 */

public class StockCheckDetailFragment extends KeyDownFragment implements View.OnClickListener {

    //region ◆ 변수(Variables)
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式
    //上下文
    private MainNaviActivity mContext;
    //popwindow
    private ImageView mivchoose;
    private PopupWindow mpopChooseBorR;
    private View mViewChooseBorR;
    //ListView
    private ListView mlvStockCheckDetail;
    private StockCheckDetailListViewAdapter mStockCheckDetailListViewAdapter;
    private ArrayList<StockCheckDeatilData> mStockCheckDeatilDataArrayList;
    private ArrayList<String> IDlist;
    private TextView tvtagqty;
    private TextView tvCheckMonth;
    private PopupWindow mpopFixQty;
    private View mViewFixQty;
    //public String InboundOrderNo;
    //
    private EditText etTagID;
    private int BRFlag = 1;
    //RadioGroup
    private RadioGroup RgInventory;
    private RadioButton rbSingle;
    private RadioButton rbAuto;
    // private Handler handler;
    private boolean threadStop = true;
    private Thread thread;
    //Auto Read
    Handler handler = null;
    boolean loopFlag = true;
    boolean scanFlag = false;
    public Barcode2DWithSoft BaecodeReader;
    private ProgressDialog myDialog;
    private boolean dismessDialogFlag = false;
    //数据库
    MyDateBaseHelper mHelper;
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
    }
    //endregion

    //region onCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stockcheckdetail, container, false);
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
                String tagid = msg.obj + "";
                String tagID = hexStringToString(tagid);
                if (!tagID.equals("")) {
                    checkAndSearchWeb(tagID);
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
                RgInventory.setVisibility(View.VISIBLE);
                showToast(mContext, "RFID", 0);
                mivchoose.setImageResource(R.drawable.rfidicon);
                if (BaecodeReader != null) {
                    BaecodeReader.stopScan();
                }
                mpopChooseBorR.dismiss();
                break;
            case R.id.btnbarcode:
                //选择R&B的popupwindow里面的Barcode按钮
                RgInventory.setVisibility(View.GONE);
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
        mStockCheckDeatilDataArrayList = new ArrayList<>();
        IDlist = new ArrayList<>();
    }
    //endregion

    //region initView
    private void initview() {
        etTagID = (EditText) getView().findViewById(R.id.etTagID);
        tvtagqty = (TextView) getView().findViewById(R.id.tvtagqty);
        tvCheckMonth = (TextView) getView().findViewById(R.id.CheckMonth);
        mivchoose = (ImageView) getView().findViewById(R.id.iv_choose);
        mivchoose.setOnClickListener(this);
        RgInventory = (RadioGroup) getView().findViewById(R.id.rgRfidScan);
        RgInventory.setOnCheckedChangeListener(new RgInventoryCheckedListener());
        rbSingle = (RadioButton) getView().findViewById(R.id.rbSingle);
        rbAuto = (RadioButton) getView().findViewById(R.id.rbAuto);
        mViewFixQty = mContext.getLayoutInflater().inflate(R.layout.fixqty, null);
        //Choose RFID or Barcode popupwindow
        mViewChooseBorR = mContext.getLayoutInflater().inflate(R.layout.chooseborr, null);
        //listViewDataList   **
        mlvStockCheckDetail = (ListView) getView().findViewById(R.id.lv_StockCheckDetail);
        mStockCheckDetailListViewAdapter = new StockCheckDetailListViewAdapter(mContext, mStockCheckDeatilDataArrayList);
        mlvStockCheckDetail.setAdapter(mStockCheckDetailListViewAdapter);
        mlvStockCheckDetail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fixQty(view,position);
                return false;
            }
        });
    }

    //endregion

    private void fixQty(View view, final int position) {
        final StockCheckDeatilData tempdata = mStockCheckDeatilDataArrayList.get(position);
        Button btnConfirm = (Button) mViewFixQty.findViewById(R.id.btnfqty);
        final EditText etFixQty = (EditText) mViewFixQty.findViewById(R.id.etfqty);
        etFixQty.setText(tempdata.getQTY() + "");
        etFixQty.setSelection(etFixQty.length());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempdata.setQTY(etFixQty.getText().toString());
                mStockCheckDeatilDataArrayList.set(position, tempdata);
                mStockCheckDetailListViewAdapter.notifyDataSetChanged();
                mlvStockCheckDetail.setSelection(position);
                tvtagqty.setText( ""+ mStockCheckDeatilDataArrayList.size());
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

    //region getStockCheckDetailbyCheckMonth
    public void getStockCheckDetailbyCheckMonth(StockCheckData stockCheck) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetStockCheckDetailQuery),stockCheck.getCHECKMONTH(), stockCheck.getWAREHOUSEID());
        Log.e("sql语句StockCheckDetail", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mStockCheckDeatilDataArrayList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                StockCheckDeatilData stockCheckDetailData = new StockCheckDeatilData();
                stockCheckDetailData.setCONSUMEABLDEFNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFNAME)));
                stockCheckDetailData.setWAREHOUSEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.WAREHOUSEID)));
                stockCheckDetailData.setUNIT(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.UNIT)));
                stockCheckDetailData.setQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.QTY)));
                if (cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)).equals("null") ||
                        cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)).equals("")) {
                    stockCheckDetailData.setCHECKQTY("0");
                } else {
                    stockCheckDetailData.setCHECKQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)));
                }
                mStockCheckDeatilDataArrayList.add(stockCheckDetailData);
            }
        }
        tvtagqty.setText(mStockCheckDeatilDataArrayList.size() + "");
        tvCheckMonth.setText(stockCheck.getCHECKMONTH());
        mStockCheckDetailListViewAdapter.notifyDataSetChanged();
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
            if (loopFlag) {
                if (!scanFlag) {
                    scanFlag = true;
                    if (mContext.mRFIDWithUHF.startInventoryTag((byte) 0, (byte) 0)) {
                        setViewEnabled(false);
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
                        setViewEnabled(true);
                    }
                } else {
                    stopInventory();
                }
            } else {
                reaRFIDTag();
            }
        } else if (BRFlag == 2) {
            readBarcodeTag();
        }
    }
    //endregion

    //region setViewEnabled
    private void setViewEnabled(boolean enabled) {
        rbAuto.setEnabled(enabled);
        rbSingle.setEnabled(enabled);
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
        if (checkIsExist(tagID) == -1) {
            Log.e("indexhand", "列表里不存在" + tagID + "查询数据库");
            IDlist.add(tagID);
        } else {
            Log.e("indexhand", "列表里存在" + tagID + "继续读");
        }
    }
    //endregion

    //region updateDataByScan
//    private void updateDataByScan(ImportOrderScanData data) {
//        String TagID = data.getTagID();
//        for (int i = 0; i < mcomsumeOutboundDataList.size(); i++) {
//            if (mcomsumeOutboundDataList.get(i).getTagID().equals(TagID)) {
//                mcomsumeOutboundDataList.get(i).setOKFlag(true);
//                int realqty = Integer.parseInt(mcomsumeOutboundDataList.get(i).getRealQty());
//                mcomsumeOutboundDataList.get(i).setRealQty(Integer.parseInt(data.getQty()) + "");
//                ImportOrderItemData temp = mcomsumeOutboundDataList.get(i);
//                int qty = Integer.parseInt(temp.getQty()) - Integer.parseInt(temp.getRealQty());
//                if (qty > 0) {
//                    mcomsumeOutboundDataList.get(i).setColor(Color.RED);
//                } else if (qty == 0) {
//                    mcomsumeOutboundDataList.get(i).setColor(Color.GREEN);
//                } else {
//                    mcomsumeOutboundDataList.get(i).setColor(Color.YELLOW);
//                }
//                adapter.notifyDataSetChanged();
//                tvtagqty.setText(":" + mcomsumeOutboundDataList.size());
//            }
//        }
//    }
    //endregion

    //region actionSearch
    protected void actionSearch() {
        //getStockCheckDetailbyCheckMonth("1111111");
    }
    //endregion

    //region actionClearAll
    public void actionClearAll() {
        mStockCheckDeatilDataArrayList.clear();
        mStockCheckDetailListViewAdapter.notifyDataSetChanged();
        IDlist.clear();
        etTagID.setText("");
        tvtagqty.setText("0");
        tvCheckMonth.setText("");
        TagInfoData data = new TagInfoData();
        data.setEnableFlag(true);
        data.setClearFlag(true);
    }
    //endregion

    //region refreshListView
    public void refreshListView() {
        Log.e("StockCheck00000", mStockCheckDeatilDataArrayList.get(0).getCHECKQTY());
        Log.e("StockCheck11111", mStockCheckDeatilDataArrayList.get(1).getCHECKQTY());
        Log.e("StockCheck22222", mStockCheckDeatilDataArrayList.get(2).getCHECKQTY());
        mStockCheckDetailListViewAdapter.notifyDataSetChanged();
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

    //region RadioGroup CheckedChangeListener
    public class RgInventoryCheckedListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == rbSingle.getId()) {
                //单次识别
                loopFlag = false;
            } else if (checkedId == rbAuto.getId()) {
                //循环识别
                loopFlag = true;
            }
        }
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
            setViewEnabled(true);

            if (mContext.mRFIDWithUHF.stopInventory()) {
                //BtnReadTag.setText(mContext.getString(R.string.title_start_Inventory));
            } else {

            }
        }
    }
    //endregion

    //endregion

}