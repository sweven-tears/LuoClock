package com.sweven.clock.parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sweven on 2018/10/10.
 * Email:sweventears@Foxmail.com
 */
public class Redo {
    public static final String BUNDLE="bundle";
    public static final String BUNDLE_PERIOD = "period";
    public static final String BUNDLE_ARRAY = "array";
    public static final String PERIOD_NULL="一次";
    public static final String PERIOD_DAILY="每天";
    public static final String PERIOD_WEEKLY="每周";
    public static final String PERIOD_MONTHLY="每月";
    public static final String PERIOD_OTHER="其他";
    public static final List<String> PERIOD_KIND;

    static {
        PERIOD_KIND = new ArrayList<>();
        PERIOD_KIND.add(PERIOD_DAILY);
        PERIOD_KIND.add(PERIOD_WEEKLY);
        PERIOD_KIND.add(PERIOD_MONTHLY);
        PERIOD_KIND.add(PERIOD_OTHER);
    }
}
