package com.sweven.clock.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sweven.clock.R;
import com.sweven.clock.activity.FormulateActivity;
import com.sweven.clock.entity.App;
import com.sweven.clock.utils.DrawableUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sweven on 2018/9/15.
 * Email:sweventears@Foxmail.com
 */
public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ListViewHolder> {

    private Context context;
    private List<App> list;
    private LayoutInflater inflater;

    public AppListAdapter(Context context, List<App> appData) {
        this.context = context;
        this.list = appData;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_app_list, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        App app = list.get(position);
        holder.appName.setText(app.getName());
        holder.appIcon.setImageDrawable(app.getIcon());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ConstraintLayout layout;
        private TextView appName;
        private ImageView appIcon;
        private CheckBox checkBox;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);

            appName = itemView.findViewById(R.id.name);
            appIcon = itemView.findViewById(R.id.appIcon);
            checkBox = itemView.findViewById(R.id.checkBox);

            checkBox.setVisibility(View.INVISIBLE);

            appName.setTextColor(Color.BLACK);
            appName.setFontFeatureSettings("仿宋");

            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout: {
                    Intent intent = new Intent(context, FormulateActivity.class);
                    List<Map<String, Object>> data = new ArrayList<>();
                    Map<String, Object> item = new HashMap<>();
                    int position = getAdapterPosition();
                    App app = list.get(position);
                    item.put("appName", app.getName());
                    item.put("appPackageName", app.getPackageName());
                    data.add(item);
                    intent.putExtra("appName", app.getName());
                    intent.putExtra("packageName", app.getPackageName());
                    intent.putExtra("icon", DrawableUtil.drawableToByte(app.getIcon()));
                    context.startActivity(intent);
                }
                break;
                default:
                    break;
            }
        }
    }

}
