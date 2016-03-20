package com.task.webchallengetask.ui.activities.presenters;

import com.task.webchallengetask.ui.activities.MainActivity;
import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseView;

/**
 * Created by andri on 20.03.2016.
 */
public class LoginPresenter extends BaseActivityPresenter<LoginPresenter.LoginView> {

    public void onFacebookClicked() {
        getView().startActivity(MainActivity.class, null);
    }

    public void onGoogleClicked() {
        getView().showInfoDialog("Example", "Login with facebook", null);
    }

    public interface LoginView extends BaseView {

    }

}
