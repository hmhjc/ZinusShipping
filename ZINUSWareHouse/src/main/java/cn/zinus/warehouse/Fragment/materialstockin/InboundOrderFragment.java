package cn.zinus.warehouse.Fragment.materialstockin;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Adapter.InboundorderListViewAdapter;
import cn.zinus.warehouse.Fragment.Event;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.JaveBean.CodeData;
import cn.zinus.warehouse.JaveBean.InboundOrderData;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;

import static cn.zinus.warehouse.util.DBManger.getCursorData;

/**
 * Created by Spring on 2017/2/18.
 */

public class InboundOrderFragment extends KeyDownFragment {

    //region ◆ 변수(Variables)
    static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
    //上下文
    private MainNaviActivity mContext;
//    //ConsumeInboundStateSpinner
//    protected Spinner spConsumeInboundState;
//    private ArrayAdapter mConsumeInboundStateAdapter;
//    private ArrayList<CodeData> ConsumeInboundStateList;
    //ConsumabledefIdSpinner
    protected Spinner spWarehouse;
    private ArrayAdapter mWarehouseAdapter;
    private ArrayList<CodeData> WarehouseList;
    //Data
    protected TextView tvFromDate;
    protected TextView tvToDate;
    private int year, pyear;
    private int month, pmonth;
    private int day, pday;
    private int fromtoflag;
    private Calendar mycalendar;
    //ListView
    private ListView mlvInboundOrder;
    private InboundorderListViewAdapter mInboundorderListViewAdapter;
    public ArrayList<InboundOrderData> mInboundOrderDataList;
    protected InboundOrderData mSelectInboundOrderdata = new InboundOrderData();
    //数据库
    MyDateBaseHelper mHelper;
    SQLiteDatabase db;
    //endregion

    //region ◆ 생성자(Creator)

