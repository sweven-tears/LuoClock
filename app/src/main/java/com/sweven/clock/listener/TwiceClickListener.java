package com.sweven.clock.listener;

import android.view.View;

/**
 * Created by Sweven on 2018/10/12.
 * Email:sweventears@Foxmail.com
 */
public abstract class TwiceClickListener implements View.OnClickListener {

    private long firstTime;

    @Override
    public void onClick(View view) {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 500) {
            firstTime = secondTime;
        } else {
            onTwiceClick(view);
        }
    }

    public void onTwiceClick(View view) {

    }
}
