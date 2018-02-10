package cn.zinus.shipping.Fragment.lotshipping;

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
import cn.zinus.shipping.Adapter.ShippingPlanListViewAdapter;
import cn.zinus.shipping.Config.AppConfig;
import cn.zinus.shipping.Fragment.Event;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.JaveBean.CodeData;
import cn.zinus.shipping.JaveBean.ShippingPlanData;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;

/**
 * Created by Spring on 2017/2/18.
 */

public class ShippingPlanFragment extends KeyDownFragment {

    //region ◆ 변수(Variables)
    static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
    //上下文
    private MainNaviActivity mContext;
    //ConsumeInboundStateSpinner
    protected Spinner spShippingPlanState;
    private ArrayAdapter mShippingPlanStateAdapter;
    private ArrayList<CodeData> ShippingPlanStateList;
    //Data
    protected TextView tvFromDate;
    protected TextView tvToDate;
    private int year, pyear;
    private int month, pmonth;
    private int day, pday;
    //ListView
    private ListView mlvShippingPlan;
    private int fromtoflag;
    private Calendar mycalendar;
    private ShippingPlanListViewAdapter mShippingPlanListViewAdapter;
    public ArrayList<ShippingPlanData> mShippingPlanDataDataList;
    protected ShippingPlanData mSelectShippingPlandata = new ShippingPlanData();
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
        return inflater.inflate(R.layout.fragment_shippingplan, container, false);
    }
    //endregion

    //region onActivityCreated
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initview();
    }

    //endregion

    //region onOptionsItemSelected(菜单键按钮)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                UpDateShippingPlan();
                break;
            case R.id.action_ClearAll:
                clearall();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearall() {
        mShippingPlanDataDataList.clear();
        mShippingPlanListViewAdapter.notifyDataSetChanged();
    }
    //endregion

    //endregion

    //region ◆ Fucation

    //region initData
    private void initData() {
        //初始化Spinner数据
        //region ShippingPlanStateList
        ShippingPlanStateList = new ArrayList<>();
        ShippingPlanStateList.add(new CodeData("",getString(R.string.SpinnerDefault)));
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.codeSpinner),Constant.SHIPPINGPLANSTATE,AppConfig.getInstance().getLanuageType());
        Log.e("ShippingPlanState", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                CodeData spinnerData = new CodeData();
                spinnerData.setCODEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CODEID)));
                spinnerData.setDICTIONARYNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.DICTIONARYNAME)));
                ShippingPlanStateList.add(spinnerData);
            }
        }
        //endregion

        mShippingPlanDataDataList = new ArrayList<>();
    }
    //endregion

    //region initview
    private void initview() {

        //region ConsumeInboundStateSpinner
        spShippingPlanState = (Spinner) getView().findViewById(R.id.sp_ShippingPlanState);
        mShippingPlanStateAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, ShippingPlanStateList);
        spShippingPlanState.setAdapter(mShippingPlanStateAdapter);
        spShippingPlanState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UpDateShippingPlan();
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
        mlvShippingPlan = (ListView) getView().findViewById(R.id.lv_ShippingPlan);
        mShippingPlanListViewAdapter = new ShippingPlanListViewAdapter(mContext, mShippingPlanDataDataList);
        mlvShippingPlan.setAdapter(mShippingPlanListViewAdapter);
        mlvShippingPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectShippingPlandata = mShippingPlanDataDataList.get(position);
                EventBus.getDefault().post(new Event.LotShippingByShippingPlanEvent(mSelectShippingPlandata));
            }
        });

        //endregion

    }
    //endregion

    //region searchShippingPlanlist
    protected void UpDateShippingPlan() {
        String ShippingPlanState =((CodeData) spShippingPlanState.getSelectedItem()).getCODEID();
        String ShippingPlanFromDate = tvFromDate.getText().toString();
        String ShippingPlanToDate = tvToDate.getText().toString();
        getShippingPlan(ShippingPlanState, ShippingPlanFromDate, ShippingPlanToDate);
        EventBus.getDefault().post(new Event.ShippingClearLotShippingItemEvent());
    }

    public void getShippingPlan(String shippingPlanState, String orderFromDate, String orderToDate) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql;
        if (shippingPlanState.equals("")) {
            selectDataListsql = String.format(getString(R.string.GetShippingPlanQueryAll),orderFromDate,orderToDate);
        }else {
        selectDataListsql =String.format(getString(R.string.GetShippingPlanQueryNormal),shippingPlanState,orderFromDate,orderToDate);
        }
        selectDataListsql = selectDataListsql+getString(R.string.gspOrderBy);
        Log.e("ShippingPlan语句", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mShippingPlanDataDataList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                ShippingPlanData shippingPlanData = new ShippingPlanData();
                shippingPlanData.setSHIPPINGPLANNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGPLANNO)));
                shippingPlanData.setCUSTOMERID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CUSTOMERID)));
                shippingPlanData.setBOOKINGNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.BOOKINGNO)));
                shippingPlanData.setPLANDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PLANDATE)));
                shippingPlanData.setSHIPPINGPLANDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGPLANDATE)));
                shippingPlanData.setSHIPPINGENDPLANDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGENDPLANDATE)));
                shippingPlanData.setSHIPPINGENDDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGENDDATE)));
                shippingPlanData.setSTATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.STATE)));
                mShippingPlanDataDataList.add(shippingPlanData);
                Log.e("shippingPlanData",shippingPlanData.toString());
            }
        }
        mShippingPlanListViewAdapter.notifyDataSetChanged();
    }
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
                UpDateShippingPlan();
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


                UpDateShippingPlan();
            }
        }
    };

    //endregion

    //endregion

}