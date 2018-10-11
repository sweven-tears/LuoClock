package com.sweven.clock.listener;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sweven.clock.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sweven on 2018/9/17.
 * Email:sweventears@Foxmail.com
 */
public class ClockOnTouch implements View.OnTouchListener {
    /**
     * 按下、抬起、移动状态
     */
    private static boolean downState, upState, moveState;
    /**
     * 设置时间的六个文本
     */

    private Context context;

    private TextView hour_up, hour_set, hour_down, minute_up, minute_set, minute_down;

    private RelativeLayout clockRedoLayout, clockLabelLayout;

    private static final int HOUR_TIME = 0, MINUTE_TIME = 1;

    private OpenActivity openRedo = null;
    private ListenerImage mListenerImage = null;


    /**
     * onTouch监听事件发生的次数
     * [按下、移动、抬起]三种监听
     * 用于控制单击事件、移动事件
     * 单击：按下count=1 -> 抬起count=2 -> 重置count=0
     * 移动：按下count=1 -> 移动count=2 -> 重置count=0
     */
    private static int touchCount = 0;

    private static final int MSG_WHAT = 0;
    private Timer timer;

    @SuppressLint("HandlerLeak")
    private Handler mAutoAmendTimeHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            int time = bundle.getInt("time");
            int position = bundle.getInt("position");

            if (position == HOUR_TIME) {
                setHourTime(time);
            } else if (position == MINUTE_TIME) {
                setMinuteTime(time);
            }
            switch (msg.what) {
                case MSG_WHAT:
                    if (!downState) {
                        stopTimer();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    public ClockOnTouch
            (ArrayList<TextView> views, ArrayList<RelativeLayout> layouts, Context context) {
        this.hour_up = views.get(0);
        this.hour_set = views.get(1);
        this.hour_down = views.get(2);
        this.minute_up = views.get(3);
        this.minute_set = views.get(4);
        this.minute_down = views.get(5);

        this.clockRedoLayout = layouts.get(0);
        this.clockLabelLayout = layouts.get(1);

        this.context = context;
    }

    /**
     * [改变小时]
     *
     * @param hour 选中的小时
     */
    @SuppressLint("SetTextI18n")
    public void setHourTime(int hour) {
        if (hour >= 10) {
            hour_set.setText(hour + "");
        } else {
            hour_set.setText("0" + hour);
        }
        if (hour - 1 >= 10) {
            hour_up.setText(hour - 1 + "");
        } else {
            if (hour - 1 < 0) {
                hour_up.setText("23");
            } else {
                hour_up.setText("0" + (hour - 1));
            }
        }
        if (hour + 1 >= 10) {
            if (hour + 1 > 23) {
                hour_down.setText("00");
            } else {
                hour_down.setText(hour + 1 + "");
            }
        } else {
            hour_down.setText("0" + (hour + 1));
        }
    }

    /**
     * [改变分钟]
     *
     * @param minute 选中的分钟
     */
    @SuppressLint("SetTextI18n")
    public void setMinuteTime(int minute) {
        if (minute >= 10) {
            minute_set.setText(minute + "");
        } else {
            minute_set.setText("0" + minute);
        }
        if (minute - 1 >= 10) {
            minute_up.setText(minute - 1 + "");
        } else {
            if (minute - 1 < 0) {
                minute_up.setText("59");
            } else {
                minute_up.setText("0" + (minute - 1));
            }
        }
        if (minute + 1 >= 10) {
            if (minute + 1 > 59) {
                minute_down.setText("00");
            } else {
                minute_down.setText(minute + 1 + "");
            }
        } else {
            minute_down.setText("0" + (minute + 1));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        downState = motionEvent.getAction() == MotionEvent.ACTION_DOWN;
        upState = motionEvent.getAction() == MotionEvent.ACTION_UP;
        moveState = motionEvent.getAction() == MotionEvent.ACTION_MOVE;
        switch (view.getId()) {
            case R.id.hour_up:
                amendTime(hour_up, HOUR_TIME);
                break;
            case R.id.hour_set:
                break;
            case R.id.hour_down:
                amendTime(hour_down, HOUR_TIME);
                break;
            case R.id.minute_up:
                amendTime(minute_up, MINUTE_TIME);
                break;
            case R.id.minute_set:
                break;
            case R.id.minute_down:
                amendTime(minute_down, MINUTE_TIME);
                break;
            case R.id.clock_redo_layout:
                fixLayout(clockRedoLayout);
                if (mListenerImage != null) {
                    mListenerImage.listener(view.getId(), motionEvent);
                }
                break;
            case R.id.clock_label_layout:
                fixLayout(clockLabelLayout);
                if (mListenerImage != null) {
                    mListenerImage.listener(view.getId(), motionEvent);
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * [设置按下时发生的变化]
     *
     * @param layout .
     */
    @SuppressLint("SetTextI18n")
    private void fixLayout(RelativeLayout layout) {
        if (downState) {
            layout.setBackgroundResource(R.color.gray_cc);
            touchCount++;
        } else {
            layout.setBackgroundResource(R.drawable.border_bottom);
            if (upState) {

                touchCount++;

                if (touchCount == 2) {
                    if (layout.getId() == R.id.clock_label_layout) {
                        //todo 弹出对话框
                        openDialog();
                    } else if (layout.getId() == R.id.clock_redo_layout) {
                        //todo 到新界面设置重复
                        openRedoActivity();
                    }
                }

                touchCount = 0;
            } else if (moveState) {
                touchCount++;
            }
        }
    }

    private void openRedoActivity() {
        if (openRedo != null) {
            openRedo.open();
        }
    }

    private void openDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setTitle("标签");
        dialog.create();
    }

    /**
     * [时间增加、减少处理]
     *
     * @param textView     被按住的组件
     * @param timePosition hour和minute
     */
    private void amendTime(TextView textView, int timePosition) {
        int time = Integer.parseInt(textView.getText().toString());
        if (downState) {
            touchCount++;
            if (timer == null) {
                timer = new Timer();
                // 模拟长按事件监听
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message message = mAutoAmendTimeHandler.obtainMessage();
                        message.what = MSG_WHAT;

                        Bundle bundle = new Bundle();
                        bundle.putInt("time", Integer.parseInt(textView.getText().toString()));
                        bundle.putInt("position", timePosition);
                        message.setData(bundle);

                        mAutoAmendTimeHandler.sendMessage(message);
                        touchCount++;
                    }
                }, 1000, 100);
            }
        } else if (moveState) {
            touchCount++;
            stopTimer();
        } else if (upState) {
            touchCount++;
            stopTimer();
            if (touchCount == 2) {
                if (timePosition == HOUR_TIME) {
                    setHourTime(time);
                } else if (timePosition == MINUTE_TIME) {
                    setMinuteTime(time);
                }
            }

            touchCount = 0;
        }
    }

    /**
     * 存在 Timer 则停止并设为 null
     */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public interface OpenActivity {
        void open();
    }

    public interface ListenerImage {
        void listener(int id, MotionEvent motionEvent);
    }

    public void openRedo(OpenActivity openRedo) {
        this.openRedo = openRedo;
    }

    public void setImageListener(ListenerImage listenerImage) {
        this.mListenerImage = listenerImage;
    }

}
