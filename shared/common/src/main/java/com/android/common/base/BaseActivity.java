package com.android.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.common.R;
import com.android.common.network.ResponseBean;
import com.android.common.utils.ActivityStack;
import com.android.common.utils.AppUtils;
import com.android.common.utils.EventBusUtil;
import com.android.common.view.ExceptionView;
import com.android.common.view.TopActionBar;
import com.android.common.widget.AllDialog;
import com.android.common.widget.DialogFactory;


public abstract class BaseActivity extends AppCompatActivity implements BaseView{

    protected LayoutInflater mLayoutInflater;
    protected View rootView;
    protected AllDialog loadingDialog;
    protected TopActionBar topActionBar;
    protected ExceptionView mErrorView;
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
            if (needSpecialErrorView()) {
                addSpecialErrorView();
            }

            mErrorView = findViewById(R.id.errorView);
            if (mErrorView != null) {
                initErrorView();
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
    public void initErrorView() {
        if (mErrorView != null) {
            mErrorView.setOnReloadListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onErrorClick();
                }
            });
        }
    }

    public void onErrorClick() {
        if (mErrorView != null) {
            mErrorView.hide();
        }
        onLoadData();
    }


    private void addSpecialErrorView() {
        FrameLayout content = findViewById(android.R.id.content);
        content.addView(mLayoutInflater.inflate(R.layout.layout_special_error_view, content, false));
    }

    protected boolean needSpecialErrorView() {
        return false;
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void setTitle(CharSequence title) {
        if (this.topActionBar != null) {
            this.topActionBar.setTitle(title);
        }
    }

    //onCreate方法彻底执行完毕的回调
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        onLoadData();
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

    protected void onLoadData() {
    }

    public boolean isAlive() {
        return AppUtils.isAlive(this);
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
        hideProgress();
        ActivityStack.getInstance().remove(this);
    }


    /**
     * 显示加载框
     */
    public void showProgress() {
        if ((!isDestroyed())) {
            if (loadingDialog == null) {
                loadingDialog = DialogFactory.createLoading(this);
            }
            loadingDialog.show();
        }
    }

    public void hideProgress() {
        if (isProgressShown()) {
            loadingDialog.dismiss();
        }
    }

    public boolean isProgressShown() {
        return (loadingDialog != null) && loadingDialog.isShowing();
    }

    @Override
    public void onFailed(ResponseBean responseBean) {
        hideProgress();
        Toast.makeText(this,responseBean.getMessage()+"["+ responseBean.getCode() +"]",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSucceed() {
        hideProgress();
    }
}
