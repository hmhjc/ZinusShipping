package cn.zinus.shipping.Fragment.standardmgnt;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rscja.deviceapi.RFIDWithUHF;

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.Fragment.KeyDownFragment;
import cn.zinus.shipping.JaveBean.LotData;
import cn.zinus.shipping.JaveBean.LotShippingData;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;

import static cn.zinus.shipping.util.Utils.showToast;
import static com.micube.control.util.Server.hexStringToString;
import static com.micube.control.util.Server.stringToHexString;

/**
 * Created by Spring on 2017/2/17.
 */

public class RegisterTagFragment extends KeyDownFragment implements View.OnClickListener {
    private MainNaviActivity mContext;
    private Button btnread;
    private Button btnwrite;
    private Button btntest;
    EditText etread;
    EditText etwrite;
    EditText etdizhi;
    EditText etchangdu;
    TextView mTextView;
    String strUII = "";

    MyDateBaseHelper mHelper;
    private SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registertag, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainNaviActivity) getActivity();
        initView();
        mHelper = DBManger.getIntance(mContext);
        db = mHelper.getWritableDatabase();
    }

    private void initView() {
        btnread = (Button) getView().findViewById(R.id.btnread);
        btnwrite = (Button) getView().findViewById(R.id.btnwrite);
        btntest = (Button) getView().findViewById(R.id.btntest);
        btnread.setOnClickListener(this);
        btnwrite.setOnClickListener(this);
        btntest.setOnClickListener(this);
        etread = (EditText) getView().findViewById(R.id.ettagread);
        etwrite = (EditText) getView().findViewById(R.id.ettagwrite);
        etdizhi = (EditText) getView().findViewById(R.id.etddizhi);
        etchangdu = (EditText) getView().findViewById(R.id.etchangdu);
        mTextView = (TextView) getView().findViewById(R.id.tv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btntest:
//                mContext.mRFIDWithUHF.setPower(10);
//                String strUII1 = mContext.mRFIDWithUHF.inventorySingleTag();
//                if (!TextUtils.isEmpty(strUII1)) {
//                    String EPC = hexStringToString(strUII1);
//                    Log.e("epcepcepc",EPC);
//                    mTextView.setText(EPC);
//                }
                String selectlOTsql = "select * from SF_LOT";
                Log.e("sql语句查po对应的lot", selectlOTsql);
                Cursor cursorLotDatalist = DBManger.selectDatBySql(db, selectlOTsql, null);
                if (cursorLotDatalist.getCount() != 0) {
                    while (cursorLotDatalist.moveToNext()) {
                        LotData mLotData = new LotData();
                        mLotData.setLOTID(cursorLotDatalist.getString(cursorLotDatalist.getColumnIndex(Constant.LOTID)));
                        mLotData.setPURCHASEORDERID(cursorLotDatalist.getString(cursorLotDatalist.getColumnIndex(Constant.PURCHASEORDERID)));
                        mLotData.setLOTSTATE(cursorLotDatalist.getString(cursorLotDatalist.getColumnIndex(Constant.LOTSTATE)));
                        mLotData.setRFID(cursorLotDatalist.getString(cursorLotDatalist.getColumnIndex(Constant.RFID)));
                        mLotData.setQTY(cursorLotDatalist.getString(cursorLotDatalist.getColumnIndex(Constant.QTY)));
                        mLotData.setVALIDSTATE(cursorLotDatalist.getString(cursorLotDatalist.getColumnIndex(Constant.VALIDSTATE)));
                        mLotData.setISPDASHIPPING(cursorLotDatalist.getString(cursorLotDatalist.getColumnIndex(Constant.ISPDASHIPPING)));
                        Log.e("lot数据", mLotData.toString());
                    }
                }

                String selectSHIPPINGPLANsql = "select * from SF_SHIPPINGPLAN";
                Cursor cursorSHIPPINGPLANDatalist = DBManger.selectDatBySql(db, selectSHIPPINGPLANsql, null);
                if (cursorSHIPPINGPLANDatalist.getCount() != 0) {
                    while (cursorSHIPPINGPLANDatalist.moveToNext()) {
                        String b  = "";
                        for (int i = 0; i < cursorSHIPPINGPLANDatalist.getColumnCount(); i++) {
                          String a = cursorSHIPPINGPLANDatalist.getColumnName(i);
                          b = b+a+"---"+ cursorSHIPPINGPLANDatalist.getString(cursorSHIPPINGPLANDatalist.getColumnIndex(a));
                        }
                        Log.e("shippingplan数据",b);
                    }
                }

                String selectlotshippingsql = "select * from SF_SHIPPINGLOT";
                Log.e("sql语句查po对应的lot", selectlOTsql);
                Cursor cursorlotshipping = DBManger.selectDatBySql(db, selectlotshippingsql, null);
                if (cursorlotshipping.getCount() != 0) {
                    while (cursorlotshipping.moveToNext()) {
                        LotShippingData mLotsHIPPINGData = new LotShippingData();
                        mLotsHIPPINGData.setLOTID(cursorlotshipping.getString(cursorlotshipping.getColumnIndex(Constant.LOTID)));
                        mLotsHIPPINGData.setSHIPPINGPLANNO(cursorlotshipping.getString(cursorlotshipping.getColumnIndex(Constant.SHIPPINGPLANNO)));
                        mLotsHIPPINGData.setSHIPPINGPLANSEQ(cursorlotshipping.getString(cursorlotshipping.getColumnIndex(Constant.SHIPPINGPLANSEQ)));
                        mLotsHIPPINGData.setCONTAINERSEQ(cursorlotshipping.getString(cursorlotshipping.getColumnIndex(Constant.CONTAINERSEQ)));
                        mLotsHIPPINGData.setQTY(cursorlotshipping.getString(cursorlotshipping.getColumnIndex(Constant.QTY)));
                        mLotsHIPPINGData.setSHIPPINGDATE(cursorlotshipping.getString(cursorlotshipping.getColumnIndex(Constant.SHIPPINGDATE)));
                        mLotsHIPPINGData.setVALIDSTATE(cursorlotshipping.getString(cursorlotshipping.getColumnIndex(Constant.VALIDSTATE)));

                        Log.e("lotshipping数据", mLotsHIPPINGData.toString());
                    }
                }
                break;
            case R.id.btnwrite:
                mContext.mRFIDWithUHF.setPower(10);
                String AA = etwrite.getText().toString();
                String A = stringToHexString(AA);

                //DB中的TagID为12位,偏移量为2,长度为6(放到设置页面中)
                if (mContext.mRFIDWithUHF.writeData("00000000",
                        RFIDWithUHF.BankEnum.valueOf("UII"), 2, 6, A, strUII)) {
                    showToast(mContext, "成功", 0);
                } else {
                    showToast(mContext, "失败", 0);
                }
                break;
            case R.id.btnread:
                mContext.mRFIDWithUHF.setPower(10);
                strUII = mContext.mRFIDWithUHF.inventorySingleTag();
                Log.e("epcepcepc13123", strUII);
                if (!TextUtils.isEmpty(strUII)) {
                    String EPC1 = hexStringToString(strUII);
                    Log.e("epcepce424pc", EPC1);
                    etread.setText(EPC1);
                }
                break;
        }
    }
}