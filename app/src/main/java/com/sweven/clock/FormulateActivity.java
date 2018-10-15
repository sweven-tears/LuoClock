package com.sweven.clock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sweven.clock.adapter.AppAdapter;
import com.sweven.clock.base.BaseActivity;
import com.sweven.clock.info.AppMsg;
import com.sweven.clock.listener.ClockOnTouch;
import com.sweven.clock.parameter.RedoParameter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sweven.clock.parameter.RedoParameter.PERIOD_DAILY;
import static com.sweven.clock.parameter.RedoParameter.PERIOD_MONTHLY;
import static com.sweven.clock.parameter.RedoParameter.PERIOD_NULL;
import static com.sweven.clock.parameter.RedoParameter.PERIOD_OTHER;
import static com.sweven.clock.parameter.RedoParameter.PERIOD_WEEKLY;

public class FormulateActivity extends BaseActivity implements ClockOnTouch.ListenerImage {

    private TextView appName;
    private ImageView appIcon;
    private RelativeLayout clockRedoLayout, clockLabelLayout;
    private TextView clockRedo, clockLabel;
    private ImageView clockRedoImage, clockLabelImage;

    /**
     * 用Intent传递过来的数据
     */
    private ArrayList<AppMsg> app;

    /**
     * 设置时间的六个文本
     */
    private TextView hour_up, hour_set, hour_down, minute_up, minute_set, minute_down;

