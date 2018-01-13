package cn.zinus.shipping.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import cn.zinus.shipping.Activity.MainNaviActivity;
import cn.zinus.shipping.R;

/**
 * Created by Spring on 2017/3/21.
 */

public class MainMenuFragment extends KeyDownFragment implements View.OnClickListener {
    MainNaviActivity mContext;
    LinearLayout mllSettings;
    LinearLayout mllShipping;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mainmenu, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainNaviActivity) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mllSettings = (LinearLayout) getView().findViewById(R.id.llSettings);
        mllShipping =  (LinearLayout) getView().findViewById(R.id.llShipping);
        mllSettings.setOnClickListener(this);
        mllShipping.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSettings:
                EventBus.getDefault().post(new Event.ToFragmentEvent(R.id.item_setting, getString(R.string.Setting)));
                break;
            case R.id.llShipping:
                EventBus.getDefault().post(new Event.ToFragmentEvent(R.id.item_Shipping, getString(R.string.Shipping)));
                break;
        }
    }

    @Override
    public void myBackKeyDwon() {
        AlertDialog isExit = new AlertDialog.Builder(mContext).
                setTitle("系统提示").
                setMessage("退出系统吗").
                setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mContext.finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }).
                setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        // 显示对话框
        isExit.show();
    }
}
