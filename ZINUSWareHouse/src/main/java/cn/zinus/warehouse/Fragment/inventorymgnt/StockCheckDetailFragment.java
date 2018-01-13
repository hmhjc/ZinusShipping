package cn.zinus.warehouse.Fragment.inventorymgnt;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Adapter.StockCheckDetailListViewAdapter;
import cn.zinus.warehouse.Config.AppConfig;
import cn.zinus.warehouse.Fragment.Event;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.JaveBean.StockCheckData;
import cn.zinus.warehouse.JaveBean.StockCheckDeatilData;
import cn.zinus.warehouse.JaveBean.StockLotCheckDeatilData;
import cn.zinus.warehouse.JaveBean.TagInfoData;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;

import static cn.zinus.warehouse.util.Constant.UPDATEUI;
import static cn.zinus.warehouse.util.DBManger.getCursorData;
import static cn.zinus.warehouse.util.Utils.showToast;

/**
 * Developer:Spring
 * DataTime :2017/9/25 13:40
 * Main Change:
 */

public class StockCheckDetailFragment extends KeyDownFragment {

    //region ◆ 변수(Variables)
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式
    //上下文
    private MainNaviActivity mContext;
    //ListView
    private ListView mlvStockCheckDetail;
    private StockCheckDetailListViewAdapter mStockCheckDetailListViewAdapter;
    private ArrayList<StockCheckDeatilData> mStockCheckDeatilDataArrayList;
    private ArrayList<String> IDlist;
    private TextView tvtagqty;
    private TextView tvCheckMonth;
    private PopupWindow mpopFixQty;
    private View mViewFixQty;
    //   private EditText etTagID;
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
        return inflater.inflate(R.layout.fragment_stockcheckdetail, container, false);
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
                            //-1代表第一次搜索
                            mlvStockCheckDetail.setSelection(0);
                        } else {
                            mlvStockCheckDetail.setSelection(position);
                            //修改好数字以后，更新数据库的数据
                            Log.e("保存了", "kaishi");
                            UpdateSF_STOCKCHECKDETAIL(mStockCheckDeatilDataArrayList.get(position));
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
            case R.id.action_save:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    //endregion

    //region ◆ Function

    //region initData
    private void initData() {
        mStockCheckDeatilDataArrayList = new ArrayList<>();
        IDlist = new ArrayList<>();
    }
    //endregion

    //region initView
    private void initview() {
        tvtagqty = (TextView) getView().findViewById(R.id.tvtagqty);
        tvCheckMonth = (TextView) getView().findViewById(R.id.CheckMonth);
        mViewFixQty = mContext.getLayoutInflater().inflate(R.layout.fixqty, null);
        //listViewDataList   **
        mlvStockCheckDetail = (ListView) getView().findViewById(R.id.lv_StockCheckDetail);
        mStockCheckDetailListViewAdapter = new StockCheckDetailListViewAdapter(mContext, mStockCheckDeatilDataArrayList);
        mlvStockCheckDetail.setAdapter(mStockCheckDetailListViewAdapter);
        //是LOT管理的资材，短按进入第三个扫描的画面
        //不是lot管理的资材，长按以后修改数据
        mlvStockCheckDetail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isInStockLotCheckDetail(mStockCheckDeatilDataArrayList.get(position).getCONSUMEABLDEFID()
                ,mStockCheckDeatilDataArrayList.get(position).getCONSUMEABLDEFVERSION())) {
                    fixQty(view, position);
                return true;
                } else {
                    return  true;
                }
            }
        });
        mlvStockCheckDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isInStockLotCheckDetail(mStockCheckDeatilDataArrayList.get(i).getCONSUMEABLDEFID()
                ,mStockCheckDeatilDataArrayList.get(i).getCONSUMEABLDEFVERSION())) {
                    EventBus.getDefault().post(new Event.StockLotCheckDetailbyConsumedefidEvent(mStockCheckDeatilDataArrayList.get(i)));
                } else {
                    showToast(mContext, mContext.getString(R.string.isNormalConsume), 0);
                }
            }
        });
    }

    private boolean isInStockLotCheckDetail(String CONSUMABLEDEFID,String CONSUMABLEDEFVERSION) {
        //返回true说明是lot管理的资材，false说明不是lot管理的资材，需要长按修改数字
        boolean returnflag = false;
        String selectDataListsql = String.format(mContext.getString(R.string.checkIsInStockLotCheck), CONSUMABLEDEFID,CONSUMABLEDEFVERSION);
        Log.e("isInStockLotCheckDetail", selectDataListsql);
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
        final StockCheckDeatilData tempdata = mStockCheckDeatilDataArrayList.get(position);
        Button btnConfirm = (Button) mViewFixQty.findViewById(R.id.btnfqty);
        final EditText etFixQty = (EditText) mViewFixQty.findViewById(R.id.etfqty);
        etFixQty.setText(tempdata.getCHECKQTY() + "");
        etFixQty.setSelection(etFixQty.length());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempdata.setCHECKQTY(etFixQty.getText().toString());
                if (Float.parseFloat(tempdata.getCHECKQTY()) > Float.parseFloat(tempdata.getQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(tempdata.getCHECKQTY()) == Float.parseFloat(tempdata.getQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymatch);
                } else {
                    tempdata.setBackgroundColor(R.color.qtyless);
                }
                mStockCheckDeatilDataArrayList.set(position, tempdata);
                mStockCheckDetailListViewAdapter.notifyDataSetChanged();
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

    //region getStockCheckDetailbyCheckMonth
    public void getStockCheckDetailbyCheckMonth(StockCheckData stockCheck) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetStockCheckDetailQuery), stockCheck.getCHECKMONTH(), stockCheck.getWAREHOUSEID());
        Log.e("sql语句StockCheckDetail", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mStockCheckDeatilDataArrayList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                StockCheckDeatilData stockCheckDetailData = new StockCheckDeatilData();
                stockCheckDetailData.setCONSUMEABLDEFNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFNAME)).trim());
                stockCheckDetailData.setWAREHOUSEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.WAREHOUSEID)));
                stockCheckDetailData.setUNIT(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.UNIT)));
                stockCheckDetailData.setQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.QTY)));
                stockCheckDetailData.setCONSUMEABLDEFID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFID)));
                stockCheckDetailData.setCONSUMEABLDEFVERSION(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFVERSION)));
                stockCheckDetailData.setCHECKMONTH(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKMONTH)));
                if (cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)).equals("null") ||
                        cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)).equals("")) {
                    stockCheckDetailData.setCHECKQTY("0");
                } else {
                    stockCheckDetailData.setCHECKQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)));
                }
                if (Float.parseFloat(stockCheckDetailData.getCHECKQTY()) > Float.parseFloat(stockCheckDetailData.getQTY())) {
                    stockCheckDetailData.setBackgroundColor(R.color.qtymore);
                } else if (Float.parseFloat(stockCheckDetailData.getCHECKQTY()) == Float.parseFloat(stockCheckDetailData.getQTY())) {
                    stockCheckDetailData.setBackgroundColor(R.color.qtymatch);
                } else {
                    stockCheckDetailData.setBackgroundColor(R.color.qtyless);
                }
                mStockCheckDeatilDataArrayList.add(stockCheckDetailData);
                Message message = new Message();
                message.what = UPDATEUI;
                message.obj = -1;
                handler.sendMessage(message);
            }
        }
        tvtagqty.setText(mStockCheckDeatilDataArrayList.size() + "");
        tvCheckMonth.setText(stockCheck.getCHECKMONTH());
        mStockCheckDetailListViewAdapter.notifyDataSetChanged();
    }
    //endregion

    public void updaCheckQtyByLot(StockLotCheckDeatilData mStockLotCheckDeatilData, String Sumqty) {
        for (int i = 0; i < mStockCheckDeatilDataArrayList.size(); i++) {
            StockCheckDeatilData data = mStockCheckDeatilDataArrayList.get(i);
            if (data.getCONSUMEABLDEFID().equals(mStockLotCheckDeatilData.getCONSUMABLEDEFID()) &&
                    data.getCONSUMEABLDEFVERSION().equals(mStockLotCheckDeatilData.getCONSUMABLEDEFVERSION())) {
                //找到对应的stockcheckdetail的数据，修改ui
                data.setCHECKQTY(Sumqty);
                mStockCheckDeatilDataArrayList.set(i, data);
                mStockCheckDetailListViewAdapter.notifyDataSetChanged();
                Message message = new Message();
                message.what = UPDATEUI;
                message.obj = i;
                handler.sendMessage(message);
                break;
            }
        }
    }

    //region UpdateSF_STOCKCHECKDETAIL
    private void UpdateSF_STOCKCHECKDETAIL(StockCheckDeatilData data) {
        //更新STOCKCHECKDETAIL的数字
        ContentValues values = new ContentValues();
        values.put(Constant.CHECKQTY, data.getCHECKQTY());
        values.put(Constant.USERID, AppConfig.getInstance().getString(Constant.UserID, null));
        db.update(Constant.SF_STOCKCHECKDETAIL, values, "WAREHOUSEID = ? AND CHECKMONTH = ?" +
                        " AND CONSUMABLEDEFID = ? AND CONSUMABLEDEFVERSION = ?"
                , new String[]{data.getWAREHOUSEID(), data.getCHECKMONTH()
                        , data.getCONSUMEABLDEFID(), data.getCONSUMEABLDEFVERSION()});
        //更新stockcheck的标志位
        ContentValues values1 = new ContentValues();
        values1.put(Constant.ISPDASAVE, "Y");
        db.update(Constant.SF_STOCKCHECK, values1, "WAREHOUSEID = ? AND CHECKMONTH = ?"
                , new String[]{data.getWAREHOUSEID(), data.getCHECKMONTH()});
    }
    //endregion

    //region actionClearAll
    public void actionClearAll() {
        mStockCheckDeatilDataArrayList.clear();
        mStockCheckDetailListViewAdapter.notifyDataSetChanged();
        IDlist.clear();
        tvtagqty.setText("0");
        tvCheckMonth.setText("");
        TagInfoData data = new TagInfoData();
        data.setEnableFlag(true);
        data.setClearFlag(true);
    }
    //endregion

    //endregion

}