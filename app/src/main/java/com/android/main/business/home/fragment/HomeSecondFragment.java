package com.android.main.business.home.fragment;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.android.common.base.rv.BaseRvAdapter;
import com.android.common.base.rv.BaseRvFragment;
import com.android.common.network.ResponseBean;
import com.android.common.utils.Router;
import com.android.main.BuildConfig;
import com.android.main.R;
import com.android.main.adapter.holder.HomeSecondHolder;
import com.android.main.mvp.model.home.HomeSecondBean;
import com.android.main.mvp.present.home.HomeSecondPresent;
import com.android.main.mvp.view.home.HomeSecondView;

public class HomeSecondFragment extends BaseRvFragment<HomeSecondBean.ResultBean>
    implements HomeSecondView {

    public static HomeSecondFragment instance;

    private HomeSecondPresent present;

    public static HomeSecondFragment getInstance(){
        if (null == instance){
            instance = new HomeSecondFragment();
        }

        return instance;
    }

    public HomeSecondFragment(){
    }

    @Override
    protected void onInitView() {
        super.onInitView();
        present = new HomeSecondPresent(this);
    }

    @Override
    protected BaseRvAdapter<HomeSecondBean.ResultBean> initAdapter(){
        BaseRvAdapter<HomeSecondBean.ResultBean> adapter = new BaseRvAdapter(this.getContext()){

            @Override
            protected RecyclerView.ViewHolder createDataViewHolder(ViewGroup parent, int viewType) {
                return new HomeSecondHolder(mLayoutInflater.inflate(R.layout.item_home_second,parent,false));
            }
        };
        adapter.setOnItemClickListener((view,position) ->{
            Router.getInstance().startActivity(this.getContext(),null, BuildConfig.HOME_SECOND);
        });
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
