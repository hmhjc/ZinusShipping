package cn.zinus.shipping.Fragment.lotshipping;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Adapter.POListViewAdapter;
import cn.zinus.shipping.Fragment.Event;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.JaveBean.CodeData;
import cn.zinus.shipping.JaveBean.PODate;
import cn.zinus.shipping.JaveBean.ShippingPlanData;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;

import static cn.zinus.shipping.util.Constant.CONTAINERNO;
import static cn.zinus.shipping.util.Constant.SEALNO;
import static cn.zinus.shipping.util.Constant.UPDATEUI;

/**
 * Created by Spring on 2017/12/28.
 */

public class ShippingPlanDetailFragment extends KeyDownFragment {

    //region ◆ 변수(Variables)
    static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
    //上下文
    private MainNaviActivity mContext;
    //这个画面的ShippingPlan的信息
    private ShippingPlanData mShippingPlanData;
    //ShippingPlanSEQSpinner
    protected Spinner spShippingPlanSEQ;
    private ArrayAdapter mShippingPlanSEQAdapter;
    private ArrayList<CodeData> ShippingPlanSEQList;
    //ShippingPlanSEQSpinner
    protected Spinner spContainerSEQ;
    private ArrayAdapter mContainerSEQAdapter;
    private ArrayList<CodeData> ContainerSEQList;
    //ListView
    private ListView mlvPOList;
    private POListViewAdapter mPOListViewAdapter;
    public ArrayList<PODate> mPODataList;
    //PopupWindow
    private PopupWindow mpopFixQty;
    private View mViewSetContainerNoAndSealNo;
    private Button btnSetContainerNoAndSealNo;
    //数据库
    MyDateBaseHelper mHelper;
    SQLiteDatabase db;
    private Handler handler = null;
    public void setShippingPlanData(ShippingPlanData shippingPlanData) {
        mShippingPlanData = shippingPlanData;
    }

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
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATEUI:
                        spShippingPlanSEQ.setSelection(-1,true);
                        spContainerSEQ.setSelection(-1,true);
                        //spShippingPlanSEQ.setSelection(-1);
                        mShippingPlanSEQAdapter.notifyDataSetChanged();
                        mContainerSEQAdapter.notifyDataSetChanged();
                        break;
                    case 123:
                        if ( spContainerSEQ.getSelectedItem()!=null){
                            searchPOlist(mShippingPlanData,((CodeData) spShippingPlanSEQ.getSelectedItem()).getCODEID(),((CodeData) spContainerSEQ.getSelectedItem()).getCODEID());
                        }
                        break;
                }
            }
        };
    }
    //endregion

    //region onCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shippingplandetail, container, false);
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
                // searchPOlist();
                break;
            case R.id.action_ClearAll:
                clearall();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearall() {
        mPODataList.clear();
        mPOListViewAdapter.notifyDataSetChanged();
    }
    //endregion

    //endregion

    //region ◆ Fucation

    //region initData
    private void initData() {
        mShippingPlanData = new ShippingPlanData();
        ShippingPlanSEQList = new ArrayList<>();
        ContainerSEQList = new ArrayList<>();
        mPODataList = new ArrayList<>();
        // ShippingPlanSEQList.add(new CodeData("",getString(R.string.SpinnerDefault)));
    }
    //endregion

    //region initview

    private void initview() {

        //region ShippingPlanSEQSpinner
        spShippingPlanSEQ = (Spinner) getView().findViewById(R.id.sp_ShippingPlanSeq);
        mShippingPlanSEQAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, ShippingPlanSEQList);
        spShippingPlanSEQ.setAdapter(mShippingPlanSEQAdapter);
        spShippingPlanSEQ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchContainerSEQ(mShippingPlanData.getSHIPPINGPLANNO(),((CodeData) spShippingPlanSEQ.getSelectedItem()).getCODEID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        //region ShippingPlanSEQSpinner
        spContainerSEQ = (Spinner) getView().findViewById(R.id.sp_ContainerSeq);
        mContainerSEQAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, ContainerSEQList);
        spContainerSEQ.setAdapter(mContainerSEQAdapter);
        spContainerSEQ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchPOlist(mShippingPlanData,((CodeData) spShippingPlanSEQ.getSelectedItem()).getCODEID(),((CodeData) spContainerSEQ.getSelectedItem()).getCODEID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        //region PO ListView
        mlvPOList = (ListView) getView().findViewById(R.id.lv_PoList);
        mPOListViewAdapter = new POListViewAdapter(mContext, mPODataList);
        mlvPOList.setAdapter(mPOListViewAdapter);
        mlvPOList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new Event.LotShippingByPOEvent(mShippingPlanData,mPODataList.get(position)));
            }
        });

        //endregion

        //region SetContainerNoAndSealNo PopWindow
        mViewSetContainerNoAndSealNo = mContext.getLayoutInflater().inflate(R.layout.setcontainernoandsealno, null);
        btnSetContainerNoAndSealNo = (Button) getView().findViewById(R.id.btnSetContainerNoAndSealNo);
        btnSetContainerNoAndSealNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetContainerNoAndSealNo(view);
            }
        });
        //endregion

    }

    //endregion

    //region SetContainerNoAndSealNo
    @SuppressLint("WrongConstant")
    private void SetContainerNoAndSealNo(View view) {
        final PODate tempdata = mPODataList.get(0);
        if (tempdata.getCONTAINERNO()==null){
            tempdata.setCONTAINERNO("");
        }
        if (tempdata.getSEALNO()==null){
            tempdata.setSEALNO("");
        }
        Button btnConfirm = (Button) mViewSetContainerNoAndSealNo.findViewById(R.id.btnfqty);
        final EditText etContainerNo = (EditText) mViewSetContainerNoAndSealNo.findViewById(R.id.etContainerNo);
        final EditText etSealNo= (EditText) mViewSetContainerNoAndSealNo.findViewById(R.id.etSealNo);
        etContainerNo.setText(tempdata.getCONTAINERNO() + "");
        etSealNo.setText(tempdata.getSEALNO()+ "");
        etContainerNo.setSelection(etContainerNo.length());
        etSealNo.setSelection(etSealNo.length());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0;i<mPODataList.size();i++){
                    PODate date = mPODataList.get(i);
                    date.setCONTAINERNO(etContainerNo.getText().toString());
                    date.setSEALNO(etSealNo.getText().toString());
                    mPODataList.set(i,date);
               }
                //修改sqlite中
                ContentValues shippingPlanDetailValues = new ContentValues();
                shippingPlanDetailValues.put(CONTAINERNO, etContainerNo.getText().toString());
                shippingPlanDetailValues.put(SEALNO, etSealNo.getText().toString());
                db.update(Constant.SF_SHIPPINGPLANDETAIL, shippingPlanDetailValues,
                        "SHIPPINGPLANNO = ? AND SHIPPINGPLANSEQ = ? AND CONTAINERSEQ = ?"
                        , new String[]{mShippingPlanData.getSHIPPINGPLANNO()
                                ,((CodeData) spShippingPlanSEQ.getSelectedItem()).getCODEID()
                                ,((CodeData) spContainerSEQ.getSelectedItem()).getCODEID()});
                mPOListViewAdapter.notifyDataSetChanged();
