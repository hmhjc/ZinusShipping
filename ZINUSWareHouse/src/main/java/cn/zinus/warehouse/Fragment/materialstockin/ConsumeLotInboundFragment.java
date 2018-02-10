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

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Adapter.ConsumeLotInboundListViewAdapter;
import cn.zinus.warehouse.Fragment.Event;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.JaveBean.ConsumeInboundData;
import cn.zinus.warehouse.JaveBean.ConsumeLotInboundData;
import cn.zinus.warehouse.JaveBean.TagInfoData;
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
    private PopupWindow mpopConfirmQty;
    private View mViewConfirmQty;
    //ListView
    private ListView mlvComsumeLotInbound;
    private ConsumeLotInboundListViewAdapter mConsumeLotInboundListViewAdapter;
    private ArrayList<ConsumeLotInboundData> mcomsumeLotInboundDataList;
    private ArrayList<ConsumeLotInboundData> _mcomsumeLotInboundDataList; //记录所有的lot
    private TextView tvSpec_Desc;
    private TextView tvtagqty;
    private TextView tvInboundOrderNo;
    private TextView tvConsumabledefId;
    private TextView tvConsumabledefname;
    private TextView tvConsumeLotInboundSum;
    private TextView tvlistSumQty;
    private TextView tvnoconsumeinboundLotQTY;
    private TextView tvORDERNO;
    private TextView tvORDERTYPE;
    private TextView tvLINENO;
    private TextView tvORDERCOMPANY;
    private String InboundOrderNo;
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
                        if (tagID.length() == 12) {
                            if (FIXFlag == 1) {
                                Log.e("入库模式", tagID);
                                checkAndSearchDB(tagID);
                            } else {
                                Log.e("修改数量模式", tagID);
                                checkAndFixQTY(tagID);
                            }
                        }
                        break;
                    case UPDATEUI:
                        int position = (Integer) msg.obj;
                        if (position == -1) {
                            //-1代表第一次搜索
                            UpdateSF_CONSUMELOTINBOUND(mcomsumeLotInboundDataList.get(mcomsumeLotInboundDataList.size()-1));
                        } else {
                            mlvComsumeLotInbound.setSelection(position);
                            //修改好數字以後更新數據庫
                            Log.e("保存了", "kaishi");
                            UpdateSF_CONSUMELOTINBOUND(mcomsumeLotInboundDataList.get(position));
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
                showToast(mContext, "入库模式", 0);
                mivchoose.setImageResource(R.drawable.rfidicon);
                if (BaecodeReader != null) {
                    BaecodeReader.stopScan();
                }
                mpopChooseBorR.dismiss();
                break;
            case R.id.btnbarcode:
                //选择R&B的popupwindow里面的Barcode按钮
                showToast(mContext, "修改数据模式", 0);
                BRFlag = 2;
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
                //actionSearch();
                //   getConsumeLotInboundByConsumeDefID("INB-2017090600001");
                break;

            case R.id.action_save:
                //actionSave();
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
        _mcomsumeLotInboundDataList = new ArrayList<>();
    }
    //endregion

    //region initView()
    private void initview() {
        etTagID = (EditText) getView().findViewById(R.id.etTagID);
        tvtagqty = (TextView) getView().findViewById(R.id.tvtagqty);
        tvInboundOrderNo = (TextView) getView().findViewById(R.id.InboundOrderNo);
        tvSpec_Desc = (TextView) getView().findViewById(R.id.tv_spec_desc);
        tvConsumabledefId = (TextView) getView().findViewById(R.id.tv_ConsumabledefId);
        tvConsumabledefname= (TextView) getView().findViewById(R.id.tv_consumabledefname);
        tvConsumeLotInboundSum = (TextView) getView().findViewById(R.id.tv_ConsumeLotInboundSum);
        tvlistSumQty = (TextView) getView().findViewById(R.id.tv_listSumQty);
        tvnoconsumeinboundLotQTY = (TextView) getView().findViewById(R.id.tv_noconsumeinboundLotQTY);
        tvORDERNO = (TextView) getView().findViewById(R.id.tv_ORDERNO);
        tvORDERTYPE = (TextView) getView().findViewById(R.id.tv_ORDERTYPE);
        tvLINENO = (TextView) getView().findViewById(R.id.tv_LINENO);
        tvORDERCOMPANY = (TextView) getView().findViewById(R.id.tv_ORDERCOMPANY);
        mivchoose = (ImageView) getView().findViewById(R.id.iv_choose);
        mivchoose.setOnClickListener(this);
        //Choose RFID or Barcode popupwindow
        mViewChooseBorR = mContext.getLayoutInflater().inflate(R.layout.readorfix, null,false);
        //finqty popupwindow
        mViewFixQty = mContext.getLayoutInflater().inflate(R.layout.confirmqty, null,false);
        mViewConfirmQty  = mContext.getLayoutInflater().inflate(R.layout.confirmqty, null,false);
        //listViewDataList   **
        mlvComsumeLotInbound = (ListView) getView().findViewById(R.id.lv_consumeLotInbound);
        mConsumeLotInboundListViewAdapter = new ConsumeLotInboundListViewAdapter(mContext, mcomsumeLotInboundDataList);
        mlvComsumeLotInbound.setAdapter(mConsumeLotInboundListViewAdapter);
        mlvComsumeLotInbound.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fixQty(view, position,mcomsumeLotInboundDataList.get(position));
                return false;
            }
        });
    }
    //endregion

    @SuppressLint("WrongConstant")
    private void fixQty(View view, final int position,final ConsumeLotInboundData tempdata) {
        Button btnConfirm = (Button) mViewFixQty.findViewById(R.id.btnfqty);
        final EditText etFixQty = (EditText) mViewFixQty.findViewById(R.id.etfqty);
        TextView tvconsumablelotid = (TextView) mViewFixQty .findViewById(R.id.tv_consumablelotid);
        etFixQty.setText(tempdata.getINQTY() + "");
        etFixQty.setSelection(etFixQty.length());
        tvconsumablelotid.setText(tempdata.getCONSUMABLELOTID());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempdata.setINQTY(etFixQty.getText().toString());
                tempdata.setDIVERSIONQTY(etFixQty.getText().toString());
                if (Float.parseFloat(tempdata.getINQTY()) > Float.parseFloat(tempdata.getPLANQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(tempdata.getINQTY()) == Float.parseFloat(tempdata.getPLANQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymatch);
                } else {
                    tempdata.setBackgroundColor(R.color.qtyless);
                }
                if(!tempdata.getRATE().equals("1")){
                    float DIVERSIONQTY = Float.parseFloat(tempdata.getINQTY())*Float.parseFloat(tempdata.getRATE());
                    DecimalFormat decimalFormat=new DecimalFormat(".00");
                    String diversionqty=decimalFormat.format(DIVERSIONQTY);
                    tempdata.setDIVERSIONQTY(diversionqty);
                }
                mcomsumeLotInboundDataList.set(position, tempdata);
                mConsumeLotInboundListViewAdapter.notifyDataSetChanged();
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

    //region openPopWindowToConfirmQTY
    @SuppressLint("WrongConstant")
    private void openPopWindowToConfirmQTY(View view, final ConsumeLotInboundData consumelotdata) {

        Button btnConfirm = (Button) mViewConfirmQty.findViewById(R.id.btnfqty);
        TextView tvconsumablelotid = (TextView) mViewConfirmQty.findViewById(R.id.tv_consumablelotid);
        final EditText etFixQty = (EditText) mViewConfirmQty.findViewById(R.id.etfqty);
        etFixQty.setText(consumelotdata.getTAGQTY() + "");
        etFixQty.setSelection(etFixQty.length());
        tvconsumablelotid.setText(consumelotdata.getTAGID());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumelotdata.setINQTY(etFixQty.getText().toString());
                consumelotdata.setDIVERSIONQTY(etFixQty.getText().toString());
                if (Float.parseFloat(consumelotdata.getINQTY()) > Float.parseFloat(consumelotdata.getPLANQTY())) {
                    consumelotdata.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(consumelotdata.getINQTY()) == Float.parseFloat(consumelotdata.getPLANQTY())) {
                    consumelotdata.setBackgroundColor(R.color.qtymatch);
                } else {
                    consumelotdata.setBackgroundColor(R.color.qtyless);
                }
                if(!consumelotdata.getRATE().equals("1")){
                    float DIVERSIONQTY = Float.parseFloat(consumelotdata.getINQTY())*Float.parseFloat(consumelotdata.getRATE());
                    DecimalFormat decimalFormat=new DecimalFormat(".00");
                    String diversionqty=decimalFormat.format(DIVERSIONQTY);
                    consumelotdata.setDIVERSIONQTY(diversionqty);
                }
                mcomsumeLotInboundDataList.add(consumelotdata);
                mConsumeLotInboundListViewAdapter.notifyDataSetChanged();
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

    //region 进入这个tab的方法
    public void getConsumeLotInboundByConsumeDefID(ConsumeInboundData inboundData) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetConsumeLotInboundQuery)
                , inboundData.getCONSUMABLEDEFID(), inboundData.getCONSUMABLEDEFVERSION());
        Log.e("sql语句lotInbound", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mcomsumeLotInboundDataList.clear();
        _mcomsumeLotInboundDataList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                ConsumeLotInboundData consumeLotInboundData = new ConsumeLotInboundData();
                consumeLotInboundData.setCONSUMABLELOTID(getCursorData(cursorDatalist, Constant.CONSUMABLELOTID).trim());
                consumeLotInboundData.setCONSUMABLEDEFID(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFID).trim());
                consumeLotInboundData.setINBOUNDNO(getCursorData(cursorDatalist, Constant.INBOUNDNO).trim());
                consumeLotInboundData.setCONSUMABLEDEFID(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFID).trim());
                consumeLotInboundData.setCONSUMABLEDEFNAME(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFNAME).trim());
                consumeLotInboundData.setCONSUMABLEDEFVERSION(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFVERSION).trim());
                consumeLotInboundData.setWAREHOUSEID(getCursorData(cursorDatalist, Constant.WAREHOUSEID).trim());
                consumeLotInboundData.setORDERNO(getCursorData(cursorDatalist, Constant.ORDERNO).trim());
                consumeLotInboundData.setORDERTYPE(getCursorData(cursorDatalist, Constant.ORDERTYPE).trim());
                consumeLotInboundData.setLINENO(getCursorData(cursorDatalist, Constant.LINENO).trim());
                consumeLotInboundData.setORDERCOMPANY(getCursorData(cursorDatalist, Constant.ORDERCOMPANY).trim());
                consumeLotInboundData.setUNIT(getCursorData(cursorDatalist, Constant.UNIT).trim());
                consumeLotInboundData.setPLANQTY(getCursorData(cursorDatalist, Constant.PLANQTY).trim());
                consumeLotInboundData.setDIVERSIONUNIT(getCursorData(cursorDatalist, Constant.DIVERSIONUNIT).trim());
                consumeLotInboundData.setRATE(getCursorData(cursorDatalist, Constant.RATE).trim());
                consumeLotInboundData.setINQTY(getCursorData(cursorDatalist, Constant.INQTY).trim());
                consumeLotInboundData.setDIVERSIONQTY(getCursorData(cursorDatalist, Constant.DIVERSIONQTY).trim());
                consumeLotInboundData.setPLANQTY(getCursorData(cursorDatalist,Constant.PLANQTY).trim());
                consumeLotInboundData.setTAGID(getCursorData(cursorDatalist,Constant.TAGID).trim());
                consumeLotInboundData.setTAGQTY(getCursorData(cursorDatalist,Constant.TAGQTY).trim());
                consumeLotInboundData.setSPEC_DESC(getCursorData(cursorDatalist,Constant.SPEC_DESC).trim());
                if (Float.parseFloat(consumeLotInboundData.getINQTY()) > Float.parseFloat(consumeLotInboundData.getPLANQTY())) {
                    consumeLotInboundData.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(consumeLotInboundData.getINQTY()) == Float.parseFloat(consumeLotInboundData.getPLANQTY())) {
                    consumeLotInboundData.setBackgroundColor(R.color.qtymatch);
                } else {
                    consumeLotInboundData.setBackgroundColor(R.color.qtyless);
                }
                Log.e("consumeLotInboundData", consumeLotInboundData.toString());
                if (!consumeLotInboundData.getINQTY().equals("0")) {
                    mcomsumeLotInboundDataList.add(consumeLotInboundData);
                }
                _mcomsumeLotInboundDataList.add(consumeLotInboundData);
            }
        }
        mConsumeLotInboundListViewAdapter.notifyDataSetChanged();
        setQTYInfo();
        tvConsumabledefId.setText(inboundData.getCONSUMABLEDEFID());
        tvSpec_Desc.setText(inboundData.getSPEC_DESC());
        tvConsumabledefname.setText(inboundData.getCONSUMABLEDEFNAME());
        tvConsumeLotInboundSum.setText(_mcomsumeLotInboundDataList.size()+"");
        tvORDERNO.setText(inboundData.getORDERNO());
        tvORDERTYPE.setText(inboundData.getORDERTYPE());
        tvLINENO.setText(inboundData.getLINENO());
        tvORDERCOMPANY.setText(inboundData.getORDERCOMPANY());
        tvInboundOrderNo.setText(inboundData.getINBOUNDNO());
        InboundOrderNo = inboundData.getINBOUNDNO();
    }
    //endregion

    private void setQTYInfo() {
        tvtagqty.setText(mcomsumeLotInboundDataList.size() + "");
        tvlistSumQty.setText(mcomsumeLotInboundDataList.size() + "");
        tvnoconsumeinboundLotQTY.setText((_mcomsumeLotInboundDataList.size() - mcomsumeLotInboundDataList.size()) + "");
    }

    //region findTagInList
    private int findTagInList(String tagid) {
        int returnint = -1;
        for (int i = 0; i < mcomsumeLotInboundDataList.size(); i++) {
            ConsumeLotInboundData data = mcomsumeLotInboundDataList.get(i);
            Log.e("list中的数据",data.toString());
            if (data.getTAGID().equals(tagid)) {
                returnint = i;
                break;
            }
        }
        return returnint;
    }
    //endregion

    //region findTagInDBList
    private int findTagInDBList(String tagid) {
        int returnint = -1;
        for (int i = 0; i < _mcomsumeLotInboundDataList.size(); i++) {
            ConsumeLotInboundData dbdata = _mcomsumeLotInboundDataList.get(i);
            if (dbdata.getTAGID().equals(tagid)) {
                //说明是这个资材的标签,现在确认一下有没有被
                returnint = i;
                for (int j = 0; j < mcomsumeLotInboundDataList.size(); j++) {
                    ConsumeLotInboundData listdata = mcomsumeLotInboundDataList.get(j);
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
                    Log.e("扫描出问题了", "重啟");
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

    //region ReadTag

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

    //region checkAndSearchDB
    private void checkAndSearchDB(String tagID) {
        int tagposition = findTagInDBList(tagID);
        if (tagposition == -2) {
            Log.e("indexhand", tagID + "重复扫描了");
            showToast(mContext, tagID + "已经入过库了", 0);
        } else if (tagposition == -1) {
            Log.e("indexhand", tagID + "不在这个list里面");
            showToast(mContext, tagID + "不是这个列表里面的标签", 0);
        } else {
            stopInventory();
            openPopWindowToConfirmQTY(getView(),_mcomsumeLotInboundDataList.get(tagposition));
        }
    }
    //endregion

    //region checkAndFixQTY
    private void checkAndFixQTY(String tagID) {
        //修改数据模式下，扫描到列表里的lot的时候就会弹出popwindow
        int tagposition = findTagInList(tagID);
       if (tagposition == -1){
           Log.e("indexhand", tagID + "不在这个list里面");
           showToast(mContext, tagID + "不在这个列表里面", 0);
       }else {
           Log.e("indexhand", tagID + "在列表里，替换Inqty的值");
           //扫到一个新的lot，打开PopWindow
           stopInventory();
           fixQty(getView(), tagposition,mcomsumeLotInboundDataList.get(tagposition));
       }
    }
    //endregion

    //region actionSearch
    protected void actionSearch() {

    }
    //endregion

    //region actionClearAll
    public void actionClearAll() {
        if (mcomsumeLotInboundDataList.size() > 0) {
            mcomsumeLotInboundDataList.clear();
        }
        if (_mcomsumeLotInboundDataList.size() > 0) {
            _mcomsumeLotInboundDataList.clear();
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

    //region UpdateSF_CONSUMELOTINBOUND
    private void UpdateSF_CONSUMELOTINBOUND(ConsumeLotInboundData data) {
        try {
            ContentValues values = new ContentValues();
            values.put(Constant.INQTY, data.getINQTY());
            values.put(Constant.DIVERSIONQTY, data.getDIVERSIONQTY());
            db.update(Constant.SF_CONSUMELOTINBOUND, values, "INBOUNDNO = ? AND " +
                            "CONSUMABLEDEFID =? AND CONSUMABLEDEFVERSION = ? AND WAREHOUSEID = ?" +
                            "AND ORDERNO = ? AND ORDERTYPE = ? AND LINENO = ? AND CONSUMABLELOTID = ?",
                    new String[]{InboundOrderNo, data.getCONSUMABLEDEFID(), data.getCONSUMABLEDEFVERSION(),
                            data.getWAREHOUSEID(), data.getORDERNO(), data.getORDERTYPE(),
                            data.getLINENO(), data.getCONSUMABLELOTID()});
            //更新對應的標誌位
            ContentValues values1 = new ContentValues();
            values1.put(Constant.ISPDASAVE, "Y");
            db.update(Constant.SF_INBOUNDORDER, values1, "INBOUNDNO =?", new String[]{InboundOrderNo});
            //查询SF_CONSUMELOTINBOUND表的总数，来修改SF_CONSUMEINBOUND表的数据
            String selectDataListsql = String.format(getString(R.string.GetConsumeInboundQTYQuery)
                    , data.getCONSUMABLEDEFID(), data.getCONSUMABLEDEFVERSION());
            Log.e("GetConsumeInboundQTY", selectDataListsql);
            Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
            if (cursorDatalist.getCount() != 0) {
                while (cursorDatalist.moveToNext()) {
                    String SUMqty = getCursorData(cursorDatalist, "SUM");
                    EventBus.getDefault().post(new Event.ConsumeInboundbyLotCheckEvent(data, SUMqty));
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