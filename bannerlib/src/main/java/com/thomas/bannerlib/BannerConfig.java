package com.thomas.bannerlib;

import android.content.res.Resources;

/**
 * Author：yangzhikuan
 * Date：2020-10-27
 * Description:BannerConfig
 */
public class BannerConfig {
    public static final float DP = Resources.getSystem().getDisplayMetrics().density;

    public static final float SCALE = 0.9f;
    public static int PAGEPADDING = (int) (DP * 0);
    public static int CARDMARGINSTART = (int) (DP * 0);

    public static final int FLING_MAX_VELOCITY = 8000; // 最大滑动速度
    public static boolean EnableLimitVelocity = true; // 最大滑动速度

    public static final int mIndicatorHeight = (int) (DP * 16);
    public static final float mIndicatorStrokeWidth = DP * 2;
    public static final int mINdicatorRadius = (int) (DP * 4);
    public static final float mIndicatorItemLength = DP * 16;
    public static final float mIndicatorItemPadding = DP * 10;
    public static final int CIRCLE = 0;
    public static final int RECT = 1;
    public static final int mIndicatorType = CIRCLE;

}