    //region onCreate
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return inflater.inflate(R.layout.fragment_inboundorder, container, false);
    }
    //endregion

    //region onActivityCreated
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initview();
        //UpDateShippingPlan();
    }

    //endregion

    //region onOptionsItemSelected(菜单键按钮)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                UpDateOrder();
                break;
            case R.id.action_ClearAll:
                clearall();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearall() {
        mInboundOrderDataList.clear();
        mInboundorderListViewAdapter.notifyDataSetChanged();
    }
    //endregion

    //endregion

    //region ◆ Fucation

    //region initData
    private void initData() {
        //初始化Spinner数据
        //region ConsumeInboundStateList
//        ConsumeInboundStateList = new ArrayList<>();
//        ConsumeInboundStateList.add(new CodeData("",getString(R.string.SpinnerDefault)));
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        String selectDataListsql = String.format(getString(R.string.codeSpinner),Constant.CONSUMEINBOUNDSTATE,AppConfig.getInstance().getLanuageType());
//        Log.e("ConsumeInboundState", selectDataListsql);
//        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
//        if (cursorDatalist.getCount() != 0) {
//            while (cursorDatalist.moveToNext()) {
//                CodeData spinnerData = new CodeData();
//                spinnerData.setCODEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CODEID)));
//                spinnerData.setDICTIONARYNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.DICTIONARYNAME)));
//                ConsumeInboundStateList.add(spinnerData);
//            }
//        }
        //endregion

        //region WarehouseList
        WarehouseList = new ArrayList<>();
        WarehouseList.add(new CodeData("",getString(R.string.SpinnerDefault)));
        String selectWarehouse = getString(R.string.WarehouseSpinner);
        Log.e("WarehouseSpinner", selectWarehouse);
        Cursor cursorWarehouse = DBManger.selectDatBySql(db, selectWarehouse, null);
        if (cursorWarehouse.getCount() != 0) {
            while (cursorWarehouse.moveToNext()) {
                CodeData spinnerData = new CodeData();
                spinnerData.setCODEID(cursorWarehouse.getString(cursorWarehouse.getColumnIndex(Constant.WAREHOUSEID)));
                spinnerData.setDICTIONARYNAME(cursorWarehouse.getString(cursorWarehouse.getColumnIndex(Constant.WAREHOUSENAME)));
                WarehouseList.add(spinnerData);
            }
        }
        //endregion

        mInboundOrderDataList = new ArrayList<>();
    }
    //endregion

    //region initview
    private void initview() {

        //region ConsumeInboundStateSpinner
//        spConsumeInboundState = (Spinner) getView().findViewById(R.id.sp_ConsumeInboundState);
//        mConsumeInboundStateAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, ConsumeInboundStateList);
//        spConsumeInboundState.setAdapter(mConsumeInboundStateAdapter);
//        spConsumeInboundState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                UpDateOrder();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        //endregion

        //region ConsumabledefIdSpinner
        spWarehouse = (Spinner) getView().findViewById(R.id.sp_Warehouse);
        mWarehouseAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, WarehouseList);
        spWarehouse.setAdapter(mWarehouseAdapter);
        spWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UpDateOrder();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        //region Date
        tvFromDate = (TextView) getView().findViewById(R.id.tvfromdate);
        tvToDate = (TextView) getView().findViewById(R.id.tvtodate);
        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(mContext, Datelistener, pyear, pmonth, pday);
                dpd.show();//显示DatePickerDialog组件
                fromtoflag = 1;
            }
        });
        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(mContext, Datelistener, year, month, day);
                dpd.show();//显示DatePickerDialog组件
                fromtoflag = 2;
            }
        });
        mycalendar = Calendar.getInstance();
        year = mycalendar.get(Calendar.YEAR);
        month = mycalendar.get(Calendar.MONTH);
        day = mycalendar.get(Calendar.DAY_OF_MONTH);
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(new Date());
        theCa.add(theCa.DATE, -30);
        pyear = theCa.get(Calendar.YEAR);
        pmonth = theCa.get(Calendar.MONTH);
        pday = theCa.get(Calendar.DAY_OF_MONTH);
        if (pmonth < 9) {
            if (pday < 10) {
                tvFromDate.setText("" + pyear + "-0" + (pmonth + 1) + "-0" + pday);
            } else {
                tvFromDate.setText("" + pyear + "-0" + (pmonth + 1) + "-" + pday);
            }
        } else {
            if (pday < 10) {
                tvFromDate.setText("" + pyear + "-" + (pmonth + 1) + "-0" + pday);
            } else {
                tvFromDate.setText("" + pyear + "-" + (pmonth + 1) + "-" + pday);
            }
        }
        if (month < 9) {
            if (day < 10) {
                tvToDate.setText("" + year + "-0" + (month + 1) + "-0" + day);
            } else {
                tvToDate.setText("" + year + "-0" + (month + 1) + "-" + day);
            }
        } else {
            if (day < 10) {
                tvToDate.setText("" + year + "-" + (month + 1) + "-0" + day);
            } else {
                tvToDate.setText("" + year + "-" + (month + 1) + "-" + day);
            }
        }
        //endregion

        //region ImportOrder ListView
        mlvInboundOrder = (ListView) getView().findViewById(R.id.lv_InboundOrder);
        mInboundorderListViewAdapter = new InboundorderListViewAdapter(mContext, mInboundOrderDataList);
        mlvInboundOrder.setAdapter(mInboundorderListViewAdapter);
        mlvInboundOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectInboundOrderdata = mInboundOrderDataList.get(position);
                EventBus.getDefault().post(new Event.ConsumeInboundByOrderEvent(mSelectInboundOrderdata.getINBOUNDNO()));
            }
        });

        //endregion

    }
    //endregion

    //region UpDateShippingPlan
    protected void UpDateOrder() {
      //  String ConsumeInboundState =((CodeData) spConsumeInboundState.getSelectedItem()).getCODEID();
        String WarehouseID =((CodeData)spWarehouse.getSelectedItem()).getCODEID();
        String OrderFromDate = tvFromDate.getText().toString();
        String OrderToDate = tvToDate.getText().toString();
        getInboundOrder(WarehouseID, OrderFromDate, OrderToDate);
        EventBus.getDefault().post(new Event.InboundClearOrderItemEvent());
    }

    private void getInboundOrder(String WarehouseID, String orderFromDate, String orderToDate) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetInboundOrderQuery),orderFromDate,orderToDate);
        if (!WarehouseID.equals("")) {
            selectDataListsql = selectDataListsql + String.format(getString(R.string.gioConditionWAREHOUSEID),WarehouseID);
        }
        selectDataListsql = selectDataListsql+getString(R.string.gioOrderBy);
        Log.e("InboundOrder语句", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mInboundOrderDataList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                InboundOrderData inboundOrderData = new InboundOrderData();
                inboundOrderData.setINBOUNDNO(getCursorData(cursorDatalist, Constant.INBOUNDNO).trim());
                inboundOrderData.setWAREHOUSEID(getCursorData(cursorDatalist, Constant.WAREHOUSEID).trim());
                inboundOrderData.setWAREHOUSENAME(getCursorData(cursorDatalist, Constant.WAREHOUSENAME).trim());
                inboundOrderData.setSCHEDULEDATE(getCursorData(cursorDatalist, Constant.SCHEDULEDATE).trim());
                inboundOrderData.setINBOUNDDATE(getCursorData(cursorDatalist, Constant.INBOUNDDATE).trim());
                inboundOrderData.setTEMPINBOUNDDATE(getCursorData(cursorDatalist, Constant.TEMPINBOUNDDATE).trim());
                inboundOrderData.setINBOUNDTYPE(getCursorData(cursorDatalist, Constant.INBOUNDTYPE).trim());
                inboundOrderData.setINBOUNDSTATE(getCursorData(cursorDatalist, Constant.INBOUNDSTATE).trim());
                inboundOrderData.setSTATENAME(getCursorData(cursorDatalist, Constant.STATENAME).trim());
                inboundOrderData.setCONSUMABLECOUNT(getCursorData(cursorDatalist, Constant.CONSUMABLECOUNT).trim());
                Log.e("inboundOrderData",inboundOrderData.toString());
                mInboundOrderDataList.add(inboundOrderData);
            }
        }
        mInboundorderListViewAdapter.notifyDataSetChanged();
    }
    //endregion

    //endregion

    //region Datelistener
    private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
        /**params：view：该事件关联的组件
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            //更新日期

            if (fromtoflag == 1) {
                pyear = myyear;
                pmonth = monthOfYear;
                pday = dayOfMonth;
                String m, d;
                if (pmonth < 9) {
                    m = "0" + (pmonth + 1);
                } else {
                    m = "" + (pmonth + 1);
                }
                if (pday < 10) {
                    d = "0" + pday;
                } else {
                    d = "" + pday;
                }
                tvFromDate.setText("" + pyear + "-" + m + "-" + d);
                UpDateOrder();
            } else {
                year = myyear;
                month = monthOfYear;
                day = dayOfMonth;
                String m, d;
                if (month < 9) {
                    m = "0" + (month + 1);
                } else {
                    m = "" + (month + 1);
                }
                if (day < 10) {
                    d = "0" + day;
                } else {
                    d = "" + day;
                }
                tvToDate.setText("" + year + "-" + m + "-" + d);
                UpDateOrder();
            }
        }
    };

    //endregion

}