package cn.zinus.warehouse.Fragment.materialstockout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Adapter.ConsumeLotOutboundListViewAdapter;
import cn.zinus.warehouse.Fragment.KeyDownFragment;
import cn.zinus.warehouse.JaveBean.ConsumableLotData;
import cn.zinus.warehouse.JaveBean.ConsumeLotOutboundData;
import cn.zinus.warehouse.JaveBean.ConsumeOutboundData;
import cn.zinus.warehouse.JaveBean.TagInfoData;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;
import cn.zinus.warehouse.util.SoundUtil;

import static cn.zinus.warehouse.util.Constant.RFIDSCAN;
import static cn.zinus.warehouse.util.Constant.UPDATEUI;
import static cn.zinus.warehouse.util.DBManger.getCursorData;
import static cn.zinus.warehouse.util.Utils.showToast;
import static com.micube.control.util.Server.hexStringToString;

/**
 * Created by Spring on 2017/2/18.
 */

public class ConsumeLotOutboundFragment extends KeyDownFragment implements View.OnClickListener {

    //region ◆ 변수(Variables)
    //上下文
    private MainNaviActivity mContext;
    //popwindow
    private ImageView mivchoose;
    private PopupWindow mpopChooseBorR;
    private View mViewChooseBorR;
    private PopupWindow mpopFixQty;
    private View mViewFixQty;
    //公共信息
    private TextView tvSpec_Desc;
    private TextView tvConsumabledefId;
    private TextView tvConsumabledefname;
    //出库请求总数量
    private TextView tvConsumeLotRequestSum;
    //列表里面的总数
    private TextView tvlistSumQty;
    //未出库数量
    private TextView tvnoconsumeOutboundLotQTY;
    //出库标签个数
    private TextView tvtagqty;
    private TextView tvConsumeRequestNo;
    //ListView
    private ListView mlvComsumeLotOutbound;
    private ConsumeLotOutboundListViewAdapter mConsumeLotOutboundListViewAdapter;
    private ArrayList<ConsumeLotOutboundData> mcomsumeLotOutboundDataList;
    //这个item的所有lot
    private ArrayList<ConsumableLotData> _mcomsumeLotOutboundDataList;
    private ArrayList<String> IDlist;
    //
    private EditText etTagID;
    private int BRFlag = 1;
    // private Handler handler;
    private boolean threadStop = true;
    private Thread thread;
    //Auto Read
    Handler handler = null;
    boolean scanFlag = false;
    public Barcode2DWithSoft BaecodeReader;
    private ProgressDialog myDialog;
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
        return inflater.inflate(R.layout.fragment_consumelotoutbound, container, false);
    }
    //endregion

    //region onActivityCreated
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initview();
        BaecodeReader = mContext.mBarcode2DWithSoft;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case RFIDSCAN:
                        String tagid = msg.obj + "";
                        String tagID = hexStringToString(tagid);
                        if (!tagID.equals("")) {
                            checkAndSearchWeb(tagID);
                        }
                        break;
                    case UPDATEUI:
                        mlvComsumeLotOutbound.setSelection((Integer) msg.obj);
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
        if (BaecodeReader != null) {
            BaecodeReader.stopScan();
        }
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
        if (BaecodeReader != null) {
            BaecodeReader.stopScan();
        }
    }
    //endregion

    //region onClick Event
    //region 按钮单击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_choose:
                //选择是RFID还是Barcode的imageView
                Button btnRFID = (Button) mViewChooseBorR.findViewById(R.id.btnrfid);
                Button btnBarCode = (Button) mViewChooseBorR.findViewById(R.id.btnbarcode);
                btnRFID.setOnClickListener(this);
                btnBarCode.setOnClickListener(this);
                mpopChooseBorR = new PopupWindow(mViewChooseBorR, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                mpopChooseBorR.setBackgroundDrawable(new ColorDrawable(0x00000000));
                mpopChooseBorR.setTouchable(true);
                mpopChooseBorR.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                mpopChooseBorR.showAsDropDown(v);
                break;
            case R.id.btnrfid:
                //选择R&B的popupwindow里面的RFID按钮
                BRFlag = 1;
                showToast(mContext, "RFID", 0);
                mivchoose.setImageResource(R.drawable.rfidicon);
                if (BaecodeReader != null) {
                    BaecodeReader.stopScan();
                }
                mpopChooseBorR.dismiss();
                break;
            case R.id.btnbarcode:
                //选择R&B的popupwindow里面的Barcode按钮
                showToast(mContext, "Barcode", 0);
                BRFlag = 2;
                mivchoose.setImageResource(R.drawable.barcodeicon);
                mpopChooseBorR.dismiss();
                break;
        }
    }
    //endregion

    //endregion

    //region onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //actionSearch();
                //getConsumeLotOutboundByConsumeRequest("INB-2017090600001");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //endregion

    //region ◆ Function

    //region initData
    private void initData() {
        mcomsumeLotOutboundDataList = new ArrayList<>();
        _mcomsumeLotOutboundDataList = new ArrayList<>();
        IDlist = new ArrayList<>();
    }
    //endregion

    //region initView()
    private void initview() {
        tvnoconsumeOutboundLotQTY = (TextView) getView().findViewById(R.id.tv_noconsumeOutboundLotQTY);
        tvlistSumQty = (TextView) getView().findViewById(R.id.tv_listSumQty);
        tvConsumeLotRequestSum = (TextView) getView().findViewById(R.id.tv_ConsumeLotRequestSum);
        tvSpec_Desc = (TextView) getView().findViewById(R.id.tv_spec_desc);
        tvConsumabledefId = (TextView) getView().findViewById(R.id.tv_ConsumabledefId);
        tvConsumabledefname = (TextView) getView().findViewById(R.id.tv_consumabledefname);
        etTagID = (EditText) getView().findViewById(R.id.etTagID);
        tvtagqty = (TextView) getView().findViewById(R.id.tvtagqty);
        tvConsumeRequestNo = (TextView) getView().findViewById(R.id.ConsumeRequestNo);
        mivchoose = (ImageView) getView().findViewById(R.id.iv_choose);
        mivchoose.setOnClickListener(this);
        //Choose RFID or Barcode popupwindow
        mViewChooseBorR = mContext.getLayoutInflater().inflate(R.layout.chooseborr, null);
        mViewFixQty = mContext.getLayoutInflater().inflate(R.layout.fixqty, null);
        //listViewDataList   **
        mlvComsumeLotOutbound = (ListView) getView().findViewById(R.id.lv_consumeLotOutbound);
        mConsumeLotOutboundListViewAdapter = new ConsumeLotOutboundListViewAdapter(mContext, mcomsumeLotOutboundDataList);
        mlvComsumeLotOutbound.setAdapter(mConsumeLotOutboundListViewAdapter);
        mlvComsumeLotOutbound.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fixQty(view, position);
                return true;
            }
        });
    }
    //endregion

    @SuppressLint("WrongConstant")
    private void fixQty(View view, final int position) {
        final ConsumeLotOutboundData tempdata = mcomsumeLotOutboundDataList.get(position);
        Button btnConfirm = (Button) mViewFixQty.findViewById(R.id.btnfqty);
        final EditText etFixQty = (EditText) mViewFixQty.findViewById(R.id.etfqty);
        etFixQty.setText(tempdata.getOUTQTY() + "");
        etFixQty.setSelection(etFixQty.length());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempdata.setOUTQTY(etFixQty.getText().toString());
//                if (Integer.parseInt(tempdata.getOUTQTY()) > Integer.parseInt(tempdata.get())) {
//                    tempdata.setBackgroundColor(R.color.qtymore);
//                } else if (Integer.parseInt(tempdata.getOUTQTY()) == Integer.parseInt(tempdata.getREQUESTQTY())) {
//                    tempdata.setBackgroundColor(R.color.qtymatch);
//                } else {
//                    tempdata.setBackgroundColor(R.color.qtyless);
//                }
                mcomsumeLotOutboundDataList.set(position, tempdata);
                mConsumeLotOutboundListViewAdapter.notifyDataSetChanged();
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

    //region getConsumeLotOutboundByConsumeDefID
    public void getConsumeLotOutboundByConsumeDefID(ConsumeOutboundData outboundData) {
        //先根据item，设置公共信息
        tvSpec_Desc.setText(outboundData.getSPEC_DESC());
        tvConsumabledefId.setText(outboundData.getCONSUMABLEDEFID());
        tvConsumabledefname.setText(outboundData.getCONSUMABLEDEFNAME());
        tvConsumeLotRequestSum.setText(outboundData.getREQUESTQTY());
        tvConsumeRequestNo.setText(outboundData.getCONSUMEREQNO());
        tvlistSumQty.setText(outboundData.getOUTQTY());
        tvnoconsumeOutboundLotQTY.setText((Float.parseFloat(outboundData.getREQUESTQTY()) - Float.parseFloat(outboundData.getOUTQTY())) + "");
        //根据出库计划和计划中的item,来搜索已经出库的lot
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(getString(R.string.GetConsumeLotOutboundQuery),
                outboundData.getCONSUMEREQNO(),
                outboundData.getCONSUMABLEDEFID(), outboundData.getCONSUMABLEDEFVERSION());
        Log.e("sql语句lotoutbound", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        mcomsumeLotOutboundDataList.clear();
        if (cursorDatalist.getCount() != 0) {
            while (cursorDatalist.moveToNext()) {
                ConsumeLotOutboundData consumeLotOutboundData = new ConsumeLotOutboundData();
                consumeLotOutboundData.setCONSUMEREQNO(getCursorData(cursorDatalist, Constant.CONSUMEREQNO).trim());
                consumeLotOutboundData.setCONSUMABLELOTID(getCursorData(cursorDatalist, Constant.CONSUMABLELOTID).trim());
                consumeLotOutboundData.setCONSUMABLEDEFID(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFID).trim());
                consumeLotOutboundData.setCONSUMABLEDEFNAME(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFNAME).trim());
                consumeLotOutboundData.setCONSUMABLEDEFVERSION(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFVERSION).trim());
                consumeLotOutboundData.setSPEC_DESC(getCursorData(cursorDatalist, Constant.SPEC_DESC).trim());
                consumeLotOutboundData.setDEFAULTUNIT(getCursorData(cursorDatalist, Constant.DEFAULTUNIT).trim());
                consumeLotOutboundData.setOUTBOUNDSTATE(getCursorData(cursorDatalist, Constant.OUTBOUNDSTATE).trim());
                consumeLotOutboundData.setWAREHOUSEID(getCursorData(cursorDatalist, Constant.WAREHOUSEID).trim());
                consumeLotOutboundData.setFROMWAREHOUSEID(getCursorData(cursorDatalist, Constant.FROMWAREHOUSEID).trim());
                consumeLotOutboundData.setTAGID(getCursorData(cursorDatalist, Constant.TAGID).trim());
                consumeLotOutboundData.setTAGQTY(getCursorData(cursorDatalist, Constant.TAGQTY).trim());
                if (getCursorData(cursorDatalist, Constant.OUTQTY).trim().equals("null") ||
                        getCursorData(cursorDatalist, Constant.OUTQTY).trim().equals("")) {
                    consumeLotOutboundData.setOUTQTY("0");
                } else {
                    consumeLotOutboundData.setOUTQTY(getCursorData(cursorDatalist, Constant.OUTQTY).trim());
                }
                mcomsumeLotOutboundDataList.add(consumeLotOutboundData);
            }
        }
        tvtagqty.setText(mcomsumeLotOutboundDataList.size() + "");
        mConsumeLotOutboundListViewAdapter.notifyDataSetChanged();

        //搜索属于这个item的lot
        String selectlotListsql = String.format(getString(R.string.GetConsumeLotOutboundQuery),
                outboundData.getCONSUMEREQNO(),
                outboundData.getCONSUMABLEDEFID(), outboundData.getCONSUMABLEDEFVERSION());
        Log.e("sql语句consumablelot", selectlotListsql);
        Cursor cursorlotlist = DBManger.selectDatBySql(db, selectlotListsql, null);
        _mcomsumeLotOutboundDataList.clear();
        if (cursorlotlist.getCount() != 0) {
            while (cursorlotlist.moveToNext()) {
                ConsumableLotData consumableLotData = new ConsumableLotData();
                consumableLotData.setCONSUMABLELOTID(getCursorData(cursorDatalist, Constant.CONSUMABLELOTID).trim());
                consumableLotData.setCONSUMABLEDEFID(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFID).trim());
                consumableLotData.setCONSUMABLEDEFVERSION(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFVERSION).trim());
                consumableLotData.setWAREHOUSEID(getCursorData(cursorDatalist, Constant.WAREHOUSEID).trim());
                consumableLotData.setRFID(getCursorData(cursorDatalist, Constant.RFID).trim());
                if (getCursorData(cursorDatalist, Constant.QTY).trim().equals("null") ||
                        getCursorData(cursorDatalist, Constant.QTY).trim().equals("")) {
                    consumableLotData.setQTY("0");
                } else {
                    consumableLotData.setQTY(getCursorData(cursorDatalist, Constant.QTY).trim());
                }
                _mcomsumeLotOutboundDataList.add(consumableLotData);
            }
        }
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

    //region TagScan
    private void TagScan() {
        if (BRFlag == 1) {
            if (tvConsumabledefId.getText().toString().equals("")) {
                // SoundUtil.play(R.raw.waring, 0);
                showToast(mContext, "请先在上一个tab中选择品种信息!", 0);
                return;
            }
            if (Float.parseFloat(tvlistSumQty.getText().toString()) == Float.parseFloat(tvConsumeLotRequestSum.getText().toString())) {
                // SoundUtil.play(R.raw.waring, 0);
                showToast(mContext, "这个item的lot已经添加完毕!", 0);
                return;
            }
            if (Float.parseFloat(tvlistSumQty.getText().toString()) > Float.parseFloat(tvConsumeLotRequestSum.getText().toString())) {
                // SoundUtil.play(R.raw.waring, 0);
                showToast(mContext, "出库数量超过计划数量，请移除多余的部分", 0);
            } else if (!scanFlag) {
                scanFlag = true;
                if (mContext.mRFIDWithUHF.startInventoryTag((byte) 0, (byte) 0)) {
                    myDialog = new ProgressDialog(mContext);
                    myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    myDialog.setMessage(getString(R.string.Scaning));
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.setCancelable(false);
                    myDialog.show();
                    myDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == 139) {
                                if (dismessDialogFlag) {
                                    dismessDialogFlag = false;
                                    stopInventory();
                                } else {
                                    dismessDialogFlag = true;
                                }
                            }
                            return false;
                        }
                    });
                    new TagThread(10).start();
                } else {
                    SoundUtil.play(R.raw.waring, 0);
                    showToast(mContext, "扫描模块异常，请再按一次", 0);
                    mContext.freeUHF();
                    mContext.initUHF();
                }
            } else {
                stopInventory();
            }
        } else if (BRFlag == 2) {
            readBarcodeTag();
        }
    }
    //endregion

    //region ReadTag
    private void reaRFIDTag() {
        String strUII = mContext.mRFIDWithUHF.inventorySingleTag();
        //Log.e("tagtagtag", "读到" + strUII);
        if (!TextUtils.isEmpty(strUII)) {
            String tagID = hexStringToString(strUII);
            if (!tagID.equals("")) {
                checkAndSearchWeb(tagID);
            }
        }
    }

    private void readBarcodeTag() {

        if (BaecodeReader != null) {
            BaecodeReader.setScanCallback(mScanCallback);
        }

        if (threadStop) {
            boolean bContinuous = false;
            thread = new DecodeThread(bContinuous, 100);
            thread.start();

        } else {
            threadStop = true;
        }
    }
    //endregion

    //region checkAndSearchWeb
    private void checkAndSearchWeb(String tagID) {
        if (Integer.parseInt(tvlistSumQty.getText().toString()) == Integer.parseInt(tvConsumeLotRequestSum.getText().toString())) {
            Log.e("全部完成", "全部完成");
            if (dismessDialogFlag) {
                dismessDialogFlag = false;
                stopInventory();
                //SoundUtil.play(R.raw.success, 0);
                showToast(mContext, "这个item的lot已经添加完毕!", 0);
            } else {
                dismessDialogFlag = true;
            }
            return;
        }
        if (Integer.parseInt(tvlistSumQty.getText().toString()) > Integer.parseInt(tvConsumeLotRequestSum.getText().toString())) {
            Log.e("全部完成", "全部完成");
            if (dismessDialogFlag) {
                dismessDialogFlag = false;
                stopInventory();
                // SoundUtil.play(R.raw.waring, 0);
                showToast(mContext, "出库数量超过计划数量，请移除多余的部分", 0);
            } else {
                dismessDialogFlag = true;
            }
        }
        int lotflag = checkIsExist(tagID);
        if (lotflag == -2) {
            showToast(mContext, tagID + "不是这个Item的lot，请确认", 0);
            //SoundUtil.play(R.raw.waring, 0);
            stopInventory();
        } else if (lotflag == -1) {
            //Log.e("indexhand", tagID + "不在这个list里面");
            //showToast(mContext, tagID + "不是这个列表里面的标签", 0);
        } else {
           // searchToUpdateUI(lotflag);
        }
    }
    //endregion

    //region updateDataByScan
