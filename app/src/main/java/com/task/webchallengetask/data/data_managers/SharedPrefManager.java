package com.task.webchallengetask.data.data_managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.CheckResult;

import com.task.webchallengetask.App;
import com.task.webchallengetask.global.SharedPrefConst;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public final class SharedPrefManager {

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

    public void saveString(String _key, String _value) {
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

    public void saveHashMap(String _key, HashMap<String, String> _value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONObject jsonObject = new JSONObject(_value);
        String jsonString = jsonObject.toString();
        saveString(_key, jsonString);
    }

    public String retrieveString(String _s) {
        return sharedPreferences.getString(_s, "");
    }

    private boolean retrieveBoolean(String _s) {
        return sharedPreferences.getBoolean(_s, false);
    }

    private int retrieveInt(String _s) {
        return sharedPreferences.getInt(_s, 0);
    }

    public HashMap<String, String> retrieveMap(String _key) {
        HashMap<String, String> outputMap = new HashMap<String, String>();

        try {
            String map = retrieveString(_key);
            JSONObject jsonObject = new JSONObject(map);
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                String value = jsonObject.getString(key);
                outputMap.put(key, value);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }

    public void storeUsername(String _username) {
        saveString(SharedPrefConst.SHARED_PREF_USERNAME, _username);
    }

    public void storeWeight(int _weight) {
        saveInt(SharedPrefConst.SHARED_PREF_WEIGHT, _weight);
    }

    public void storeHeight(int _height) {
        saveInt(SharedPrefConst.SHARED_PREF_HEIGHT, _height);
    }

    public void storeGender(String _gender) {
        saveString(SharedPrefConst.SHARED_PREF_GENDER, _gender);
    }

    public int retrieveWeight() {
        return retrieveInt(SharedPrefConst.SHARED_PREF_WEIGHT);
    }

    public int retrieveHeight() {
        return retrieveInt(SharedPrefConst.SHARED_PREF_HEIGHT);
    }

    public String retrieveGender() {
        return retrieveString(SharedPrefConst.SHARED_PREF_GENDER);
    }

    @CheckResult
    public String retrieveUsername() {
        return retrieveString(SharedPrefConst.SHARED_PREF_USERNAME);
    }

    public void storeUrlPhoto(String _url) {
        saveString(SharedPrefConst.SHARED_PREF_URL_PHOTO, _url);
    }

    @CheckResult
    public String retrieveUrlPhoto() {
        return retrieveString(SharedPrefConst.SHARED_PREF_URL_PHOTO);
    }

    public void storeActiveSocial(String _s) {
        saveString(SharedPrefConst.SHARED_PREF_ACTIVE_SOCIAL, _s);
    }

    @CheckResult
    public int retrieveAge() {
        return retrieveInt(SharedPrefConst.SHARED_PREF_AGE);
    }

    public void storeAge(int _age) {
        saveInt(SharedPrefConst.SHARED_PREF_AGE, _age);
    }


    @CheckResult
    public int retrieveTimeSynchronization() {
        return retrieveInt(SharedPrefConst.SHARED_PREF_TIME_SYNCHRONIZATION);
    }

    public void storeTimeSynchronization(int _key) {
        saveInt(SharedPrefConst.SHARED_PREF_TIME_SYNCHRONIZATION, _key);
    }

    @CheckResult
    public String retrieveActiveSocial() {
        return retrieveString(SharedPrefConst.SHARED_PREF_ACTIVE_SOCIAL);
    }


    public void storeNotificationState(boolean _value) {
        saveBoolean(SharedPrefConst.SHARED_PREF_NOTIFICATION_STATE, _value);
    }

    public boolean isNotificationStateExist() {
        return sharedPreferences.contains(SharedPrefConst.SHARED_PREF_NOTIFICATION_STATE);
    }

    public boolean contains(String _key) {
        return sharedPreferences.contains(_key);
    }


    @CheckResult
    public boolean retrieveNotificationState() {
        return retrieveBoolean(SharedPrefConst.SHARED_PREF_NOTIFICATION_STATE);
    }

    public void storeProgramResults(HashMap<String, String> _map) {
        saveHashMap(SharedPrefConst.SHARED_PREF_RPOGRAM_RESULTS, _map);
    }

    public HashMap<String, String> retrieveProgramResults() {
        return retrieveMap(SharedPrefConst.SHARED_PREF_RPOGRAM_RESULTS);
    }

}
