package com.android.common.base.rv;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.common.R;
import com.android.common.base.BaseActivity;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import java.util.ArrayList;
import java.util.List;


/**
 * 基类recyclerViewActivity
 */
public abstract class BaseRvActivity<T> extends BaseActivity {

    public RecyclerView recyclerView;
    public SmartRefreshLayout mSmartRefreshLayout;
    protected BaseRvAdapter<T> mAdapter;
    //默人一页加载20条数据
    private int mPageItems = 20;
    //第一页
    public int page = 1;
    /**下拉*/
    public boolean isRefresh = false;
    /**上拉*/
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

    @Override
    protected int onSetRootViewId() {
        return R.layout.activity_base_recycler_list_layout;
    }

    /**
     * @return 设置layoutManager类型
     */
    protected int getLayManagerType() {
        return 1;
    }

    protected void beforeInitView() {
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        beforeInitView();
        super.onInitView(savedInstanceState);
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

        initAdapter();
        if (mAdapter != null) {
            if (getLayManagerType() == 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }

            recyclerView.setAdapter(mAdapter);
            if (needAutoRefresh()) mSmartRefreshLayout.autoRefresh();
        } else {
            finish();
        }
    }

    @Override
    public void onErrorClick() {
        mErrorView.hide();
        mSmartRefreshLayout.setVisibility(View.VISIBLE);
        if (!mSmartRefreshLayout.autoRefresh()) {
            onPullRefresh();
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

    public boolean needAutoRefresh() {
        return false;
    }


    /**
     * 上拉加载或下拉刷新成功后的回调
     */
    @UiThread
    public void onRefreshOrLoadSuccess(List<T> list) {
        if (list == null) list = new ArrayList<>();
        onRefreshOrLoadSuccess(list, list.size() >= mPageItemNums);
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
                // 多种item时，list为空时其他item可能不为空，也不显示空页面
                if (mAdapter.getItemCount() == 0) {
                    mErrorView.setStateAndShow(CommonAbNormalView.NO_CONTENT);
                    mSmartRefreshLayout.setEnableLoadMore(false);
                } else {
                    mErrorView.hide();
                    mSmartRefreshLayout.setEnableLoadMore(true);
                    RefreshUtil.setNoMoreData(mSmartRefreshLayout, !hasMoreData);
                    //mSmartRefreshLayout.setNoMoreData(!hasMoreData);
                }
            } else if (isLoadMore) {
                isLoadMore = false;
                mSmartRefreshLayout.finishLoadMore(300, true, !hasMoreData);
                mAdapter.appendDataAndNotify(list);
            } else {
                mAdapter.setDataAndNotify(list);
                if (mAdapter.getItemCount() == 0) {
                    mErrorView.setStateAndShow(CommonAbNormalView.NO_CONTENT);
                    mSmartRefreshLayout.setEnableLoadMore(false);
                } else {
                    mErrorView.hide();
                    mSmartRefreshLayout.setEnableLoadMore(true);
                    RefreshUtil.setNoMoreData(mSmartRefreshLayout, !hasMoreData);
                    //mSmartRefreshLayout.setNoMoreData(!hasMoreData);
                }
            }
        }
    }

    /**
     * 上拉加载或下拉刷新成功后的回调
     */
    @UiThread
    public void onRefreshOrLoadSuccessAppendData(List<T> list, boolean isShowNoContent
            , boolean isChecked) {
        if (ContextUtil.isAlive(this)) {
            mErrorView.setVisibility(View.GONE);
            mSmartRefreshLayout.setVisibility(View.VISIBLE);
            if (list == null) list = new ArrayList<>();
            if (isRefresh) {
                isRefresh = false;
                mSmartRefreshLayout.finishRefresh();
                mAdapter.setDataAndNotify(list);
                // 多种item时，list为空时其他item可能不为空，也不显示空页面
                if (list.size() <= 1) {
                    showNoContentHint(isShowNoContent,isChecked);
                    mSmartRefreshLayout.setEnableLoadMore(false);
                } else {
                    mErrorView.hide();
                    mSmartRefreshLayout.setEnableLoadMore(true);
                    RefreshUtil.setNoMoreData(mSmartRefreshLayout, list.size() < mPageItemNums);
                    //mSmartRefreshLayout.setNoMoreData(list.size() < mPageItemNums);
                }
            } else if (isLoadMore) {
                isLoadMore = false;
                mSmartRefreshLayout.finishLoadMore(300, true, list.size() < mPageItemNums);
                mAdapter.appendDataAndNotify(list);
            } else {
                mAdapter.appendDataAndNotify(list);

                if (list.size() <= 1) {
                    showNoContentHint(isShowNoContent,isChecked);
                    mSmartRefreshLayout.setEnableLoadMore(false);
                } else {
                    mErrorView.hide();
                    mSmartRefreshLayout.setEnableLoadMore(true);
                    RefreshUtil.setNoMoreData(mSmartRefreshLayout, list.size() < mPageItemNums);
                    //mSmartRefreshLayout.setNoMoreData(list.size() < mPageItemNums);
                }
            }
        }
    }

    public void showNoContentHint(boolean isShow,boolean isChecked){
        if (isShow){
            mErrorView.setRootBackGround(R.mipmap.real_exam_bg);
            mErrorView.setStateAndShow(CommonAbNormalView.NO_CONTENT);
            mErrorView.setHintIvImageResource(R.mipmap.real_exam_no_content);
            if (isChecked){
                mErrorView.setAbNormalWarn1Text("还没有未开始的考场哦~");
            }else {
                mErrorView.setAbNormalWarn1Text("还没有新考场哦~");
            }

        }
    }

    @UiThread
    public void onRefreshOrLoadFailed(CommonException ex) {
        if (ContextUtil.isAlive(this)) {
            if (isRefresh || !isLoadMore) {
                mAdapter.clear();
                if (isRefresh) {
                    isRefresh = false;
                    mSmartRefreshLayout.finishRefresh(false);
                }
                mErrorView.setVisibility(View.VISIBLE);
                mSmartRefreshLayout.setVisibility(View.GONE);
                if (ex == null) ex = CommonException.unKnowError();
                switch (ex.getCode()) {
                    case 1003:
                        mErrorView.setStateAndShow(CommonAbNormalView.NO_CONNECTIONING);
                        break;
                    default:
                        mErrorView.setStateAndShow(CommonAbNormalView.RELOAD);
                        //toast(e.getDisplayMessage());
                        break;
                }
            } else {
                isLoadMore = false;
                page--;
                mSmartRefreshLayout.finishLoadMore(false);
                if (ex == null) {
                    ex = CommonException.unKnowError();
                }
                toast(ex.getDisplayMessage());
            }
        }
    }
}
