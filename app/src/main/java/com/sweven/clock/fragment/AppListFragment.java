package com.sweven.clock.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweven.clock.R;
import com.sweven.clock.adapter.AppListAdapter;
import com.sweven.clock.base.BaseFragment;
import com.sweven.clock.entity.App;
import com.sweven.clock.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sweven on 2019/7/2.
 * Email:sweventears@Foxmail.com
 */
public class AppListFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private AppListAdapter appListAdapter;
    private LinearLayoutManager manager;

    private List<App> list = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.panel_app_list, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindView();
        initData();
    }

    @Override
    protected void bindView() {
        recyclerView = activity.findViewById(R.id.app_list);
    }

    @Override
    protected void initData() {
        list = new AppUtil(context).getAllApp();
        appListAdapter = new AppListAdapter(activity, list);
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(appListAdapter);
    }

    private boolean top = false;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (false) {
                if (top) {
                    recyclerView.setAdapter(appListAdapter);
                    manager.scrollToPositionWithOffset(0, 0);
                } else {
                    top = true;
                    toast.showShort("双击回顶部");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            top = false;
                            cancel();
                        }
                    }, 1000);
                }
            }
        }
    }
}
