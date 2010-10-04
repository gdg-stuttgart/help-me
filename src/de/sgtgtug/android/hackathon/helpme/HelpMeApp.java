package de.sgtgtug.android.hackathon.helpme;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class HelpMeApp extends Application {
    
	public static String LOG_TAG = "HelpME";
	
	private static HelpMeApp singeltonInstance;
	private final HandlerThread mHandlerThread = new HandlerThread("AppHandlerThread");
	private Handler mHandler;
    
    public static class Preferences {

        private static SharedPreferences sharedPreferences;

        private static SharedPreferences.Editor editor;

        public Preferences(Context context) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
        }
        
        public static Boolean firstLaunchofApplication() {
        	return sharedPreferences.getBoolean("initial_launch", false);
        }
    }
    
    public static HelpMeApp getInstance() {
    	return singeltonInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singeltonInstance = this;
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());		
        
        // initialize global preferences
        new Preferences(this);
    }
    
    public void showToast(final String string, final int duration) {
        mHandler.post(new Runnable() {
            public void run() {
                Toast.makeText(HelpMeApp.this, string, duration).show();
            }
        });
    }
    
}
