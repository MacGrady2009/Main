package com.android.common.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import com.android.common.R;

public abstract class BaseDialog extends Dialog {

    public Context mContext;
    public LayoutInflater layoutInflater;
    public View rootView ;
    public int mHeight ;
    public int mWidth ;


    public BaseDialog(Context context, int width, int height, int themeResId) {
        super(context, themeResId > 0 ? themeResId : R.style.base_dialog);
        mContext = context;
        mHeight = height;
        mWidth = width;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDialogSize(int width, int height){
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        params.height = height;
        getWindow().setAttributes(params);
    }

    public abstract int setRootView();

    public void initView(View view){
    }

    protected boolean needOverlay(){
        return false;
    }

    protected boolean cancelable(){
        return true;
    }

    protected boolean canceledOnTouchOutside() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (needOverlay()){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              if (Settings.canDrawOverlays(mContext)){
                  getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
              }
            }else {
                getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }
        }

        rootView = layoutInflater.inflate(setRootView(), null);
        setContentView(rootView);
        initView(rootView);
        setDialogSize(mWidth, mHeight);
        setCancelable(cancelable());
        setCanceledOnTouchOutside(canceledOnTouchOutside());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }



}
