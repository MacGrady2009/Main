package com.android.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.common.R;
import com.android.common.view.TopActionBar;
import com.android.common.widget.CustomLoadingDialog;
import org.greenrobot.eventbus.EventBus;


public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected View rootView;
    protected LayoutInflater mLayoutInflater;
    protected Bundle args;
    protected boolean isViewCreated = false;
    protected TopActionBar topActionBar;
    protected boolean isRestore = false;
    //public static int REQUEST_FRAGMENT_RESULT = 4772;
    protected CustomLoadingDialog progressDlg;
    protected boolean isShow = false; // 当前Fragment是否显示
    private boolean firstCreate = true; // 当前Fragment是否创建到显示

    public BaseFragment() {
    }

    public abstract int onSetRootViewId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.isRestore = true;
        }

        this.mActivity = this.getActivity();
        this.args = this.getArguments();
        if (this.args == null) {
            this.args = new Bundle();
        }
        firstCreate = true;
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mLayoutInflater = inflater;
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            this.rootView = this.mLayoutInflater.inflate(this.onSetRootViewId(), container, false);
        }
        return this.rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onPageChange();
        if (!this.restoreStateFromArguments()) {
            this.onLoadData();
        }
    }

    protected boolean needButterKnife() {
        return true;
    }

    protected boolean needEventBus() {
        return false;
    }

    /**
     * 当前Fragment是否在viewpager中
     * @return true or false
     */
    protected boolean isInViewPager() {
        return false;
    }

    public void onBackPressed() {}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (needEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }

        if (topActionBar != null) {
            initTopActionBar();
        }
        this.onInitView();
        this.onInitListener();
        this.isViewCreated = true;
        if (!isInViewPager()){
            isShow = true;
        }
    }

    public void initTopActionBar() {
        this.topActionBar.showButtonImage(R.drawable.ic_arrow_title, 1);
        this.topActionBar.setButtonClickListener(new TopActionBar.OnTopBarButtonClickListener() {
            public void onLeftButtonClick(View view) {
                BaseFragment.this.onBackPressed();
            }

            public void onRightButton2Click(View view) {
            }

            public void onRightButtonClick(View view) {
            }
        });
    }

    public void setTitle(CharSequence title) {
        if (this.topActionBar != null) {
            this.topActionBar.setTitle(title);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //  this.saveStateToArguments();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firstCreate) {
            firstCreate = false;
        } else {
            if (isShow) {
                onPageStart();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isShow) {
            onPageEnd();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private boolean restoreStateFromArguments() {
        Bundle savedState = this.args.getBundle("internalSavedViewState" + this.getClass().getSimpleName());
        if (savedState != null) {
            this.onRestoreState(savedState);
            return true;
        } else {
            return false;
        }
    }

    protected void onSaveState(Bundle savedInstanceState) {
    }

    protected void onRestoreState(Bundle savedInstanceState) {
    }

    protected void onInitView() {
    }

    protected void onInitListener() {
    }

    protected void onLoadData() {
    }

    /**
     * 显示加载框
     */
    public void showProgress() {
        showProgress(null, true);
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
        if (!isDetached()) {
            if (progressDlg == null) {
                progressDlg = new CustomLoadingDialog(mActivity);
            }
            progressDlg.setCancelable(cancelable);
            if (isProgressShown()) {
                progressDlg.setMsg(msg);
            } else {
                progressDlg.show(msg);
            }
        }
    }

    public boolean isProgressShown() {
        return progressDlg != null && progressDlg.isShowing();
    }

    public void hideProgress() {
        if (isProgressShown()) {
            progressDlg.dismiss();
        }
    }

    /**
     * 网络连接上的调用方法，可一自己根据需要去重写
     * @param isAvailable 是否可用
     * @param isWifi      是否wifi
     */
    public void onNetWorkConnectChanged(boolean isAvailable, boolean isWifi) {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!isInViewPager()) {
            isShow = !hidden;
            onPageChange();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isInViewPager()) {
            isShow = isVisibleToUser;
            onPageChange();
        }
    }

    protected void onPageChange() {
        if (isViewCreated) {
            if (isShow) {
                onPageStart();
            } else {
                onPageEnd();
            }
        }
    }

    /**
     * 页面显示，需要设置{@link #isInViewPager()}
     */
    protected void onPageStart() {
    }

    /**
     * 页面隐藏，需要设置{@link #isInViewPager()}
     */
    protected void onPageEnd() {
    }

}
