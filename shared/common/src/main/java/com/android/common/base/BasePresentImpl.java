package com.android.common.base;

import com.android.common.network.CommonApi;
import com.android.common.network.RetrofitManager;
import com.android.common.utils.RxUtils;
import io.reactivex.rxjava3.core.Observable;

public class BasePresentImpl<V extends BaseView> implements BaseInterface {
    protected V mBaseView;

    public BasePresentImpl() {
    }

    public BasePresentImpl(V view) {
        mBaseView = view;
    }

    protected CommonApi getService(){
        return RetrofitManager.getInstance().getApiService(CommonApi.class);
    }

    public static <T> Observable<T> compose(Observable<T> observable) {
        return RxUtils.subscribe(observable);
    }



}
