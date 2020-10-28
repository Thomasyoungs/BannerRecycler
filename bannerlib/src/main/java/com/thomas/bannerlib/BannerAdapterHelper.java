package com.thomas.bannerlib;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import static com.thomas.bannerlib.BannerConfig.PAGEPADDING;
import static com.thomas.bannerlib.BannerConfig.CARDMARGINSTART;

/**
 * Author：yangzhikuan
 * Date：2020-10-27
 * Description:BannerAdapterHelper
 */
public class BannerAdapterHelper {
    private int realCount;

    public BannerAdapterHelper(int size) {
        realCount = size;
    }

    public void onCreateViewHolder(ViewGroup parent, View itemView) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        lp.width = parent.getWidth() - 2 * (PAGEPADDING + CARDMARGINSTART);
        itemView.setLayoutParams(lp);
    }

    public void onBindViewHolder(View itemView, final int position, int itemCount) {

        itemView.setPadding(PAGEPADDING, 0, PAGEPADDING, 0);
        int leftMarin = position == 0 ? PAGEPADDING + CARDMARGINSTART : 0;
        int rightMarin = position == itemCount - 1 ? PAGEPADDING + CARDMARGINSTART : 0;
        setViewMargin(itemView, leftMarin, 0, rightMarin, 0);
    }

    private void setViewMargin(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            view.setLayoutParams(lp);
        }
    }

    public int getRealCount() {
        return realCount;
    }

}
