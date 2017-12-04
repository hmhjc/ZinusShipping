package cn.zinus.warehouse.Fragment.inventorymgnt;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Adapter.StockCheckDetailListViewAdapter;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.JaveBean.StockCheckData;
import cn.zinus.warehouse.JaveBean.StockCheckDeatilData;
import cn.zinus.warehouse.JaveBean.TagInfoData;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;

import static cn.zinus.warehouse.util.Constant.UPDATEUI;

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
                        mlvStockCheckDetail.setSelection((Integer) msg.obj);
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
                actionSave();
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
        mlvStockCheckDetail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fixQty(view,position);
                return false;
            }
        });
    }

    //endregion

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
                if (Integer.parseInt(tempdata.getCHECKQTY()) > Integer.parseInt(tempdata.getQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymore);
                } else if (Integer.parseInt(tempdata.getCHECKQTY()) == Integer.parseInt(tempdata.getQTY())) {
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
//                mlvStockCheckDetail.setSelection(position);
//                tvtagqty.setText( ""+ mStockCheckDeatilDataArrayList.size());
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

    //region getStockCheckDetailbyCheckMonth
    public void getStockCheckDetailbyCheckMonth(StockCheckData stockCheck) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetStockCheckDetailQuery),stockCheck.getCHECKMONTH(), stockCheck.getWAREHOUSEID());
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
                if (cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)).equals("null") ||
                        cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)).equals("")) {
                    stockCheckDetailData.setCHECKQTY("0");
                } else {
                    stockCheckDetailData.setCHECKQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)));
                }
                if (Integer.parseInt(stockCheckDetailData.getCHECKQTY()) > Integer.parseInt(stockCheckDetailData.getQTY())) {
                    stockCheckDetailData.setBackgroundColor(R.color.qtymore);
                } else if (Integer.parseInt(stockCheckDetailData.getCHECKQTY()) == Integer.parseInt(stockCheckDetailData.getQTY())) {
                    stockCheckDetailData.setBackgroundColor(R.color.qtymatch);
                } else {
                    stockCheckDetailData.setBackgroundColor(R.color.qtyless);
                }
                mStockCheckDeatilDataArrayList.add(stockCheckDetailData);
            }
        }
        tvtagqty.setText(mStockCheckDeatilDataArrayList.size() + "");
        tvCheckMonth.setText(stockCheck.getCHECKMONTH());
        mStockCheckDetailListViewAdapter.notifyDataSetChanged();
    }
    //endregion

    //region checkIsExist
    private int checkIsExist(String tagid) {
        int returnint = -1;
        for (int i = 0; i < IDlist.size(); i++)
            if (IDlist.get(i).equals(tagid)) {
                returnint = i;
            }
        return returnint;
    }
    //endregion

    //region checkAndSearchWeb
    private void checkAndSearchWeb(String tagID) {
        if (checkIsExist(tagID) == -1) {
            Log.e("indexhand", "列表里不存在" + tagID + "查询数据库");
            IDlist.add(tagID);
        } else {
            Log.e("indexhand", "列表里存在" + tagID + "继续读");
        }
    }
    //endregion

    private void actionSave() {
        Log.e("保存", "保存StockCheckDetail");
        //修改
        for (int i = 0;i<mStockCheckDeatilDataArrayList.size();i++){
            StockCheckDeatilData data = mStockCheckDeatilDataArrayList.get(i);
            ContentValues values = new ContentValues();
            values.put(Constant.CHECKQTY, data.getCHECKQTY());
            db.update(Constant.SF_STOCKCHECKDETAIL, values, "CONSUMEABLDEFNAME = ?", new String[]{data.getCONSUMEABLDEFNAME()});
        }

//        ContentValues values = new ContentValues();
//        values.put(Constant.ISPDASHIPPING, "Y");
//        if (tvlotshippingqty.getText().toString().equals(tvtagqty.getText().toString())) {
//            values.put(Constant.STATE, "Finished");
//        } else {
//            values.put(Constant.STATE, "Run");
//        }
//
//        db.update(Constant.SF_SHIPPINGPLAN, values, "SHIPPINGPLANNO = ?", new String[]{tvshippingPlanNo.getText().toString()});
    }

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