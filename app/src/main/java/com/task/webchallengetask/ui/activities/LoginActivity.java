package com.task.webchallengetask.ui.activities;

import android.os.Bundle;
import android.widget.Button;

import com.task.webchallengetask.R;
import com.task.webchallengetask.global.utils.RxUtils;
import com.task.webchallengetask.ui.activities.presenters.LoginPresenter;
import com.task.webchallengetask.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginPresenter.LoginView{

    private Button btnFacebook;
    private Button btnGoogle;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void findUI() {
        btnFacebook = (Button) findViewById(R.id.btnFacebook_AL);
        btnGoogle = (Button) findViewById(R.id.btnGoogle_AL);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        RxUtils.click(btnFacebook, o -> getPresenter().onFacebookClicked());
        RxUtils.click(btnGoogle, o -> getPresenter().onGoogleClicked());
    }
}
