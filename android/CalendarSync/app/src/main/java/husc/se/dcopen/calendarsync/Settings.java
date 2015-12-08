package husc.se.dcopen.calendarsync;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class Settings {
//    public static final String MY_PREFS = "SettingPrefs";
    public static final String USER_NAME = "UserName";
    public static final String PASSWORD = "Password";
    public static final String REMEMBERR = "Remember";
    public static final String SYNC_COLOR = "SyncColor";
    public static final String NO_SYNC_COLOR = "NoSyncColor";
    public static final String NUMBER_DATE_SYNC_UP = "NumberDateSyncUp";
    public static final String NUMBER_DATE_SYNC_DOWN = "NumberDateSyncDown";
    public static final String TASK_TODATE_COLOR = "TaskToDateColor";

    private SharedPreferences sharedPreferences;

    public Settings(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public Settings putUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
        return this;
    }

    public Settings putPassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD, password);
        editor.commit();
        return this;
    }

    public Settings putRemember(boolean remember) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(REMEMBERR, remember);
        editor.commit();
        return this;
    }

    public Settings putSyncColor(int color) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SYNC_COLOR, color);
        editor.commit();
        return this;
    }

    public Settings putNoSyncColor(int color) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NO_SYNC_COLOR, color);
        editor.commit();
        return this;
    }

    public Settings putTaskToDateColor(int color) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TASK_TODATE_COLOR, color);
        editor.commit();
        return this;
    }

    public Settings putNumberDateSyncUp(int numberDate) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NUMBER_DATE_SYNC_UP, numberDate);
        editor.commit();
        return this;
    }

    public Settings putNumberDateSyncDown(int numberDate) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NUMBER_DATE_SYNC_DOWN, numberDate);
        editor.commit();
        return this;
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, "");
    }

    public boolean isRemember() {
        return sharedPreferences.getBoolean(REMEMBERR, false);
    }

    public int getSyncColor() {
        return sharedPreferences.getInt(SYNC_COLOR, Color.parseColor("#8bc34a"));
    }

    public int getNoSyncColor() {
        return sharedPreferences.getInt(NO_SYNC_COLOR, Color.parseColor("#9e9e9e"));
    }

    public int getTaskToDateColor() {
        return sharedPreferences.getInt(TASK_TODATE_COLOR, Color.parseColor("#009688"));
    }

    public int getNumberDateSyncUp() {
        return sharedPreferences.getInt(NUMBER_DATE_SYNC_UP, 30);
    }

    public int getNumberDateSyncDown() {
        return sharedPreferences.getInt(NUMBER_DATE_SYNC_DOWN, 30);
    }
}
