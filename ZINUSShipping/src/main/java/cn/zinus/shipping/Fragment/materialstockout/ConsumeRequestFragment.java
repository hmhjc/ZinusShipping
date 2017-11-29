package cn.zinus.shipping.Fragment.materialstockout;

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

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Adapter.ConsumeRequestListViewAdapter;
import cn.zinus.shipping.Config.AppConfig;
import cn.zinus.shipping.Fragment.Event;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.JaveBean.AreaData;
import cn.zinus.shipping.JaveBean.CodeData;
import cn.zinus.shipping.JaveBean.ConsumeRequestData;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;

/**
 * Created by Spring on 2017/2/18.
 */

public class ConsumeRequestFragment extends KeyDownFragment {

    //region ◆ 변수(Variables)
    static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
    //上下文
    private MainNaviActivity mContext;
    //ConsumeInboundStateSpinner
    protected Spinner spConsumeOutboundState;
    private ArrayAdapter mConsumeOutboundStateAdapter;
    private ArrayList<CodeData> ConsumeOutboundStateList;
    //AreaSpinner
    protected Spinner spArea;
    private ArrayAdapter mAreaAdapter;
    private ArrayList<AreaData> AreaList;
    //Data
    protected TextView tvFromDate;
    protected TextView tvToDate;
    private int year, pyear;
    private int month, pmonth;
    private int day, pday;
    private int fromtoflag;
    private Calendar mycalendar;
    //ListView
    private ListView mlvOutboundOrder;
    private ConsumeRequestListViewAdapter mConsumeRequestListViewAdapter;
    public ArrayList<ConsumeRequestData> mConsumeRequsetDataList;
    protected ConsumeRequestData mSelectConsumeRequestdata = new ConsumeRequestData();
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
        return inflater.inflate(R.layout.fragment_consumerequest, container, false);
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
        mConsumeRequsetDataList.clear();
        mConsumeRequestListViewAdapter.notifyDataSetChanged();
    }
    //endregion

    //endregion

    //region ◆ Fucation

    //region initData
    private void initData() {
        //初始化Spinner数据
        //region ConsumeOutboundStateList
        ConsumeOutboundStateList = new ArrayList<>();
        ConsumeOutboundStateList.add(new CodeData("",getString(R.string.SpinnerDefault)));
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.codeSpinner),Constant.CONSUMEOUTBOUNDSTATE,AppConfig.getInstance().getLanuageType());
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("State_query", cursorDatalist.getCount() + "");
            while (cursorDatalist.moveToNext()) {
                CodeData spinnerData = new CodeData();
                spinnerData.setCODEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CODEID)));
                spinnerData.setDICTIONARYNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.DICTIONARYNAME)));
                ConsumeOutboundStateList.add(spinnerData);
            }
        }
        //endregion

        //region AreaList
        AreaList = new ArrayList<>();
        AreaList.add(new AreaData("",getString(R.string.SpinnerDefault)));
        String selectAreaList = String.format(getString(R.string.AreaSpinner));
        Cursor cursorAreaList = DBManger.selectDatBySql(db, selectAreaList, null);
        if (cursorAreaList.getCount() != 0) {
            Log.e("Area_query", cursorDatalist.getCount() + "");
            while (cursorAreaList.moveToNext()) {
                AreaData areaSpinnerData = new AreaData();
                areaSpinnerData.setAREAID(cursorAreaList.getString(cursorAreaList.getColumnIndex(Constant.AREAID)));
                areaSpinnerData.setAREANAME(cursorAreaList.getString(cursorAreaList.getColumnIndex(Constant.AREANAME)));
                AreaList.add(areaSpinnerData);
            }
        }
        //endregion

        mConsumeRequsetDataList = new ArrayList<>();
    }
    //endregion

    //region initview
    private void initview() {

        //region spConsumeOutboundState
        spConsumeOutboundState = (Spinner) getView().findViewById(R.id.sp_ConsumeOutboundState);
        mConsumeOutboundStateAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, ConsumeOutboundStateList);
        spConsumeOutboundState.setAdapter(mConsumeOutboundStateAdapter);
        spConsumeOutboundState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UpDateOrder();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        //region spArea
        spArea = (Spinner) getView().findViewById(R.id.sp_AreaID);
        mAreaAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, AreaList);
        spArea.setAdapter(mAreaAdapter);
        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            tvFromDate.setText("" + pyear + "-0" + (pmonth + 1) + "-" + pday);
        } else {
            tvFromDate.setText("" + pyear + "-" + (pmonth + 1) + "-" + pday);
        }
        if (month < 9) {
            tvToDate.setText("" + year + "-0" + (month + 1) + "-" + day);
        } else {
            tvToDate.setText("" + year + "-" + (month + 1) + "-" + day);
        }
        //endregion

        //region ImportOrder ListView
        mlvOutboundOrder = (ListView) getView().findViewById(R.id.lv_consumeRequest);
        mConsumeRequestListViewAdapter = new ConsumeRequestListViewAdapter(mContext, mConsumeRequsetDataList);
        mlvOutboundOrder.setAdapter(mConsumeRequestListViewAdapter);
        mlvOutboundOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectConsumeRequestdata = mConsumeRequsetDataList.get(position);
                EventBus.getDefault().post(new Event.ConsumeOutboundByConsumeRequestEvent(mSelectConsumeRequestdata.getCONSUMEREQNO()));
            }
        });
        //endregion

    }
    //endregion

    //region UpDateShippingPlan
    protected void UpDateOrder() {
        String ConsumeOutboundState =((CodeData) spConsumeOutboundState.getSelectedItem()).getCODEID();
        String Areaid = ((AreaData)spArea.getSelectedItem()).getAREAID();
        String OrderFromDate = tvFromDate.getText().toString();
        String OrderToDate = tvToDate.getText().toString();
        getConsumeRequest(ConsumeOutboundState,Areaid, OrderFromDate, OrderToDate);
        EventBus.getDefault().post(new Event.OutboundClearOrderItemEvent());
    }

    private void getConsumeRequest(String ConsumeIOutboundState, String Areaid, String orderFromDate, String orderToDate) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetConsumeRequestQuery),orderFromDate,orderToDate);
        if (!ConsumeIOutboundState.equals("")) {
            selectDataListsql = selectDataListsql+String.format(getString(R.string.gcrConditionState),ConsumeIOutboundState);
        }
        if (!Areaid.equals("")) {
            selectDataListsql = selectDataListsql + String.format(getString(R.string.gcrConditionAreaId),Areaid);
        }
        selectDataListsql = selectDataListsql + getString(R.string.gcrOrderBy);
        Log.e("查询ConsumeRequestsql语句", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mConsumeRequsetDataList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                ConsumeRequestData inboundOrderData = new ConsumeRequestData();
                inboundOrderData.setCONSUMEREQNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMEREQNO)));
                inboundOrderData.setAREAID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.AREAID)));
                inboundOrderData.setUSERID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.USERID)));
                inboundOrderData.setREQUESTDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.REQUESTDATE)));
                inboundOrderData.setOUTBOUNDSTATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.OUTBOUNDSTATE)));
                mConsumeRequsetDataList.add(inboundOrderData);
            }
        }
        mConsumeRequestListViewAdapter.notifyDataSetChanged();
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