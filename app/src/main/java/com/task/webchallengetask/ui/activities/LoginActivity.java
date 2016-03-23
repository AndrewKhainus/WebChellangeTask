package com.task.webchallengetask.ui.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.task.webchallengetask.App;
import com.task.webchallengetask.R;
import com.task.webchallengetask.global.utils.Logger;
import com.task.webchallengetask.global.utils.RxUtils;
import com.task.webchallengetask.ui.activities.presenters.LoginPresenter;
import com.task.webchallengetask.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginPresenter.LoginView {

    private SignInButton btnSignIn;
    private LoginButton loginButton;
    private Button btnPrediction;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void findUI(View _rootView) {
        btnSignIn = (SignInButton) findViewById(R.id.btnSignIn_AL);
        loginButton = (LoginButton) findViewById(R.id.btnFacebookLogin_AL);
        btnPrediction = (Button) findViewById(R.id.btnPrediction_AL);
    }

    @Override
    public void setupUI() {
        btnSignIn.setStyle(SignInButton.SIZE_STANDARD, SignInButton.COLOR_DARK);
        setGooglePlusButtonText(btnSignIn, "Sign up with google +");
        RxUtils.click(btnSignIn, o -> getPresenter().onGoogleSignInClicked());
        RxUtils.click(btnPrediction, o -> getPresenter().onPredictionClicked());
    }

    @Override
    public void startSenderIntent(IntentSender _intentSender, int _const) throws IntentSender.SendIntentException{
        this.startIntentSenderForResult(_intentSender,
                _const, null, 0, 0, 0);
    }

    @Override
    public void setLoginPermission(String _loginPermission) {
        loginButton.setPublishPermissions(_loginPermission);
    }

    @Override
    public void setCallbackManager(CallbackManager _callbackManager) {
        loginButton.registerCallback(_callbackManager, getPresenter());
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                tv.setTextSize(16);
                tv.setAllCaps(true);
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getPresenter().onActivityResult(requestCode, resultCode, data);
    }
}
