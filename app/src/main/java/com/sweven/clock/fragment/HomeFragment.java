package com.sweven.clock.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweven.clock.R;
import com.sweven.clock.base.BaseFragment;
import com.sweven.util.LogUtil;

/**
 * Created by Sweven on 2019/7/2.
 * Email:sweventears@Foxmail.com
 */
public class HomeFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        new LogUtil(TAG).d("onCreateView");
        return inflater.inflate(R.layout.panel_home, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        new LogUtil(TAG).d("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        bindView();
        initData();
    }
}
