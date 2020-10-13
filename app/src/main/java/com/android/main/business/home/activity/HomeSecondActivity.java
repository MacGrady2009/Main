package com.android.main.business.home.activity;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.android.common.base.rv.BaseRvActivity;
import com.android.common.base.rv.BaseRvAdapter;
import com.android.common.network.ResponseBean;
import com.android.main.R;
import com.android.main.adapter.holder.HomeSecondHolder;
import com.android.main.mvp.model.home.HomeSecondBean;
import com.android.main.mvp.present.home.HomeSecondPresent;
import com.android.main.mvp.view.home.HomeSecondView;

public class HomeSecondActivity extends BaseRvActivity<HomeSecondBean.ResultBean>
    implements HomeSecondView {

    private HomeSecondPresent present;

    @Override
    protected void onInitView() {
        super.onInitView();
        present = new HomeSecondPresent(this);
    }

    @Override
    protected BaseRvAdapter<HomeSecondBean.ResultBean> initAdapter(){
        BaseRvAdapter<HomeSecondBean.ResultBean> adapter = new BaseRvAdapter(this){

            @Override
            protected RecyclerView.ViewHolder createDataViewHolder(ViewGroup parent, int viewType) {
                return new HomeSecondHolder(mLayoutInflater.inflate(R.layout.item_home_second,parent,false));
            }
        };
        return adapter;
    }


    @Override
    public void onFailed(ResponseBean responseBean) {
        super.onFailed(responseBean);
        refreshOrLoadFailed();
    }

    @Override
    protected void onLoadData() {
        super.onLoadData();
        showProgress();
        present.getHomeSecondList();
    }

    @Override
    public void onPullRefresh() {
        super.onPullRefresh();
        present.getHomeSecondList();
    }

    @Override
    public void onPullLoadMore() {
        super.onPullLoadMore();
        present.getHomeSecondList();
    }

    @Override
    public void onGetHomeSecondList(HomeSecondBean homeSecondBean) {
        refreshOrLoadSuccess(homeSecondBean.getResult());
    }

    @Override
    protected boolean needSpecialErrorView() {
        return true;
    }
}
