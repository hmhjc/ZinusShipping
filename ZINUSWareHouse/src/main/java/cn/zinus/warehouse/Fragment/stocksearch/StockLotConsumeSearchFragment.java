package cn.zinus.warehouse.Fragment.stocksearch;

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

import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Adapter.StockCheckListViewAdapter;
import cn.zinus.warehouse.Config.AppConfig;
import cn.zinus.warehouse.Fragment.Event;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.JaveBean.CodeData;
import cn.zinus.warehouse.JaveBean.StockCheckData;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;

/**
 * Developer:Spring
 * DataTime :2017/9/25 10:05
 * Main Change:
 */

public class StockLotConsumeSearchFragment extends KeyDownFragment {

    //region ◆ 변수(Variables)
    static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
    //上下文
    private MainNaviActivity mContext;
    //StockCheckStateSpinner
    protected Spinner spStockCheckState;
    private ArrayAdapter mStockCheckStateAdapter;
    private ArrayList<String> StockCheckStateList;
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
        StockCheckStateList = new ArrayList<>();
        StockCheckStateList.add(0, getString(R.string.SpinnerDefault));
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = "SELECT * FROM " + Constant.SF_CODE
                + " WHERE " + Constant.CODECLASSID + " = 'ConsumeOutboundState' and "
                + Constant.LANGUAGETYPE + " = '" + AppConfig.getInstance().getLanuageType() + "'";
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("sqlquery", cursorDatalist.getCount() + "");
            while (cursorDatalist.moveToNext()) {
                CodeData codeDataData = new CodeData();
                codeDataData.setCODEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CODEID)));
                codeDataData.setCODECLASSID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CODECLASSID)));
                codeDataData.setDICTIONARYNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.DICTIONARYNAME)));
                codeDataData.setLANGUAGETYPE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.LANGUAGETYPE)));
                Log.e("sqlquery结果", codeDataData.toString());
                StockCheckStateList.add(codeDataData.getDICTIONARYNAME());
            }
        }

        //初始化
        mStockCheckDataArrayList = new ArrayList<>();
        //UpDateShippingPlan();
    }
    //endregion

    //region initview
    private void initview() {

        //region Spinner
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

    //region public void setenable(boolean flag)
    public void setenable(boolean flag) {
        if (flag) {
            mlvStockCheck.setEnabled(true);
        } else {
            mlvStockCheck.setEnabled(false);
        }
    }
    //endregion

    //region UpDateShippingPlan
    protected void UpDateOrder() {
        String ConsumeOutboundState = spStockCheckState.getSelectedItem().toString();
        String OrderFromDate = tvCheckMonth.getText().toString();

        getStockCheck(ConsumeOutboundState, OrderFromDate);
        //    EventBus.getDefault().post(new Event.InboundClearOrderItemEvent());
    }

    private void getStockCheck(String ConsumeIOutboundState, String orderFromDate) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql;
        if (ConsumeIOutboundState.equals(getString(R.string.SpinnerDefault))) {
            selectDataListsql = "select * from " + Constant.SF_STOCKCHECK + " where 1 = 1 "
                    + " and " + Constant.CHECKMONTH + " = '" + orderFromDate + "'"
                    + " order by " + Constant.WAREHOUSEID
            ;
        } else {
            selectDataListsql = "select * from " + Constant.SF_CONSUMEREQUEST + " where 1 = 1 "
                    + " and " + Constant.INBOUNDSTATE + " = '" + ConsumeIOutboundState + "' "
                    + " and " + Constant.CHECKMONTH + " = '" + orderFromDate + "'"
                    + " order by " + Constant.WAREHOUSEID
            ;
        }
        Log.e("查询StockChecksql语句222", selectDataListsql);
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
                stockCheckData.setSTATENAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.STATE)));
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