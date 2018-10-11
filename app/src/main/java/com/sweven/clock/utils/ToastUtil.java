package com.sweven.clock.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Sweven on 2018/9/15.
 * Email:sweventears@Foxmail.com
 * <p>
 * Toast工具类
 */
public class ToastUtil {
    private static Toast toast;

    /**
     * 显示一个较短时间的提示
     *
     * @param context 上下文
     * @param message 显示的文字
     */
    @SuppressLint("ShowToast")
    public static void showShort(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 显示一个较长时间的提示
     *
     * @param context 上下文
     * @param message 显示的文字
     */
    @SuppressLint("ShowToast")
    public static void showLong(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 取消显示
     */
    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }

    @SuppressLint("ShowToast")
    public static void showError(Context context, String message){
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
}
