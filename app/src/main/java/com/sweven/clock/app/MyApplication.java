package com.sweven.clock.app;

import android.app.Application;

import com.sweven.util.ToastUtil;

import static com.sweven.util.ToastUtil.Gravity.BOTTOM;

/**
 * Created by Sweven on 2019/6/9.
 * Email:sweventears@Foxmail.com
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        if (instance == null) {
            synchronized (Application.class) {
                instance = new MyApplication();
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.setGravity(this, BOTTOM);
    }
}
