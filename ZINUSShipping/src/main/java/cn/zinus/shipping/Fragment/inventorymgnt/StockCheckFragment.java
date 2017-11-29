package cn.zinus.shipping.Fragment.inventorymgnt;

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

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Adapter.StockCheckListViewAdapter;
import cn.zinus.shipping.Config.AppConfig;
import cn.zinus.shipping.Fragment.Event;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.JaveBean.StockCheckData;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;

/**
 * Developer:Spring
 * DataTime :2017/9/25 10:05
 * Main Change:
 */

public class StockCheckFragment extends KeyDownFragment {

    //region ◆ 변수(Variables)
    static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
    //上下文
    private MainNaviActivity mContext;
    //StockCheckStateSpinner
    protected Spinner spStockCheckState;
    private ArrayAdapter mStockCheckStateAdapter;
    private ArrayList<String> StockCheckStateList;
    //WarehouseSpinner
    protected Spinner spWarehouse;
    private ArrayAdapter mWarehouseAdapter;
    private ArrayList<String> WarehouseList;
    //Data
    protected TextView tvCheckMonth;
    private int year;
    private int month;
    private int day;
    private Calendar mycalendar;
    //ListView
    private ListView mlvStockCheck;
    private StockCheckListViewAdapter mStockCheckListViewAdapter;
    public ArrayList<StockCheckData> mStockCheckDataArrayList;
    protected StockCheckData mSelectStockCheckData = new StockCheckData();
    //数据库
    MyDateBaseHelper mHelper;
    //endregion

    //region ◆ 생성자(Creator)

    //region onCreate
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainNaviActivity) getActivity();
        setHasOptionsMenu(true);
        mHelper = DBManger.getIntance(mContext);
    }
    //endregion

    //region onCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stockcheck, container, false);
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
        mStockCheckDataArrayList.clear();
        mStockCheckListViewAdapter.notifyDataSetChanged();
    }
    //endregion

    //endregion

    //region ◆ Fucation

    //region initData
    private void initData() {
        //初始化Spinner数据
        SQLiteDatabase db = mHelper.getWritableDatabase();
        //region StockCheckStateList
        StockCheckStateList = new ArrayList<>();
        StockCheckStateList.add(0, getString(R.string.SpinnerDefault));
        String selectDataListsql = String.format(getString(R.string.codeSpinner),Constant.CHECKSTATE,AppConfig.getInstance().getLanuageType());
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                StockCheckStateList.add(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.DICTIONARYNAME)));
            }
        }
        //endregion

        //region StockCheckStateList
        WarehouseList = new ArrayList<>();
        WarehouseList.add(0, getString(R.string.SpinnerDefault));
        String WarehouseListsql = String.format(getString(R.string.WarehouseSpinner));
        Cursor cursorWarehouseList = DBManger.selectDatBySql(db, WarehouseListsql, null);
        if (cursorWarehouseList.getCount() != 0) {
            while (cursorWarehouseList.moveToNext()) {
                WarehouseList.add(cursorWarehouseList.getString(cursorWarehouseList.getColumnIndex(Constant.WAREHOUSEID)));
            }
        }
        //endregion

        mStockCheckDataArrayList = new ArrayList<>();
    }
    //endregion

    //region initview
    private void initview() {

        //region StockCheckStateSpinner
        spStockCheckState = (Spinner) getView().findViewById(R.id.sp_StockCheckState);
        mStockCheckStateAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, StockCheckStateList);
        spStockCheckState.setAdapter(mStockCheckStateAdapter);
        spStockCheckState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UpDateOrder();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        //region WarehouseSpinner
        spWarehouse = (Spinner) getView().findViewById(R.id.sp_StockCheckWarehouseID);
        mWarehouseAdapter= new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, WarehouseList);
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
        tvCheckMonth = (TextView) getView().findViewById(R.id.tv_CheckMonth);
        tvCheckMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(mContext, Datelistener, year, month, day);
                dpd.show();//显示DatePickerDialog组件
            }
        });
        mycalendar = Calendar.getInstance();
        year = mycalendar.get(Calendar.YEAR);
        month = mycalendar.get(Calendar.MONTH);
        if (month < 9) {
            tvCheckMonth.setText("" + year + "-0" + (month + 1));
        } else {
            tvCheckMonth.setText("" + year + "-" + (month + 1));
        }
        //endregion

        //region ImportOrder ListView
        mlvStockCheck = (ListView) getView().findViewById(R.id.lv_StockCheck);
        mStockCheckListViewAdapter = new StockCheckListViewAdapter(mContext, mStockCheckDataArrayList);
        mlvStockCheck.setAdapter(mStockCheckListViewAdapter);
        mlvStockCheck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectStockCheckData = mStockCheckDataArrayList.get(position);
                EventBus.getDefault().post(new Event.StockCheckDetailbyCheckMonthEvent(mSelectStockCheckData));
            }
        });
        //endregion

    }
    //endregion

    //region UpDateShippingPlan
    protected void UpDateOrder() {
        String StockCheckState = spStockCheckState.getSelectedItem().toString();
        String Warehouse = spWarehouse.getSelectedItem().toString();
        String OrderFromDate = tvCheckMonth.getText().toString();
        getStockCheck(StockCheckState, OrderFromDate,Warehouse);
        EventBus.getDefault().post(new Event.StockCheckClearOrderItemEvent());
    }

    private void getStockCheck(String StockCheckState, String orderFromDate,String Warehouse) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql;
        selectDataListsql = String.format(getString(R.string.GetStockCheckQuery),orderFromDate);
        if (!StockCheckState.equals(getString(R.string.SpinnerDefault))) {
            selectDataListsql  = selectDataListsql+ String.format(getString(R.string.gscConditionState),StockCheckState);
        }
        if (!Warehouse.equals(getString(R.string.SpinnerDefault))){
            selectDataListsql  = selectDataListsql+ String.format(getString(R.string.gscConditionWarehouse),Warehouse);
        }
        selectDataListsql = selectDataListsql+getString(R.string.gscOrderBy);
        Log.e("查询StockChecksql语句", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mStockCheckDataArrayList.clear();
        if (cursorDatalist.getCount() != 0) {
            Log.e("sqlquery", cursorDatalist.getCount() + "");
            while (cursorDatalist.moveToNext()) {
                StockCheckData stockCheckData = new StockCheckData();
                stockCheckData.setCHECKMONTH(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKMONTH)));
                stockCheckData.setWAREHOUSEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.WAREHOUSEID)));
                stockCheckData.setSTARTDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.STARTDATE)));
                stockCheckData.setENDDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.ENDDATE)));
                stockCheckData.setSTATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.STATE)));
                Log.e("sqlquery结果", stockCheckData.toString());
                mStockCheckDataArrayList.add(stockCheckData);
            }
        }
        mStockCheckListViewAdapter.notifyDataSetChanged();
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

            year = myyear;
            month = monthOfYear;
            day = dayOfMonth;
            if (month < 9) {
                tvCheckMonth.setText("" + year + "-0" + (month + 1));
            } else {
                tvCheckMonth.setText("" + year + "-" + (month + 1));
            }
            UpDateOrder();
        }
    };

    //endregion

}