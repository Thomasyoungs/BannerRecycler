package com.thomas.bannerlib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
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
        init(context);
    }

    public BannerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BannerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    public void init(Context context) {
        mLoopTask = new AutoLoopTask(this);
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
    private BannerSwipHelper mBannerSwiper;

    public void setAdapterHelper(BannerAdapterHelper bannerAdaperHelper) {
        mBannerAdaperHelper = bannerAdaperHelper;
    }

    public int getRealCount() {
        if (mBannerAdaperHelper == null)
            return 0;
        return mBannerAdaperHelper.getRealCount();
    }

    public void setSwiperHelper(BannerSwipHelper bannerSwipHelper) {
        this.mBannerSwiper = bannerSwipHelper;
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

    public int getCurrentItem() {
        if (mBannerSwiper == null) {
            return NO_POSITION;
        }
        return mBannerSwiper.getCurrentItem();
    }

    static class AutoLoopTask implements Runnable {
        private final WeakReference<BannerRecyclerView> reference;

        AutoLoopTask(BannerRecyclerView banner) {
            this.reference = new WeakReference<>(banner);
        }

        @Override
        public void run() {
            BannerRecyclerView banner = reference.get();
            if (banner != null) {
                int count = banner.getRealCount();
                if (count == 0) {
                    return;
                }
                if (banner.getCurrentItem() != RecyclerView.NO_POSITION) {
                    int next = banner.getCurrentItem() + 1;
                    banner.setCurrentItem(next, true);
                    banner.postDelayed(banner.mLoopTask, banner.mLoopTime);
                }
            }
        }
    }

    private AutoLoopTask mLoopTask;
    private long mLoopTime = BannerConfig.LOOP_TIME;
    private boolean mIsAutoLoop = true;


    /**
     * 开始轮播
     */
    public BannerRecyclerView start() {
        if (mIsAutoLoop) {
            stop();
            postDelayed(mLoopTask, mLoopTime);
        }
        return this;
    }

    /**
     * 停止轮播
     */
    public BannerRecyclerView stop() {
        if (mIsAutoLoop) {
            removeCallbacks(mLoopTask);
        }
        return this;
    }

    /**
     * 是否允许自动轮播
     *
     * @param isAutoLoop ture 允许，false 不允许
     */
    public BannerRecyclerView isAutoLoop(boolean isAutoLoop) {
        this.mIsAutoLoop = isAutoLoop;
        return this;
    }


    /**
     * 设置轮播间隔时间
     *
     * @param loopTime 时间（毫秒）
     */
    public BannerRecyclerView setLoopTime(long loopTime) {
        this.mLoopTime = loopTime;
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_OUTSIDE) {
            start();
        } else if (action == MotionEvent.ACTION_DOWN) {
            stop();
        }
        return super.dispatchTouchEvent(ev);
    }
}