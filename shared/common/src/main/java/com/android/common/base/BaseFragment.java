package com.android.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.common.R;
import com.android.common.network.ResponseBean;
import com.android.common.utils.AppUtils;
import com.android.common.utils.FragmentStack;
import com.android.common.view.ExceptionView;
import com.android.common.widget.AllDialog;
import com.android.common.widget.DialogFactory;
import org.greenrobot.eventbus.EventBus;


public abstract class BaseFragment extends Fragment
    implements BaseView{
    protected Activity mActivity;
    protected ViewGroup mRootView;
    protected LayoutInflater mLayoutInflater;
    protected Bundle args;
    protected ExceptionView mErrorView;
    protected AllDialog loadingDlg;

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
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mLayoutInflater = inflater;
        this.mRootView = (ViewGroup) this.mLayoutInflater.inflate(this.onSetRootViewId(), container, false);

        if (needSpecialErrorView()) {
            addSpecialErrorView();
        }

        mErrorView = mRootView.findViewById(R.id.errorView);

        if (mErrorView != null) {
            initErrorView();
        }

        return this.mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.onLoadData();
    }

    protected boolean needEventBus() {
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

        onFindView();
        onInitView();
        onInitEvent();
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
    }

    private void addSpecialErrorView() {
        mRootView.addView(mLayoutInflater.inflate(R.layout.layout_special_error_view, mRootView, false));
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
}
