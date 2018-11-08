package com.sweven.clock.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sweven.clock.FormulateActivity;
import com.sweven.clock.R;
import com.sweven.clock.entity.App;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sweven on 2018/9/15.
 * Email:sweventears@Foxmail.com
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ListViewHolder> {

    private Context context;
    private ArrayList<App> apps;
    private LayoutInflater inflater;

    public AppAdapter(Context context, ArrayList<App> appData) {
        this.context=context;
        this.apps =appData;
        this.inflater= LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=inflater.inflate(R.layout.item_app_list,viewGroup,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        App app = apps.get(position);
        holder.appName.setText(app.getName());
        holder.appIcon.setImageDrawable(app.getIcon());
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ConstraintLayout layout;
        private TextView appName;
        private ImageView appIcon;
        private CheckBox checkBox;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            layout =itemView.findViewById(R.id.layout);

            appName=itemView.findViewById(R.id.name);
            appIcon=itemView.findViewById(R.id.appIcon);
            checkBox=itemView.findViewById(R.id.checkBox);

            checkBox.setVisibility(View.INVISIBLE);

            appName.setTextColor(Color.BLACK);
            appName.setFontFeatureSettings("仿宋");

            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.layout:{
                    Intent intent=new Intent(context, FormulateActivity.class);
                    List<Map<String,Object>> data=new ArrayList<>();
                    Map<String,Object> item=new HashMap<>();
                    int position=getAdapterPosition();
                    App app = apps.get(position);
                    item.put("appName", app.getName());
                    item.put("appPackageName", app.getPackageName());
                    data.add(item);
                    intent.putExtra("appName", app.getName());
                    intent.putExtra("packageName", app.getPackageName());
                    intent.putExtra("icon", drawableToByte(app.getIcon()));
                    context.startActivity(intent);
                }
                break;
                default:break;
            }
        }
    }

    // ------------------------将drawable 图像转化成二进制字节----------------
    public  synchronized  byte[] drawableToByte(Drawable drawable) {

        if (drawable != null) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            int size = bitmap.getWidth() * bitmap.getHeight() * 4;
            // 创建一个字节数组输出流,流的大小为size
            ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            // 将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();
            return imagedata;
        }
        return null;
    }

    public static synchronized Drawable byteToDrawable(byte[] img) {
        Bitmap bitmap;
        if (img != null) {


            bitmap = BitmapFactory.decodeByteArray(img,0, img.length);
            Drawable drawable = new BitmapDrawable(bitmap);

            return drawable;
        }
        return null;

    }

}
