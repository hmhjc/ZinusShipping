package cn.zinus.warehouse.Fragment.materialstockin;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Adapter.ConsumeInboundListViewAdapter;
import cn.zinus.warehouse.Fragment.Event;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.JaveBean.ConsumeInboundData;
import cn.zinus.warehouse.JaveBean.ConsumeLotInboundData;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;

import static cn.zinus.warehouse.util.Constant.UPDATEUI;
import static cn.zinus.warehouse.util.DBManger.getCursorData;
import static cn.zinus.warehouse.util.Utils.showToast;

/**
 * Created by Spring on 2017/2/18.
 */

public class ConsumeInboundFragment extends KeyDownFragment {

    //region ◆ 변수(Variables)
    //上下文
    private MainNaviActivity mContext;
    //popwindow
    private PopupWindow mpopFixQty;
    private View mViewFixQty;
    //ListView
    private ListView mlvComsumeInbound;
    private ConsumeInboundListViewAdapter mConsumeInboundListViewAdapter;
    private ArrayList<ConsumeInboundData> mcomsumeInboundDataList;
    private ArrayList<String> IDlist;
    private TextView tvtagqty;
    private TextView tvInboundOrderNo;
    private String InboundOrderNo;
    // private Handler handler;
    Handler handler = null;
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
        return inflater.inflate(R.layout.fragment_consumeinbound, container, false);
    }
    //endregion

    //region onActivityCreated
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initview();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATEUI:
                        int position = (Integer) msg.obj;
                        if (position == -1) {
                            mlvComsumeInbound.setSelection(0);
                        } else {
                            mlvComsumeInbound.setSelection(position);
                            UpdateSF_CONSUMEINBOUND(mcomsumeInboundDataList.get(position));
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

    }
    //endregion

    //region onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                actionSearch();
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
        mcomsumeInboundDataList = new ArrayList<>();
        IDlist = new ArrayList<>();
    }
    //endregion

    //region initView
    private void initview() {
        tvtagqty = (TextView) getView().findViewById(R.id.tvtagqty);
        tvInboundOrderNo = (TextView) getView().findViewById(R.id.InboundOrderNo);
        //finqty popupwindow
        mViewFixQty = mContext.getLayoutInflater().inflate(R.layout.fixqty, null);
        //listViewDataList   **
        mlvComsumeInbound = (ListView) getView().findViewById(R.id.lv_consumeInbound);
        mConsumeInboundListViewAdapter = new ConsumeInboundListViewAdapter(mContext, mcomsumeInboundDataList);
        mlvComsumeInbound.setAdapter(mConsumeInboundListViewAdapter);
        mlvComsumeInbound.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!isInconsumeLotInbound(mcomsumeInboundDataList.get(position).getCONSUMABLEDEFID()
                        , mcomsumeInboundDataList.get(position).getCONSUMABLEDEFVERSION())) {
                    fixQty(view, position);
                }
                return true;
            }
        });
        mlvComsumeInbound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (isInconsumeLotInbound(mcomsumeInboundDataList.get(position).getCONSUMABLEDEFID()
                        , mcomsumeInboundDataList.get(position).getCONSUMABLEDEFVERSION())) {
                    EventBus.getDefault().post(new Event.ConsumeLotInboundByConsumeDefIDEvent(mcomsumeInboundDataList.get(position)));
                } else {
                    showToast(mContext, mContext.getString(R.string.isNormalConsume), 0);
                }
            }
        });
    }

    private boolean isInconsumeLotInbound(String CONSUMABLEDEFID, String CONSUMABLEDEFVERSION) {
        //返回true说明是lot管理的资材，false说明不是lot管理的资材，需要长按修改数字
        boolean returnflag = false;
        String selectDataListsql = String.format(mContext.getString(R.string.checkisInconsumeLotInbound), CONSUMABLEDEFID, CONSUMABLEDEFVERSION);
        Log.e("isInconsumeLotInbound", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist != null) {
            if (cursorDatalist.getCount() != 0) {
                while (cursorDatalist.moveToNext()) {
                    if (!getCursorData(cursorDatalist, Constant.COUNT).equals("0"))
                        returnflag = true;
                }
            }
        }
        return returnflag;
    }

    //endregion

    //region fixQty
    @SuppressLint("WrongConstant")
    private void fixQty(View view, final int position) {
        final ConsumeInboundData tempdata = mcomsumeInboundDataList.get(position);
        Button btnConfirm = (Button) mViewFixQty.findViewById(R.id.btnfqty);
        final EditText etFixQty = (EditText) mViewFixQty.findViewById(R.id.etfqty);
        etFixQty.setText(tempdata.getINQTY() + "");
        etFixQty.setSelection(etFixQty.length());
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
                mcomsumeInboundDataList.set(position, tempdata);
                mConsumeInboundListViewAdapter.notifyDataSetChanged();
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

    public void updaCheckQtyByLot(ConsumeLotInboundData consumeLotInboundData, String sumqty) {
        for (int i = 0; i < mcomsumeInboundDataList.size(); i++) {
            ConsumeInboundData data = mcomsumeInboundDataList.get(i);
            if (data.getCONSUMABLEDEFID().equals(consumeLotInboundData.getCONSUMABLEDEFID()) &&
                    data.getCONSUMABLEDEFVERSION().equals(consumeLotInboundData.getCONSUMABLEDEFVERSION())) {
                //找到对应的consumeInbound的数据，修改ui
                data.setINQTY(sumqty);
                data.setDIVERSIONQTY(sumqty);
                if(!data.getRATE().equals("1")){
                    float DIVERSIONQTY = Float.parseFloat(data.getINQTY())*Float.parseFloat(data.getRATE());
                    DecimalFormat decimalFormat=new DecimalFormat(".00");
                    String diversionqty=decimalFormat.format(DIVERSIONQTY);
                    data.setDIVERSIONQTY(diversionqty);
                }
                mcomsumeInboundDataList.set(i, data);
                mConsumeInboundListViewAdapter.notifyDataSetChanged();
                Message message = new Message();
                message.what = UPDATEUI;
                message.obj = i;
                handler.sendMessage(message);
                break;
            }
        }
    }

    //region getConsumeInboundByInboundOrder
    public void getConsumeInboundByInboundOrder(String inboundNo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetConsumeInboundQuery), inboundNo);
        Log.e("sql语句getConsumeInbound", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mcomsumeInboundDataList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                ConsumeInboundData consumeInboundData = new ConsumeInboundData();
                consumeInboundData.setCONSUMABLEDEFID(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFID).trim());
                consumeInboundData.setINBOUNDNO(getCursorData(cursorDatalist, Constant.INBOUNDNO).trim());
                consumeInboundData.setCONSUMABLEDEFID(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFID).trim());
                consumeInboundData.setCONSUMABLEDEFNAME(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFNAME).trim());
                consumeInboundData.setCONSUMABLEDEFVERSION(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFVERSION).trim());
                consumeInboundData.setWAREHOUSEID(getCursorData(cursorDatalist, Constant.WAREHOUSEID).trim());
                consumeInboundData.setORDERNO(getCursorData(cursorDatalist, Constant.ORDERNO).trim());
                consumeInboundData.setORDERTYPE(getCursorData(cursorDatalist, Constant.ORDERTYPE).trim());
                consumeInboundData.setLINENO(getCursorData(cursorDatalist, Constant.LINENO).trim());
                consumeInboundData.setORDERCOMPANY(getCursorData(cursorDatalist, Constant.ORDERCOMPANY).trim());
                consumeInboundData.setUNIT(getCursorData(cursorDatalist, Constant.UNIT).trim());
                consumeInboundData.setPLANQTY(getCursorData(cursorDatalist, Constant.PLANQTY).trim());
                consumeInboundData.setDIVERSIONUNIT(getCursorData(cursorDatalist, Constant.DIVERSIONUNIT).trim());
                consumeInboundData.setRATE(getCursorData(cursorDatalist, Constant.RATE).trim());
                consumeInboundData.setINQTY(getCursorData(cursorDatalist, Constant.INQTY).trim());
                consumeInboundData.setDIVERSIONQTY(getCursorData(cursorDatalist, Constant.DIVERSIONQTY).trim());
                consumeInboundData.setSPEC_DESC(getCursorData(cursorDatalist, Constant.SPEC_DESC).trim());
                if (Float.parseFloat(consumeInboundData.getINQTY()) > Float.parseFloat(consumeInboundData.getPLANQTY())) {
                    consumeInboundData.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(consumeInboundData.getINQTY()) == Float.parseFloat(consumeInboundData.getPLANQTY())) {
                    consumeInboundData.setBackgroundColor(R.color.qtymatch);
                } else {
                    consumeInboundData.setBackgroundColor(R.color.qtyless);
                }
                mcomsumeInboundDataList.add(consumeInboundData);
            }
        }
        tvtagqty.setText(mcomsumeInboundDataList.size() + "");
        tvInboundOrderNo.setText(inboundNo);
        InboundOrderNo = inboundNo;
        mConsumeInboundListViewAdapter.notifyDataSetChanged();
        Message message = new Message();
        message.what = UPDATEUI;
        message.obj = -1;
        handler.sendMessage(message);
    }
    //endregion

    //region actionSearch
    protected void actionSearch() {

    }
    //endregion

    //region actionClearAll
    public void actionClearAll() {
        mcomsumeInboundDataList.clear();
        mConsumeInboundListViewAdapter.notifyDataSetChanged();
        IDlist.clear();
        tvtagqty.setText("0");
        tvInboundOrderNo.setText("");
        InboundOrderNo = "";
    }
    //endregion

    private void UpdateSF_CONSUMEINBOUND(ConsumeInboundData data) {
        //更新consumeinbound的数字
        ContentValues values = new ContentValues();
        values.put(Constant.INQTY, data.getINQTY());
        values.put(Constant.DIVERSIONQTY, data.getDIVERSIONQTY());
        db.update(Constant.SF_CONSUMEINBOUND, values, "INBOUNDNO = ? AND " +
                        "CONSUMABLEDEFID =? AND CONSUMABLEDEFVERSION = ? AND WAREHOUSEID = ?" +
                        "AND ORDERNO = ? AND ORDERTYPE = ? AND LINENO = ?",
                new String[]{InboundOrderNo, data.getCONSUMABLEDEFID(), data.getCONSUMABLEDEFVERSION(),
                        data.getWAREHOUSEID(), data.getORDERNO(), data.getORDERTYPE(),
                        data.getLINENO()});
        //更新inboundorder的标志位
        ContentValues values1 = new ContentValues();
        values1.put(Constant.ISPDASAVE, "Y");
        db.update(Constant.SF_INBOUNDORDER, values1, "INBOUNDNO =?", new String[]{InboundOrderNo});
    }

    //endregion

}