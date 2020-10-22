package com.android.common.base.rv;

import static com.android.common.constant.Constant.ITEMS_EVERY_PAGE;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.UiThread;
import com.android.common.R;
import com.android.common.base.BaseFragment;
import com.android.common.utils.AppUtils;
import com.android.common.view.ExceptionView;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRvFragment<T>
    extends BaseFragment implements BaseRvBindView.Listener{

    private TextView tvToolbarTitle;

    public BaseRvBindView<T> mBaseRvBind;

    public int onSetRootViewId() {
        return R.layout.fragment_base_rv;
    }


    @Override
    protected void onInitView() {
        tvToolbarTitle = mRootView.findViewById(R.id.tv_toolbar_title);
        mBaseRvBind = new BaseRvBindView(mRootView);
        mBaseRvBind.setListener(this);
        mBaseRvBind.setAdapter(initAdapter());
    }

    @Override
    public void onErrorClick() {
        mErrorView.hide();
        mBaseRvBind.mSmartRefreshLayout.setVisibility(View.VISIBLE);
        if (needAutoRefresh()) {
            if (!mBaseRvBind.mSmartRefreshLayout.autoRefresh()) {
                onPullRefresh();
            }
        } else {
            onLoadData();
        }
    }

    /**
     * 上拉加载的通用回调
     * 需要特殊处理时，重写该方法
     */
    @Override
    public void onPullLoadMore() {
        onRefreshOrLoadMore();
    }

    /**
     * 下拉刷新的通用回调
     * 需要特殊处理时，重写该方法
     */
    @Override
    public void onPullRefresh() {
        onRefreshOrLoadMore();
    }

    protected void onRefreshOrLoadMore() {
    }

    protected abstract BaseRvAdapter<T> initAdapter();

    /**
     * 是否需要自动刷新
     */
    public boolean needAutoRefresh() {
        return false;
    }

    /**
     * 上拉加载或下拉刷新成功后的回调
     */
    @UiThread
    public void refreshOrLoadSuccess(List<T> list) {
        if (list == null) list = new ArrayList<>();
        refreshOrLoadSuccess(list, list.size() >= ITEMS_EVERY_PAGE);
    }

    /**
     * 上拉加载或下拉刷新成功后的回调
     */
    @UiThread
    private void refreshOrLoadSuccess(List<T> list, boolean hasMoreData) {
        if (!isAlive() || mErrorView == null) return;
        mErrorView.setVisibility(View.GONE);
        if (AppUtils.isAlive(this)) {
            if (list == null) list = new ArrayList<>();
            if (mBaseRvBind.isRefresh()) {
                if (list.size() == 0) {
                    mErrorView.setStateAndShow(ExceptionView.NO_CONTENT);
                } else {
                    mErrorView.hide();
                }
            } else {
                if (list.size() == 0) {
                    mErrorView.setStateAndShow(ExceptionView.NO_CONTENT);
                } else {
                    mErrorView.hide();
                }
            }
            mBaseRvBind.refreshOrLoadSuccess(list,hasMoreData);
        }
    }

    @UiThread
    public void refreshOrLoadFailed() {
        if (!isAlive() || mErrorView == null) return;
        if (AppUtils.isAlive(this)) {
            if (mBaseRvBind.isRefresh() || !mBaseRvBind.isLoadMore()) {
                mErrorView.setVisibility(View.VISIBLE);
                mErrorView.setStateAndShow(ExceptionView.RELOAD);
            }
            mBaseRvBind.refreshOrLoadFailed();
        }
    }

    public void setTitle(String title){
        tvToolbarTitle.setText(title);
    }
}
