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

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Adapter.StockLotCheckDetailListViewAdapter;
import cn.zinus.warehouse.Config.AppConfig;
import cn.zinus.warehouse.Fragment.Event;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.JaveBean.StockCheckDeatilData;
import cn.zinus.warehouse.JaveBean.StockLotCheckDeatilData;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;
import cn.zinus.warehouse.util.Utils;

import static cn.zinus.warehouse.util.Constant.RFIDSCAN;
import static cn.zinus.warehouse.util.Constant.UPDATEUI;
import static cn.zinus.warehouse.util.DBManger.getCursorData;
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

    private PopupWindow mpopConfirmQty;
    private View mViewConfirmQty;
    //ListView
    private ListView mlvStockLotCheckDetail;
    private StockLotCheckDetailListViewAdapter mStockLotCheckDetailListViewAdapter;
    private ArrayList<StockLotCheckDeatilData> mStockLotCheckDeatilDataArrayList;
    private ArrayList<StockLotCheckDeatilData> _mStockLotCheckDeatilDataArrayList;
    private TextView tvConsumabledefId;
    private TextView tvlistSumQty;
    private TextView tvconsumabledefname;
    private TextView tvStockLotCheckedSum;
    private TextView tvCheckMonth;
    private TextView tvnoStockChectLotQTY;
    //
    private EditText etTagID;
    private int BRFlag = 1;
    private int FIXFlag = 1;
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
    boolean _savePlanFlag = false;
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
                    case RFIDSCAN:
                        String tagid = msg.obj + "";
                        String tagID = hexStringToString(tagid);
                        if (tagID.length() == 12) {

                            if (FIXFlag == 1) {
                                Log.e("盘点模式", tagID);
                                checkAndSearchWeb(tagID);
                            } else {
                                Log.e("修改数量模式", tagID);
                                checkAndFixQTY(tagID);
                            }
                        }
                        break;
                    case UPDATEUI:
                        int position = (Integer) msg.obj;
                        if (position == -1) {
                            //-1代表刚扫上标签，用EventBus来更新stockcheck的数据，并更新db
                            updatestockcheckDetail(mStockLotCheckDeatilDataArrayList.get(mStockLotCheckDeatilDataArrayList.size() - 1));
                        } else {
                            mlvStockLotCheckDetail.setSelection(position);
                            //修改好数量以后，用EventBus来更新stockcheck的数据，并更新db
                            updatestockcheckDetail(mStockLotCheckDeatilDataArrayList.get(position));
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
                FIXFlag = 1;
                showToast(mContext, "盘点模式", 0);
                mivchoose.setImageResource(R.drawable.rfidicon);
                if (BaecodeReader != null) {
                    BaecodeReader.stopScan();
                }
                mpopChooseBorR.dismiss();
                break;
            case R.id.btnbarcode:
                //选择R&B的popupwindow里面的Barcode按钮
                showToast(mContext, "修改数据模式", 0);
                //BRFlag = 2;
                FIXFlag = 2;
                mivchoose.setImageResource(R.drawable.fix);
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
        _mStockLotCheckDeatilDataArrayList = new ArrayList<>();
    }
    //endregion

    //region initView()
    private void initview() {
        etTagID = (EditText) getView().findViewById(R.id.etTagID);
        tvConsumabledefId = (TextView) getView().findViewById(R.id.tv_ConsumabledefId);
        tvlistSumQty = (TextView) getView().findViewById(R.id.tv_listSumQty);
        tvconsumabledefname = (TextView) getView().findViewById(R.id.tv_consumabledefname);
        tvStockLotCheckedSum = (TextView) getView().findViewById(R.id.tv_StockLotCheckedSum);
        tvCheckMonth = (TextView) getView().findViewById(R.id.tv_CheckMonth);
        tvnoStockChectLotQTY = (TextView) getView().findViewById(R.id.tv_noStockChectLotQTY);
        mivchoose = (ImageView) getView().findViewById(R.id.iv_choose);
        mivchoose.setOnClickListener(this);
        //Choose RFID or Barcode popupwindow
        mViewChooseBorR = mContext.getLayoutInflater().inflate(R.layout.readorfix, null);
        //finqty popupwindow
        mViewFixQty = mContext.getLayoutInflater().inflate(R.layout.confirmqty, null);
        mViewConfirmQty = mContext.getLayoutInflater().inflate(R.layout.confirmqty, null);
        //listViewDataList   **
        mlvStockLotCheckDetail = (ListView) getView().findViewById(R.id.lv_StockLotCheckDetail);
        mStockLotCheckDetailListViewAdapter = new StockLotCheckDetailListViewAdapter(mContext, mStockLotCheckDeatilDataArrayList);
        mlvStockLotCheckDetail.setAdapter(mStockLotCheckDetailListViewAdapter);
        mlvStockLotCheckDetail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fixQty(view, position, mStockLotCheckDeatilDataArrayList.get(position));
                return false;
            }
        });
    }
    //endregion

    //region fixQty
    @SuppressLint("WrongConstant")
    private void fixQty(View view, final int position, final StockLotCheckDeatilData stockLotCheckDeatilData) {
        Button btnConfirm = (Button) mViewFixQty.findViewById(R.id.btnfqty);
        TextView tvRFID = (TextView) mViewFixQty.findViewById(R.id.tv_consumablelotid);
        final EditText etFixQty = (EditText) mViewFixQty.findViewById(R.id.etfqty);
        etFixQty.setText(stockLotCheckDeatilData.getTAGQTY() + "");
        etFixQty.setSelection(etFixQty.length());
        String a = stockLotCheckDeatilData.getCONSUMABLELOTID();
        tvRFID.setText(a);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockLotCheckDeatilData.setCHECKQTY(etFixQty.getText().toString());
                if (Float.parseFloat(stockLotCheckDeatilData.getCHECKQTY()) > Float.parseFloat(stockLotCheckDeatilData.getQTY())) {
                    stockLotCheckDeatilData.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(stockLotCheckDeatilData.getCHECKQTY()) == Float.parseFloat(stockLotCheckDeatilData.getQTY())) {
                    stockLotCheckDeatilData.setBackgroundColor(R.color.qtymatch);
                } else {
                    stockLotCheckDeatilData.setBackgroundColor(R.color.qtyless);
                }
                String localUserid = AppConfig.getInstance().getString(Constant.UserID, null);
                String localUsername = AppConfig.getInstance().getString(Constant.UserName, null);
                if (!stockLotCheckDeatilData.getUSERID().equals(localUserid)) {
                    stockLotCheckDeatilData.setUSERID(localUserid);
                    stockLotCheckDeatilData.setUSERNAME(localUsername);
                }
                mStockLotCheckDeatilDataArrayList.set(position, stockLotCheckDeatilData);
                mStockLotCheckDetailListViewAdapter.notifyDataSetChanged();
                Message message = new Message();
                message.what = UPDATEUI;
                message.obj = position;
                handler.sendMessage(message);
                setQTYInfo();
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

    //region openPopWindowToConfirmQTY
    @SuppressLint("WrongConstant")
    private void openPopWindowToConfirmQTY(View view, final StockLotCheckDeatilData stockLotCheckDeatilData) {

        Button btnConfirm = (Button) mViewConfirmQty.findViewById(R.id.btnfqty);
        TextView tvRFID = (TextView) mViewConfirmQty.findViewById(R.id.tv_consumablelotid);
        final EditText etFixQty = (EditText) mViewConfirmQty.findViewById(R.id.etfqty);
        etFixQty.setText(stockLotCheckDeatilData.getTAGQTY() + "");
        etFixQty.setSelection(etFixQty.length());
        tvRFID.setText(stockLotCheckDeatilData.getTAGID());

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockLotCheckDeatilData.setCHECKQTY(etFixQty.getText().toString());
                if (Float.parseFloat(stockLotCheckDeatilData.getCHECKQTY()) > Float.parseFloat(stockLotCheckDeatilData.getQTY())) {
                    stockLotCheckDeatilData.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(stockLotCheckDeatilData.getCHECKQTY()) == Float.parseFloat(stockLotCheckDeatilData.getQTY())) {
                    stockLotCheckDeatilData.setBackgroundColor(R.color.qtymatch);
                } else {
                    stockLotCheckDeatilData.setBackgroundColor(R.color.qtyless);
                }
                String localUserid = AppConfig.getInstance().getString(Constant.UserID, null);
                String localUsername = AppConfig.getInstance().getString(Constant.UserName, null);
                if (!stockLotCheckDeatilData.getUSERID().equals(localUserid)) {
                    stockLotCheckDeatilData.setUSERID(localUserid);
                    stockLotCheckDeatilData.setUSERNAME(localUsername);
                }
                mStockLotCheckDeatilDataArrayList.add(stockLotCheckDeatilData);
                mStockLotCheckDetailListViewAdapter.notifyDataSetChanged();
                Message message = new Message();
                message.what = UPDATEUI;
                message.obj = -1;
                setQTYInfo();
                handler.sendMessage(message);
                mpopConfirmQty.dismiss();
            }
        });
        mpopConfirmQty = new PopupWindow(mViewConfirmQty, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mpopConfirmQty.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mpopConfirmQty.setTouchable(true);
        mpopConfirmQty.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mpopConfirmQty.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mpopConfirmQty.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        mpopConfirmQty.showAtLocation(view, Gravity.CENTER, 0, 0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) etFixQty.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etFixQty, 0);
            }
        }, 500);
    }
    //endregion

    //region getStockLotCheckDetailbystockCheckDeatilData
    public void getStockLotCheckDetailbystockCheckDeatilData(StockCheckDeatilData stockCheckDeatilData) {
        String selectDataListsql = String.format(getString(R.string.GetStockLotCheckDetailQuery)
                , stockCheckDeatilData.getCONSUMEABLDEFID(), stockCheckDeatilData.getCONSUMEABLDEFVERSION());
        Log.e("StockLotCheckDetail", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mStockLotCheckDeatilDataArrayList.clear();
        _mStockLotCheckDeatilDataArrayList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                StockLotCheckDeatilData stockLotCheckDeatilData = new StockLotCheckDeatilData();
                stockLotCheckDeatilData.setWAREHOUSEID(getCursorData(cursorDatalist, Constant.WAREHOUSEID).trim());
                stockLotCheckDeatilData.setCHECKMONTH(getCursorData(cursorDatalist, Constant.CHECKMONTH).trim());
                stockLotCheckDeatilData.setCONSUMABLEDEFNAME(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFNAME).trim());
                stockLotCheckDeatilData.setCONSUMABLEDEFID(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFID).trim());
                stockLotCheckDeatilData.setCONSUMABLEDEFVERSION(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFVERSION).trim());
                stockLotCheckDeatilData.setUNIT(getCursorData(cursorDatalist, Constant.UNIT).trim());
                stockLotCheckDeatilData.setQTY(getCursorData(cursorDatalist, Constant.QTY).trim());
                stockLotCheckDeatilData.setUSERID(getCursorData(cursorDatalist, Constant.USERID).trim());
                stockLotCheckDeatilData.setUSERNAME(getCursorData(cursorDatalist, Constant.USERNAME).trim());
                stockLotCheckDeatilData.setCHECKUNIT(getCursorData(cursorDatalist, Constant.CHECKUNIT).trim());
                stockLotCheckDeatilData.setCONSUMABLELOTID(getCursorData(cursorDatalist, Constant.CONSUMABLELOTID).trim());
                stockLotCheckDeatilData.setTAGID(getCursorData(cursorDatalist, Constant.TAGID).trim());
                stockLotCheckDeatilData.setTAGQTY(getCursorData(cursorDatalist, Constant.TAGQTY).trim());
                if (getCursorData(cursorDatalist, Constant.CHECKQTY).trim().equals("null") ||
                        getCursorData(cursorDatalist, Constant.CHECKQTY).trim().equals("")) {
                    stockLotCheckDeatilData.setCHECKQTY("0");
                } else {
                    stockLotCheckDeatilData.setCHECKQTY(getCursorData(cursorDatalist, Constant.CHECKQTY).trim());
                }
                if (Float.parseFloat(stockLotCheckDeatilData.getCHECKQTY()) > Float.parseFloat(stockLotCheckDeatilData.getQTY())) {
                    stockLotCheckDeatilData.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(stockLotCheckDeatilData.getCHECKQTY()) == Float.parseFloat(stockLotCheckDeatilData.getQTY())) {
                    stockLotCheckDeatilData.setBackgroundColor(R.color.qtymatch);
                } else {
                    stockLotCheckDeatilData.setBackgroundColor(R.color.qtyless);
                }
              //  Log.e("stockLotCheckDeatilData", stockLotCheckDeatilData.toString());
                if (!stockLotCheckDeatilData.getCHECKQTY().equals("0")) {
                    mStockLotCheckDeatilDataArrayList.add(stockLotCheckDeatilData);
                }
                _mStockLotCheckDeatilDataArrayList.add(stockLotCheckDeatilData);
            }
            mStockLotCheckDetailListViewAdapter.notifyDataSetChanged();
        }
        setQTYInfo();
        tvStockLotCheckedSum.setText(_mStockLotCheckDeatilDataArrayList.size() + "");
        tvConsumabledefId.setText(stockCheckDeatilData.getCONSUMEABLDEFID());
        tvconsumabledefname.setText(stockCheckDeatilData.getCONSUMEABLDEFNAME());
        tvCheckMonth.setText(stockCheckDeatilData.getCHECKMONTH());
    }

    private void setQTYInfo() {
        tvlistSumQty.setText(mStockLotCheckDeatilDataArrayList.size() + "");
        tvnoStockChectLotQTY.setText((_mStockLotCheckDeatilDataArrayList.size() - mStockLotCheckDeatilDataArrayList.size()) + "");
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
                    new TagThread(50).start();
                } else {
                    Log.e("扫描出问题了", "重啟");
                    showToast(mContext, "扫描模块异常，请再按一次", 0);
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

        } else {
            threadStop = true;
        }
    }
    //endregion

    //region checkAndSearchWeb
    private void checkAndSearchWeb(String tagID) {
        //第一次盘点
        int tagposition = findTagInDBList(tagID);
        if (tagposition == -2) {
            //已经盘点过的返回-2
            showToast(mContext, tagID + "已经盘点过了", 0);
        } else if (tagposition == -1) {
            Log.e("indexhand", tagID + "不在这个list里面");
            showToast(mContext, tagID + "不是这个列表里面的标签", 0);
        } else {
            Log.e("indexhand", tagID + "在列表里，替换Inqty的值");
            //扫到一个新的lot，打开PopWindow
            //stopInventory();
            //openPopWindowToConfirmQTY(getView(), _mStockLotCheckDeatilDataArrayList.get(tagposition));
            StockLotCheckDeatilData stockLotCheckDeatilData =  _mStockLotCheckDeatilDataArrayList.get(tagposition);
            stockLotCheckDeatilData.setCHECKQTY("1");
            if (Float.parseFloat(stockLotCheckDeatilData.getCHECKQTY()) > Float.parseFloat(stockLotCheckDeatilData.getQTY())) {
                stockLotCheckDeatilData.setBackgroundColor(R.color.qtymore);
            } else if (Float.parseFloat(stockLotCheckDeatilData.getCHECKQTY()) == Float.parseFloat(stockLotCheckDeatilData.getQTY())) {
                stockLotCheckDeatilData.setBackgroundColor(R.color.qtymatch);
            } else {
                stockLotCheckDeatilData.setBackgroundColor(R.color.qtyless);
            }
            String localUserid = AppConfig.getInstance().getString(Constant.UserID, null);
            String localUsername = AppConfig.getInstance().getString(Constant.UserName, null);
            if (!stockLotCheckDeatilData.getUSERID().equals(localUserid)) {
                stockLotCheckDeatilData.setUSERID(localUserid);
                stockLotCheckDeatilData.setUSERNAME(localUsername);
            }
            mStockLotCheckDeatilDataArrayList.add(stockLotCheckDeatilData);
            mStockLotCheckDetailListViewAdapter.notifyDataSetChanged();
            Message message = new Message();
            message.what = UPDATEUI;
            message.obj = -1;
            setQTYInfo();
            handler.sendMessage(message);
        }
    }
    //endregion

    //region checkAndFixQTY
    private void checkAndFixQTY(String tagID) {
        //修改数据模式下，扫描到列表里的tag的时候就会弹出popwindow
        int tagposition = findTagInList(tagID);
        if (tagposition == -1) {
            Log.e("indexhand", tagID + "不在这个list里面");
            showToast(mContext, tagID + "是没有盘点过的标签", 0);
        } else {
            Log.e("indexhand", tagID + "在列表里，替换Inqty的值");
            //扫到一个新的lot，打开PopWindow
            stopInventory();
            fixQty(getView(), tagposition, mStockLotCheckDeatilDataArrayList.get(tagposition));
        }
    }
    //endregion

    //region findTagInDBList
    //找第一次扫描的
    private int findTagInDBList(String tagid) {
        int returnint = -1;
        for (int i = 0; i < _mStockLotCheckDeatilDataArrayList.size(); i++) {
            StockLotCheckDeatilData dbdata = _mStockLotCheckDeatilDataArrayList.get(i);
            if (dbdata.getTAGID().equals(tagid)) {
                //说明是这个资材的标签,现在确认一下有没有被
                returnint = i;
                for (int j = 0; j < mStockLotCheckDeatilDataArrayList.size(); j++) {
                    StockLotCheckDeatilData listdata = mStockLotCheckDeatilDataArrayList.get(j);
                    if (listdata.getTAGID().equals(tagid)) {
                        returnint = -2;
                        break;
                    }
                }
                break;
            }
        }
        return returnint;
    }
    //endregion

    //region findTagInList
    private int findTagInList(String tagid) {
        int returnint = -1;
        for (int i = 0; i < mStockLotCheckDeatilDataArrayList.size(); i++) {
            StockLotCheckDeatilData data = mStockLotCheckDeatilDataArrayList.get(i);
            Log.e("list中的数据", data.toString());
            if (data.getTAGID().equals(tagid)) {
                returnint = i;
                break;
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
        if (_mStockLotCheckDeatilDataArrayList.size() > 0) {
            _mStockLotCheckDeatilDataArrayList.clear();
        }
        mStockLotCheckDetailListViewAdapter.notifyDataSetChanged();
        tvConsumabledefId.setText("");
        tvlistSumQty.setText("0");
        tvconsumabledefname.setText("");
        tvStockLotCheckedSum.setText("0");
        tvCheckMonth.setText("");
        tvnoStockChectLotQTY.setText("0");
        etTagID.setText("");
    }
    //endregion

    //region updatestockcheckDetail
    private void updatestockcheckDetail(StockLotCheckDeatilData data) {
        try {
            //先保存数据库的数据CHECKQTY
            ContentValues values = new ContentValues();
            Log.e("保存了", data.toString());
            values.put(Constant.CHECKQTY, data.getCHECKQTY());
            values.put(Constant.USERID, AppConfig.getInstance().getString(Constant.UserID, null));
            db.update(Constant.SF_STOCKLOTCHECKDETAIL, values, "WAREHOUSEID = ? AND CHECKMONTH = ?" +
                            " AND CONSUMABLEDEFID = ? AND CONSUMABLEDEFVERSION = ? AND CONSUMABLELOTID = ?"
                    , new String[]{data.getWAREHOUSEID(), data.getCHECKMONTH()
                            , data.getCONSUMABLEDEFID(), data.getCONSUMABLEDEFVERSION(), data.getCONSUMABLELOTID()});
            //第一次修改数据的时候把STOCKCHECK表的标志位改成Y
            if (!_savePlanFlag) {
                ContentValues values1 = new ContentValues();
                values1.put(Constant.ISPDASAVE, "Y");
                _savePlanFlag = true;
                db.update(Constant.SF_STOCKCHECK, values1, "WAREHOUSEID = ? AND CHECKMONTH = ?"
                        , new String[]{data.getWAREHOUSEID(), data.getCHECKMONTH()});
            }
            //查询SF_STOCKLOTCHECKDETAIL表的总数，来修改SF_STOCKCHECKDETAIL表的内容
            String selectDataListsql = String.format(getString(R.string.GetStockCheckDetailQTYQuery)
                    , data.getCONSUMABLEDEFID(), data.getCONSUMABLEDEFVERSION());
            Log.e("GetStockCheckDetailQTY", selectDataListsql);
            Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
            if (cursorDatalist.getCount() != 0) {
                while (cursorDatalist.moveToNext()) {
                    String SUMqty = getCursorData(cursorDatalist, "SUM");
                    EventBus.getDefault().post(new Event.StockCheckDetailbyLotCheckEvent(data, SUMqty));
                }
            }
        } catch (Exception e) {
            Utils.showToast(mContext, e.getMessage(), 0);
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