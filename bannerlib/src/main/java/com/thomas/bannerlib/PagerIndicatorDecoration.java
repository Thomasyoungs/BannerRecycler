/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 David Medenjak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.thomas.bannerlib;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.LocaleList;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.thomas.bannerlib.BannerConfig.mINdicatorRadius;
import static com.thomas.bannerlib.BannerConfig.mIndicatorHeight;
import static com.thomas.bannerlib.BannerConfig.mIndicatorItemLength;
import static com.thomas.bannerlib.BannerConfig.mIndicatorItemPadding;
import static com.thomas.bannerlib.BannerConfig.mIndicatorStrokeWidth;

/**
 * Author：yangzhikuan
 * Date：2020-10-27
 * Description:PagerIndicatorDecoration
 */
public class PagerIndicatorDecoration extends RecyclerView.ItemDecoration {

    private int colorActive = 0xFF000000;
    private int colorInactive = 0x66FF00FF;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private final Paint mPaint = new Paint();
    private int itemCount;

    public PagerIndicatorDecoration(int itemCount) {

        this.itemCount = itemCount;
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mIndicatorStrokeWidth);
        mPaint.setAntiAlias(true);
    }

    public PagerIndicatorDecoration(int itemCount, int colorActive, int colorInactive) {
        this.colorActive = colorActive;
        this.colorInactive = colorInactive;
        this.itemCount = itemCount;
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mIndicatorStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        // 通过recyclerview当前的position计算当前高亮指示器的位置显示
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int activePosition = layoutManager.findFirstVisibleItemPosition();
        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }
        if (BannerConfig.CIRCLE == BannerConfig.mIndicatorType) {
            mPaint.setStyle(Paint.Style.FILL);
            // 计算指示器总长度 并保证居中显示
            float totalLength = mINdicatorRadius * 2 * itemCount;
            float paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding;
            float indicatorTotalWidth = totalLength + paddingBetweenItems;
            float centerstartX = (parent.getWidth() - indicatorTotalWidth) / 2F + mINdicatorRadius;
            float centerstartY = (parent.getHeight() - mINdicatorRadius * 2F) / 2F + mINdicatorRadius;

            drawInactiveCircleIndicators(c, centerstartX, centerstartY, itemCount);

            final View activeChild = layoutManager.findViewByPosition(activePosition);
            int left = activeChild.getLeft();
            int width = activeChild.getWidth();
            //计算当前进度
            float progress = mInterpolator.getInterpolation(left * -1 / (float) width);
            drawCircleHighlights(c, centerstartX, centerstartY, activePosition, progress, itemCount);

        } else {
            mPaint.setStyle(Paint.Style.STROKE);
            // 计算指示器总长度 并保证居中显示

            float totalLength = mIndicatorItemLength * itemCount;
            float paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding;
            float indicatorTotalWidth = totalLength + paddingBetweenItems;
            float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;
            float indicatorPosY = parent.getHeight() - mIndicatorHeight / 2F;

            drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount);

            final View activeChild = layoutManager.findViewByPosition(activePosition);
            int left = activeChild.getLeft();
            int width = activeChild.getWidth();
            //计算当前进度
            float progress = mInterpolator.getInterpolation(left * -1 / (float) width);
            drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount);
        }
    }

    /**
     * 画固定指示器
     *
     * @param canvas
     * @param centerX
     * @param centerY
     * @param itemCount
     */
    private void drawInactiveCircleIndicators(Canvas canvas, float centerX, float centerY, int itemCount) {
        mPaint.setColor(colorInactive);

        // 指示器的宽度包含左右padding
        final float itemWidth = mINdicatorRadius * 2 + mIndicatorItemPadding;

        float start = centerX;
        for (int i = 0; i < itemCount; i++) {
            canvas.drawCircle(start, centerY, mINdicatorRadius, mPaint);
            start += itemWidth;
        }
    }

    /**
     * 画固定指示器
     *
     * @param c
     * @param indicatorStartX
     * @param indicatorPosY
     * @param itemCount
     */
    private void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
        mPaint.setColor(colorInactive);

        // 指示器的宽度包含padding
        final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;
        float start = indicatorStartX;
        for (int i = 0; i < itemCount; i++) {
            Log.i("indicatorStartX", "start   " + start);
            c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint);
            start += itemWidth;
        }
    }

    /**
     * 高亮指示器
     *
     * @param canvas
     * @param indicatorStartX
     * @param indicatorPosY
     * @param highlightPosition
     * @param progress
     * @param itemCount
     */
    private void drawHighlights(Canvas canvas, float indicatorStartX, float indicatorPosY,
                                int highlightPosition, float progress, int itemCount) {
        mPaint.setColor(colorActive);
        highlightPosition %= itemCount;

        // 指示器的宽度包含padding
        final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;
        float highlightStart = indicatorStartX + itemWidth * highlightPosition;
        Log.i("indicatorStartX", "progress  " + progress);
        if (progress == 0F) {
            Log.i("indicatorStartX", "indicatorStartX  " + highlightStart + "  " + highlightPosition);
            canvas.drawLine(highlightStart, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);

        } else {
            // 计算高亮指示器在当前指示条跟随card移动的距离
            float partialLength = mIndicatorItemLength * progress;

            canvas.drawLine(highlightStart + partialLength, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);

            // 计算高亮指示器在下一指示条跟随card移动的距离
            if (highlightPosition < itemCount - 1) {
                highlightStart += itemWidth;
                canvas.drawLine(highlightStart, indicatorPosY,
                        highlightStart + partialLength, indicatorPosY, mPaint);
            }
        }
    }

    /**
     * @param canvas
     * @param centerStartX
     * @param centerStartY
     * @param highlightPosition
     * @param progress
     * @param itemCount
     */
    private void drawCircleHighlights(Canvas canvas, float centerStartX, float centerStartY,
                                      int highlightPosition, float progress, int itemCount) {
        mPaint.setColor(colorActive);
        highlightPosition %= itemCount;

        // 指示器的宽度包含左右padding
        final float itemWidth = mINdicatorRadius * 2 + mIndicatorItemPadding;

        if (progress == 0F) {
            float highlightStart = centerStartX + itemWidth * highlightPosition;
            canvas.drawCircle(highlightStart, centerStartY,
                    mINdicatorRadius, mPaint);

        } else {
            // 计算高亮指示器在下一指示条跟随card移动的距离
            float centerX = 0;
            if (highlightPosition == itemCount - 1 && progress > 0 || highlightPosition == 0 && progress < 0) {
                centerX = centerStartX + itemWidth * highlightPosition;
            } else {
                centerX = centerStartX + itemWidth * highlightPosition + itemWidth * progress;
            }
            // 计算高亮指示器在当前指示条跟随card移动的距离
            canvas.drawCircle(centerX, centerStartY,
                    mINdicatorRadius, mPaint);


        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mIndicatorHeight;
    }
}