//                mcomsumeInboundDataList.set(position, tempdata);
//                mConsumeInboundListViewAdapter.notifyDataSetChanged();
//                Message message = new Message();
//                message.what = UPDATEUI;
//                message.obj = position;
//                handler.sendMessage(message);
                mpopFixQty.dismiss();
            }
        });
        mpopFixQty = new PopupWindow(mViewSetContainerNoAndSealNo, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
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
//                InputMethodManager inputManager = (InputMethodManager) etFixQty.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.showSoftInput(etFixQty, 0);
            }
        }, 500);
    }
    //endregion

    //region searchPOlist
    protected void searchPOlist(ShippingPlanData shippingPlanData,String ShippingplanSeq,String ContainerSeq) {
        mPODataList.clear();
        String selectDataListsql = String.format(getString(R.string.GetPOQuery),shippingPlanData.getSHIPPINGPLANNO(),ShippingplanSeq,ContainerSeq);
        Log.e("GetPOQuery", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                PODate poDate = new PODate();
                poDate.setPOID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.POID)));
                poDate.setPOQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.QTY)));
                poDate.setCONTAINERNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONTAINERNO)));
                poDate.setSEALNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SEALNO)));
                poDate.setPRODUCTDEFID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PRODUCTDEFID)));
                poDate.setPRODUCTDEFNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PRODUCTDEFNAME)));
                poDate.setPRODUCTDEFVERSION(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PRODUCTDEFVERSION)));
                poDate.setORDERNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.ORDERNO)));
                poDate.setORDERTYPE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.ORDERTYPE)));
                poDate.setLINENO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.LINENO)));
                poDate.setCONTAINERSEQ(ContainerSeq);
                poDate.setSHIPPINGPLANSEQ(ShippingplanSeq);
                mPODataList.add(poDate);
            }
            mPOListViewAdapter.notifyDataSetChanged();
        }
    }

    public void searchPlanSeq(String shippingplanNo) {
        ShippingPlanSEQList.clear();
        String selectDataListsql = String.format(getString(R.string.ShippingPlanSEQSpinner),shippingplanNo);
        Log.e("ShippingPlanSEQ", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                CodeData spinnerData = new CodeData();
                spinnerData.setCODEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGPLANSEQ)));
                spinnerData.setDICTIONARYNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGPLANSEQ)));
                Log.e("spinnerData",spinnerData.toString());
                ShippingPlanSEQList.add(spinnerData);
            }
        }
        mShippingPlanSEQAdapter.notifyDataSetChanged();

        Message message = new Message();
        message.what = UPDATEUI;
        handler.sendMessage(message);
        if ( spShippingPlanSEQ.getSelectedItem()!=null){
            searchContainerSEQ(mShippingPlanData.getSHIPPINGPLANNO(),((CodeData) spShippingPlanSEQ.getSelectedItem()).getCODEID());
        }
    }

    private void searchContainerSEQ(String shippingPlanNo,String shippingPlanSeq) {
        ContainerSEQList.clear();
        String selectDataListsql = String.format(getString(R.string.ContainerSEQSpinner),shippingPlanNo,shippingPlanSeq);
        Log.e("ContainerSEQList", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                CodeData spinnerData = new CodeData();
                spinnerData.setCODEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONTAINERSEQ)));
                spinnerData.setDICTIONARYNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CODENAME)));
                ContainerSEQList.add(spinnerData);
            }
        }
        mContainerSEQAdapter.notifyDataSetChanged();
        Message message = new Message();
        message.what = 123;
        handler.sendMessage(message);

    }

    //endregion

//endregion
}