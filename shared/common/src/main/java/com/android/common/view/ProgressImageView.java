package com.android.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import androidx.appcompat.widget.AppCompatImageView;
import com.android.common.R;

/**
 * 动画View
 */
public class ProgressImageView extends AppCompatImageView {
    private int frameCount;
    private Animation mAnimation;
    public ProgressImageView(Context context) {
        this(context, null);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAnimation(attrs);
    }
    private void setAnimation(AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressImageView);
        frameCount = a.getInt(R.styleable.ProgressImageView_frameCount, 8);
        int animRes = a.getResourceId(R.styleable.ProgressImageView_animResource, -1);
        int duration = a.getInt(R.styleable.ProgressImageView_animDuration, 1000);
        a.recycle();

        if (animRes > 0 && frameCount != 0) {
            mAnimation = AnimationUtils.loadAnimation(getContext(), animRes);
            mAnimation.setDuration(duration);
            mAnimation.setInterpolator(new LinearInterpolator() {

                @Override
                public float getInterpolation(float input) {
                    return (float) Math.floor(input * frameCount) / (float)frameCount;
                }
            });
            //startAnimation(mAnimation);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getVisibility() == VISIBLE) {
            startAnimation(mAnimation);
        } else {
            clearAnimation();
            setVisibility(getVisibility());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
    }

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        if(isVisible) {
            startAnimation(mAnimation);
        } else {
            clearAnimation();
        }
        super.onVisibilityAggregated(isVisible);
    }

    @Override
    public void setVisibility(int visibility) {
        if(visibility == View.VISIBLE ) {
            startAnimation(mAnimation);
        } else {
            clearAnimation();
        }
        super.setVisibility(visibility);
    }
}
