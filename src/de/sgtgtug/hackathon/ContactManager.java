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

package de.sgtgtug.hackathon;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;

public final class ContactManager extends Activity
{

    public static final String TAG = "ContactManager";

    private Button mDoneButton;
    private ListView mContactList;
    private boolean mShowInvisible;
    private CheckBox mShowInvisibleControl;

    /**
     * Called when the activity is first created. Responsible for initializing the UI.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_manager);

        // Obtain handles to UI objects
        mDoneButton = (Button) findViewById(R.id.doneContactButton);
        mContactList = (ListView) findViewById(R.id.contactList);
        
        

        
        
        mShowInvisibleControl = (CheckBox) findViewById(R.id.showInvisible);

        // Initialize class properties
        mShowInvisible = false;
        mShowInvisibleControl.setChecked(mShowInvisible);

         //Register handler for UI elements
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "mDoneButton clicked");
                
                long[] checkedItemIds = mContactList.getCheckedItemIds();
                SimpleCursorAdapter sca = (SimpleCursorAdapter)mContactList.getAdapter();
                Cursor cursor = sca.getCursor();
                      
                int columnIndexOfID = cursor.getColumnIndexOrThrow(ContactsContract.Data._ID);
                
                for(long i : checkedItemIds) {
                	cursor.moveToPosition((int) i-1);
                	Log.d(TAG, "Selected Contact ID is: " + cursor.getLong(columnIndexOfID));
                }
            }
        });
        mShowInvisibleControl.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "mShowInvisibleControl changed: " + isChecked);
                mShowInvisible = isChecked;
                populateContactList();
            }
        });

        // Populate the contact list
        populateContactList();
    }

    /**
     * Populate the contact list based on account currently selected in the account spinner.
     */
    private void populateContactList() {
        // Build adapter with contact entries
        Cursor cursor = getContacts();
        String[] fields = new String[] {
                ContactsContract.Data.DISPLAY_NAME
        };
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_entry, cursor,
//                fields, new int[] {R.id.contactEntryText});
//        mContactList.setAdapter(adapter);
        
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, cursor,
                fields, new int[] {android.R.id.text1});
        mContactList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        mContactList.setAdapter(adapter);
        
        
//        mContactList.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_multiple_choice, lv_items));
//                mContactList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    /**
     * Obtains the contact list for the currently selected account.
     *
     * @return A cursor for for accessing the contact list.
     */
    private Cursor getContacts()
    {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
                (mShowInvisible ? "0" : "1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }
    
//    public class CheckedCursorAdapter extends SimpleCursorAdapter {
//
//        Activity context;
//        Cursor c;
//
//        public CheckedCursorAdapter(Activity context, int rowContact, Cursor cursor, String[] strings, int[] is) {
//          super(context, rowContact, cursor, strings, is);
//          this.context = context;
//          this.c = cursor;
//
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//          View row = convertView;
//          ContactRowWrapper wrapper;
//
//          if (row == null) {
//            LayoutInflater inflater=context.getLayoutInflater();
//            row = inflater.inflate(R.layout.row_contact, null);
//            //
//            wrapper = new ContactRowWrapper(row);
//            row.setTag(wrapper);
//          } else {
//            wrapper = (ContactRowWrapper)row.getTag();
//          }
//          c.moveToPosition(position);
//          wrapper.getTextView().setText( c.getString(c.getColumnIndex(Contacts.People.NUMBER)) );
//          wrapper.getcheckBox().setText( c.getString(c.getColumnIndex(Contacts.People.NAME)) );
//          wrapper.getcheckBox().setChecked(getListView().isItemChecked(position));
//          //
//          return row;
//        }
}
