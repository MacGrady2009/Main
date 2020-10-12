package com.android.common.base.rv;


import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.common.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import java.util.ArrayList;
import java.util.List;

public class BaseBindView<T> {
    //当前页数
    private int page = 1;

    private boolean isRefresh = false;

    private boolean isLoadMore = false;

    SmartRefreshLayout mSmartRefreshLayout;

    RecyclerView mRv;

    BaseRvAdapter<T> mAdapter;

    Listener listener;

    public BaseBindView(View rootView){
        mSmartRefreshLayout  = rootView.findViewById(R.id.refreshLayout);
        mRv = rootView.findViewById(R.id.recyclerView);
        initViews();
    }

    private void initViews(){
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pullRefresh();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pullLoadMore();
            }

        });
        mRv.setLayoutManager(new LinearLayoutManager(mRv.getContext()));
        if (mAdapter != null) {
            mRv.setAdapter(this.mAdapter);
        }
    }


    void pullRefresh(){
        isRefresh = true;
        page = 1;
        if (listener != null) {
            listener.onPullRefresh();
        }
    }

    void pullLoadMore(){
        isLoadMore = true;
        page++;
        if (listener != null) {
            listener.onPullLoadMore();
        }
    }

    void pullLoadMoreFailed(){
        page--;
    }

    public interface Listener{
        void onPullRefresh();
        void onPullLoadMore();
    }

    public int getPageNumber(){
        return page;
    }

    public boolean isRefresh(){
        return isRefresh;
    }

    public boolean isLoadMore(){
        return isLoadMore;
    }

    /**
     * 上拉加载或下拉刷新成功后的回调
     */
    @UiThread
    public void refreshOrLoadSuccess(List<T> list, boolean hasMoreData) {
        mSmartRefreshLayout.setVisibility(View.VISIBLE);
        if (list == null) list = new ArrayList<>();
        if (isRefresh) {
            isRefresh = false;
            mSmartRefreshLayout.finishRefresh();
            mAdapter.setDataAndNotify(list);
            if (mAdapter.getItemCount() == 0) {
                mSmartRefreshLayout.setEnableLoadMore(false);
            } else {
                mSmartRefreshLayout.setEnableLoadMore(true);
                mSmartRefreshLayout.setNoMoreData(!hasMoreData);
            }
        } else if (isLoadMore) {
            isLoadMore = false;
            mSmartRefreshLayout.finishLoadMore(300, true, !hasMoreData);
            mAdapter.appendDataAndNotify(list);
        } else {
            mAdapter.setDataAndNotify(list);
            if (mAdapter.getItemCount() == 0) {
                mSmartRefreshLayout.setEnableLoadMore(false);
            } else {
                mSmartRefreshLayout.setEnableLoadMore(true);
                mSmartRefreshLayout.setNoMoreData(!hasMoreData);
            }
        }
    }

    @UiThread
    public void refreshOrLoadFailed() {
        if (isRefresh || !isLoadMore) {
            mAdapter.clear();
            mSmartRefreshLayout.finishRefresh(false);
            mSmartRefreshLayout.setVisibility(View.GONE);
        } else {
            mSmartRefreshLayout.finishLoadMore(false);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setAdapter(BaseRvAdapter<T> adapter) {
        this.mAdapter = adapter;
    }
}
