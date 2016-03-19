package com.task.webchallengetask.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by klim on 21.10.15.
 */
public class SharedPrefManager {

    private static SharedPrefManager instance;
    private SharedPreferences sharedPreferences;

    private SharedPrefManager(Context _context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
    }

    public static SharedPrefManager getInstance() {
        if (instance == null) {
            instance = new SharedPrefManager(App.getAppContext());
        }
        return instance;
    }

    private void saveString(String _key, String _value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(_key, _value);
        editor.apply();
    }

    private void saveInt(String _key, int _value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(_key, _value);
        editor.apply();
    }

    private void saveBoolean(String _key, boolean _value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(_key, _value);
        editor.apply();
    }

    private String retrieveString(String _s) {
        return sharedPreferences.getString(_s, "");
    }

    private boolean retrieveBoolean(String _s) {
        return sharedPreferences.getBoolean(_s, false);
    }

    private int retrieveInt(String _s) {
        return sharedPreferences.getInt(_s, -1);
    }

    public boolean retrieveRememberMe() {
        return retrieveBoolean(SharedPrefConst.REMEMBER_ME);
    }

    public void storeRememberMe(boolean _remember) {
        saveBoolean(SharedPrefConst.REMEMBER_ME, _remember);
    }

    public void storeLanguage(String _localeLanguage) {
        saveString(SharedPrefConst.APP_LANGUAGE, _localeLanguage);
    }

    public String retrieveLanguage() {
        return retrieveString(SharedPrefConst.APP_LANGUAGE);
    }

}
