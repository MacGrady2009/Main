package com.android.common.widget;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import androidx.annotation.NonNull;
import com.android.common.base.BaseDialog;
import com.android.common.base.BaseListener;

public class AllDialog extends BaseDialog {

    Builder mBuilder;

    public AllDialog(Builder builder) {
        super(builder.context, builder.width, builder.height, 0);
        mBuilder = builder;
    }

    @Override
    public int setRootView() {
        return mBuilder.layoutId;
    }


    @Override
    protected boolean needOverlay() {
        return mBuilder.needOverlay;
    }

    @Override
    protected boolean nonNeedModel() {
        return mBuilder.nonNeedModel;
    }


    @Override
    public void initView(View view) {
        if (null != mBuilder.listener)
            mBuilder.listener.onInit(this, view);
    }

    @Override
    public void onBackPressed() {
        if (!mBuilder.nonBack)
            super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBuilder.backKeyDisimiss) {
            dismiss();
            if (mBuilder.rnListener != null) {//返回键按下回调
                mBuilder.rnListener.onReturnKeyPress();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class Builder {
        private Context context;
        private int width;
        private int height;
        private int layoutId;
        private boolean needOverlay;
        private boolean nonNeedModel;
        private boolean nonBack;
        private boolean backKeyDisimiss;//返回键消失
        private ReturnKeyListener rnListener;
        private BaseListener<AllDialog, View> listener;

        public Builder(Context mContext) {
            this.context = mContext;
        }

        public Builder setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setOverlay(boolean needOverlay) {
            this.needOverlay = needOverlay;
            return this;
        }


        public Builder setNonNeedModel(boolean nonNeedModel) {
            this.nonNeedModel = nonNeedModel;
            return this;
        }

        public Builder setNonBack(boolean nonBack) {
            this.nonBack = nonBack;
            return this;
        }

        public Builder setListener(BaseListener<AllDialog, View> listener) {
            this.listener = listener;
            return this;
        }


        public AllDialog build() {
            return new AllDialog(this);
        }

        public Builder setBackKeyDisimiss(boolean backKeyDisimiss) {
            this.backKeyDisimiss = backKeyDisimiss;
            return this;
        }

        public Builder setRnListener(ReturnKeyListener rnListener) {
            this.rnListener = rnListener;
            return this;
        }
    }

    /**
     * 返回键回调 RN专用
     */
    public interface ReturnKeyListener {
        void onReturnKeyPress();
    }
}
