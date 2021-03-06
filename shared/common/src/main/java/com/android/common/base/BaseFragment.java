package com.android.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.android.common.R;
import com.android.common.constant.Constant;
import com.android.common.network.ResponseBean;
import com.android.common.utils.AppUtils;
import com.android.common.utils.FragmentStack;
import com.android.common.utils.permission.FragmentPermissionDispatcher;
import com.android.common.utils.permission.PermissionCallback;
import com.android.common.view.ExceptionView;
import com.android.common.view.ToolBarBinding;
import com.android.common.widget.AllDialog;
import com.android.common.widget.DialogFactory;
import org.greenrobot.eventbus.EventBus;

public abstract class BaseFragment extends Fragment
    implements BaseView ,PermissionCallback, ToolBarBinding.Listener{
    protected Activity mActivity;
    protected LinearLayout mRootView;
    protected Toolbar mToolBar;
    protected ToolBarBinding mToolBarBinding;
    protected FrameLayout mContentView;
    protected LayoutInflater mLayoutInflater;
    protected Bundle args;
    protected ExceptionView mErrorView;
    protected AllDialog loadingDlg;
    protected AllDialog permissionDialog;
    private FragmentPermissionDispatcher dispatcher;

    public BaseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentStack.getInstance().add(this);
        this.mActivity = this.getActivity();
        this.args = this.getArguments();
        if (this.args == null) {
            this.args = new Bundle();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mRootView = (LinearLayout) mLayoutInflater.inflate(R.layout.fragment_base, container, false);
        mToolBar = mRootView.findViewById(R.id.toolbar);
        mContentView = mRootView.findViewById(R.id.fl_rootContent);
        mLayoutInflater.inflate(onSetRootViewId(),mContentView);
        mToolBarBinding = new ToolBarBinding(mToolBar);
        if (needSpecialErrorView()) {
            addSpecialErrorView();
        }
        mErrorView = mRootView.findViewById(R.id.errorView);

        if (mErrorView != null) {
            initErrorView();
        }

        if (!onNeedToolBar()){
            mToolBar.setVisibility(View.GONE);
        }

        mToolBarBinding.showBack(onNeedBack());
        mToolBarBinding.setTitleText(onSetTitleText());
        mToolBarBinding.setListener(this);
        return this.mRootView;
    }

    protected boolean needEventBus() {
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (needEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
        onFindView();
        onInitView();
        onInitEvent();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.onLoadData();
        initPermission();
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
        hideProgress();
        FragmentStack.getInstance().remove(this);
        dispatcher.clear(this);
    }

    private void addSpecialErrorView() {
        mContentView.addView(mLayoutInflater.inflate(R.layout.layout_special_error_view, mRootView, false));
    }

    protected boolean needSpecialErrorView() {
        return false;
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

    protected abstract int onSetRootViewId();

    protected void onFindView() {
    }

    protected void onInitView() {
    }

    protected void onInitEvent(){
    }

    protected void onLoadData() {
    }

    //toolbar start
    protected boolean onNeedToolBar(){
        return true;
    }

    protected boolean onNeedBack(){
        return true;
    }

    protected String onSetTitleText(){
        return "";
    }

    @Override
    public void onBackClickListener() {
        getActivity().finish();
    }
    //toolbar end

    private void initPermission(){
        dispatcher = FragmentPermissionDispatcher.getInstance();
        dispatcher.setPermissionCallback(this, this);
        permissionDialog = DialogFactory.createPermissionDialog(this.getActivity());
        dispatcher.checkedWithStorage(this);
    }

    public boolean isAlive() {
        return AppUtils.isAlive(this);
    }

    /**
     * 显示加载框
     */
    public void showProgress() {
        if (!isDetached()) {
            if (loadingDlg == null) {
                loadingDlg = DialogFactory.createLoading(getActivity());
            }
            loadingDlg.show();
        }
    }

    public boolean isProgressShown() {
        return loadingDlg != null && loadingDlg.isShowing();
    }

    public void hideProgress() {
        if (isProgressShown()) {
            loadingDlg.dismiss();
        }
    }

    @Override
    public void onFailed(ResponseBean responseBean) {
        hideProgress();
        Toast.makeText(getContext(),responseBean.getMessage()+"["+ responseBean.getCode() +"]",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSucceed() {
        hideProgress();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dispatcher.onRequestPermissionResult(this, requestCode, grantResults);
    }

    /**
     * 权限申请回调
     * @param request
     * @param state
     */
    @Override
    public void onPermission(int request, int state) {
        switch (request) {
            case Constant.PERMISSION_STORAGE_REQUEST_CODE:
                switch (state) {
                    case PermissionCallback.SUCCESS:
                        dispatcher.checkedWithCamera(this);
                        break;
                    case PermissionCallback.DENIED:
                    case PermissionCallback.NEVER_ASK_AGAIN:
                    case PermissionCallback.EXPLAIN:
                        if (!permissionDialog.isShowing()){
                            permissionDialog.show();
                        }
                        dispatcher.checkedWithCamera(this);
                        break;
                }
                break;
            case Constant.PERMISSION_CAMERA_REQUEST_CODE:
                switch (state) {
                    case PermissionCallback.SUCCESS:
                        dispatcher.checkedWithLocation(this);
                        break;
                    case PermissionCallback.DENIED:
                    case PermissionCallback.NEVER_ASK_AGAIN:
                    case PermissionCallback.EXPLAIN:
                        if (!permissionDialog.isShowing()){
                            permissionDialog.show();
                        }
                        dispatcher.checkedWithLocation(this);
                        break;
                }
                break;
            case Constant.PERMISSION_LOCATION_REQUEST_CODE:
                switch (state) {
                    case PermissionCallback.SUCCESS:
                        dispatcher.checkedWithPhone(this);
                        break;
                    case PermissionCallback.DENIED:
                    case PermissionCallback.NEVER_ASK_AGAIN:
                    case PermissionCallback.EXPLAIN:
                        if (!permissionDialog.isShowing()){
                            permissionDialog.show();
                        }
                        dispatcher.checkedWithPhone(this);
                        break;
                }
                break;
            case Constant.PERMISSION_PHONE_REQUEST_CODE:
                switch (state) {
                    case PermissionCallback.SUCCESS:
                        break;
                    case PermissionCallback.DENIED:
                    case PermissionCallback.NEVER_ASK_AGAIN:
                    case PermissionCallback.EXPLAIN:
                        if (!permissionDialog.isShowing()){
                            permissionDialog.show();
                        }
                        break;
                }
                break;
        }
    }
}
