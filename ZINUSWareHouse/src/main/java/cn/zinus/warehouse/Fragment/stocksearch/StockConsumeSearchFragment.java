package cn.zinus.warehouse.Fragment.stocksearch;

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
import android.widget.ListView;
import android.widget.Spinner;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Adapter.StockCheckListViewAdapter;
import cn.zinus.warehouse.Config.AppConfig;
import cn.zinus.warehouse.Fragment.Event;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.JaveBean.StockCheckData;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;

/**
 * Developer:Spring
 * DataTime :2017/9/27 14:05
 * Main Change:
 */

public class StockConsumeSearchFragment extends KeyDownFragment {

    //region ◆ 변수(Variables)
    //上下文
    private MainNaviActivity mContext;
    //WarehousTypeSpinner
    protected Spinner spWarehousType;
    private ArrayAdapter mWarehousTypeAdapter;
    private ArrayList<String> WarehousTypeList;
    //ConsumabledefIdSpinner
    protected Spinner spConsumabledefId;
    private ArrayAdapter mConsumabledefIdAdapter;
    private ArrayList<String> ConsumabledefIdList;
    //LocationIdSpinner
    protected Spinner spLocationId;
    private ArrayAdapter mLocationIdAdapter;
    private ArrayList<String> LocationIdList;
    //ConsumbleTypeSpinner
    protected Spinner spConsumbleType;
    private ArrayAdapter mConsumbleTypeAdapter;
    private ArrayList<String> ConsumbleTypeList;
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
        return inflater.inflate(R.layout.fragment_stockconsumesearch, container, false);
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
        //region WarehousTypeSpinner
        WarehousTypeList = new ArrayList<>();
        WarehousTypeList.add(0, getString(R.string.SpinnerDefault));
        String selectDataListsql = String.format(getString(R.string.codeSpinner),Constant.WAREHOUSETYPE,AppConfig.getInstance().getLanuageType());
        Log.e("codespinner",selectDataListsql);
        Cursor cursorWarehouseType = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorWarehouseType.getCount() != 0) {
            while (cursorWarehouseType.moveToNext()) {
                WarehousTypeList.add(cursorWarehouseType.getString(cursorWarehouseType.getColumnIndex(Constant.DICTIONARYNAME)));
            }
        }
        //endregion

        //region ConsumabledefIdSpinner
        ConsumabledefIdList = new ArrayList<>();
        ConsumabledefIdList.add(0, getString(R.string.SpinnerDefault));
        String selectConsumabledefId = getString(R.string.ConsumabledefIDSpinner);
        Log.e("selectConsumabledefId",selectConsumabledefId);
        Cursor cursorConsumabledefId = DBManger.selectDatBySql(db, selectConsumabledefId, null);
        if (cursorConsumabledefId.getCount() != 0) {
            while (cursorConsumabledefId.moveToNext()) {
                ConsumabledefIdList.add(cursorConsumabledefId.getString(cursorConsumabledefId.getColumnIndex(Constant.CONSUMABLEDEFID)));
            }
        }
        //endregion

        //region LocationIdListSpinner
        LocationIdList = new ArrayList<>();
        LocationIdList.add(0, getString(R.string.SpinnerDefault));
        String LocationIdListsql = String.format(getString(R.string.codeSpinner),Constant.PROCESSTYPE,AppConfig.getInstance().getLanuageType());
        Cursor cursorLocationId = DBManger.selectDatBySql(db, LocationIdListsql, null);
        if (cursorLocationId.getCount() != 0) {
            while (cursorLocationId.moveToNext()) {
                LocationIdList.add(cursorLocationId.getString(cursorLocationId.getColumnIndex(Constant.DICTIONARYNAME)));
            }
        }
        //endregion

        //region WarehousTypeSpinner
        ConsumbleTypeList = new ArrayList<>();
        ConsumbleTypeList.add(0, getString(R.string.SpinnerDefault));
        String ConsumbleTypesql = String.format(getString(R.string.codeSpinner),Constant.CONSUMABLETYPE,AppConfig.getInstance().getLanuageType());
        Cursor cursorConsumbleType = DBManger.selectDatBySql(db, ConsumbleTypesql, null);
        if (cursorConsumbleType.getCount() != 0) {
            while (cursorConsumbleType.moveToNext()) {
                ConsumbleTypeList.add(cursorConsumbleType.getString(cursorConsumbleType.getColumnIndex(Constant.DICTIONARYNAME)));
            }
        }
        //endregion

        mStockCheckDataArrayList = new ArrayList<>();
    }
    //endregion

    //region initview
    private void initview() {

        //region WarehousTypeSpinner
        spWarehousType = (Spinner) getView().findViewById(R.id.sp_WarehousType);
        mWarehousTypeAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, WarehousTypeList);
        spWarehousType.setAdapter(mWarehousTypeAdapter);
        spWarehousType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UpDateOrder();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        //region ConsumabledefIdSpinner
        spConsumabledefId = (Spinner) getView().findViewById(R.id.sp_ConsumabledefId);
        mConsumabledefIdAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, ConsumabledefIdList);
        spConsumabledefId.setAdapter(mConsumabledefIdAdapter);
        spConsumabledefId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UpDateOrder();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        //region LocationIdSpinner
        spLocationId = (Spinner) getView().findViewById(R.id.sp_LocationId);
        mLocationIdAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, LocationIdList);
        spLocationId.setAdapter(mLocationIdAdapter);
        spLocationId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UpDateOrder();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        //region ConsumbleTypeSpinner
        spConsumbleType = (Spinner) getView().findViewById(R.id.sp_ConsumbleType);
        mConsumbleTypeAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, ConsumbleTypeList);
        spConsumbleType.setAdapter(mConsumbleTypeAdapter);
        spConsumbleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UpDateOrder();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        //region ImportOrder ListView
        mlvStockCheck = (ListView) getView().findViewById(R.id.lv_StockConsumeSearch);
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
        String WarehousType = spWarehousType.getSelectedItem().toString();
        String ConsumabledefId = spConsumabledefId.getSelectedItem().toString();
        String LocationId = spLocationId.getSelectedItem().toString();
        String ConsumbleType = spConsumbleType.getSelectedItem().toString();
        //getStockConsumeSearch(WarehousType, ConsumabledefId, LocationId, ConsumbleType);
    }

    private void getStockConsumeSearch(String WarehousType, String ConsumabledefId, String LocationId, String ConsumbleType) {
        if (WarehousType.equals(getString(R.string.SpinnerDefault))) {
            WarehousType = "";
        }
        if (ConsumabledefId.equals(getString(R.string.SpinnerDefault))) {
            ConsumabledefId = "";
        }
        if (LocationId.equals(getString(R.string.SpinnerDefault))) {
            LocationId = "";
        }
        if (ConsumbleType.equals(getString(R.string.SpinnerDefault))) {
            ConsumbleType = "";
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql;
        selectDataListsql = "select * from " + Constant.SF_CONSUMESTOCK + " where 1 = 1 "
                + " and " + Constant.INBOUNDSTATE + " like '%" + WarehousType + "%' "
                + " and " + Constant.INBOUNDSTATE + " like '%" + WarehousType + "%' "
                + " order by " + Constant.WAREHOUSEID
        ;
        Log.e("查询StockChecksql语句1111", selectDataListsql);
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

}