package com.android.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.common.utils.ActivityStack;
import com.android.common.utils.EventBusUtil;


public abstract class BaseActivity extends AppCompatActivity{

    protected LayoutInflater mLayoutInflater;
    protected View rootView;
    protected Intent originIntent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.getInstance().add(this);
        this.originIntent = this.getIntent();

        int rootViewId = this.onSetRootViewId();
        if (rootViewId <= 0) {
            this.finish();
        } else {
            this.mLayoutInflater = LayoutInflater.from(this);
            this.rootView = this.mLayoutInflater.inflate(rootViewId, (ViewGroup) null, false);
            this.setContentView(rootView);

            if (needEventBus()) {
                EventBusUtil.register(this);
            }

            onFindView();
            onInitView();
            onInitEvent();
            onInitFragment();
        }
    }


    public void onBackClick(View view) {
        onBackPressed();
    }

    protected boolean needEventBus() {
        return false;
    }

    protected abstract int onSetRootViewId();

    protected void onFindView() {
    }

    protected void onInitView() {
    }

    protected void onInitEvent(){
    }

    protected void onInitFragment(){
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.originIntent = intent;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
        ActivityStack.getInstance().remove(this);
    }
}
