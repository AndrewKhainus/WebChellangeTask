package com.task.webchallengetask.ui.activities.presenters;

import android.content.Intent;
import android.text.TextUtils;

import com.task.webchallengetask.global.utils.SharedPrefManager;
import com.task.webchallengetask.ui.activities.LoginActivity;
import com.task.webchallengetask.ui.activities.MainActivity;
import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseView;

/**
 * Created by Sergbek on 22.03.2016.
 */
public class CheckPresenter extends BaseActivityPresenter<CheckPresenter.CheckView> {

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        if (!TextUtils.isEmpty(SharedPrefManager.getInstance().retrieveUsername())
                && !TextUtils.isEmpty(SharedPrefManager.getInstance().retrieveActiveSocial())) {
            getView().startActivity(MainActivity.class, Intent.FLAG_ACTIVITY_NO_ANIMATION, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            getView().startActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_NO_ANIMATION, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        getView().finishActivity();
    }

    public interface CheckView extends BaseView {}
}
