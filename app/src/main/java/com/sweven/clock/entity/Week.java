package com.sweven.clock.entity;

/**
 * Created by Sweven on 2019/7/2.
 * Email:sweventears@Foxmail.com
 */
public class Week {
    private String week;
    private boolean selected;

    public Week() {
    }

    public Week(String week, boolean selected) {
        this.week = week;
        this.selected = selected;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
