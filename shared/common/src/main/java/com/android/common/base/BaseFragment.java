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
import com.android.common.utils.FragmentStack;
import com.android.common.view.TopActionBar;
import com.android.common.widget.AllDialog;
import com.android.common.widget.LoadingDialog;
import org.greenrobot.eventbus.EventBus;


public abstract class BaseFragment extends Fragment
    implements BaseView{
    protected Activity mActivity;
    protected View rootView;
    protected LayoutInflater mLayoutInflater;
    protected Bundle args;
    protected boolean isViewCreated = false;
    protected TopActionBar topActionBar;
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
        onPageChange(false);
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

        if (topActionBar != null) {
            initTopActionBar();
        }
        onFindView();
        onInitView();
        onInitEvent();
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
        onPageStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        onPageEnd();
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

    public abstract int onSetRootViewId();

    protected void onFindView() {
    }

    protected void onInitView() {
    }

    protected void onInitEvent(){
    }

    protected void onLoadData() {
    }


    /**
     * 显示加载框
     */
    public void showProgress() {
        if (!isDetached()) {
            if (loadingDlg == null) {
                loadingDlg = LoadingDialog.create(getActivity());
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
    public void onHiddenChanged(boolean hidden) {
        onPageChange(hidden);
    }

    protected void onPageChange(boolean hidden) {
        if (isViewCreated) {
            if (hidden) {
                onPageStart();
            } else {
                onPageEnd();
            }
        }
    }

    protected void onPageStart() {
    }

    protected void onPageEnd() {
    }

    @Override
    public void onFailed(ResponseBean responseBean) {
        Toast.makeText(getContext(),responseBean.getMessage()+"["+ responseBean.getCode() +"]",Toast.LENGTH_LONG).show();
    }
}
