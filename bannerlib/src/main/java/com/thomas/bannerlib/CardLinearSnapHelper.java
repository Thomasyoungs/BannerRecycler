package com.thomas.bannerlib;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Author：yangzhikuan
 * Date：2020-10-27
 * Description:BannerSwipHelper
 */
public class CardLinearSnapHelper extends PagerSnapHelper {
    public boolean mNoNeedToScroll = false;
    public int[] finalSnapDistance = {0, 0};

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        if (mNoNeedToScroll) {
            finalSnapDistance[0] = 0;
            finalSnapDistance[1] = 0;
        } else {
            finalSnapDistance = super.calculateDistanceToFinalSnap(layoutManager, targetView);
        }
        return finalSnapDistance;
    }
}