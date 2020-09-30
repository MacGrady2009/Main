package com.android.common.base;

import com.android.common.network.RetrofitManager;
import com.android.common.utils.RxUtils;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public class BasePresentImpl<V extends BaseView,S extends BaseApi> implements BaseInterface {
    protected V mBaseView;

    public BasePresentImpl(V view) {
        mBaseView = view;
    }

    protected S getService(Class<S> apiServiceClass) {
        RetrofitManager<S> instance = RetrofitManager.getInstance();
        return instance.getApiService(apiServiceClass);
    }

    public static <T> Observable<T> compose(Observable<T> observable) {
        return RxUtils.subscribe(observable);
    }

    public static <T> Flowable<T> compose(Flowable<T> flowAble) {
        return RxUtils.subscribe(flowAble);
    }
}
