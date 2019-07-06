package com.sweven.clock.entity;

/**
 * Created by Sweven on 2019/7/2.
 * Email:sweventears@Foxmail.com
 */
public class Month {
    private String day;
    private boolean selected;
    private boolean top;

    public Month() {
    }

    public Month(String day, boolean selected) {
        this.day = day;
        this.selected = selected;
    }

    public Month(boolean top, String day) {
        this.top = top;
        this.day = day;
    }


    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
