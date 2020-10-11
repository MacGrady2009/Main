package com.android.common.base.rv;

import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.common.R;
import com.android.common.base.BaseFragment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRvFragment<T> extends BaseFragment {

    protected RecyclerView listView;
    public SmartRefreshLayout mSmartRefreshLayout;
    protected BaseRvAdapter<T> mAdapter;
    protected final ArrayList<T> dataList = new ArrayList<>();
    protected LayoutInflater mInflater;
    private int mPageItems = 20;//默人一页加载20条数据
    public int page = 1;//当前页数
    public boolean isRefresh = false;
    public boolean isLoadMore = false;
    /**
     * @param num 设置单页个数，默认20
     */
    protected void setPageItemNums(int num) {
        mPageItems = num;
    }

    /**
     * 返回单页加载的数量
     */
    protected int getPageItemNums() {
        return mPageItems;
    }

    public int onSetRootViewId() {
        mInflater = LayoutInflater.from(getActivity());
        return R.layout.fragment_base_recyler_list_layout;
    }

    /**
     * @return 设置layout manager类型
     */
    protected int getLayManagerType() {
        return 1;
    }

    protected void onInitView() {
        this.topActionBar = this.rootView.findViewById(R.id.common_view_toolbar_id);
        this.listView = this.rootView.findViewById(R.id.recyclerView);
        mSmartRefreshLayout = this.rootView.findViewById(R.id.refreshLayout);
        mErrorView = this.rootView.findViewById(R.id.errorView);
        mErrorView.setOnReloadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErrorClick();
            }
        });
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                onPullLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                onPullRefresh();
            }
        });

        this.initAdapter();
        if (this.mAdapter != null) {
            if (getLayManagerType() == 1) {
                this.listView.setLayoutManager(new LinearLayoutManager(mActivity));
            }
            this.listView.setAdapter(this.mAdapter);
            if (needAutoRefresh() && needCreateRefresh()) mSmartRefreshLayout.autoRefresh();
        } else {
            this.mActivity.finish();
        }
    }

    //  2018/7/17 基本的下拉刷新可以调用该方法，特殊情况可以重写该方法
    public void onErrorClick() {
        mErrorView.hide();
        mSmartRefreshLayout.setVisibility(View.VISIBLE);
        if (needAutoRefresh()) {
            if (!mSmartRefreshLayout.autoRefresh()) {
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
    protected void onPullLoadMore() {
        isLoadMore = true;
        page++;
        onLoadData();
        onRefreshOrLoadMore();
    }

    /**
     * 下拉刷新的通用回调
     * 需要特殊处理时，重写该方法
     */
    protected void onPullRefresh() {
        isRefresh = true;
        page = 1;
        //单个页面的网络请求都在这里处理
        //dataList.clear();
        onLoadData();
        onRefreshOrLoadMore();
    }

    protected void onRefreshOrLoadMore() {
    }


    public abstract void initAdapter();

    /**
     * 是否需要自动刷新
     */
    public boolean needAutoRefresh() {
        return false;
    }

    /**
     * 若为自动刷新时是否创建时就刷新
     * @return true or false
     */
    public boolean needCreateRefresh(){
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 上拉加载或下拉刷新成功后的回调
     */
    @UiThread
    public void onRefreshOrLoadSuccess(List<T> list) {
        if (list == null) list = new ArrayList<>();
        onRefreshOrLoadSuccess(list, list.size() >= mPageItems);
    }

    /**
     * 上拉加载或下拉刷新成功后的回调
     */
    @UiThread
    public void onRefreshOrLoadSuccess(List<T> list, boolean hasMoreData) {
        hideProgress();
        if (!isAlive() || mErrorView == null || mSmartRefreshLayout == null) return;
        mErrorView.setVisibility(View.GONE);
        mSmartRefreshLayout.setVisibility(View.VISIBLE);
        if (ContextUtil.isAlive(this)) {
            if (list == null) list = new ArrayList<>();
            if (isRefresh) {
                isRefresh = false;
                mSmartRefreshLayout.finishRefresh();
                mAdapter.setDataAndNotify(list);
                if (mAdapter.getItemCount() == 0) {
                    showEmpty();
                    //mErrorView.setStateAndShow(CommonAbNormalView.NO_CONTENT);
                    mSmartRefreshLayout.setEnableLoadMore(false);
                } else {
                    mErrorView.hide();
                    mSmartRefreshLayout.setEnableLoadMore(true);
                    //mSmartRefreshLayout.setNoMoreData(!hasMoreData);
                    RefreshUtil.setNoMoreData(mSmartRefreshLayout, !hasMoreData);
                }
            } else if (isLoadMore) {
                isLoadMore = false;
                mSmartRefreshLayout.finishLoadMore(300, true, !hasMoreData);
                mAdapter.appendDataAndNotify(list);
            } else {
                mAdapter.setDataAndNotify(list);
                if (mAdapter.getItemCount() == 0) {
                    showEmpty();
                    //mErrorView.setStateAndShow(CommonAbNormalView.NO_CONTENT);
                    mSmartRefreshLayout.setEnableLoadMore(false);
                } else {
                    mErrorView.hide();
                    mSmartRefreshLayout.setEnableLoadMore(true);
                    //mSmartRefreshLayout.setNoMoreData(!hasMoreData);
                    RefreshUtil.setNoMoreData(mSmartRefreshLayout, !hasMoreData);
                }
            }
        }
    }

    /**
     * 空数据
     */
    protected void showEmpty(){
        mErrorView.setStateAndShow(CommonAbNormalView.NO_CONTENT);
    }
    // 2018/7/17 根据e做网络请求结果的统一处理

    @UiThread
    public void onRefreshOrLoadFailed(CommonException e) {
        hideProgress();
        if (!isAlive() || mErrorView == null || mSmartRefreshLayout == null) return;
        if (ContextUtil.isAlive(this)) {
            if (isRefresh || !isLoadMore) {
                mAdapter.clear();
                if (isRefresh) {
                    isRefresh = false;
                    mSmartRefreshLayout.finishRefresh(false);
                }
                mErrorView.setVisibility(View.VISIBLE);
                mSmartRefreshLayout.setVisibility(View.GONE);
                // 2018/7/17 网络情况的view进行统一处理
                if (e == null) e = CommonException.unKnowError();
                switch (e.getCode()) {
                    case 1003:
                        mErrorView.setStateAndShow(CommonAbNormalView.NO_CONNECTIONING);
                        break;
                    default:
                        mErrorView.setStateAndShow(CommonAbNormalView.RELOAD);

                        // 2018/7/19 测试提bug了，等待下来刷新的的不弹提示
                        //toast(e.getDisplayMessage());
                        break;
                }
            } else {
                isLoadMore = false;
                page--;
                mSmartRefreshLayout.finishLoadMore(false);
                if (e == null) {
                    e = CommonException.unKnowError();
                }
                toast(e.getDisplayMessage());
            }
        }
    }
}
