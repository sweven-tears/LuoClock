package com.sweven.clock.info;

import android.graphics.drawable.Drawable;

/**
 * Created by Sweven on 2018/9/15.
 * Email:sweventears@Foxmail.com
 */
public class AppMsg {
    private Drawable icon;
    private String appName;
    private String packageName;
    private String comment;
    private boolean isChecked;

    public AppMsg() {
    }

    public AppMsg(Drawable icon, String appName, String packageName, String comment) {
        this.icon = icon;
        this.appName = appName;
        this.packageName = packageName;
        this.comment = comment;
    }

    public AppMsg(Drawable icon, String appName, String packageName, String comment, boolean isChecked) {
        this.icon = icon;
        this.appName = appName;
        this.packageName = packageName;
        this.comment = comment;
        this.isChecked = isChecked;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
