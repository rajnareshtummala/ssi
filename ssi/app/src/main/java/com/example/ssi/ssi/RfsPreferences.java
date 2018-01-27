package com.example.ssi.ssi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RfsPreferences {

    private static final String APP_SHARED_PREFS = "com.example.ssi";
    private SharedPreferences appSharedPrefs;
    private Editor prefsEditor;

    public RfsPreferences(Context context) {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    /*
     *    Delete the all the preferences
	 */
    public void deletePref() {
        this.prefsEditor.clear();
        this.prefsEditor.commit();
    }

    public void saveLatitude(String latitude) {
        prefsEditor.putString("lat", latitude);
        prefsEditor.commit();
    }

    public String getLatitude() {
        return appSharedPrefs.getString("lat", "00.00");
    }

    public void saveLongitude(String longitude) {
        prefsEditor.putString("longitude", longitude);
        prefsEditor.commit();
    }

    public String getLongitude() {
        return appSharedPrefs.getString("longitude", "00.00");
    }




}
