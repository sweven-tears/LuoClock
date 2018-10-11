package com.sweven.clock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sweven.clock.listener.CustomListener;
import com.sweven.clock.utils.LogUtil;
import com.sweven.clock.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;

import static com.sweven.clock.parameter.RedoParameter.BUNDLE_ARRAY;
import static com.sweven.clock.parameter.RedoParameter.BUNDLE_PERIOD;
import static com.sweven.clock.parameter.RedoParameter.PERIOD_DAILY;
import static com.sweven.clock.parameter.RedoParameter.PERIOD_KIND;
import static com.sweven.clock.parameter.RedoParameter.PERIOD_MONTHLY;
import static com.sweven.clock.parameter.RedoParameter.PERIOD_NULL;
import static com.sweven.clock.parameter.RedoParameter.PERIOD_OTHER;
import static com.sweven.clock.parameter.RedoParameter.PERIOD_WEEKLY;

public class RedoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private LinearLayout redoLayout, periodLayout;
    private Spinner periodSelect;
    private Switch redoSwitch;
    private LinearLayout periodWeeklyPanel, periodMonthlyPanel, periodOtherPanel;
    private TextView periodText;
    private LinearLayout weeklyLayout;
    private TableLayout tableView;
    private TextView doubtImage;
    private EditText otherEdit;

    public static final int RESULT = 201, REQUEST = 202;

    private static ArrayList<Integer> periodArray;
    private static Bundle periodBundle;
    private String[] week;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redo);

        initID();
        initPeriod();
        initListener();
        initData();
    }

    /**
     * [配置ID资源]
     */
    private void initID() {

        periodLayout = findViewById(R.id.period_layout);
        periodSelect = findViewById(R.id.period_select);

        redoLayout = findViewById(R.id.redo_layout);
        redoSwitch = findViewById(R.id.redo_switch);

        periodText = findViewById(R.id.period_text);

        periodWeeklyPanel = findViewById(R.id.redo_period_weekly);
        periodMonthlyPanel = findViewById(R.id.redo_period_monthly);
        periodOtherPanel = findViewById(R.id.redo_period_other);

        weeklyLayout = findViewById(R.id.period_weekly_layout);

        tableView = findViewById(R.id.period_monthly_table);
        doubtImage = findViewById(R.id.period_monthly_doubt);

        otherEdit = findViewById(R.id.period_other_edit);

        monthlyTable();

        weeklyCheckBox();
    }

    /**
     * [“每周”周期 布局设置]
     */
    @SuppressLint("ClickableViewAccessibility")
    private void weeklyCheckBox() {
        week = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        LinearLayout layout;
        TextView textView;
        for (String aWeek : week) {
            layout = new LinearLayout(this);
            layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 100));
            layout.setBackgroundResource(R.drawable.border_bottom);
            layout.setGravity(Gravity.CENTER);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            textView = new TextView(this);
            textView.setText(aWeek);
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 5);
            lp.gravity = Gravity.CENTER;
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER | Gravity.START);
            textView.setPadding(5, 5, 0, 5);


            CheckBox checkBox = new CheckBox(this);
            checkBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            checkBox.setGravity(Gravity.CENTER);

            layout.addView(textView);
            layout.addView(checkBox);

            CustomListener touchListener = new CustomListener();
            touchListener.setOnPressListener(
                    (view) -> {
                        LinearLayout linearLayout= (LinearLayout) view;
                        linearLayout.setBackgroundResource(R.color.gray_cc);
                    });
            touchListener.setOnExceptPressListener(
                    (view) -> {
                        LinearLayout linearLayout= (LinearLayout) view;
                        linearLayout.setBackgroundResource(R.drawable.border_bottom);
                    });
            touchListener.setOnUpListener(
                    (view) -> {
                        ViewGroup linearLayout = (ViewGroup) view;
                        TextView text = (TextView) linearLayout.getChildAt(0);
                        CheckBox box = (CheckBox) linearLayout.getChildAt(1);
                        for (String aWeek1 : week) {
                            if (text.getText().toString().equals(aWeek1)) {
                                if (box.isChecked()) {
                                    box.setChecked(false);
                                } else {
                                    box.setChecked(true);
                                }
                            }
                        }

                    });
            layout.setOnTouchListener(touchListener);

            TextView finalTextView = textView;
            checkBox.setOnCheckedChangeListener(
                    (btn, b) -> {
                        if (b) {
                            for (int i = 0; i < week.length; i++) {
                                if (finalTextView.getText().toString().equals(week[i])) {
                                    periodArray.add(i + 1);
                                }
                            }
                        } else {
                            for (int i = 0; i < week.length; i++) {
                                if (finalTextView.getText().toString().equals(week[i])) {
                                    periodArray.remove((Object) (i + 1));
                                }
                            }
                        }
                    });

            weeklyLayout.addView(layout);
        }
    }

    /**
     * [“每月”周期 号数的设置与布局]
     */
    @SuppressLint({"SetTextI18n"})
    private void monthlyTable() {
        TableRow row;
        TextView textView;
        for (int i = 1; i < 6; i++) {
            row = new TableRow(this);
            for (int j = 1; j <= 7; j++) {
                textView = new TextView(this);
                textView.setPaddingRelative(30, 30, 30, 30);
                textView.setText((i - 1) * 7 + j + "");
                textView.setTextColor(Color.BLACK);
                if (i == 1) {
                    if (j == 1) {
                        textView.setBackgroundResource(R.drawable.border_all);
                    } else {
                        textView.setBackgroundResource(R.drawable.border_right_top_bottom);
                    }
                } else {
                    if (j == 1) {
                        textView.setBackgroundResource(R.drawable.border_left_right_bottom);
                    } else {
                        textView.setBackgroundResource(R.drawable.border_right_bottom);
                    }
                }
                textView.setOnClickListener(this::onClickTableTextView);
                row.addView(textView);
                if (i == 5 && j >= 4) {
                    row.removeView(textView);
                }
            }
            tableView.addView(row);
        }
    }

    /**
     * [“每月”-->号数点击变化]
     *
     * @param view textView
     */
    private void onClickTableTextView(View view) {
        boolean isBlue = ((TextView) view).getCurrentTextColor() == Color.BLUE;
        int num = Integer.parseInt(((TextView) view).getText().toString());
        if (isBlue) {
            ((TextView) view).setTextColor(Color.BLACK);
            periodArray.remove((Object) num);
        } else {
            ((TextView) view).setTextColor(Color.BLUE);
            periodArray.add(num);
        }
    }

    /**
     * [初始化周期下拉框]
     */
    private void initPeriod() {
        //适配器
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PERIOD_KIND);
        //设置样式
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        periodSelect.setAdapter(arrayAdapter);
    }

    /**
     * [初始化数据]
     */
    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String period = intent.getStringExtra(BUNDLE_PERIOD);
            if (period.equals(PERIOD_NULL)) {
                redoSwitch.setChecked(false);
            } else {
                redoSwitch.setChecked(true);
                // 设置初始选择的选项
                switch (period) {
                    case PERIOD_DAILY:
                        periodSelect.setSelection(0, true);
                        break;
                    case PERIOD_WEEKLY:
                        periodSelect.setSelection(1, true);
                        break;
                    case PERIOD_MONTHLY:
                        periodSelect.setSelection(2, true);
                        break;
                    case PERIOD_OTHER:
                        periodSelect.setSelection(3, true);
                        break;
                }
            }
        }

        periodArray = new ArrayList<>();
        periodBundle = new Bundle();

        if (redoSwitch.isChecked()) {
            periodText.setTextColor(getResources().getColor(R.color.black));
            periodSelect.setEnabled(true);
            periodLayout.setEnabled(true);
        } else {
            periodText.setTextColor(getResources().getColor(R.color.gray_cc));
            periodSelect.setEnabled(false);
            periodLayout.setEnabled(false);
        }

        doubtImage.setBackgroundResource(R.drawable.doubt);
    }

    /**
     * [设置监听器]
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        CustomListener customListener = new CustomListener();

        redoSwitch.setOnClickListener(this);
        redoSwitch.setOnCheckedChangeListener(this);
        redoLayout.setOnTouchListener(customListener);

        periodSelect.setOnItemSelectedListener(this);
        periodLayout.setOnTouchListener(customListener);

        doubtImage.setOnClickListener(this);

        setCustomListener(customListener);
    }

    /**
     * @param customListener 自定义 onTouch 监听器
     */
    private void setCustomListener(CustomListener customListener) {
        customListener.setOnPressListener(view -> {
            int id = view.getId();
            if (id == R.id.redo_layout) {
                redoLayout.setBackgroundResource(R.color.gray_cc);
            } else if (id == R.id.period_layout) {
                periodLayout.setBackgroundResource(R.color.gray_cc);
            }
        });
        customListener.setOnExceptPressListener(view -> {
            int id = view.getId();
            if (id == R.id.redo_layout) {
                redoLayout.setBackgroundResource(R.drawable.border_bottom);
            } else if (id == R.id.period_layout) {
                periodLayout.setBackgroundResource(R.drawable.border_bottom);
            }
        });
        customListener.setOnUpListener(view -> {
            int id = view.getId();
            if (id == R.id.redo_layout) {
                if (redoSwitch.isChecked()) {
                    redoSwitch.setChecked(false);
                } else {
                    redoSwitch.setChecked(true);
                }
            } else if (id == R.id.period_layout) {
                periodSelect.performClick();
            }
        });
    }

    /**
     * [切换周期方式选择对应的页面]
     *
     * @param adapterView item
     * @param view        .
     * @param i           .
     * @param l           .
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String kind = (String) adapterView.getSelectedItem();
        periodArray.clear();
        switch (kind) {
            case PERIOD_DAILY:
                cutPeriodKind(PERIOD_DAILY);

                clearCheckedPeriod(PERIOD_WEEKLY);
                clearCheckedPeriod(PERIOD_MONTHLY);
                clearCheckedPeriod(PERIOD_OTHER);
                break;
            case PERIOD_WEEKLY:
                cutPeriodKind(PERIOD_WEEKLY);

                clearCheckedPeriod(PERIOD_MONTHLY);
                clearCheckedPeriod(PERIOD_OTHER);
                break;
            case PERIOD_MONTHLY:
                cutPeriodKind(PERIOD_MONTHLY);

                clearCheckedPeriod(PERIOD_WEEKLY);
                clearCheckedPeriod(PERIOD_OTHER);
                break;
            case PERIOD_OTHER:
                cutPeriodKind(PERIOD_OTHER);

                clearCheckedPeriod(PERIOD_WEEKLY);
                clearCheckedPeriod(PERIOD_MONTHLY);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * [切换周期类型的panel的显示]
     *
     * @param kind 周期类型
     */
    private void cutPeriodKind(String kind) {
        periodWeeklyPanel.setVisibility(View.INVISIBLE);
        periodMonthlyPanel.setVisibility(View.INVISIBLE);
        periodOtherPanel.setVisibility(View.INVISIBLE);
        switch (kind) {
            case PERIOD_WEEKLY:
                periodWeeklyPanel.setVisibility(View.VISIBLE);
                break;
            case PERIOD_MONTHLY:
                periodMonthlyPanel.setVisibility(View.VISIBLE);
                break;
            case PERIOD_OTHER:
                periodOtherPanel.setVisibility(View.VISIBLE);
                break;
            case PERIOD_DAILY:
                break;
        }
    }

    /**
     * [用于切换周期类型时清除选中的选项]
     *
     * @param kind 重复周期类型
     */
    private void clearCheckedPeriod(String kind) {
        switch (kind) {
            //清除“每周”选中的选项
            case PERIOD_WEEKLY:
                for (int n = 0; n < week.length; n++) {
                    ViewGroup group = (ViewGroup) weeklyLayout.getChildAt(n);
                    CheckBox box = (CheckBox) group.getChildAt(1);
                    box.setChecked(false);
                }
                break;
            //清除“每月”选中的选项
            case PERIOD_MONTHLY:
                for (int m = 1; m < 6; m++) {
                    ViewGroup group = (ViewGroup) tableView.getChildAt(m);
                    TextView textView;
                    if (m < 5) {
                        for (int n = 1; n <= 7; n++) {
                            textView = (TextView) group.getChildAt(n);
//                            textView.setTextColor(Color.BLACK);
                        }
                    } else {
                        for (int n = 1; n <= 3; n++) {
                            textView = (TextView) group.getChildAt(n);
//                            textView.setTextColor(Color.BLACK);
                        }
                    }
                }
                break;
            //清除“其他”输入的数据
            case PERIOD_OTHER:
                otherEdit.setText("");
                break;
        }
    }

    /**
     * [重复状态切换]
     *
     * @param compoundButton .
     * @param b              .
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            periodText.setTextColor(getResources().getColor(R.color.black));
            periodSelect.setEnabled(true);
            periodLayout.setEnabled(true);
        } else {
            periodText.setTextColor(getResources().getColor(R.color.gray_cc));
            periodLayout.setEnabled(false);
            periodSelect.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.redo_switch) {
            if (redoSwitch.isChecked()) {
                periodText.setTextColor(getResources().getColor(R.color.black));
                periodSelect.setEnabled(true);
                periodLayout.setEnabled(true);
            } else {
                periodText.setTextColor(getResources().getColor(R.color.gray_cc));
                periodLayout.setEnabled(false);
                periodSelect.setEnabled(false);
            }
        } else if (view.getId() == R.id.period_monthly_doubt) {
            ToastUtil.showShort(RedoActivity.this, "解惑（待完善）");
        }
    }

    /**
     * [将“重复”设置的内容传回上一级activity]
     *
     * @param keyCode .
     * @param event   .
     * @return .
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(RedoActivity.this, FormulateActivity.class);
            String period;
            if (redoSwitch.isChecked()) {
                period = (String) periodSelect.getSelectedItem();
                if (period.equals(PERIOD_OTHER)) {
                    periodArray.clear();
                    try {
                        periodArray.add(Integer.valueOf(otherEdit.getText().toString()));
                    } catch (NumberFormatException e) {
                        new LogUtil("RedoActivity");
                        LogUtil.v("输入框未输入任何内容");
                    }
                }
            } else {
                period = PERIOD_NULL;
            }
            Collections.sort(periodArray);
            periodBundle.putString(BUNDLE_PERIOD, period);
            periodBundle.putIntegerArrayList(BUNDLE_ARRAY, periodArray);
            intent.putExtras(periodBundle);
            setResult(RESULT, intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
