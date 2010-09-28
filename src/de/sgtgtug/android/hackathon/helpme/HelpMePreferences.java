package de.sgtgtug.android.hackathon.helpme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;

public class HelpMePreferences extends PreferenceActivity implements
		OnPreferenceChangeListener {

	private Preference shareContactsPreference;

	public static final String PREFERENCE_USER_NAME = "user_name";
	public static final String PREFERENCE_USER_SEX = "user_sex";
	public static final String PREFERENCE_USER_AGE = "user_age";
	public static final String PREFERENCE_SECURITY_NR = "user_security_nr";
	public static final String PREFERENCE_CONTACTS = "contacts";

	protected static final int ACTIVITY_RESULT_CONTACTS = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		findPreference(HelpMePreferences.PREFERENCE_USER_NAME)
				.setOnPreferenceChangeListener(this);
		findPreference(HelpMePreferences.PREFERENCE_USER_SEX)
				.setOnPreferenceChangeListener(this);
		findPreference(HelpMePreferences.PREFERENCE_USER_AGE)
				.setOnPreferenceChangeListener(this);
		findPreference(HelpMePreferences.PREFERENCE_SECURITY_NR)
				.setOnPreferenceChangeListener(this);

		configurePreferences();
	}

	private void configurePreferences() {

		shareContactsPreference = findPreference(this.PREFERENCE_CONTACTS);
		shareContactsPreference
				.setIntent(new Intent(this, ContactManager.class));
		shareContactsPreference.setTitle(R.string.contacts_pref_title);
		shareContactsPreference.setSummary(R.string.contacts_pref_summary);

		populateData();

	}

	// @Override
	// public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
	// Preference preference) {
	// String key = preference.getKey();
	//
	// if (HelpMePreferences.PREFERENCE_CONTACTS.equals(key)) {
	// // startActivityForResult(new Intent(getApplicationContext(),
	// // ContactManager), ACTIVITY_RESULT_CONTACTS);
	// startActivity(new Intent(getApplicationContext(),
	// ContactManager.class));
	//
	// }
	//
	// return super.onPreferenceTreeClick(preferenceScreen, preference);
	// }

	/**
	 * populate user data
	 */
	private void populateData() {

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		findPreference(HelpMePreferences.PREFERENCE_USER_NAME).setSummary(
				sharedPrefs.getString(HelpMePreferences.PREFERENCE_USER_NAME,
						getString(R.string.preferences_name)));

		findPreference(HelpMePreferences.PREFERENCE_USER_AGE).setSummary(
				sharedPrefs.getString(HelpMePreferences.PREFERENCE_USER_AGE,
						getString(R.string.preferences_age)));

		String strSexSummary = getString(R.string.preferences_sex);

		switch (new Integer(sharedPrefs.getString(
				HelpMePreferences.PREFERENCE_USER_SEX, "-1"))) {
		case 0:
			strSexSummary = getString(R.string.preferences_sex_female);
			break;
		case 1:
			strSexSummary = getString(R.string.preferences_sex_male);
			break;
		}
		findPreference(HelpMePreferences.PREFERENCE_USER_SEX).setSummary(
				strSexSummary);

		findPreference(HelpMePreferences.PREFERENCE_SECURITY_NR).setSummary(
				sharedPrefs.getString(HelpMePreferences.PREFERENCE_SECURITY_NR,
						getString(R.string.preferences_insurance_id)));

	}

	public boolean onPreferenceChange(Preference preference, Object newValue) {

		/*
		 * TODO: add values validation
		 */

		if (preference.getKey().equals(PREFERENCE_USER_NAME)) {
			preference.setSummary(newValue.toString());
			return true;
		}

		if (preference.getKey().equals(PREFERENCE_USER_AGE)) {
			preference.setSummary(newValue.toString());
			return true;
		}
		if (preference.getKey().equals(PREFERENCE_USER_SEX)) {

			String strSexSummary = getString(R.string.preferences_sex);

			switch (new Integer(newValue.toString())) {
			case 0:
				strSexSummary = getString(R.string.preferences_sex_female);
				break;
			case 1:
				strSexSummary = getString(R.string.preferences_sex_male);
				break;
			}
			preference.setSummary(strSexSummary);

			return true;
		}
		if (preference.getKey().equals(PREFERENCE_SECURITY_NR)) {
			preference.setSummary(newValue.toString());
			return true;
		}

		return false;
	}

}
