/*
 * Copyright (C) 2012 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.common.view.pagerindicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.content.ContextCompat;
import com.android.common.R;
import com.android.common.view.CustomViewPager;

/**
 * 不选中圆形，选中的线形
 */
public class CircleLinePageIndicator extends View implements PageIndicator {
    private static final int INVALID_POINTER = -1;

    private final Paint mPaintUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPaintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
    private CustomViewPager mViewPager;
    private CustomViewPager.OnPageChangeListener mListener;
    private int mCurrentPage;
    private boolean mCentered;
    private float mLineWidth; // 选中的线宽
    private float mGapWidth, mRadius;

    private int mTouchSlop;
    private float mLastMotionX = -1;
    private int mActivePointerId = INVALID_POINTER;
    private boolean mIsDragging;

    public CircleLinePageIndicator(Context context) {
        this(context, null);
    }

    public CircleLinePageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLinePageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;

        //Load defaults from resources
        final Resources res = getResources();
        final int defaultSelectedColor = ContextCompat.getColor(context, R.color.default_circle_line_indicator_selected_color);
        final int defaultUnselectedColor = ContextCompat.getColor(context, R.color.default_circle_line_indicator_unselected_color);
        final float defaultLineWidth = res.getDimension(R.dimen.dp_4);
        final float defaultGapWidth = res.getDimension(R.dimen.dp_4);
        final float defaultRadius = res.getDimension(R.dimen.dp_4);
        final boolean defaultCentered = res.getBoolean(R.bool.default_circle_line_indicator_centered);

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleLinePageIndicator, defStyle, 0);

        mCentered = a.getBoolean(R.styleable.CircleLinePageIndicator_centered, defaultCentered);
        mLineWidth = a.getDimension(R.styleable.CircleLinePageIndicator_lineWidth, defaultLineWidth);
        mGapWidth = a.getDimension(R.styleable.CircleLinePageIndicator_gapWidth, defaultGapWidth);
        mRadius = a.getDimension(R.styleable.CircleLinePageIndicator_unSelectRadius, defaultRadius);
        mPaintUnselected.setColor(a.getColor(R.styleable.CircleLinePageIndicator_unselectedColor, defaultUnselectedColor));
        mPaintUnselected.setStyle(Paint.Style.FILL);
        mPaintSelected.setColor(a.getColor(R.styleable.CircleLinePageIndicator_selectedColor, defaultSelectedColor));
        mPaintSelected.setStrokeCap(Paint.Cap.ROUND);

        Drawable background = a.getDrawable(R.styleable.CircleLinePageIndicator_android_background);
        if (background != null) {
            //noinspection deprecation
            setBackgroundDrawable(background);
        }

        a.recycle();

        mTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }


    public void setCentered(boolean centered) {
        mCentered = centered;
        invalidate();
    }

    public boolean isCentered() {
        return mCentered;
    }

    public void setUnselectedColor(int unselectedColor) {
        mPaintUnselected.setColor(unselectedColor);
        invalidate();
    }

    public int getUnselectedColor() {
        return mPaintUnselected.getColor();
    }

    public void setSelectedColor(int selectedColor) {
        mPaintSelected.setColor(selectedColor);
        invalidate();
    }

    public int getSelectedColor() {
        return mPaintSelected.getColor();
    }

    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
        invalidate();
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public void setGapWidth(float gapWidth) {
        mGapWidth = gapWidth;
        invalidate();
    }

    public float getGapWidth() {
        return mGapWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mViewPager == null || mViewPager.getAdapter() == null) {
            return;
        }
        final int count = mViewPager.getAdapter().getCount();
        if (count == 0) {
            return;
        }

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        final float widthAndGap = mRadius * 2 + mGapWidth;
        final float indicatorWidth = (count * widthAndGap) + mLineWidth - mGapWidth;
        final float paddingTop = getPaddingTop();
        final float paddingLeft = getPaddingLeft();
        final float paddingRight = getPaddingRight();

        float verticalOffset = paddingTop + ((getHeight() - paddingTop - getPaddingBottom()) / 2.0f);
        float horizontalOffset = paddingLeft;
        if (mCentered) {
            horizontalOffset += ((getWidth() - paddingLeft - paddingRight) / 2.0f) - (indicatorWidth / 2.0f);
        }

        mPaintSelected.setStrokeWidth(mRadius * 2);
        //Draw
        for (int i = 0; i < count; i++) {
            float dX = horizontalOffset + (i * widthAndGap) + (i > mCurrentPage ? mLineWidth : 0) + mRadius;
            if (i == mCurrentPage) {
                // 画线
                float dX2 = dX + mLineWidth;
                canvas.drawLine(dX, verticalOffset, dX2, verticalOffset, mPaintSelected);
            } else {
                // 画圆
                canvas.drawCircle(dX, verticalOffset, mRadius, mPaintUnselected);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (super.onTouchEvent(ev)) {
            return true;
        }
        if ((mViewPager == null) || (mViewPager.getAdapter() == null) || (mViewPager.getAdapter().getCount() == 0)) {
            return false;
        }

        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mLastMotionX = ev.getX();
                break;

            case MotionEvent.ACTION_MOVE: {
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(activePointerIndex);
                final float deltaX = x - mLastMotionX;

                if (!mIsDragging) {
                    if (Math.abs(deltaX) > mTouchSlop) {
                        mIsDragging = true;
                    }
                }

                if (mIsDragging) {
                    mLastMotionX = x;
                    if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()) {
                        mViewPager.fakeDragBy(deltaX);
                    }
                }

                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!mIsDragging) {
                    final int count = mViewPager.getAdapter().getCount();
                    final int width = getWidth();
                    final float halfWidth = width / 2f;
                    final float sixthWidth = width / 6f;

                    if ((mCurrentPage > 0) && (ev.getX() < halfWidth - sixthWidth)) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager.setCurrentItem(mCurrentPage - 1);
                        }
                        return true;
                    } else if ((mCurrentPage < count - 1) && (ev.getX() > halfWidth + sixthWidth)) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager.setCurrentItem(mCurrentPage + 1);
                        }
                        return true;
                    }
                }

                mIsDragging = false;
                mActivePointerId = INVALID_POINTER;
                if (mViewPager.isFakeDragging()) mViewPager.endFakeDrag();
                break;

            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                mLastMotionX = ev.getX(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                final int pointerIndex = ev.getActionIndex();
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                mLastMotionX = ev.getX(ev.findPointerIndex(mActivePointerId));
                break;
        }

        return true;
    }

    @Override
    public void setViewPager(CustomViewPager viewPager) {
        if (mViewPager == viewPager) {
            return;
        }
        if (mViewPager != null) {
            //Clear us from the old pager.
            mViewPager.removeOnPageChangeListener(this);
        }
        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        invalidate();
    }

    @Override
    public void setViewPager(CustomViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        invalidate();
    }

    @Override
    public void notifyDataSetChanged() {
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPage = position;
        invalidate();

        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void setOnPageChangeListener(CustomViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        float result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
            //We were told how big to be
            result = specSize;
        } else {
            //Calculate the width according the views count
            final int count = mViewPager.getAdapter() == null ? 0 : mViewPager.getAdapter().getCount();
            result = getPaddingLeft() + getPaddingRight() + (count * mRadius * 2) +
                    ((count - 1) * mGapWidth) + mLineWidth;
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return (int) Math.ceil(result);
    }

    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        float result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize;
        } else {
            //Measure the height
            result = (int) (2 * mRadius) + getPaddingTop() + getPaddingBottom();
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return (int) Math.ceil(result);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPage = savedState.currentPage;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mCurrentPage;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }
    }
}