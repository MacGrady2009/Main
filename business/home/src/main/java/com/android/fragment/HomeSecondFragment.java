package com.android.fragment;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.android.common.base.rv.BaseRvAdapter;
import com.android.common.base.rv.BaseRvFragment;
import com.android.common.network.ResponseBean;
import com.android.common.utils.Router;
import com.android.home.BuildConfig;
import com.android.home.R;
import com.android.adapter.holder.HomeSecondHolder;
import com.android.mvp.model.home.HomeSecondBean;
import com.android.mvp.present.home.HomeSecondPresent;
import com.android.mvp.view.home.HomeSecondView;

public class HomeSecondFragment extends BaseRvFragment<HomeSecondBean.ResultBean>
    implements HomeSecondView {

    public static HomeSecondFragment instance;

    private HomeSecondPresent present;

    private boolean mNeedBack = false;

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
    protected boolean onNeedBack() {
        return mNeedBack;
    }

    @Override
    protected String onSetTitleText() {
        return getString(R.string.contact);
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

    public void setNeedBack(boolean needBack) {
        this.mNeedBack = needBack;
    }
}
