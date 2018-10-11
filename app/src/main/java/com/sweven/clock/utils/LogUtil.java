package com.sweven.clock.utils;

import android.util.Log;

/**
 * Created by Sweven on 2018/9/15.
 * Email:sweventears@Foxmail.com
 */
public class LogUtil {
    private static String TAG = "Activity";
    private static boolean show = true;

    public LogUtil(String TAG) {
        LogUtil.TAG = TAG;
    }

    public static void v(String msg) {
        if (show) {
            Log.v(TAG + "-------->>", msg);
        } else {
            Log.v(TAG + "-------->>", "Log记录已关闭");
        }
    }

    public static void d(String msg) {
        if (show) {
            Log.d(TAG + "-------->>", msg);
        } else {
            Log.v(TAG + "-------->>", "Log记录已关闭");
        }
    }

    public static void i(String msg) {
        if (show) {
            Log.i(TAG + "-------->>", msg);
        } else {
            Log.v(TAG + "-------->>", "Log记录已关闭");
        }
    }

    public static void w(String msg) {
        if (show) {
            Log.w(TAG + "-------->>", msg);
        } else {
            Log.v(TAG + "-------->>", "Log记录已关闭");
        }
    }

    public static void e(String msg) {
        if (show) {
            Log.e(TAG + "-------->>", msg);
        } else {
            Log.v(TAG + "-------->>", "Log记录已关闭");
        }
    }

    public static void a(String msg) {
        if (show) {
            Log.wtf(TAG + "-------->>", msg);
        } else {
            Log.v(TAG + "-------->>", "Log记录已关闭");
        }
    }

    public static void show() {
        if (!show) {
            show = true;
        }
    }

    public static void hidden() {
        if (show) {
            show = false;
        }
    }
}
