package com.sweven.clock.listener;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Sweven on 2018/10/8.
 * Email:sweventears@Foxmail.com
 */
public class CustomListener implements View.OnTouchListener {

    /**
     * onTouch监听事件发生的次数
     * [按下、移动、抬起]三种监听
     * 用于控制单击事件、移动事件
     * 单击：按下count=1 -> 抬起count=2 -> 重置count=0
     * 移动：按下count=1 -> 移动count=2 -> 重置count=0
     */
    private static int touchCount = 0;

    private static setOnPressListener onPressListener;
    private static setOnExceptPressListener onExceptPressListener;
    private static setOnMoveListener onMoveListener;
    private static setOnUpliftListener onUpliftListener;

    public CustomListener() {
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean downState = motionEvent.getAction() == MotionEvent.ACTION_DOWN;
        boolean moveState = motionEvent.getAction() == MotionEvent.ACTION_MOVE;
        boolean upState = motionEvent.getAction() == MotionEvent.ACTION_UP;
        if (downState) {
            if (onPressListener != null) {
                onPressListener.onPress(view);
            }
            touchCount++;
        } else {
            if (onExceptPressListener != null) {
                onExceptPressListener.onExcept(view);
            }
            if (moveState) {
                if (onMoveListener != null) {
                    onMoveListener.onMove(view);
                }
                touchCount++;
            } else if (upState) {
                touchCount++;
                if (touchCount == 2) {
                    if (onUpliftListener != null) {
                        onUpliftListener.onUp(view);
                    }
                }

                touchCount = 0;
            }
        }
        return true;
    }

    public interface setOnPressListener {
        void onPress(View view);
    }

    public interface setOnExceptPressListener {
        void onExcept(View view);
    }


    public interface setOnMoveListener {
        void onMove(View view);

    }

    public interface setOnUpliftListener {
        void onUp(View view);
    }

    public void setOnPressListener(setOnPressListener onPressListener) {
        CustomListener.onPressListener = onPressListener;
    }

    public void setOnExceptPressListener(setOnExceptPressListener onExceptPressListener) {
        CustomListener.onExceptPressListener = onExceptPressListener;
    }

    public void setOnMoveListener(setOnMoveListener onMoveListener) {
        CustomListener.onMoveListener = onMoveListener;
    }

    public void setOnUpListener(setOnUpliftListener onUpListener) {
        CustomListener.onUpliftListener = onUpListener;
    }
}
