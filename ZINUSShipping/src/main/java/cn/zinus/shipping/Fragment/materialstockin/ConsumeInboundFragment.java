package cn.zinus.shipping.Fragment.materialstockin;

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

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Adapter.ConsumeInboundListViewAdapter;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.JaveBean.ConsumeInboundData;
import cn.zinus.shipping.JaveBean.TagInfoData;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;

import static cn.zinus.shipping.util.Constant.UPDATEUI;

/**
 * Created by Spring on 2017/2/18.
 */

public class ConsumeInboundFragment extends KeyDownFragment {

    //region ◆ 변수(Variables)
    //上下文
    private MainNaviActivity mContext;
    //popwindow
    private PopupWindow mpopFixQty;
    private View mViewFixQty;
    //ListView
    private ListView mlvComsumeInbound;
    private ConsumeInboundListViewAdapter mConsumeInboundListViewAdapter;
    private ArrayList<ConsumeInboundData> mcomsumeInboundDataList;
    private ArrayList<String> IDlist;
    private TextView tvtagqty;
    private TextView tvInboundOrderNo;
    // private Handler handler;
    //Auto Read
    Handler handler = null;
    private boolean dismessDialogFlag = false;
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
        return inflater.inflate(R.layout.fragment_consumeinbound, container, false);
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
                        mlvComsumeInbound.setSelection((Integer) msg.obj);
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
        mcomsumeInboundDataList = new ArrayList<>();
        IDlist = new ArrayList<>();
    }
    //endregion

    //region initView
    private void initview() {
        tvtagqty = (TextView) getView().findViewById(R.id.tvtagqty);
        tvInboundOrderNo = (TextView) getView().findViewById(R.id.InboundOrderNo);
        //finqty popupwindow
        mViewFixQty = mContext.getLayoutInflater().inflate(R.layout.fixqty, null);
        //listViewDataList   **
        mlvComsumeInbound = (ListView) getView().findViewById(R.id.lv_consumeInbound);
        mConsumeInboundListViewAdapter = new ConsumeInboundListViewAdapter(mContext, mcomsumeInboundDataList);
        mlvComsumeInbound.setAdapter(mConsumeInboundListViewAdapter);
        mlvComsumeInbound.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                fixQty(view, position);
                return false;
            }
        });
    }

    //endregion

    //region fixQty
    private void fixQty(View view, final int position) {
        final ConsumeInboundData tempdata = mcomsumeInboundDataList.get(position);
        Button btnConfirm = (Button) mViewFixQty.findViewById(R.id.btnfqty);
        final EditText etFixQty = (EditText) mViewFixQty.findViewById(R.id.etfqty);
        etFixQty.setText(tempdata.getINQTY() + "");
        etFixQty.setSelection(etFixQty.length());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempdata.setINQTY(etFixQty.getText().toString());
                if (Integer.parseInt(tempdata.getINQTY()) > Integer.parseInt(tempdata.getPLANQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymore);
                } else if (Integer.parseInt(tempdata.getINQTY()) == Integer.parseInt(tempdata.getPLANQTY())) {
                    tempdata.setBackgroundColor(R.color.qtymatch);
                } else {
                    tempdata.setBackgroundColor(R.color.qtyless);
                }
                mcomsumeInboundDataList.set(position, tempdata);
                mConsumeInboundListViewAdapter.notifyDataSetChanged();
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

    //region getConsumeInboundByInboundOrder
    public void getConsumeInboundByInboundOrder(String inboundNo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetConsumeInboundQuery), inboundNo);
        Log.e("sql语句getConsumeInbound", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mcomsumeInboundDataList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                ConsumeInboundData consumeInboundData = new ConsumeInboundData();
                consumeInboundData.setCONSUMABLEDEFNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFNAME)));
                consumeInboundData.setUNIT(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.UNIT)));
                if (cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.INQTY)).equals("null")||
                        cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.INQTY)).equals("")) {
                    consumeInboundData.setINQTY("0");
                } else {
                    consumeInboundData.setINQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.INQTY)));
                }
                consumeInboundData.setPLANQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PLANQTY)));
                if (Integer.parseInt(consumeInboundData.getINQTY()) > Integer.parseInt(consumeInboundData.getPLANQTY())) {
                    consumeInboundData.setBackgroundColor(R.color.qtymore);
                } else if (Integer.parseInt(consumeInboundData.getINQTY()) == Integer.parseInt(consumeInboundData.getPLANQTY())) {
                    consumeInboundData.setBackgroundColor(R.color.qtymatch);
                } else {
                    consumeInboundData.setBackgroundColor(R.color.qtyless);
                }
                mcomsumeInboundDataList.add(consumeInboundData);
            }
        }
        tvtagqty.setText(mcomsumeInboundDataList.size() + "");
        tvInboundOrderNo.setText(inboundNo);
        mConsumeInboundListViewAdapter.notifyDataSetChanged();
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


    //region actionSearch
    protected void actionSearch() {

    }
    //endregion

    //region actionClearAll
    public void actionClearAll() {
        mcomsumeInboundDataList.clear();
        mConsumeInboundListViewAdapter.notifyDataSetChanged();
        IDlist.clear();
        tvtagqty.setText("0");
        tvInboundOrderNo.setText("");
        TagInfoData data = new TagInfoData();
        data.setEnableFlag(true);
        data.setClearFlag(true);
    }
    //endregion

    //endregion

}