package cn.zinus.shipping.Fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.rscja.deviceapi.RFIDWithUHF;
import com.zebra.adc.decoder.Barcode2DWithSoft;

/**
 * 所有Fragment的父类
 * 1:myOnKeyDown 扫码枪手柄按钮事件
 * 2:myBackKeyDwon 返回按钮事件
 * Created by Spring on 2017/2/20.
 */

public class KeyDownFragment extends Fragment {
    public RFIDWithUHF mRFIDWithUHF;
    public Barcode2DWithSoft mBarcode2DWithSoft;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public Message getHandlerMessage(int queryrequest, String s) {
        Message msg = new Message();
        msg.what = queryrequest;
        msg.obj = s;
        return msg;
    }

    public void myOnKeyDown() {
    }

    public void myBackKeyDwon() {
    }

    public void myOnKeyuUp() {
    }


//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//    }
//
//
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//
//    }

    public void freeUHF(RFIDWithUHF mRFIDWithUHF) {
        if (mRFIDWithUHF != null) {
            Log.e("freefreefree", "mRFIDWithUHF");
            mRFIDWithUHF.free();
        }
    }

    public void freeBarcode(Barcode2DWithSoft mBarcode2DWithSoft) {
        if (mBarcode2DWithSoft != null) {
            mBarcode2DWithSoft.close();
        }
    }

}
