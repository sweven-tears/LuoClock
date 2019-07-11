package com.sweven.clock.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweven.util.LogUtil;
import com.sweven.util.ToastUtil;

/**
 * Created by Sweven on 2019/7/2.
 * Email:sweventears@Foxmail.com
 */
public class BaseFragment extends Fragment {

    public final String TAG = this.getClass().getSimpleName();

    protected View fragment;
    protected Activity activity;
    protected Context context;

    protected ToastUtil toast;

    @Override
    public void onAttach(Context context) {
        new LogUtil(TAG).d("onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        new LogUtil(TAG).d("onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        new LogUtil(TAG).d("onCreateView");
        // 绑定view inflater.inflate(R.layout.xxx,null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        new LogUtil(TAG).d("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        context = getContext();
        toast = new ToastUtil(activity);
    }

    /**
     * 绑定view中的组件
     */
    protected void bindView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        new LogUtil(TAG).d("Hidden change--" + hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onStart() {
        new LogUtil(TAG).d("onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        new LogUtil(TAG).d("onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        new LogUtil(TAG).d("onDestroy");
        super.onDestroy();
    }
}
