package cn.zinus.warehouse.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Developer:Spring
 * DataTime :2017/10/24 16:49
 * Main Change:
 */

public class ThreadUtil {
    private ExecutorService services;

    public static ThreadUtil thread;

    public static ThreadUtil getInstance(){
        if(thread == null){
            thread = new ThreadUtil();
        }
        return  thread;
    }

    public ThreadUtil(){
        services = Executors.newSingleThreadExecutor();
    }

    public void add(final Method method, final Object _class){
        services.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    method.invoke(_class);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
