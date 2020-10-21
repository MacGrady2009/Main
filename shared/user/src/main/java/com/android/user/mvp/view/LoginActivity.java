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
import com.android.user.mvp.view.LoginView;

public class LoginActivity extends BaseActivity {

    @Override
    protected int onSetRootViewId() {
        return R.layout.activity_login;
    }
}
