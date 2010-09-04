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
		contactCategory.setTitle("Contacts");
		root.addPreference(contactCategory);
		
		PreferenceScreen contacts = getPreferenceManager().createPreferenceScreen(this);
		contacts.setIntent(new Intent(this, ContactManager.class));
		contacts.setTitle("Contact List");
		contacts.setSummary("Choose Emergency Contacts...");
		contactCategory.addPreference(contacts);

		return root;
	}
}
