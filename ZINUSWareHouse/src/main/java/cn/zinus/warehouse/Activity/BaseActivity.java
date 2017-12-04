package cn.zinus.warehouse.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cn.zinus.warehouse.Config.AppConfig;
import cn.zinus.warehouse.util.Constant;

/**
 * requestSmartFactory(what,request,listener)
 * Created by Spring on 2017/6/20.
 */

public class BaseActivity extends AppCompatActivity {
    //public RequestQueue mRequestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mRequestQueue = NoHttp.newRequestQueue();
        if (AppConfig.getInstance().getLanuageType() == null) {
            AppConfig.getInstance().setLanuageType(Constant.ZH_CN);
        }
        changeAppLanguage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void changeAppLanguage() {
        String sta = AppConfig.getInstance().getLanuageType();
        Log.e("","");
        // 本地语言设置
//        Locale myLocale = new Locale(sta);
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf, dm);
    }


}