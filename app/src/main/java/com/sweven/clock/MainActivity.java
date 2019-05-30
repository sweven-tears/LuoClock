package com.sweven.clock;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.sweven.clock.adapter.AppAdapter;
import com.sweven.clock.base.BaseActivity;
import com.sweven.clock.entity.App;
import com.sweven.clock.utils.AppUtil;
import com.sweven.util.ToastUtil;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    /**
     * 用于三个页面的切换
     */
    private final static int HOME = 1, LIST = 2, SETTING = 3;
    /**
     * 页面加载参数，[0：未加载；1：已加载]
     */
    private static final int NO_LOAD = 0, LOADED = 1;
    /**
     * 设置默认页面
     */
    private static int presentPanel = HOME;
    /**
     * 用于判断双击的时间戳
     */
    private static long firstExitTime = 0;
    private static long firstHomeTime = 0;
    private static long firstListTime = 0;
    private static long firstSettingTime = 0;
    private RecyclerView recyclerView;
    private ArrayList<App> appList;
    /**
     * 三个页面的布局
     */
    private RelativeLayout layoutHome, layoutList, layoutSetting;
    private int homeState = NO_LOAD, listState = NO_LOAD, settingSate = NO_LOAD;
    /**
     * [页面切换时的操作]
     */
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int state = (int) msg.obj;
            presentPanel = msg.what;
            switch (presentPanel) {
                case HOME:
                    if (state == NO_LOAD) {
                        setHomeData();
                    }
                    cutPanel(HOME);
                    break;
                case LIST:
                    if (state == NO_LOAD) {
                        setListData();
                    }
                    cutPanel(LIST);
                    break;
                case SETTING:
                    if (state == NO_LOAD) {
                        setSettingData();
                    }
                    cutPanel(SETTING);
                    break;
            }
        }
    };
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        initData();
    }

    @Override
    protected void initData() {

        AppUtil appUtil = new AppUtil(activity);
        appList = new ArrayList<>();
        appList = appUtil.getAllApp();

        layoutHome = findViewById(R.id.id_home);
        layoutList = findViewById(R.id.id_list);
        layoutSetting = findViewById(R.id.id_setting);

        recyclerView = findViewById(R.id.app_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        // 设置页面
        cutPanel(presentPanel);

        switch (presentPanel) {
            case HOME:
                setHomeData();
                break;
            case LIST:
                setListData();
                break;
            case SETTING:
                setSettingData();
                break;
            default:
                break;
        }
    }

    /**
     * 为home页面设置数据
     */
    private void setHomeData() {
        homeState = LOADED;
    }

    /**
     * 为list页面设置数据
     */
    private void setListData() {
        AppAdapter appAdapter = new AppAdapter(activity, appList);
        recyclerView.setAdapter(appAdapter);
        listState = LOADED;
    }

    /**
     * 为Setting页面设置数据
     */
    private void setSettingData() {
        settingSate = LOADED;
    }

    /**
     * [页面切换及加载]
     *
     * @param panelID 切换的页面编号
     */
    private void cutPanel(int panelID) {
        layoutHome.setVisibility(View.INVISIBLE);
        layoutList.setVisibility(View.INVISIBLE);
        layoutSetting.setVisibility(View.INVISIBLE);
        if (panelID == 1) {
            setActionBarTitle(R.string.app_name);
            layoutHome.setVisibility(View.VISIBLE);
        } else if (panelID == 2) {
            setActionBarTitle(R.string.app_list);
            layoutList.setVisibility(View.VISIBLE);
        } else {
            setActionBarTitle(R.string.title_setting);
            layoutSetting.setVisibility(View.VISIBLE);
        }
    }

    /**
     * [双击退出设置]
     *
     * @param keyCode .
     * @param event   .
     * @return .
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstExitTime > 2000) {
                firstExitTime = secondTime;
                ToastUtil.showShort(activity, "再按一次退出");
            } else {
                presentPanel = HOME;
                finish();
            }
        }
        return false;
    }

    /**
     * [三个页面切换]
     *
     * @param item .
     * @return .
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Message message = handler.obtainMessage();
        switch (item.getItemId()) {
            case R.id.navigation_home:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstHomeTime > 500) {
                    firstHomeTime = secondTime;
                } else {
                    homeState = NO_LOAD;
                }
                message.what = HOME;
                message.obj = homeState;
                handler.sendMessage(message);
                return true;
            case R.id.navigation_list:
                secondTime = System.currentTimeMillis();
                if (secondTime - firstListTime > 500) {
                    firstListTime = secondTime;
                } else {
                    listState = NO_LOAD;
                }
                message.what = LIST;
                message.obj = listState;
                handler.sendMessage(message);
                return true;
            case R.id.navigation_setting:
                secondTime = System.currentTimeMillis();
                if (secondTime - firstSettingTime > 500) {
                    firstSettingTime = secondTime;
                } else {
                    settingSate = NO_LOAD;
                }
                message.what = SETTING;
                message.obj = settingSate;
                handler.sendMessage(message);
                return true;
        }
        return false;
    }

    /**
     * 应用重启时修复显示页面
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (navigation.getSelectedItemId() == R.id.navigation_home) {
            presentPanel = HOME;
            navigation.setSelectedItemId(R.id.navigation_home);
        } else if (navigation.getSelectedItemId() == R.id.navigation_list) {
            presentPanel = LIST;
            navigation.setSelectedItemId(R.id.navigation_list);
        } else if (navigation.getSelectedItemId() == R.id.navigation_setting) {
            presentPanel = SETTING;
            navigation.setSelectedItemId(R.id.navigation_setting);
        }
    }
}
