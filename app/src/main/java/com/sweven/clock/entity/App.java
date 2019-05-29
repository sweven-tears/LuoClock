package com.sweven.clock.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Sweven on 2018/9/15.
 * Email:sweventears@Foxmail.com
 */
public class App {
    private Drawable icon;
    private String name;
    private String packageName;
    private String comment;

    public App() {
    }

    public App(Drawable icon, String name, String packageName, String comment) {
        this.icon = icon;
        this.name = name;
        this.packageName = packageName;
        this.comment = comment;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "App{" +
                "icon=" + icon +
                ", name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public String toBinaryString() {
        char[] ch = toString().toCharArray();
        StringBuilder safe = new StringBuilder();
        for (char aCh : ch) {
            safe.append(Integer.toBinaryString(aCh));
        }
        return safe.toString();
    }
}
