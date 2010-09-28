/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.sgtgtug.android.hackathon.helpme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public final class ContactManager extends Activity {

	public static final String TAG = "ContactManager";

	private Button mDoneButton;
	private ListView mContactList;
	private boolean mShowInvisible;

	/**
	 * Called when the activity is first created. Responsible for initializing
	 * the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "Activity State: onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_manager);

		// Obtain handles to UI objects
		mDoneButton = (Button) findViewById(R.id.doneContactButton);
		mContactList = (ListView) findViewById(R.id.contactList);

		// Register handler for UI elements
		mDoneButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "mDoneButton clicked");

				long[] checkedItemIds = mContactList.getCheckedItemIds();
				SimpleCursorAdapter sca = (SimpleCursorAdapter) mContactList
						.getAdapter();
				Cursor cursor = sca.getCursor();

				int columnIndexOfID = cursor
						.getColumnIndexOrThrow(ContactsContract.Data._ID);

				List<Long> contactIds = new ArrayList<Long>();

				for (long i : checkedItemIds) {
					cursor.moveToPosition((int) i - 1);

					long contactId = cursor.getLong(columnIndexOfID);

					contactIds.add(contactId);
				}

				PreferencesUtil.saveEmergencyContactIdsToPreferences(
						getPreferences(MODE_PRIVATE), contactIds);

				finish();
			}
		});

		// Populate the contact list
		populateContactList();
		setCheckBoxes();
	}

	private void setCheckBoxes() {

		if (mContactList.getAdapter().getCount() == 0) {
			return;
		}
		;

		List<Long> ids = PreferencesUtil
				.loadEmergencyContactIdsFromPreferences(getPreferences(MODE_PRIVATE));

		SimpleCursorAdapter sca = (SimpleCursorAdapter) mContactList
				.getAdapter();
		Cursor cursor = sca.getCursor();
		int columnIndexOfID = cursor
				.getColumnIndexOrThrow(ContactsContract.Data._ID);

		for (int i = 0; i < mContactList.getAdapter().getCount(); i++) {
			cursor.moveToPosition(i);
			if (ids.contains(cursor.getLong(columnIndexOfID))) {
				mContactList.setItemChecked(i, true);
			}
		}
	}

	/**
	 * Populate the contact list based on account currently selected in the
	 * account spinner.
	 */
	private void populateContactList() {
		Cursor cursor = getContacts();
		String[] fields = new String[] { ContactsContract.Data.DISPLAY_NAME };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_multiple_choice, cursor,
				fields, new int[] { android.R.id.text1 });
		mContactList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		mContactList.setAdapter(adapter);
	}

	/**
	 * Obtains the contact list for the currently selected account.
	 * 
	 * @return A cursor for for accessing the contact list.
	 */
	private Cursor getContacts() {
		// Run query
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME };
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
				+ (mShowInvisible ? "0" : "1") + "'";
		String[] selectionArgs = null;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		return managedQuery(uri, projection, selection, selectionArgs,
				sortOrder);
	}
}
