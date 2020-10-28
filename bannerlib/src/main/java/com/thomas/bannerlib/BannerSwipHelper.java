package com.thomas.bannerlib;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;


import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Author：yangzhikuan
 * Date：2020-10-27
 * Description:BannerSwipHelper
 */
public class BannerSwipHelper {
    private BannerRecyclerView mRecyclerView;
    private Context mContext;

    private float mScale = BannerConfig.SCALE; // 两边视图scale
    private int mPagePadding = BannerConfig.PAGEPADDING; // 卡片的padding, 卡片间的距离等于2倍的mPagePadding
    private int mShowLeftCardWidth = BannerConfig.CARDMARGINSTART;   // 左边卡片显示大小

    private int mCardWidth; // 卡片宽度
    private int mOnePageWidth; // 滑动一页的距离
    private int mCardGalleryWidth;

    private int mFirstItemPos;
    private int mCurrentItemOffset;

    private CardLinearSnapHelper mLinearSnapHelper = new CardLinearSnapHelper();
    private int mLastPos;

    public void attachToRecyclerView(final BannerRecyclerView mRecyclerView) {
        if (mRecyclerView == null) {
            return;
        }
        this.mRecyclerView = mRecyclerView;

        mContext = mRecyclerView.getContext();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Log.e("TAG", "RecyclerView.OnScrollListener onScrollStateChanged");
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mLinearSnapHelper.mNoNeedToScroll = getCurrentItem() == 0 ||
                            getCurrentItem() == mRecyclerView.getAdapter().getItemCount() - 2;
                    if (mLinearSnapHelper.finalSnapDistance[0] == 0
                            && mLinearSnapHelper.finalSnapDistance[1] == 0) {
                        mCurrentItemOffset = 0;
                        mLastPos = getCurrentItem();
                        //认为是一次滑动停止 这里可以写滑动停止回调
                        mRecyclerView.dispatchOnPageSelected(mLastPos);
                        Log.e("TAG", "滑动停止后最终位置为" + getCurrentItem());
                    }
                } else {
                    mLinearSnapHelper.mNoNeedToScroll = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                Log.e("TAG", String.format("onScrolled dx=%s, dy=%s", dx, dy));
                super.onScrolled(recyclerView, dx, dy);
                // dx>0则表示右滑, dx<0表示左滑, dy<0表示上滑, dy>0表示下滑
                mCurrentItemOffset += dx;
                Log.e("TAG", String.format("onScrolled dx=%s, dy=%s", mCurrentItemOffset, mOnePageWidth));
                onScrolledChangedCallback();
            }
        });
        initWidth();
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 初始化卡片宽度
     */
    private void initWidth() {
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mCardGalleryWidth = mRecyclerView.getWidth();
                mCardWidth = mCardGalleryWidth - 2 * (mPagePadding + mShowLeftCardWidth);
                mOnePageWidth = mCardWidth;
                scrollToPosition(mFirstItemPos);
            }
        });
    }

    public void setCurrentItem(int item) {
        setCurrentItem(item, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        if (mRecyclerView == null) {
            return;
        }
        if (smoothScroll) {
            mRecyclerView.smoothScrollToPosition(item);
        } else {
            scrollToPosition(item);
        }
    }

    public void scrollToPosition(int pos) {
        if (mRecyclerView == null) {
            return;
        }
        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).
                scrollToPositionWithOffset(pos,
                        mPagePadding + mShowLeftCardWidth);
        mCurrentItemOffset = 0;
        mLastPos = pos;
        //认为是一次滑动停止 这里可以写滑动停止回调
        mRecyclerView.dispatchOnPageSelected(mLastPos);
        //onScrolledChangedCallback();
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                onScrolledChangedCallback();
            }
        });
    }

    public void setFirstItemPos(int firstItemPos) {
        this.mFirstItemPos = firstItemPos;
    }


    /**
     * RecyclerView位移事件监听, view大小随位移事件变化
     */
    private void onScrolledChangedCallback() {
        if (mOnePageWidth == 0) {
            return;
        }
        int currentItemPos = getCurrentItem();
        int offset = mCurrentItemOffset - (currentItemPos - mLastPos) * mOnePageWidth;
        float percent = (float) Math.max(Math.abs(offset) * 1.0 / mOnePageWidth, 0.0001);

//        Log.e("TAG", String.format("offset=%s, percent=%s", offset, percent));
        View leftView = null;
        View currentView;
        View rightView = null;
        if (currentItemPos > 0) {
            leftView = mRecyclerView.getLayoutManager().findViewByPosition(currentItemPos - 1);
        }
        currentView = mRecyclerView.getLayoutManager().findViewByPosition(currentItemPos);
        if (currentItemPos < mRecyclerView.getAdapter().getItemCount() - 1) {
            rightView = mRecyclerView.getLayoutManager().findViewByPosition(currentItemPos + 1);
        }

        if (leftView != null) {
            // y = (1 - mScale)x + mScale
            leftView.setScaleY((1 - mScale) * percent + mScale);
        }
        if (currentView != null) {
            // y = (mScale - 1)x + 1
            currentView.setScaleY((mScale - 1) * percent + 1);
        }
        if (rightView != null) {
            // y = (1 - mScale)x + mScale
            rightView.setScaleY((1 - mScale) * percent + mScale);
        }
    }

    public int getCurrentItem() {
        //return ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition() - 1;
        return mRecyclerView.getLayoutManager().getPosition(mLinearSnapHelper.findSnapView(mRecyclerView.getLayoutManager()));
    }

    public void setScale(float scale) {
        mScale = scale;
    }

    public void setPagePadding(int pagePadding) {
        mPagePadding = pagePadding;
    }

    public void setShowLeftCardWidth(int showLeftCardWidth) {
        mShowLeftCardWidth = showLeftCardWidth;
    }


}
