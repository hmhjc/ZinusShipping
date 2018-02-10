package cn.zinus.shipping.Fragment.standardmgnt;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.ArrayList;

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Adapter.TestListViewAdapter;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.SoundUtil;

import static cn.zinus.shipping.util.Constant.UPDATEUI;
import static cn.zinus.shipping.util.Utils.showToast;

/**
 * Created by Spring on 2017/2/17.
 */

public class TestFragment extends KeyDownFragment implements View.OnClickListener {
    private MainNaviActivity mContext;
    private EditText etTagID;
    private int BRFlag = 1;
    private boolean threadStop = true;
    private Thread thread;
    private ListView lvtest;
    private ArrayList<String> mArrayList;
    TestListViewAdapter mAdapter;
    //Auto Read
    Handler handler = null;
    boolean scanFlag = false;
    public Barcode2DWithSoft BaecodeReader;
    private ProgressDialog myDialog;
    private boolean dismessDialogFlag = false;
    public static final int RFIDSCAN = 111;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainNaviActivity) getActivity();
        initData();
        initView();
    }

    private void initData() {
        mArrayList = new ArrayList<>();
    }

    private void initView() {
        etTagID = (EditText) getView().findViewById(R.id.ettagread);
        lvtest = (ListView) getView().findViewById(R.id.lv_test);
        mAdapter = new TestListViewAdapter(mContext,mArrayList);
        lvtest.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    private void TagScan() {
        if (BRFlag == 1) {
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
                    showToast(mContext, "扫描模块异常，请再按一次", 1);
                    mContext.freeUHF();
                    mContext.initUHF();
                }
        } else if (BRFlag == 2) {
            readBarcodeTag();
        }
    }

    //region ◆ Barcode相关

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

    @Override
    public void myOnKeyDown() {
        TagScan();
    }

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
            if (!TextUtils.isEmpty(barcode)) {
               // checkAndRemoveShipping(barcode);
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
                    msg.what = RFIDSCAN;
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
                Message message = new Message();
                message.what = UPDATEUI;
                message.obj = 0;
                handler.sendMessage(message);
            }
            if (mContext.mRFIDWithUHF.stopInventory()) {

            } else {
                Log.e("扫描出问题了", "扫描出问题了668");
            }
        }
    }
    //endregion

    //endregion
}