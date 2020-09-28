package com.android.user.mvp.view;

import android.os.Bundle;
import android.widget.Button;
import com.android.common.base.BaseActivity;
import com.android.common.constant.ModuleConstant;
import com.android.common.utils.Router;
import com.android.common.utils.SpUtils;
import com.android.user.BuildConfig;
import com.android.user.R;
import com.android.user.mvp.model.LoginRes;
import com.android.user.mvp.present.LoginPresent;

public class LoginActivity extends BaseActivity implements LoginView{

    private LoginPresent present;

    private Button mLoginBtn;
    @Override
    protected int onSetRootViewId() {
        return R.layout.activity_login;
    }

    protected void onFindView() {
        mLoginBtn = findViewById(R.id.btn_login);
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        present = new LoginPresent(this);
    }

    protected void onInitEvent(){
        mLoginBtn.setOnClickListener(view -> {
            showProgress();
            present.login();
        });
    }

    @Override
    public void onLogin(LoginRes userBean) {
        hideProgress();
        SpUtils.putBoolean(this, ModuleConstant.IS_LOGIN,true);
        skip();
    }

    private void skip(){
        Router.getInstance().startActivity(this,null, BuildConfig.MAIN);
        finish();
    }
}