    private ClockOnTouch clockOnTouch;
    private static String presentPeriodKind = PERIOD_NULL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulate);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.FormulateActivityTitle);
        }

        initIntent();
        bindViewId();
        initData();
        initListener();

    }

    /**
     * 获取从上一个 Activity 获取到的数据
     */
    private void initIntent() {
        Intent intent = getIntent();
        String packageName = intent.getStringExtra("packageName");
        String appName = intent.getStringExtra("appName");
        byte[] drawableByte = intent.getByteArrayExtra("icon");
        Drawable icon = AppAdapter.byteToDrawable(drawableByte);

        app = new ArrayList<>();
        app.add(new AppMsg(icon, appName, packageName, ""));
    }

    @Override
    protected void bindViewId() {
        appIcon = findViewById(R.id.app_icon);
        appName = findViewById(R.id.app_name);

        hour_up = findViewById(R.id.hour_up);
        hour_set = findViewById(R.id.hour_set);
        hour_down = findViewById(R.id.hour_down);
        minute_up = findViewById(R.id.minute_up);
        minute_set = findViewById(R.id.minute_set);
        minute_down = findViewById(R.id.minute_down);

        clockRedoLayout = findViewById(R.id.clock_redo_layout);
        clockLabelLayout = findViewById(R.id.clock_label_layout);

        clockRedo = findViewById(R.id.clock_redo);
        clockLabel = findViewById(R.id.clock_label);

        clockRedoImage = findViewById(R.id.clock_rode_image);
        clockLabelImage = findViewById(R.id.clock_label_image);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    @Override
    protected void initData() {
        appIcon.setImageDrawable(app.get(0).getIcon());
        this.appName.setText(app.get(0).getAppName());

        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        String time = new SimpleDateFormat("HH:mm").format(date);
        /*
         *   选中的小时数、分钟数
         */
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3));

        clockRedo.setText("一次");
        clockLabel.setText("新任务");

        // 将所有时间 TextView 打包发送
        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(hour_up);
        textViews.add(hour_set);
        textViews.add(hour_down);
        textViews.add(minute_up);
        textViews.add(minute_set);
        textViews.add(minute_down);

        // 将 RelativeLayout 打包发送
        ArrayList<RelativeLayout> layouts = new ArrayList<>();
        layouts.add(clockRedoLayout);
        layouts.add(clockLabelLayout);

        clockOnTouch = new ClockOnTouch(textViews, layouts, FormulateActivity.this);

        clockOnTouch.setHourTime(hour);
        clockOnTouch.setMinuteTime(minute);

        clockOnTouch.openRedo(() -> {
            Intent intent = new Intent(FormulateActivity.this, RedoActivity.class);
            intent.putExtra("period", presentPeriodKind);
            startActivityForResult(intent, RedoActivity.REQUEST);
        });

        clockOnTouch.setImageListener(this);

    }

    /**
     * 添加监听器
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        hour_up.setOnTouchListener(clockOnTouch);
        hour_set.setOnTouchListener(clockOnTouch);
        hour_down.setOnTouchListener(clockOnTouch);
        minute_up.setOnTouchListener(clockOnTouch);
        minute_set.setOnTouchListener(clockOnTouch);
        minute_down.setOnTouchListener(clockOnTouch);

        clockRedoLayout.setOnTouchListener(clockOnTouch);
        clockLabelLayout.setOnTouchListener(clockOnTouch);
    }

    /**
     * [回传数据：定时任务的定时周期]
     *
     * @param requestCode .
     * @param resultCode  .
     * @param data        .
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RedoActivity.RESULT) {
            assert data != null;
            Bundle periodBundle = data.getExtras();
            assert periodBundle != null;
            String period = periodBundle.getString(RedoParameter.BUNDLE_PERIOD);
            presentPeriodKind = period;
            List<Integer> array = periodBundle.getIntegerArrayList(RedoParameter.BUNDLE_ARRAY);
            assert period != null;
            period = dealPeriod(period, array);
            clockRedo.setText(period);
        }
    }

    /**
     * [将传递回的数据整理出应显示的文本]
     *
     * @param period 重复周期类型
     * @param array  重复周期选项
     * @return 文本
     */
    private String dealPeriod(String period, List<Integer> array) {
        @SuppressLint("UseSparseArrays")
        Map<Integer, String> weeks = new HashMap<>();
        weeks.put(1, "周一");
        weeks.put(2, "周二");
        weeks.put(3, "周三");
        weeks.put(4, "周四");
        weeks.put(5, "周五");
        weeks.put(6, "周六");
        weeks.put(7, "周日");
        switch (period) {
            case PERIOD_WEEKLY:
                StringBuilder periodBuilder = new StringBuilder();
                if (array == null || array.size() < 1) {
                    presentPeriodKind = period = PERIOD_NULL;
                } else if (array.size() < 7) {
                    array.size();
                    for (int i = 0; i < array.size(); i++) {
                        if (i != array.size() - 1) {
                            periodBuilder.append(weeks.get(array.get(i))).append("、");
                        } else {
                            periodBuilder.append(weeks.get(array.get(i)));
                        }
                    }
                    period = periodBuilder.toString();
                } else {
                    presentPeriodKind = period = PERIOD_DAILY;
                }
                break;
            case PERIOD_MONTHLY:
                if (array == null || array.size() < 1) {
                    presentPeriodKind = period = PERIOD_NULL;
                } else if (array.size() < 31) {
                    array.size();
                    StringBuilder periodBuilder1 = new StringBuilder(PERIOD_MONTHLY);
                    for (int i = 0; i < array.size(); i++) {
                        if (i != array.size() - 1) {
                            periodBuilder1.append(array.get(i)).append("、");
                        } else {
                            periodBuilder1.append(array.get(i));
                        }
                    }
                    period = periodBuilder1.toString() + "号";
                } else {
                    presentPeriodKind = period = PERIOD_DAILY;
                }
                break;
            case PERIOD_OTHER:
                if (array == null || array.size() < 1) {
                    presentPeriodKind = period = PERIOD_NULL;
                } else {
                    array.size();
                    period = array.get(0) + "天/次";
                }
                break;
        }
        return period;
    }

    /**
     * [确认任务后将 period_kind 重新初始化]
     */
    @Override
    protected void onStop() {
        super.onStop();
        presentPeriodKind = PERIOD_NULL;
    }

    /**
     * [箭头 ">" 的背景按压变化]
     *
     * @param id          .
     * @param motionEvent .
     */
    @Override
    public void listener(int id, MotionEvent motionEvent) {
        switch (id) {
            case R.id.clock_redo_layout:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    clockRedoImage.setImageResource(R.drawable.enable_on_press_arrows_right);
                } else {
                    clockRedoImage.setImageResource(R.drawable.enable_off_press_arrows_right);
                }
                break;
            case R.id.clock_label_layout:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    clockLabelImage.setImageResource(R.drawable.enable_on_press_arrows_right);
                } else {
                    clockLabelImage.setImageResource(R.drawable.enable_off_press_arrows_right);
                }
                break;
        }
    }
}
