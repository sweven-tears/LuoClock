package com.sweven.clock.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sweven.clock.R;
import com.sweven.clock.base.BaseAdapter;
import com.sweven.clock.entity.Month;

import java.util.List;

/**
 * Created by Sweven on 2019/7/2.
 * Email:sweventears@Foxmail.com
 */
public class MonthAdapter extends BaseAdapter<Month> {

    public MonthAdapter(Activity activity, List<Month> list) {
        super(activity, list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.item_list_month, viewGroup, false);
        return new MonthViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MonthViewHold hold = (MonthViewHold) viewHolder;
        Month month = list.get(position);
        hold.text.setText(month.getDay());
        hold.text.setTextColor(month.isSelected() ? Color.BLUE : Color.BLACK);
    }

    public void clearSelected() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    public class MonthViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView text;

        public MonthViewHold(@NonNull View view) {
            super(view);
            text = view.findViewById(R.id.text);
            text.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (!list.get(getAdapterPosition()).isTop()) {
                boolean isSelect = !list.get(getAdapterPosition()).isSelected();
                list.get(getAdapterPosition()).setSelected(isSelect);
                text.setTextColor(isSelect ? Color.BLUE : Color.BLACK);
            }
        }
    }
}
