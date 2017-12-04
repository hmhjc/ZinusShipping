package cn.zinus.warehouse;

import cn.zinus.warehouse.util.SoundUtil;

/**
 * Created by Spring on 2017/6/12.
 */

public class Application extends android.app.Application {
    private static Application _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        SoundUtil.initSoundPool(this);
    }

    public static Application getInstance() {
        return _instance;
    }

}
