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
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Adapter.PlanSeqListViewAdapter;
import cn.zinus.shipping.Fragment.Event;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.JaveBean.CodeData;
import cn.zinus.shipping.JaveBean.ShippingCommonData;
import cn.zinus.shipping.JaveBean.ShippingPlanData;
import cn.zinus.shipping.JaveBean.ShippingPlanDetailData;
import cn.zinus.shipping.JaveBean.ShippingPlanSeqListData;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;

import static cn.zinus.shipping.util.Constant.CONTAINERNO;
import static cn.zinus.shipping.util.Constant.SEALNO;
import static cn.zinus.shipping.util.Constant.UPDATEUI;
import static cn.zinus.shipping.util.DBManger.getCursorData;

/**
 * Created by Spring on 2017/12/28.
 */

public class ShippingPlanDetailFragment extends KeyDownFragment {

    //region ◆ 변수(Variables)
    static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
    //上下文
    private MainNaviActivity mContext;
    //这个画面的ShippingPlan的信息
    private ShippingPlanData _mShippingPlanData;
    private ShippingPlanDetailData _mShippingPlanDetailData;
    private String _ContainerNO = "";
    private String _SealNO = "";
    private TextView tvBookingNo;
    private TextView tvShippingEndPlanDate;
    private TextView tvContainerNO;
    private TextView tvSealNO;
    //给第三个tab传下面公共的数据
    private ShippingCommonData _mShippingCommonData;
    //ContainerSEQSpinner
    protected Spinner spContainerSEQ;
    private ArrayAdapter mContainerSEQAdapter;
    private ArrayList<CodeData> ContainerSEQList;
    //ListView
    private ListView mlvPlanSeqList;
    private PlanSeqListViewAdapter mPlanSeqListViewAdapter;
    public ArrayList<ShippingPlanSeqListData> mPlanSeqListData;
    //PopupWindow
    private PopupWindow mpopFixQty;
    private View mViewSetContainerNoAndSealNo;
    private Button btnSetContainerNoAndSealNo;
    private Button btnScanContainerTag;
    //数据库
    MyDateBaseHelper mHelper;
    SQLiteDatabase db;
    private Handler handler = null;

