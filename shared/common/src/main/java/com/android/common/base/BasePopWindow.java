package com.android.common.base;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;


public abstract  class BasePopWindow extends PopupWindow {

    public Context mContext;
    public WindowManager mWindowManager;
    public LayoutInflater layoutInflater;
    public View rootView;
    public int mHeight ;
    public int mWidth ;


    public BasePopWindow(final Context context, int width , int height) {
        mContext=context;
        mWidth = width;
        mHeight = height;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

    }

    public BasePopWindow init(){
        rootView = layoutInflater.inflate(setRootView(), null);
        initView(rootView);
        setPopWindowSize(mWidth, mHeight);
        setContentView(rootView);

        if (needOverlay()){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(mContext)){
                    setWindowLayoutType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                }
            }
        }


        setOutsideTouchable(!needModel());
        setFocusable(!needModel());

        return this;
    }


    public void setPopWindowSize(int height ,int width){
        setHeight(height);
        setWidth(width);
    }

    /**
     * 设置Layout
     */
    public abstract int setRootView();

    /**
     * layout上的rootview
     */
    public void initView(View view){

    }

    /**
     * 是否要显示在应用最上层
     */
    protected boolean needOverlay(){
        return false;
    }

    /**
     * 是否是模态
     */
    protected boolean needModel(){
        return false;
    }


}
