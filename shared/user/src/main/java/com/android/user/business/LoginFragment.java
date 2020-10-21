package com.android.user.business;

import android.widget.Button;
import com.android.common.base.BaseFragment;
import com.android.common.constant.ModuleConstant;
import com.android.common.utils.Router;
import com.android.common.utils.SpUtils;
import com.android.user.BuildConfig;
import com.android.user.R;
import com.android.user.mvp.model.LoginRes;
import com.android.user.mvp.present.LoginPresent;
import com.android.user.mvp.view.LoginView;

public class LoginFragment extends BaseFragment
    implements LoginView {

    private LoginPresent present;

    private Button mLoginBtn;
    @Override
    protected int onSetRootViewId() {
        return R.layout.fragment_login;
    }

    protected void onFindView() {
        mLoginBtn = mRootView.findViewById(R.id.btn_login);
    }

    @Override
    protected void onInitView() {
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
        SpUtils.putBoolean(this.getContext(), ModuleConstant.IS_LOGIN,true);
        skip();
    }

    private void skip(){
        Router.getInstance().startActivity(this.getContext(),null, BuildConfig.MAIN);
        this.getActivity().finish();
    }
}
