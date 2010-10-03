package de.sgtgtug.android.hackathon.helpme;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class HelpMeApp extends Application {
    
	public static String LOG_TAG = "HelpME";
    public final static int NOTIFICATION_ID = 0;
    
    public static class Preferences {

        private static SharedPreferences sharedPreferences;

        private static SharedPreferences.Editor editor;

        public Preferences(Context context) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize global preferences
        new Preferences(this);
    }
    
}
