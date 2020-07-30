package com.android.common.view;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import com.android.common.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class ExceptionView extends FrameLayout {

    // 显示img
    private View mRoot;
    // 显示img
    private ImageView mIvAbNormal;
    // 显示text
    private TextView mTvAbNormalWarn1;
    // 显示text
    private TextView mTvAbNormalWarn2;
    // 重新刷新
    private TextView mBtnReload;
    private View layout; // bug修改，NestedScrollView子布局设置点击事件才有效


    /* ========================================================
     * abnormal的4类方式
     * */

    public static final int NO_CONNECTIONING = 0;
    public static final int NO_CONTENT = 1;
    public static final int PAGE_ERROR = 2;
    public static final int RELOAD = 3;
    public int mState = NO_CONNECTIONING;
    private OnClickListener listener;

    public ExceptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View mContentView = View.inflate(context, R.layout.layout_abnormal_view, null);
        mRoot = mContentView.findViewById(R.id.root);
        layout = mContentView.findViewById(R.id.abnormal_layout);
        mIvAbNormal = mContentView.findViewById(R.id.iv_abnormal);
        mTvAbNormalWarn1 = mContentView.findViewById(R.id.tv_warn1);
        mTvAbNormalWarn2 = mContentView.findViewById(R.id.tv_warn2);
        mBtnReload = mContentView.findViewById(R.id.btn_warn3);
        mBtnReload.setOnClickListener(new Click());
        layout.setOnClickListener(new Click());
        addView(mContentView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    // 设置reload事件
    public void setOnReloadListener(final OnClickListener listener) {
        this.listener = listener;
    }

    private class Click implements OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_warn3) {
                if (listener != null) {
                    listener.onClick(v);
                }
            } else if (id == R.id.abnormal_layout) {
                if (mState != RELOAD) {
                    return;
                }
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        }
    }

    public ExceptionView setState(@AbNormalType int state) {
        return setState(state,null);
    }

    public ExceptionView setState(@AbNormalType int state, String text) {
        mState = state;
        mBtnReload.setVisibility(View.GONE);
        mTvAbNormalWarn2.setVisibility(View.GONE);
        if (state == NO_CONNECTIONING) {
            mBtnReload.setVisibility(View.VISIBLE);
            mTvAbNormalWarn2.setVisibility(View.VISIBLE);
            mIvAbNormal.setImageResource(R.drawable.ic_abnormal_no_connecting);
            if (!TextUtils.isEmpty(text)) {
                mTvAbNormalWarn1.setText(text);
            } else {
                mTvAbNormalWarn1.setText(getContext().getResources().getString(R.string.abnormal_no_connection));
            }

        } else if (state == NO_CONTENT) {
            mIvAbNormal.setImageResource(R.drawable.ic_abnormal_no_content);
            if (!TextUtils.isEmpty(text)) {
                mTvAbNormalWarn1.setText(text);
            } else {
                mTvAbNormalWarn1.setText(getContext().getResources().getString(R.string.abnormal_no_content));
            }

        } else if (state == PAGE_ERROR) {
            mIvAbNormal.setImageResource(R.drawable.ic_abnormal_page_error);
            if (!TextUtils.isEmpty(text)) {
                mTvAbNormalWarn1.setText(text);
            } else {
                mTvAbNormalWarn1.setText(getContext().getResources().getString(R.string.abnormal_page_error));
            }
        } else {
            mIvAbNormal.setImageResource(R.drawable.ic_abnormal_reload);
            if (!TextUtils.isEmpty(text)) {
                mTvAbNormalWarn1.setText(text);
            } else {
                mTvAbNormalWarn1.setText(R.string.abnormal_reload);
            }
        }
        return this;
    }

    public ExceptionView show() {
        setFocusable(true);
        setVisibility(View.VISIBLE);
        return this;
    }

    public ExceptionView hide() {
        setVisibility(View.GONE);
        return this;
    }

    public void setStateAndShow(@AbNormalType int state) {
        setState(state);
        show();
    }

    public void setStateAndShow(@AbNormalType int state, String text) {
        setState(state, text);
        show();
    }

    public void setRootBackGround(@DrawableRes int drawableId) {
        mRoot.setBackground(getResources().getDrawable(drawableId));
    }

    public void setHintIvImageResource(@DrawableRes int drawableId) {
        mIvAbNormal.setImageResource(drawableId);
    }


    public void setAbNormalWarn1Text(String text) {
        mTvAbNormalWarn1.setText(text);
    }

    /**
     * 是否加载失败状态（显示并且不是数据为空状态）
     * @return true or false
     */
    public boolean isErrorStatus(){
        return getVisibility() == VISIBLE && mState != NO_CONTENT;
    }

    @IntDef({NO_CONNECTIONING, NO_CONTENT, PAGE_ERROR, RELOAD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AbNormalType {
    }

}
