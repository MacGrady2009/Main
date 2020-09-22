package com.android.user.mvp.view;

import com.android.common.base.BaseView;
import com.android.user.mvp.model.LoginRes;

public interface LoginView extends BaseView {

    void onLogin(LoginRes userBean);
}
