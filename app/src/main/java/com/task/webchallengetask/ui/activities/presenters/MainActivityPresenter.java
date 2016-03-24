package com.task.webchallengetask.ui.activities.presenters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.squareup.picasso.Picasso;
import com.task.webchallengetask.App;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.SharedPrefConst;
import com.task.webchallengetask.global.utils.GoogleApiUtils;
import com.task.webchallengetask.global.utils.SharedPrefManager;
import com.task.webchallengetask.ui.activities.LoginActivity;
import com.task.webchallengetask.ui.activities.MainActivity;
import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseActivityView;
import com.task.webchallengetask.ui.base.BaseView;
import com.task.webchallengetask.ui.fragments.ActivityListFragment;
import com.task.webchallengetask.ui.fragments.AnalyticsFragment;
import com.task.webchallengetask.ui.fragments.ProgramsListFragment;
import com.task.webchallengetask.ui.fragments.SettingsFragment;

import java.io.IOException;

public class MainActivityPresenter extends BaseActivityPresenter<MainActivityPresenter.MainView> {

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        GoogleApiUtils.getInstance().buildGoogleApiClient();
        getView().switchFragment(ActivityListFragment.newInstance(), false);
        getView().setHeaderTitle(SharedPrefManager.getInstance().retrieveUsername());
        getView().setHeaderSubTitle("Cool men");
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
