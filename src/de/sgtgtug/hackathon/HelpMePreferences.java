package de.sgtgtug.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

public class HelpMePreferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setPreferenceScreen(createPreferenceHierarchy());
	}

	private PreferenceScreen createPreferenceHierarchy() {
		// Root
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
		
		PreferenceCategory contactCategory = new PreferenceCategory(this);
		contactCategory.setTitle(R.string.contacts_pref_cat_title);
		root.addPreference(contactCategory);
		
		PreferenceScreen contacts = getPreferenceManager().createPreferenceScreen(this);
		contacts.setIntent(new Intent(this, ContactManager.class));
		contacts.setTitle(R.string.contacts_pref_title);
		contacts.setSummary(R.string.contacts_pref_summary);
		contactCategory.addPreference(contacts);

		return root;
	}
}