//    private void updateDataByScan(ImportOrderScanData data) {
//        String TagID = data.getTagID();
//        for (int i = 0; i < mcomsumeLotOutboundDataList.size(); i++) {
//            if (mcomsumeLotOutboundDataList.get(i).getTagID().equals(TagID)) {
//                mcomsumeLotOutboundDataList.get(i).setOKFlag(true);
//                int realqty = Integer.parseInt(mcomsumeLotOutboundDataList.get(i).getRealQty());
//                mcomsumeLotOutboundDataList.get(i).setRealQty(Integer.parseInt(data.getQty()) + "");
//                ImportOrderItemData temp = mcomsumeLotOutboundDataList.get(i);
//                int qty = Integer.parseInt(temp.getQty()) - Integer.parseInt(temp.getRealQty());
//                if (qty > 0) {
//                    mcomsumeLotOutboundDataList.get(i).setColor(Color.RED);
//                } else if (qty == 0) {
//                    mcomsumeLotOutboundDataList.get(i).setColor(Color.GREEN);
//                } else {
//                    mcomsumeLotOutboundDataList.get(i).setColor(Color.YELLOW);
//                }
//                adapter.notifyDataSetChanged();
//                tvtagqty.setText(":" + mcomsumeLotOutboundDataList.size());
//            }
//        }
//    }
    //endregion

    //region actionSearch
    protected void actionSearch() {

    }
    //endregion

    //region actionClearAll
    public void actionClearAll() {
        if (mcomsumeLotOutboundDataList.size() > 0) {
            mcomsumeLotOutboundDataList.clear();
        }
        mConsumeLotOutboundListViewAdapter.notifyDataSetChanged();
        IDlist.clear();
        etTagID.setText("");
        tvtagqty.setText("0");
        tvConsumeRequestNo.setText("");
        TagInfoData data = new TagInfoData();
        data.setEnableFlag(true);
        data.setClearFlag(true);
    }
    //endregion

    //endregion

    //region ◆ Listener&extends

    //region PAD KEY Event(extends KeyDownFragmemt)
    @Override
    public void myOnKeyDown() {
        TagScan();
    }

    //endregion

    //endregion

    //region ◆ Barcode相关

    //region Barcode Thread Class
    private class DecodeThread extends Thread {
        private boolean isContinuous = false;
        private long sleepTime = 1000;

        public DecodeThread(boolean isContinuous, int sleep) {
            this.isContinuous = isContinuous;
            this.sleepTime = sleep;
        }

        @Override
        public void run() {
            super.run();
            do {
                BaecodeReader.scan();
                if (isContinuous) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (isContinuous && !threadStop);
        }
    }
    //endregion

    //region BarCode CallBack(扫描到Barcode时的回调事件)
    public Barcode2DWithSoft.ScanCallback mScanCallback = new Barcode2DWithSoft.ScanCallback() {
        @Override
        public void onScanComplete(int i, int length, byte[] data) {
            if (length < 1) {
                return;
            }
            BaecodeReader.stopScan();
            String barcode = new String(data).trim();
            //Log.e("barcode", barcode);
            if (!TextUtils.isEmpty(barcode)) {
                checkAndSearchWeb(barcode);
            }
        }
    };
    //endregion

    //endregion

    //region ◆ RFID循环识别相关

    //region RFID Thread Class
    class TagThread extends Thread {

        private int mBetween = 80;

        public TagThread(int iBetween) {
            mBetween = iBetween;
        }

        public void run() {

            String[] res = null;

            while (scanFlag) {

                res = mContext.mRFIDWithUHF.readTagFromBuffer();

                if (res != null) {
                    Message msg = handler.obtainMessage();
                    msg.obj = res[1];
                    handler.sendMessage(msg);
                }
                try {
                    sleep(mBetween);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    //endregion

    //region stopInventory :停止识别
    private void stopInventory() {

        if (scanFlag) {

            scanFlag = false;
            if (myDialog != null) {
                myDialog.dismiss();
            }
            if (mContext.mRFIDWithUHF.stopInventory()) {
                //BtnReadTag.setText(mContext.getString(R.string.title_start_Inventory));
            } else {

            }
        }
    }
    //endregion

    //endregion

}