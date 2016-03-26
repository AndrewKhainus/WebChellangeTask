package com.task.webchallengetask.ui.modules.main;

import android.content.Intent;

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

public class MainActivityPresenter extends BaseActivityPresenter<MainActivityPresenter.MainView> {

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        GoogleApiUtils.getInstance().buildGoogleApiClient();
        getView().switchFragment(ActivityListFragment.newInstance(), false);
        getView().setHeaderTitle(SharedPrefManager.getInstance().retrieveUsername());
        getView().setHeaderSubTitle("Cool men");

        PredictionDataProvider.getInstance().connectAndTrain()
                .subscribe(aBoolean -> {}, Logger::e);
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
        switch (SharedPrefManager.getInstance().retrieveActiveSocial()){
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

    public interface MainView extends BaseActivityView<MainActivityPresenter> {
        boolean isDrawerOpen();

        void closeDrawer(MainActivity.DrawerCallBack _callback);

        void setHeaderSubTitle(String _description);

        void setHeaderTitle(String _title);

    }

}
