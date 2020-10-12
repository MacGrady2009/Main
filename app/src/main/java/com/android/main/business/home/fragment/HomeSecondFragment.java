package com.android.main.business.home.fragment;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.android.common.base.rv.BaseRvAdapter;
import com.android.common.base.rv.BaseRvFragment;
import com.android.common.network.ResponseBean;
import com.android.main.mvp.model.home.SecondBean;
import java.util.ArrayList;
import java.util.List;

public class HomeSecondFragment extends BaseRvFragment<SecondBean> {

    private List<SecondBean> dataList = new ArrayList<>();

    public static HomeSecondFragment instance;

    public static HomeSecondFragment getInstance(){
        if (null == instance){
            instance = new HomeSecondFragment();
        }

        return instance;
    }

    private HomeSecondFragment(){
    }

    @Override
    protected BaseRvAdapter<SecondBean> initAdapter(){
        BaseRvAdapter<SecondBean> adapter = new BaseRvAdapter(this.getContext(),dataList){

            @Override
            protected RecyclerView.ViewHolder createDataViewHolder(ViewGroup parent, int viewType) {
                return null;
            }
        };
        adapter.setOnItemClickListener((view,position) ->{

        });
        return adapter;
    }


    @Override
    public void onFailed(ResponseBean responseBean) {
        super.onFailed(responseBean);
        refreshOrLoadFailed();
    }
}