    public void setShippingPlanData(ShippingPlanData shippingPlanData) {
        _mShippingPlanData = shippingPlanData;
        setCommonText();
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
                        spContainerSEQ.setSelection(-1, true);
                        mContainerSEQAdapter.notifyDataSetChanged();
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
                // searchShippingPlanlist();
                break;
            case R.id.action_ClearAll:
                clearall();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearall() {
        mPlanSeqListData.clear();
        mPlanSeqListViewAdapter.notifyDataSetChanged();
        ContainerSEQList.clear();
    }
    //endregion

    //endregion

    //region ◆ Fucation

    //region initData
    private void initData() {
        _mShippingPlanData = new ShippingPlanData();
        _mShippingPlanDetailData = new ShippingPlanDetailData();
        _mShippingCommonData = new ShippingCommonData();
        ContainerSEQList = new ArrayList<>();
        mPlanSeqListData = new ArrayList<>();
    }
    //endregion

    //region initview

    private void initview() {

        //region CommonTexiView
        tvBookingNo = (TextView) getView().findViewById(R.id.tv_BookingNo);
        tvShippingEndPlanDate = (TextView) getView().findViewById(R.id.tv_ShippingEndPlanDate);
        tvContainerNO = (TextView) getView().findViewById(R.id.tv_ContainerNo);
        tvSealNO = (TextView) getView().findViewById(R.id.tv_SealNo);
        //endregion

        //region ContainerSEQSpinner
        spContainerSEQ = (Spinner) getView().findViewById(R.id.sp_ContainerSeq);
        mContainerSEQAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, ContainerSEQList);
        spContainerSEQ.setAdapter(mContainerSEQAdapter);
        spContainerSEQ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("searchShippingPlanlist","193");
                searchShippingPlanlist( ((CodeData) spContainerSEQ.getSelectedItem()).getCODEID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //endregion

        //region PlanSeq ListView
        mlvPlanSeqList = (ListView) getView().findViewById(R.id.lv_PoList);
        mPlanSeqListViewAdapter = new PlanSeqListViewAdapter(mContext, mPlanSeqListData);
        mlvPlanSeqList.setAdapter(mPlanSeqListViewAdapter);
        mlvPlanSeqList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EventBus.getDefault().post(new Event.LotShippingByContainerEvent(_mShippingPlanData, mPlanSeqListData,_mShippingCommonData));
            }
        });

        //endregion

        //region Button
        mViewSetContainerNoAndSealNo = mContext.getLayoutInflater().inflate(R.layout.setcontainernoandsealno, null);
        btnSetContainerNoAndSealNo = (Button) getView().findViewById(R.id.btnSetContainerNoAndSealNo);
        btnSetContainerNoAndSealNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetContainerNoAndSealNo(view);
            }
        });

        btnScanContainerTag = (Button) getView().findViewById(R.id.btnScanContainerTag);
        btnScanContainerTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new Event.LotShippingByContainerEvent(_mShippingPlanData, mPlanSeqListData,_mShippingCommonData));
            }
        });
        //endregion

    }

    //endregion

    //region SetContainerNoAndSealNo
    @SuppressLint("WrongConstant")
    private void SetContainerNoAndSealNo(View view) {
        final ShippingPlanSeqListData tempdata = mPlanSeqListData.get(0);
        if (tempdata.getCONTAINERNO() == null) {
            tempdata.setCONTAINERNO("");
        }
        if (tempdata.getSEALNO() == null) {
            tempdata.setSEALNO("");
        }
        Button btnConfirm = (Button) mViewSetContainerNoAndSealNo.findViewById(R.id.btnfqty);
        final EditText etContainerNo = (EditText) mViewSetContainerNoAndSealNo.findViewById(R.id.etContainerNo);
        final EditText etSealNo = (EditText) mViewSetContainerNoAndSealNo.findViewById(R.id.etSealNo);
        etContainerNo.setText(tempdata.getCONTAINERNO() + "");
        etSealNo.setText(tempdata.getSEALNO() + "");
        etContainerNo.setSelection(etContainerNo.length());
        etSealNo.setSelection(etSealNo.length());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改显示的内容
                _ContainerNO = etContainerNo.getText().toString();
                _SealNO = etSealNo.getText().toString();
                setCommonText();
                //修改sqlite中的内容
                ContentValues shippingPlanDetailValues = new ContentValues();
                shippingPlanDetailValues.put(CONTAINERNO, etContainerNo.getText().toString());
                shippingPlanDetailValues.put(SEALNO, etSealNo.getText().toString());
                db.update(Constant.SF_SHIPPINGPLANDETAIL, shippingPlanDetailValues,
                        "SHIPPINGPLANNO = ? AND CONTAINERSEQ = ?"
                        , new String[]{_mShippingPlanData.getSHIPPINGPLANNO()
                                , ((CodeData) spContainerSEQ.getSelectedItem()).getCODEID()});
                //修改sqlite的标志位
                updateSqliteFlag();
                mPlanSeqListViewAdapter.notifyDataSetChanged();
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
            }
        }, 500);
    }

    private void updateSqliteFlag() {
        //修改plan的ispdashipping为Y
        ContentValues shippingplanValues = new ContentValues();
        shippingplanValues.put(Constant.ISPDASHIPPING, "Y");
        db.update(Constant.SF_SHIPPINGPLAN, shippingplanValues, "SHIPPINGPLANNO = ?", new String[]{_mShippingPlanData.getSHIPPINGPLANNO()});
        //修改planDeatil的ispdashipping为Y
        ContentValues shippingplanDetailValues = new ContentValues();
        shippingplanDetailValues.put(Constant.ISPDASHIPPING, "Y");
        db.update(Constant.SF_SHIPPINGPLANDETAIL, shippingplanValues, "SHIPPINGPLANNO = ?" +
                "AND CONTAINERSEQ = ? ", new String[]{_mShippingPlanData.getSHIPPINGPLANNO()
                ,((CodeData) spContainerSEQ.getSelectedItem()).getCODEID()});
    }

    //endregion

    //region searchShippingPlanlist

    /**
     * 货柜序号变化以后,根据计划序号和货柜序号,查询
     * @param ContainerSeq
     */
    public void searchShippingPlanlist(String ContainerSeq) {
        mPlanSeqListData.clear();
        String selectDataListsql = String.format(getString(R.string.GetPlanSeqQuery), _mShippingPlanData.getSHIPPINGPLANNO(), ContainerSeq);
        Log.e("GetPlanSeqQuery", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                ShippingPlanSeqListData shippingPlanSeqListData = new ShippingPlanSeqListData();
                shippingPlanSeqListData.setSHIPPINGPLANSEQ(getCursorData(cursorDatalist,Constant.SHIPPINGPLANSEQ));
                shippingPlanSeqListData.setPOID(getCursorData(cursorDatalist,Constant.POID));
                shippingPlanSeqListData.setQTY(getCursorData(cursorDatalist,Constant.PLANQTY));
                shippingPlanSeqListData.setCONTAINERNO(getCursorData(cursorDatalist,Constant.CONTAINERNO));
                shippingPlanSeqListData.setSEALNO(getCursorData(cursorDatalist,Constant.SEALNO));
                shippingPlanSeqListData.setPRODUCTDEFID(getCursorData(cursorDatalist,Constant.PRODUCTDEFID));
                shippingPlanSeqListData.setPRODUCTDEFNAME(getCursorData(cursorDatalist,Constant.PRODUCTDEFNAME));
                shippingPlanSeqListData.setPRODUCTDEFVERSION(getCursorData(cursorDatalist,Constant.PRODUCTDEFVERSION));
                shippingPlanSeqListData.setORDERNO(getCursorData(cursorDatalist,Constant.ORDERNO));
                shippingPlanSeqListData.setORDERTYPE(getCursorData(cursorDatalist,Constant.ORDERTYPE));
                shippingPlanSeqListData.setLINENO(getCursorData(cursorDatalist,Constant.LINENO));
                shippingPlanSeqListData.setSHIPPINGEDQTY(getCursorData(cursorDatalist,Constant.SHIPPINGEDQTY));
                shippingPlanSeqListData.setNOSHIPPINGEDQTY(getCursorData(cursorDatalist,Constant.NOSHIPPINGEDQTY));
                shippingPlanSeqListData.setCONTAINERSEQ(ContainerSeq);
                mPlanSeqListData.add(shippingPlanSeqListData);
                _ContainerNO = getCursorData(cursorDatalist,Constant.CONTAINERNO);
                _SealNO = getCursorData(cursorDatalist,Constant.SEALNO);

            }
            setCommonText();
            mPlanSeqListViewAdapter.notifyDataSetChanged();
        }
    }

    //endregion

    /**
     * 单击第一个tab的计划,查询货柜顺序绑定到spinner上,并查询计划顺序,显示到下面的list中
     *
     * @param shippingplanNo 计划序号
     */
    public void searchContainerSeq(String shippingplanNo) {
        String selectDataListsql = String.format(getString(R.string.ContainerSEQSpinner), shippingplanNo);
        Log.e("ContainerSEQ", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                CodeData spinnerData = new CodeData();
                spinnerData.setCODEID(getCursorData(cursorDatalist,Constant.CONTAINERSEQ));
                spinnerData.setDICTIONARYNAME(getCursorData(cursorDatalist,Constant.CODENAME));
                Log.e("spinnerData", spinnerData.toString());
                ContainerSEQList.add(spinnerData);
            }
        }
        mContainerSEQAdapter.notifyDataSetChanged();
        Message message = new Message();
        message.what = UPDATEUI;
        handler.sendMessage(message);
        if (spContainerSEQ.getSelectedItem() != null) {
            Log.e("searchShippingPlanlist","367");
            searchShippingPlanlist( ((CodeData) spContainerSEQ.getSelectedItem()).getCODEID());
        }
    }

    private void setCommonText() {
        tvBookingNo.setText(_mShippingPlanData.getBOOKINGNO());
        tvShippingEndPlanDate.setText(_mShippingPlanData.getSHIPPINGENDPLANDATE());
        tvContainerNO.setText(_ContainerNO);
        tvSealNO.setText(_SealNO);
        _mShippingCommonData.setBOOKINGNO(_mShippingPlanData.getBOOKINGNO());
        _mShippingCommonData.setShippingEndPlanDate(_mShippingPlanData.getSHIPPINGENDPLANDATE());
        _mShippingCommonData.setContainerNo(_ContainerNO);
        _mShippingCommonData.setSEALNO(_SealNO);
    }

//endregion

}