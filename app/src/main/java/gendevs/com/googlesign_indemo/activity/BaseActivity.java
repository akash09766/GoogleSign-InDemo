package gendevs.com.googlesign_indemo.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import gendevs.com.googlesign_indemo.contstant.Constants;

/**
 * Created by Akash on 09/02/16.
 */
public class BaseActivity extends AppCompatActivity{


    protected String getPreferencesString(String key, String defaultVaule, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, defaultVaule);
    }

    protected void saveStringInPreferences(String key, String value,Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    protected Boolean getBooleanInPreference(String key, Boolean defaultVaule, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defaultVaule);
    }

    protected void saveBooleanInPreference(String key, boolean value , Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    protected int getIntInPreference(String key, int defaultVaule, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(key, defaultVaule);
    }

    protected void saveIntInPreference(String key, int value , Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                Constants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void showShortSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);

        snackbar.show();
    }

    public void showLongSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}
