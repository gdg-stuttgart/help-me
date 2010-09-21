package de.sgtgtug.hackathon;


import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;

public class HelpMePreferences extends PreferenceActivity {

	private Preference shareContactsPreference;
	public static final String PREFERENCE_CONTACTS = "contacts";
	protected static final int ACTIVITY_RESULT_CONTACTS = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		configurePreferences();
	}

	private void configurePreferences() {
		shareContactsPreference = findPreference(HelpMePreferences.PREFERENCE_CONTACTS);
		shareContactsPreference.setIntent(new Intent(this, ContactManager.class));
		shareContactsPreference.setTitle(R.string.contacts_pref_title);
		shareContactsPreference.setSummary(R.string.contacts_pref_summary);
	}

//	@Override
//	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
//			Preference preference) {
//		String key = preference.getKey();
//
//		if (HelpMePreferences.PREFERENCE_CONTACTS.equals(key)) {
////			startActivityForResult(new Intent(getApplicationContext(),
////					ContactManager), ACTIVITY_RESULT_CONTACTS);
//			startActivity(new Intent(getApplicationContext(),
//					ContactManager.class));
//			
//		}
//
//		return super.onPreferenceTreeClick(preferenceScreen, preference);
//	}

}
