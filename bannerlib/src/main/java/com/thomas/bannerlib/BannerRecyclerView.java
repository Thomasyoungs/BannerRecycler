package com.thomas.bannerlib;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.thomas.bannerlib.BannerConfig.FLING_MAX_VELOCITY;
import static com.thomas.bannerlib.BannerConfig.EnableLimitVelocity;

/**
 * Author：yangzhikuan
 * Date：2020-10-27
 * Description:BannerRecyclerView
 */
public class BannerRecyclerView extends RecyclerView {

    private List<OnPageChangeListener> mOnPageChangeListeners;
    private OnPageChangeListener mOnPageChangeListener;

    public BannerRecyclerView(Context context) {
        super(context);
    }

    public BannerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void init() {

    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        if (EnableLimitVelocity) {
            velocityX = solveVelocity(velocityX);
            velocityY = solveVelocity(velocityY);
        }
        return super.fling(velocityX, velocityY);
    }

    private int solveVelocity(int velocity) {
        if (velocity > 0) {
            return Math.min(velocity, FLING_MAX_VELOCITY);
        } else {
            return Math.max(velocity, -FLING_MAX_VELOCITY);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (mOnPageChangeListeners == null) {
            mOnPageChangeListeners = new ArrayList<>();
        }
        mOnPageChangeListeners.add(listener);
    }

    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        if (mOnPageChangeListeners != null) {
            mOnPageChangeListeners.remove(listener);
        }
    }

    public void clearOnPageChangeListeners() {
        if (mOnPageChangeListeners != null) {
            mOnPageChangeListeners.clear();
        }
    }

    private BannerAdapterHelper mBannerAdaperHelper;

    public void setAdapterHelper(BannerAdapterHelper bannerAdaperHelper) {
        mBannerAdaperHelper = bannerAdaperHelper;
    }

    public int getRealCount() {
        if (mBannerAdaperHelper == null)
            return 0;
//        return mBannerAdaperHelper.getRealCount();
        return 3;
    }

    public interface OnPageChangeListener {
        void onPageSelected(int position);
    }

    public void dispatchOnPageSelected(int position) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
        if (mOnPageChangeListeners != null) {
            for (int i = 0, z = mOnPageChangeListeners.size(); i < z; i++) {
                OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                if (listener != null) {
                    listener.onPageSelected(position);
                }
            }
        }

    }

    public void setCurrentItem(int item) {
        setCurrentItem(item, false);
    }

    /**
     * @param item
     * @param smoothScroll
     */
    public void setCurrentItem(int item, boolean smoothScroll) {

        if (smoothScroll) {
            smoothScrollToPosition(item);
        } else {
            scrollToPosition(item);
        }
    }
}