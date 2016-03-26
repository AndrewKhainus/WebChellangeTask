package com.task.webchallengetask.ui.modules.main;

import android.app.PendingIntent;
import android.content.Intent;
<<<<<<< HEAD:app/src/main/java/com/task/webchallengetask/ui/activities/presenters/MainActivityPresenter.java
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.GoogleApiUtils;
import com.task.webchallengetask.global.utils.SharedPrefManager;
import com.task.webchallengetask.ui.activities.LoginActivity;
import com.task.webchallengetask.ui.activities.MainActivity;
import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseActivityView;
import com.task.webchallengetask.ui.fragments.ActivityListFragment;
import com.task.webchallengetask.ui.fragments.AnalyticsFragment;
import com.task.webchallengetask.ui.fragments.ProgramsListFragment;
import com.task.webchallengetask.ui.fragments.SettingsFragment;

public class MainActivityPresenter extends BaseActivityPresenter<MainActivityPresenter.MainView> implements GoogleApiClient.ConnectionCallbacks {

    private PendingIntent mSignInIntent;
    private GoogleApiClient googleApiClient;
=======

import com.facebook.login.LoginManager;
import com.task.webchallengetask.data.data_providers.PredictionDataProvider;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.data.data_managers.GoogleApiUtils;
import com.task.webchallengetask.global.utils.Logger;
import com.task.webchallengetask.data.data_managers.SharedPrefManager;
import com.task.webchallengetask.ui.modules.login.LoginActivity;
import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseActivityView;
import com.task.webchallengetask.ui.modules.activity.views.ActivityListFragment;
import com.task.webchallengetask.ui.modules.analytics.AnalyticsFragment;
import com.task.webchallengetask.ui.modules.program.ProgramsListFragment;
import com.task.webchallengetask.ui.modules.settings.SettingsFragment;
>>>>>>> 98afec53d2551c676bef39fdc5c655b54d058f3c:app/src/main/java/com/task/webchallengetask/ui/modules/main/MainActivityPresenter.java


    @Override
    public void onViewCreated() {
        super.onViewCreated();
        GoogleApiUtils.getInstance().buildGoogleApiClient();
        if (TextUtils.equals(SharedPrefManager.getInstance().retrieveActiveSocial(), Constants.SOCIAL_FACEBOOK)) {
            googleApiClient = setupGoogleApiClient(this, _connectionResult -> {
                mSignInIntent = _connectionResult.getResolution();
                resolveSignInError();
            }, false);
        }
        getView().switchFragment(ActivityListFragment.newInstance(), false);
        getView().setHeaderTitle(SharedPrefManager.getInstance().retrieveUsername());
        getView().setHeaderSubTitle("Cool men");

        PredictionDataProvider.getInstance().connectAndTrain()
                .subscribe(aBoolean -> {}, Logger::e);
    }

    private void resolveSignInError() {
        if (mSignInIntent != null) {
            try {
                getView().startSenderIntent(mSignInIntent.getIntentSender(),
                        Constants.RC_SIGN_IN_GOOGLE_PLUS);
            } catch (IntentSender.SendIntentException e) {
                googleApiClient.connect();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getView().isDrawerOpen()) {
            getView().closeDrawer(null);
        } else
            super.onBackPressed();
    }

    public void onActivityListClicked() {
        getView().closeDrawer(() ->
                getView().switchFragment(ActivityListFragment.newInstance(), false));
    }

    public void onAnalyticsClicked() {
        getView().closeDrawer(() ->
                getView().switchFragment(AnalyticsFragment.newInstance(), false));
    }

    public void onProgramsClicked() {
        getView().closeDrawer(() ->
                getView().switchFragment(ProgramsListFragment.newInstance(), false));
    }

    public void onSettingsClicked() {
        getView().closeDrawer(() ->
                getView().switchFragment(SettingsFragment.newInstance(), false));
    }

    public void onLogoutClicked() {
        getView().showConfirmDialog("Confirm",
                "Do you really want to logout?",
                v -> {
                    revokeAccess();
                    getView().startActivity(LoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getView().finishActivity();
                });
    }

    private void revokeAccess() {
        switch (SharedPrefManager.getInstance().retrieveActiveSocial()) {
            case Constants.SOCIAL_FACEBOOK:
                LoginManager.getInstance().logOut();
                break;
            case Constants.SOCIAL_GOOGLE_PLUS:
                if (GoogleApiUtils.getInstance().buildGoogleApiClientWithGooglePlus() != null &&
                        GoogleApiUtils.getInstance().buildGoogleApiClientWithGooglePlus().isConnected())
                    GoogleApiUtils.getInstance().logoutGooglePlus();
        }
        SharedPrefManager.getInstance().storeUsername("");
        SharedPrefManager.getInstance().storeActiveSocial("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle _bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public interface MainView extends BaseActivityView<MainActivityPresenter> {
        boolean isDrawerOpen();

        void closeDrawer(MainActivity.DrawerCallBack _callback);

        void setHeaderSubTitle(String _description);

        void setHeaderTitle(String _title);

        void startSenderIntent(IntentSender _intentSender, int _const) throws IntentSender.SendIntentException;


    }

}