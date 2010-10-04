package de.sgtgtug.android.hackathon.helpme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;

public class HelpMEPreferences extends PreferenceActivity implements
		OnPreferenceChangeListener {

	private Preference shareContactsPreference;

	public static final String PREFERENCE_CONTACTS = "contacts";
	public static final String PREFERENCE_USER_NAME = "user_name";
	public static final String PREFERENCE_USER_SEX = "user_sex";
	public static final String PREFERENCE_USER_AGE = "user_age";
	public static final String PREFERENCE_SECURITY_NR = "user_security_nr";

	// Notifications
	public static final String PREFERENCE_USE_SMS_MSG = "use_sms";
	public static final String PREFERENCE_USE_EMAIL_MSG = "use_email";

	// Speech services
	public static final String PREFERENCE_USE_SPEECH_SERVICES = "use_tts_stt";
	public static final String PREFERENCE_SPEECH_SERVICES_LOCALES = "locales_tts_stt";
	public static final String PREFERENCE_SPEECH_SERVICES_LANGUAGE_MODEL = "language_model_tts_stt";

	protected static final int ACTIVITY_RESULT_CONTACTS = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		findPreference(HelpMEPreferences.PREFERENCE_USER_NAME)
				.setOnPreferenceChangeListener(this);
		findPreference(HelpMEPreferences.PREFERENCE_USER_SEX)
				.setOnPreferenceChangeListener(this);
		findPreference(HelpMEPreferences.PREFERENCE_USER_AGE)
				.setOnPreferenceChangeListener(this);
		findPreference(HelpMEPreferences.PREFERENCE_SECURITY_NR)
				.setOnPreferenceChangeListener(this);
		findPreference(HelpMEPreferences.PREFERENCE_USE_SPEECH_SERVICES)
				.setOnPreferenceChangeListener(this);
		findPreference(HelpMEPreferences.PREFERENCE_USE_SMS_MSG)
				.setOnPreferenceChangeListener(this);
		findPreference(HelpMEPreferences.PREFERENCE_USE_EMAIL_MSG)
				.setOnPreferenceChangeListener(this);
		findPreference(HelpMEPreferences.PREFERENCE_SPEECH_SERVICES_LOCALES)
				.setOnPreferenceChangeListener(this);
		findPreference(HelpMEPreferences.PREFERENCE_SPEECH_SERVICES_LANGUAGE_MODEL)
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

		findPreference(HelpMEPreferences.PREFERENCE_USER_NAME).setSummary(
				sharedPrefs.getString(HelpMEPreferences.PREFERENCE_USER_NAME,
						getString(R.string.preferences_name)));

		findPreference(HelpMEPreferences.PREFERENCE_USER_AGE).setSummary(
				sharedPrefs.getString(HelpMEPreferences.PREFERENCE_USER_AGE,
						getString(R.string.preferences_age)));

		String strSexSummary = getString(R.string.preferences_sex);

		switch (new Integer(sharedPrefs.getString(
				HelpMEPreferences.PREFERENCE_USER_SEX, "-1"))) {
		case 0:
			strSexSummary = getString(R.string.preferences_sex_female);
			break;
		case 1:
			strSexSummary = getString(R.string.preferences_sex_male);
			break;
		}
		findPreference(HelpMEPreferences.PREFERENCE_USER_SEX).setSummary(
				strSexSummary);

		findPreference(HelpMEPreferences.PREFERENCE_SECURITY_NR).setSummary(
				sharedPrefs.getString(HelpMEPreferences.PREFERENCE_SECURITY_NR,
						getString(R.string.preferences_insurance_id)));

		findPreference(HelpMEPreferences.PREFERENCE_USE_SPEECH_SERVICES)
				.setSummary(
						sharedPrefs
								.getBoolean(
										HelpMEPreferences.PREFERENCE_USE_SPEECH_SERVICES,
										true) ? getString(R.string.summary_speech_services_enabled_preference)
								: getString(R.string.summary_speech_services_disabled_preference));

		findPreference(HelpMEPreferences.PREFERENCE_USE_SMS_MSG)
				.setSummary(
						sharedPrefs.getBoolean(
								HelpMEPreferences.PREFERENCE_USE_SMS_MSG, true) ? getString(R.string.summary_sms_enabled_preference)
								: getString(R.string.summary_sms_disabled_preference));

		findPreference(HelpMEPreferences.PREFERENCE_USE_EMAIL_MSG)
				.setSummary(
						sharedPrefs.getBoolean(
								HelpMEPreferences.PREFERENCE_USE_EMAIL_MSG,
								true) ? getString(R.string.summary_email_enabled_preference)
								: getString(R.string.summary_email_disabled_preference));

		StringBuffer selectedLocale = new StringBuffer(
				getString(R.string.speech_services_choose_locale) + " ");

		switch (new Integer(sharedPrefs.getString(
				HelpMEPreferences.PREFERENCE_SPEECH_SERVICES_LOCALES, "0"))) {
		case 0:
			selectedLocale.append(getString(R.string.language_us_En));
			break;
		case 1:
			selectedLocale.append(getString(R.string.language_de));
			break;
		case 2:
			selectedLocale.append(getString(R.string.language_fr));
			break;
		}

		findPreference(HelpMEPreferences.PREFERENCE_SPEECH_SERVICES_LOCALES)
				.setSummary(selectedLocale.toString());

		StringBuffer selectedLanguageModel = new StringBuffer(
				getString(R.string.summary_speech_services_choose_language_model)
						+ " ");

		switch (new Integer(sharedPrefs.getString(
				HelpMEPreferences.PREFERENCE_SPEECH_SERVICES_LANGUAGE_MODEL,
				"0"))) {
		case 0:
			selectedLanguageModel
					.append(getString(R.string.lang_model_free_form));
			break;
		case 1:
			selectedLanguageModel
					.append(getString(R.string.lang_model_web_search));
			break;
		}

		findPreference(
				HelpMEPreferences.PREFERENCE_SPEECH_SERVICES_LANGUAGE_MODEL)
				.setSummary(selectedLanguageModel.toString());
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

		if (preference.getKey().equals(PREFERENCE_USE_SPEECH_SERVICES)) {
			preference
					.setSummary(((Boolean) newValue) ? getString(R.string.summary_speech_services_enabled_preference)
							: getString(R.string.summary_speech_services_disabled_preference));
			return true;
		}

		if (preference.getKey().equals(PREFERENCE_USE_SMS_MSG)) {
			preference
					.setSummary(((Boolean) newValue) ? getString(R.string.summary_sms_enabled_preference)
							: getString(R.string.summary_sms_disabled_preference));
			return true;
		}

		if (preference.getKey().equals(PREFERENCE_USE_EMAIL_MSG)) {
			preference
					.setSummary(((Boolean) newValue) ? getString(R.string.summary_email_enabled_preference)
							: getString(R.string.summary_email_disabled_preference));
			return true;
		}

		if (preference.getKey().equals(PREFERENCE_SPEECH_SERVICES_LOCALES)) {
			StringBuffer selectedLocale = new StringBuffer(
					getString(R.string.speech_services_choose_locale) + " ");

			switch (new Integer(newValue.toString())) {
			case 0:
				selectedLocale.append(getString(R.string.language_us_En));
				break;
			case 1:
				selectedLocale.append(getString(R.string.language_de));
				break;
			case 2:
				selectedLocale.append(getString(R.string.language_fr));
				break;
			}
			preference.setSummary(selectedLocale.toString());
			return true;
		}

		if (preference.getKey().equals(
				PREFERENCE_SPEECH_SERVICES_LANGUAGE_MODEL)) {
			StringBuffer selectedLangModel = new StringBuffer(
					getString(R.string.summary_speech_services_choose_language_model)
							+ " ");

			switch (new Integer(newValue.toString())) {
			case 0:
				selectedLangModel
						.append(getString(R.string.lang_model_free_form));
				break;
			case 1:
				selectedLangModel
						.append(getString(R.string.lang_model_web_search));
				break;
			}
			preference.setSummary(selectedLangModel.toString());
			return true;
		}
		return false;
	}
}
