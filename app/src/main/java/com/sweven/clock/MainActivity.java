package com.sweven.clock;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.sweven.clock.base.BaseActivity;
import com.sweven.clock.fragment.AppListFragment;
import com.sweven.clock.fragment.HomeFragment;
import com.sweven.clock.fragment.MineFragment;
import com.sweven.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int HOME = 0, LIST = 1, MINE = 2;
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";

    private BottomNavigationView navigation;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private AppListFragment appListFragment = new AppListFragment();
    private MineFragment mineFragment = new MineFragment();

    private int currentIndex = HOME;
    private Fragment currentFragment = homeFragment;
    private List<Fragment> fragments = new ArrayList<>();

    private static boolean exit;

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        initData();
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, HOME);
            fragments.clear();
            fragments.add(fragmentManager.findFragmentByTag(0 + ""));
            fragments.add(fragmentManager.findFragmentByTag(1 + ""));
            fragments.add(fragmentManager.findFragmentByTag(2 + ""));
            resetFragment();
        } else {
            fragments.add(homeFragment);
            fragments.add(appListFragment);
            fragments.add(mineFragment);
        }
    }

    @Override
    protected void bindView() {
        navigation = findViewById(R.id.navigation);
    }

    @Override
    protected void initData() {
        navigation.setOnNavigationItemSelectedListener(this);
    }

    private void resetFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == currentIndex) {
                transaction.show(fragments.get(i));
            } else {
                transaction.hide(fragments.get(i));
            }
        }
        transaction.commit();
        currentFragment = fragments.get(currentIndex);
    }

    private void showFragment() {
        FragmentTransaction transition = fragmentManager.beginTransaction();
        if (fragments.get(currentIndex).isAdded()) {
            transition.hide(currentFragment)
                    .add(R.id.panel, fragments.get(currentIndex), currentIndex + "");
        } else {
            transition.hide(currentFragment)
                    .show(fragments.get(currentIndex));
        }
        currentFragment = fragments.get(currentIndex);
        transition.commit();
    }

    /**
     * [页面切换及加载]
     *
     * @param panelID 切换的页面编号
     */
    private void cutPanel(int panelID) {
        if (panelID == HOME) {
            setActionBarTitle(R.string.app_name);
        } else if (panelID == LIST) {
            setActionBarTitle(R.string.app_list);
        } else if (panelID == MINE) {
            setActionBarTitle(R.string.title_setting);
        }
        try {
            currentIndex = panelID;
            showFragment();
        } catch (Exception e) {
            //TODO 为了测试添加的异常处理 --by 2019.7.11
            e.printStackTrace();
        }
    }

    /**
     * [三个页面切换]
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                cutPanel(HOME);
                break;
            case R.id.navigation_list:
                cutPanel(LIST);
                break;
            case R.id.navigation_mine:
                cutPanel(MINE);
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * [双击退出设置]
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (exit) {
                currentIndex = HOME;
                finish();
            } else {
                exit = true;
                ToastUtil.showShort(activity, "再按一次退出");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        exit = false;
                        cancel();
                    }
                }, 2000);
            }
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
            currentIndex = HOME;
            currentFragment = homeFragment;
            navigation.setSelectedItemId(R.id.navigation_home);
        } else if (navigation.getSelectedItemId() == R.id.navigation_list) {
            currentIndex = LIST;
            currentFragment = appListFragment;
            navigation.setSelectedItemId(R.id.navigation_list);
        } else if (navigation.getSelectedItemId() == R.id.navigation_mine) {
            currentIndex = MINE;
            currentFragment = mineFragment;
            navigation.setSelectedItemId(R.id.navigation_mine);
        }
    }
}
