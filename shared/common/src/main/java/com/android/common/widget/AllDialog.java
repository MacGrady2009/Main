package com.android.common.widget;

import android.content.Context;
import android.view.View;
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
    protected boolean cancelable() {
        return mBuilder.cancelable;
    }

    @Override
    protected boolean canceledOnTouchOutside() {
        return mBuilder.canceledOnTouchOutside;
    }

    @Override
    public void initView(View view) {
        if (null != mBuilder.listener)
            mBuilder.listener.onInit(this, view);
    }



    public static class Builder {
        private Context context;
        private int width;
        private int height;
        private int layoutId;
        private boolean needOverlay;
        private boolean cancelable;
        private boolean canceledOnTouchOutside;
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

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }


        public Builder setListener(BaseListener<AllDialog, View> listener) {
            this.listener = listener;
            return this;
        }


        public AllDialog build() {
            return new AllDialog(this);
        }

    }
}
