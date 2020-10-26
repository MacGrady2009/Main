package com.android.common.base;

import com.android.common.constant.Constant;
import com.android.common.network.ResponseBean;
import com.android.common.utils.RxUtils;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {

    Disposable disposable;
    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        failed(Constant.ERROR,e.getMessage());
    }

    protected void failed(int code, String msg){
        ResponseBean result = new ResponseBean();
        result.setCode(code);
        result.setMessage(msg);
        onFailed(result);
        unSubscribe();
    }


    @Override
    public void onComplete() {
        unSubscribe();
    }

    private void unSubscribe(){
        RxUtils.dispose(disposable);
    }

    public abstract void onSuccess(T t);

    public abstract void onFailed(ResponseBean responseBean);
}
