package com.sweven.clock.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.sweven.clock.R;
import com.sweven.clock.base.BaseAdapter;
import com.sweven.clock.entity.Week;

import java.util.List;

/**
 * Created by Sweven on 2019/7/2.
 * Email:sweventears@Foxmail.com
 */
public class WeekAdapter extends BaseAdapter<Week> {

    public WeekAdapter(Activity activity, List<Week> list) {
        super(activity, list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.item_list_week, viewGroup, false);
        return new WeekViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        WeekViewHold hold = (WeekViewHold) viewHolder;
        Week week = list.get(position);
        hold.checkBox.setText(week.getWeek());
        hold.checkBox.setChecked(week.isSelected());
    }

    public void clearSelected() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    public class WeekViewHold extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private CheckBox checkBox;

        public WeekViewHold(@NonNull View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkBox);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            list.get(getAdapterPosition()).setSelected(isChecked);
        }
    }
}
