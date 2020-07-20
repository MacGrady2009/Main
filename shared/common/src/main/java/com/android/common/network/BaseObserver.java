package com.android.common.network;

import com.android.common.base.BaseBean;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.Response;


abstract class BaseObserver<T> implements Observer<T> {

    Disposable disposable;
    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(T t) {
        if (t instanceof Response ) {
            Response response = (Response)t;
            if (response.isSuccessful()) {
                if (response.body() instanceof ResponseBean){
                    onSuccess(null,(ResponseBean)response.body());
                }
            }else {
                failed(response.code(),response.message());
            }
        }else {
            onSuccess(t,null);
        }

    }


    @Override
    public void onError(Throwable e) {
        failed(Constant.ERROR,e.getMessage());
    }

    private void failed(int code, String msg){
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
        if (disposable != null && disposable.isDisposed()){
            disposable.dispose();
        }
    }

    public abstract void onSuccess(T t,ResponseBean responseBean);

    public abstract void onFailed(ResponseBean responseBean);
}
