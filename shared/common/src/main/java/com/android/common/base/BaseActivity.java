package com.android.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.common.R;
import com.android.common.utils.ActivityStack;
import com.android.common.utils.EventBusUtil;
import com.android.common.view.TopActionBar;


public abstract class BaseActivity extends AppCompatActivity{

    protected LayoutInflater mLayoutInflater;
    protected View rootView;
    protected TopActionBar topActionBar;
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

            if (topActionBar != null) {
                initTopActionBar();
            }

            onFindView();
            onInitView();
            onInitEvent();
            onInitFragment();
        }
    }

    public void initTopActionBar() {
        topActionBar.showButtonImage(R.mipmap.ic_arrow_title, TopActionBar.LEFT_AREA);
        topActionBar.setButtonClickListener(new TopActionBar.OnTopBarButtonClickListener() {
            public void onLeftButtonClick(View view) {
                leftButtonClick();
            }

            public void onRightButton2Click(View view) {
                right2ButtonClick();
            }

            public void onRightButtonClick(View view) {
                rightButtonClick();
            }
        });
    }

    protected void leftButtonClick(){
        onBackPressed();
    }

    protected void rightButtonClick() {

    }
    protected void right2ButtonClick() {

    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void setTitle(CharSequence title) {
        if (this.topActionBar != null) {
            this.topActionBar.setTitle(title);
        }
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
