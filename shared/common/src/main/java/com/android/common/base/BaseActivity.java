package com.android.common.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.common.R;
import com.android.common.utils.ActivityStack;
import com.android.common.utils.EventBusUtil;
import com.android.common.view.ExceptionView;
import com.android.common.view.TopActionBar;
import com.android.common.widget.CustomLoadingDialog;


public abstract class BaseActivity extends AppCompatActivity {

    protected LayoutInflater mLayoutInflater;
    protected View rootView;
    protected CustomLoadingDialog progressDlg;
    protected TopActionBar topActionBar;
    protected ExceptionView errorView;
    protected Intent originIntent = null;
    protected boolean isRestoreState;
    private boolean isSupportProgress = true;

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
            this.setContentView(rootViewId);

            if (needEventBus()) {
                EventBusUtil.register(this);
            }

            if (topActionBar != null) {
                initTopActionBar();
            }
            if (needSpecialErrorView()) {
                addSpecialErrorView();
            }

            if (errorView != null) {
                initErrorView();
            }

            onFindView();
            onInitView();
            onInitEvent();
        }
    }

    public void initTopActionBar() {
        topActionBar.showButtonImage(R.drawable.ic_arrow_title, TopActionBar.LEFT_AREA);
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
        if (errorView != null) {
            errorView.setOnReloadListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onErrorClick();
                }
            });
        }
    }

    public void onErrorClick() {
        if (errorView != null) {
            errorView.hide();
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


    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!(isRestoreState && hasRestore())) {
            onLoadData();
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

    protected void onLoadData() {
    }

    protected boolean hasRestore() {
        return false;
    }

    protected void setSupportProgress(boolean flag) {
        this.isSupportProgress = flag;
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
        if (this.progressDlg != null && this.progressDlg.isShowing()) {
            this.progressDlg.dismiss();
        }
        ActivityStack.getInstance().remove(this);
    }


    /**
     * 显示加载框
     */
    public void showProgress() {
        if (isSupportProgress) {
            showProgress(null, true);
        }
    }

    /**
     * 显示加载框
     * @param cancelable 是否可以取消
     */
    public void showProgress(boolean cancelable) {
        showProgress(null, cancelable);
    }

    /**
     * 显示加载框
     * @param msg 显示加载框内容
     */
    public void showProgress(String msg) {
        showProgress(msg, true);
    }

    /**
     * 显示加载框
     * @param msg        显示加载框内容
     * @param cancelable 是否可以取消
     */
    public void showProgress(String msg, boolean cancelable) {
        if ((!isDestroyed()) && isSupportProgress) {
            if (progressDlg == null) {
                progressDlg = new CustomLoadingDialog(this);
            }
            progressDlg.setCancelable(cancelable);
            if (isProgressShown()) {
                progressDlg.setMsg(msg);
            } else {
                progressDlg.show(msg);
            }
        }
    }

    public void hideProgress() {
        if (isProgressShown()) {
            progressDlg.dismiss();
        }
    }

    public boolean isProgressShown() {
        return (isSupportProgress && progressDlg != null) && progressDlg.isShowing();
    }
}
