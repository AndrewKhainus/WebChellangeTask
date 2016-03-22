package com.task.webchallengetask.ui.activities.presenters;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.task.webchallengetask.App;
import com.task.webchallengetask.BuildConfig;
import com.task.webchallengetask.global.Constants;
import com.task.webchallengetask.global.utils.PredictionSample;
import com.task.webchallengetask.global.utils.SharedPrefManager;
import com.task.webchallengetask.ui.activities.MainActivity;
import com.task.webchallengetask.ui.base.BaseActivityPresenter;
import com.task.webchallengetask.ui.base.BaseView;

import rx.Observable;
import rx.schedulers.Schedulers;


public class LoginPresenter extends BaseActivityPresenter<LoginPresenter.LoginView> implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, FacebookCallback<LoginResult> {

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    private PendingIntent mSignInIntent;
    private int mSignInProgress;

    private static final int STATE_SIGNING_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

    private static final int PROFILE_PIC_SIZE = 100;

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        getView().setLoginPermission("publish_actions");
        mGoogleApiClient = buildGoogleApiClient();
        mCallbackManager = CallbackManager.Factory.create();
        getView().setCallbackManager(mCallbackManager);
    }

    public void onGoogleSignInClicked() {
        mGoogleApiClient.connect();
        resolveSignInError();
    }

    public void onPredictionClicked() {

        try {
            Observable.just(null)
                    .subscribeOn(Schedulers.newThread())
                    .doOnNext(o -> {
                        PredictionSample sample = new PredictionSample();
                        try {
                            sample.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }).subscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(App.getAppContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.PLUS_MOMENTS))
                .build();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.RC_SIGN_IN_GOOGLE_PLUS:
                if (resultCode == Activity.RESULT_OK) {
                    mSignInProgress = STATE_SIGNING_IN;
                } else {
                    mSignInProgress = Constants.RC_SIGN_IN_GOOGLE_PLUS;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;
            case Constants.RC_SIGN_IN_FACEBOOK:
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        AppEventsLogger.activateApp(App.getAppContext());
        mGoogleApiClient.connect();
//        revokeAccess();
    }

    private void revokeAccess() {
//        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//        Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
//        LoginManager.getInstance().logOut();
    }

    @Override
    public void onStop() {
        super.onStop();
        AppEventsLogger.deactivateApp(App.getAppContext());
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle _bundle) {
//        getView().showLoadingDialog();
        mSignInProgress = Constants.RC_SIGN_IN_GOOGLE_PLUS;

        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);


        if (currentPerson != null) {
            storeData(currentPerson);
        }
    }

    private void resolveSignInError() {
        if (mSignInIntent != null) {
            try {
                mSignInProgress = STATE_IN_PROGRESS;
                getView().startSenderIntent(mSignInIntent.getIntentSender(),
                        Constants.RC_SIGN_IN_GOOGLE_PLUS);
            } catch (IntentSender.SendIntentException e) {
                mSignInProgress = STATE_SIGNING_IN;
                mGoogleApiClient.connect();
            }
        }
    }

    private void storeData(Person _person) {
        SharedPrefManager.getInstance().storeUsername(_person.getDisplayName());
        String urlPhoto = _person.getImage().getUrl();
        urlPhoto = urlPhoto.substring(0, urlPhoto.length() - 2)
                + PROFILE_PIC_SIZE;

        SharedPrefManager.getInstance().storeUrlPhoto(urlPhoto);
        SharedPrefManager.getInstance().storeActiveSocial(Constants.SOCIAL_GOOGLE_PLUS);

        Picasso.with(App.getAppContext())
                .load(urlPhoto)
                .fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        getView().hideLoadingDialog();
                        startMainActivity();
                    }

                    @Override
                    public void onError() {
                        getView().showErrorDialog("Failure", "Something went wrong", null);
                    }
                });

    }

    private void startMainActivity() {
        getView().startActivity(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getView().finishActivity();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult _connectionResult) {
        if (mSignInProgress != STATE_IN_PROGRESS) {
            mSignInIntent = _connectionResult.getResolution();
            if (mSignInProgress == STATE_SIGNING_IN) {
                resolveSignInError();
            }
        }
    }

    @Override
    public void onSuccess(LoginResult result) {
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                this.stopTracking();
                getView().showLoadingDialog();
                SharedPrefManager.getInstance().storeUsername(currentProfile.getName());
                SharedPrefManager.getInstance().storeUrlPhoto(currentProfile.getProfilePictureUri(PROFILE_PIC_SIZE, PROFILE_PIC_SIZE).toString());
                SharedPrefManager.getInstance().storeActiveSocial(Constants.SOCIAL_FACEBOOK);
                Picasso.with(App.getAppContext())
                        .load(currentProfile.getProfilePictureUri(PROFILE_PIC_SIZE, PROFILE_PIC_SIZE))
                        .fetch(new Callback() {
                            @Override
                            public void onSuccess() {
                                getView().hideLoadingDialog();
                                startMainActivity();
                            }

                            @Override
                            public void onError() {
                                getView().showErrorDialog("Failure", "Something went wrong", null);
                            }
                        });
            }
        };
        profileTracker.startTracking();
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onError(FacebookException error) {
        getView().showErrorDialog("Failure", "Something went wrong", null);
    }

    public interface LoginView extends BaseView {
        void startSenderIntent(IntentSender _intentSender, int _const) throws IntentSender.SendIntentException;

        void setLoginPermission(String _loginPermission);

        void setCallbackManager(CallbackManager _callbackManager);
    }

}

