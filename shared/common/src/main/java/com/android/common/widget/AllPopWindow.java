package com.android.common.widget;

import android.content.Context;
import android.view.View;
import com.android.common.base.BaseListener;
import com.android.common.base.BasePopWindow;

public class AllPopWindow extends BasePopWindow {

    Builder mBuilder;

    public AllPopWindow(AllPopWindow.Builder builder) {
        super(builder.context, builder.height, builder.width);
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
    protected boolean needModel() {
        return mBuilder.needModel;
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
        private boolean needModel;

        private BaseListener<AllPopWindow, View> listener;

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


        public Builder setNeedModel(boolean needModel) {
            this.needModel = needModel;
            return this;
        }


        public Builder setListener(BaseListener<AllPopWindow, View> listener) {
            this.listener = listener;
            return this;
        }

        public AllPopWindow build() {
            return new AllPopWindow(this);
        }

    }

}
