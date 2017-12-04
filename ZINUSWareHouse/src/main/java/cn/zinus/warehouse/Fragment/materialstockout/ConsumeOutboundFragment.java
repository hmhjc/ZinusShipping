package cn.zinus.warehouse.Fragment.materialstockout;

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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Adapter.ConsumeOutboundListViewAdapter;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.JaveBean.ConsumeOutboundData;
import cn.zinus.warehouse.JaveBean.TagInfoData;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;

import static cn.zinus.warehouse.util.Constant.UPDATEUI;

/**
 * Created by Spring on 2017/2/18.
 */

public class ConsumeOutboundFragment extends KeyDownFragment {

    //region ◆ 변수(Variables)
    //上下文
    private MainNaviActivity mContext;
    //popwindow
    private PopupWindow mpopFixQty;
    private View mViewFixQty;
    //ListView
    private ListView mlvComsumeOutbound;
    private ConsumeOutboundListViewAdapter mConsumeOutboundListViewAdapter;
    private ArrayList<ConsumeOutboundData> mcomsumeOutboundDataList;
    private ArrayList<String> IDlist;
    private TextView tvtagqty;
    private TextView tvConsumeRequestNo;
    private Handler handler = null;
    //数据库
    MyDateBaseHelper mHelper;
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
    }
    //endregion

    //region onCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consumeoutbound, container, false);
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
                switch (msg.what){
                    case UPDATEUI:
                        mlvComsumeOutbound.setSelection((Integer) msg.obj);
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
            case R.id.action_search:
                actionSearch();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //endregion

    //region ◆ Function

    //region initData
    private void initData() {
        mcomsumeOutboundDataList = new ArrayList<>();
        IDlist = new ArrayList<>();
    }
    //endregion

    //region initView()
    private void initview() {
        tvtagqty = (TextView) getView().findViewById(R.id.tvtagqty);
        tvConsumeRequestNo = (TextView) getView().findViewById(R.id.ConsumeRequestNo);
        //finqty popupwindow
        mViewFixQty = mContext.getLayoutInflater().inflate(R.layout.fixqty, null);
        //listViewDataList   **
        mlvComsumeOutbound = (ListView) getView().findViewById(R.id.lv_consumeOutbound);
        mConsumeOutboundListViewAdapter = new ConsumeOutboundListViewAdapter(mContext, mcomsumeOutboundDataList);
        mlvComsumeOutbound.setAdapter(mConsumeOutboundListViewAdapter);
        mlvComsumeOutbound.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fixQty(view,position);
                return false;
            }
        });
    }
    //endregion

    //region fixQty
    private void fixQty(View view, final int position) {
        final ConsumeOutboundData tempdata = mcomsumeOutboundDataList.get(position);
        Button btnConfirm = (Button) mViewFixQty.findViewById(R.id.btnfqty);
        final EditText etFixQty = (EditText) mViewFixQty.findViewById(R.id.etfqty);
        etFixQty.setText(tempdata.getOUTQTY() + "");
        etFixQty.setSelection(etFixQty.length());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempdata.setOUTQTY(etFixQty.getText().toString());
                if (Integer.parseInt(tempdata.getOUTQTY()) > Integer.parseInt(tempdata.getREQUESTQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymore);
                } else if (Integer.parseInt(tempdata.getOUTQTY()) == Integer.parseInt(tempdata.getREQUESTQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymatch);
                } else {
                    tempdata.setBackgroundColor(R.color.qtyless);
                }
                mcomsumeOutboundDataList.set(position, tempdata);
                mConsumeOutboundListViewAdapter.notifyDataSetChanged();
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

    //region getConsumeOutboundByConsumeRequest
    public void getConsumeOutboundByConsumeRequest(String consumeRequestNo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetConsumeOutboundQuery), consumeRequestNo);
        Log.e("sql语句outbound", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mcomsumeOutboundDataList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                ConsumeOutboundData consumeOutboundData = new ConsumeOutboundData();
                consumeOutboundData.setCONSUMABLEDEFNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFNAME)));
                consumeOutboundData.setUNIT(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.UNIT)));
                consumeOutboundData.setREQUESTQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.REQUESTQTY)));
                if (cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.OUTQTY)).equals("null")||
                        cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.OUTQTY)).equals("")) {
                    consumeOutboundData.setOUTQTY("0");
                } else {
                    consumeOutboundData.setOUTQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.OUTQTY)));
                }
                if (Integer.parseInt(consumeOutboundData.getOUTQTY()) > Integer.parseInt(consumeOutboundData.getREQUESTQTY())) {
                    consumeOutboundData.setBackgroundColor(R.color.qtymore);
                } else if (Integer.parseInt(consumeOutboundData.getOUTQTY()) == Integer.parseInt(consumeOutboundData.getREQUESTQTY())) {
                    consumeOutboundData.setBackgroundColor(R.color.qtymatch);
                } else {
                    consumeOutboundData.setBackgroundColor(R.color.qtyless);
                }

                mcomsumeOutboundDataList.add(consumeOutboundData);
            }
        }
        tvtagqty.setText(mcomsumeOutboundDataList.size() + "");
        tvConsumeRequestNo.setText(consumeRequestNo);
        mConsumeOutboundListViewAdapter.notifyDataSetChanged();
    }
    //endregion

    //region actionSearch
    protected void actionSearch() {
        //getConsumeOutboundByConsumeRequest("1111111");
    }
    //endregion

    //region actionClearAll
    public void actionClearAll() {
        mcomsumeOutboundDataList.clear();
        mConsumeOutboundListViewAdapter.notifyDataSetChanged();
        IDlist.clear();
        tvtagqty.setText("0");
        tvConsumeRequestNo.setText("");
        TagInfoData data = new TagInfoData();
        data.setEnableFlag(true);
        data.setClearFlag(true);
    }
    //endregion

    //endregion

}