package com.android.user.mvp.present;

import com.android.common.base.BasePresentImpl;
import com.android.common.network.BusinessObserver;
import com.android.common.network.ResponseBean;
import com.android.user.mvp.model.LoginApi;
import com.android.user.mvp.model.LoginRes;
import com.android.user.mvp.view.LoginView;

public class LoginPresent extends BasePresentImpl<LoginView, LoginApi> {

    public LoginPresent(LoginView loginView){
        super(loginView);
    }


    /**
     * 获取广告对象
     */
    public void login(){
        compose(getService(LoginApi.class).login())
            .subscribe(new BusinessObserver<LoginRes>() {

                @Override
                public void onSucceed(LoginRes userBean) {
                    mBaseView.onSucceed();
                    mBaseView.onLogin(userBean);
                }

                @Override
                public void onFailed(ResponseBean responseBean) {
                    mBaseView.onFailed(responseBean);
                }
            });
    }
}
