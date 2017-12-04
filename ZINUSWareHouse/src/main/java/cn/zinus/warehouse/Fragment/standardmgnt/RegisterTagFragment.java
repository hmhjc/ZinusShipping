package cn.zinus.warehouse.Fragment.standardmgnt;

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

import cn.zinus.warehouse.R;
import cn.zinus.warehouse.Activity.MainNaviActivity;
import cn.zinus.warehouse.Fragment.KeyDownFragment;

import static com.micube.control.util.Server.hexStringToString;
import static com.micube.control.util.Server.stringToHexString;
import static cn.zinus.warehouse.util.Utils.showToast;

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
                mContext.mRFIDWithUHF.setPower(10);
                String strUII1 = mContext.mRFIDWithUHF.inventorySingleTag();
                if (!TextUtils.isEmpty(strUII1)) {
                    String EPC = hexStringToString(strUII1);
                    Log.e("epcepcepc",EPC);
                    mTextView.setText(EPC);
                }
                break;
            case R.id.btnwrite:
                mContext.mRFIDWithUHF.setPower(10);
                String AA =etwrite.getText().toString();
                String A = stringToHexString(AA);

                //DB中的TagID为12位,偏移量为2,长度为6(放到设置页面中)
                if (mContext.mRFIDWithUHF.writeData("00000000",
                        RFIDWithUHF.BankEnum.valueOf("UII"), 2,6, A, strUII)) {
                    showToast(mContext, "成功", 0);
                } else {
                    showToast(mContext, "失败", 0);
                }
                break;
            case R.id.btnread:
                mContext.mRFIDWithUHF.setPower(10);
                strUII = mContext.mRFIDWithUHF.inventorySingleTag();
                Log.e("epcepcepc13123",strUII);
                if (!TextUtils.isEmpty(strUII)) {
                    String EPC1 = hexStringToString(strUII);
                    Log.e("epcepce424pc",EPC1);
                    etread.setText(EPC1);
                }
                break;
        }
    }
}