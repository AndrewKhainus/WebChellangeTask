package com.task.webchallengetask.global;

public abstract class Constants {

    public static final String PREDICTION_END_POINT = "https://www.googleapis.com";
    public static final int TIMEOUT = 10;  //seconds


    public static final String APPLICATION_NAME = "HelloPrediction";
    public static final String STORAGE_DATA_LOCATION = "webchallengebuckettrain/language_id.txt";
    public static final String MODEL_ID = "languageidentifier";
    public static final String PROJECT_ID = "WebChallengeProject";
    public static final String SERVICE_ACCT_EMAIL = "1061853864883-compute@developer.gserviceaccount.com";
    public static final String SERVICE_ACCT_KEYFILE = "key.p12";


    public static final int RC_SIGN_IN_GOOGLE_PLUS = 0;
    public static final int RC_SIGN_IN_FACEBOOK = 64206;

    public static final String SOCIAL_GOOGLE_PLUS = "google plus";
    public static final String SOCIAL_FACEBOOK = "facebook";



    public static final String MAIN_ACTION = "action.main";

    public static final String START_TIMER_ACTION = "START_TIMER_ACTION";
    public static final String PAUSE_TIMER_ACTION = "PAUSE_TIMER_ACTION";
    public static final String STOP_TIMER_ACTION = "STOP_TIMER_ACTION";

    public static final String SEND_TIMER_UPDATE_ACTION = "SEND_TIMER_UPDATE_ACTION";
    public static final String SEND_TIMER_UPDATE_KEY = "SEND_TIMER_UPDATE_KEY";
    public static final String ACTIVITY_NAME_KEY = "ACTIVITY_NAME_KEY";

    public static int FOREGROUND_NOTIFICATION_SERVICE_ID = 101;


}
