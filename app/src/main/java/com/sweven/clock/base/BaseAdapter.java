package com.sweven.clock.base;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Sweven on 2019/7/2.
 * Email:sweventears@Foxmail.com
 */
public class BaseAdapter<T> extends RecyclerView.Adapter {

    protected Activity activity;
    protected List<T> list;
    protected LayoutInflater inflater;

    public BaseAdapter(Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    public BaseAdapter(Activity activity, List<T> list) {
        this.activity = activity;
        this.list = list;
        inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
//        xxx hold= (xxx) viewHolder;
//        T t=list.get(position);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public List<T> getList() {
        return list;
    }
}
