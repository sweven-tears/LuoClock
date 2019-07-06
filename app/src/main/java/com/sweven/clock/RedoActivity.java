package com.sweven.clock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.sweven.clock.adapter.MonthAdapter;
import com.sweven.clock.adapter.WeekAdapter;
import com.sweven.clock.base.BaseActivity;
import com.sweven.clock.entity.Month;
import com.sweven.clock.entity.Week;
import com.sweven.clock.listener.CustomListener;
import com.sweven.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.sweven.clock.parameter.Redo.BUNDLE_ARRAY;
import static com.sweven.clock.parameter.Redo.BUNDLE_PERIOD;
import static com.sweven.clock.parameter.Redo.PERIOD_DAILY;
import static com.sweven.clock.parameter.Redo.PERIOD_KIND;
import static com.sweven.clock.parameter.Redo.PERIOD_MONTHLY;
import static com.sweven.clock.parameter.Redo.PERIOD_NULL;
import static com.sweven.clock.parameter.Redo.PERIOD_OTHER;
import static com.sweven.clock.parameter.Redo.PERIOD_WEEKLY;

public class RedoActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    public static final int RESULT = 201, REQUEST = 202;
    private static List<Integer> periodArray;
    private static Bundle periodBundle;
    private LinearLayout periodLayout;
    private Spinner periodSelect;
    private Switch redoSwitch;
    private LinearLayout periodWeeklyPanel, periodMonthlyPanel, periodOtherPanel;
    private TextView periodText;
    private TextView doubtImage;
    private EditText otherEdit;

    private RecyclerView weekRecyclerView;
    private WeekAdapter weekAdapter;
    private List<Week> weeks = new ArrayList<>();

    private TextView calendarTop;
    private RecyclerView monthRecyclerView;
    private MonthAdapter monthAdapter;
    private List<Month> months = new ArrayList<>();
    private int year, month;

    {
        String[] week = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        for (String s : week) {
            weeks.add(new Week(s, false));
        }
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        int weekdayOfMonthFirstDay = DateUtil.getWeekdayOfMonthFirstDay(year, month) - 1;
        int maxDate = DateUtil.getDaysOfMonth(year, month);
        for (int i = 0; i < weekdayOfMonthFirstDay % 7 + maxDate; i++) {
            int j = i - weekdayOfMonthFirstDay % 7;
            Month m = new Month();
            if (j < 0) {
                m.setDay("");
            } else {
                m.setDay((j + 1) + "");
            }
            months.add(m);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redo);

        bindViewId();
        initPeriod();
        initListener();
        initData();
    }

    @Override
    protected void bindViewId() {

        periodLayout = findViewById(R.id.period_layout);
        periodSelect = findViewById(R.id.period_select);

        redoSwitch = findViewById(R.id.redo_switch);

        periodText = findViewById(R.id.period_text);

        periodWeeklyPanel = findViewById(R.id.redo_period_weekly);
        periodMonthlyPanel = findViewById(R.id.redo_period_monthly);
        periodOtherPanel = findViewById(R.id.redo_period_other);

        otherEdit = findViewById(R.id.period_other_edit);

        weekRecyclerView = findViewById(R.id.period_weekly_list);
        weekAdapter = new WeekAdapter(this, weeks);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        weekRecyclerView.setLayoutManager(manager);
        weekRecyclerView.setAdapter(weekAdapter);

        calendarTop = findViewById(R.id.calendar_top);
        monthRecyclerView = findViewById(R.id.period_monthly_list);
        monthAdapter = new MonthAdapter(this, months);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 7);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        monthRecyclerView.setLayoutManager(layoutManager);
        monthRecyclerView.setAdapter(monthAdapter);
    }

    /**
     * [初始化周期下拉框]
     */
    private void initPeriod() {
        //适配器
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PERIOD_KIND);
        //设置样式
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        periodSelect.setAdapter(arrayAdapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            periodBundle = intent.getExtras();
            assert periodBundle != null;
            String period = periodBundle.getString(BUNDLE_PERIOD);
            if (Objects.requireNonNull(period).equals(PERIOD_NULL)) {
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

        calendarTop.setText(year + "-" + (month < 10 ? month : "0" + month));

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

    }

    /**
     * [设置监听器]
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        CustomListener customListener = new CustomListener();

        redoSwitch.setOnClickListener(this);
        redoSwitch.setOnCheckedChangeListener(this);

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
            if (id == R.id.period_layout) {
                periodLayout.setBackgroundResource(R.color.gray_cc);
            }
        });
        customListener.setOnExceptPressListener(view -> {
            int id = view.getId();
            if (id == R.id.period_layout) {
                periodLayout.setBackgroundResource(R.drawable.border_bottom);
            }
        });
        customListener.setOnUpListener(view -> {
            int id = view.getId();
            if (id == R.id.period_layout) {
                periodSelect.performClick();
            }
        });
    }

    /**
     * [切换周期方式选择对应的页面]
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
        periodWeeklyPanel.setVisibility(View.GONE);
        periodMonthlyPanel.setVisibility(View.GONE);
        periodOtherPanel.setVisibility(View.GONE);
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
                weekAdapter.clearSelected();
                break;
            //清除“每月”选中的选项
            case PERIOD_MONTHLY:
                monthAdapter.clearSelected();
                break;
            //清除“其他”输入的数据
            case PERIOD_OTHER:
                otherEdit.setText("");
                break;
        }
    }

    /**
     * [重复状态切换]
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
            Intent intent = new Intent(activity, FormulateActivity.class);
            String period;
            if (redoSwitch.isChecked()) {
                period = (String) periodSelect.getSelectedItem();
                if (period.equals(PERIOD_OTHER)) {
                    periodArray.clear();
                    try {
                        periodArray.add(Integer.valueOf(otherEdit.getText().toString()));
                    } catch (NumberFormatException e) {
                        log.v("输入框未输入任何内容");
                    }
                } else if (period.equals(PERIOD_WEEKLY)) {
                    periodArray.clear();
                    for (int i = 0; i < weekAdapter.getList().size(); i++) {
                        if (weekAdapter.getList().get(i).isSelected()) {
                            periodArray.add(i + 1);
                        }
                    }
                } else if (period.equals(PERIOD_MONTHLY)) {
                    periodArray.clear();
                    List<Month> months = monthAdapter.getList();
                    for (int i = 0; i < months.size(); i++) {
                        if (months.get(i).isSelected()) {
                            periodArray.add(Integer.valueOf(months.get(i).getDay()));
                        }
                    }
                }
            } else {
                period = PERIOD_NULL;
            }
            Collections.sort(periodArray);
            periodBundle.putString(BUNDLE_PERIOD, period);
            periodBundle.putIntegerArrayList(BUNDLE_ARRAY, (ArrayList<Integer>) periodArray);
            intent.putExtras(periodBundle);
            setResult(RESULT, intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
