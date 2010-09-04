package de.sgtgtug.hackathon;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

public class PreferencesUtil {

	public final static List<Long> loadEmergencyContactIdsFromPreferences(SharedPreferences preferences) {

		String contactIds = preferences.getString("contactIds", "");

		String split[] = contactIds.split(":");

		List<Long> ids = new ArrayList<Long>();

		for (String id : split) {
			if (id.length() == 0) {
				continue;
			}

			ids.add(Long.valueOf(id));
		}

		return ids;
	}
	
	public final static void saveEmergencyContactIdsToPreferences(SharedPreferences preferences, List<Long> ids){
		StringBuffer preferencesString = new StringBuffer();
		
		for (Long id : ids) {

			if (preferencesString.length() == 0) {
				preferencesString.append(id);
			} else {
				preferencesString.append(":" + id);
			}
		}

		Editor editor = preferences.edit();
		editor.putString("contactIds", preferencesString.toString());
		editor.commit();
	}

	public final static String getSingleEmergencyContact(Activity activity, long id) {
		ContentResolver cr = activity.getContentResolver();
		
		Cursor cur = cr.query(Data.CONTENT_URI, new String[] { Phone.NUMBER }, Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'", new String[] { String.valueOf(id) }, null);

		cur.moveToFirst();
		
		int columnIndexForPhoneNumber = cur.getColumnIndex(Phone.NUMBER);
		return cur.getString(columnIndexForPhoneNumber);
	}
	
	public final static List<String> getAllEmergencyContacts(Activity activity){
		List<String> emergencyContacts = new ArrayList<String>();
		List<Long> ids = loadEmergencyContactIdsFromPreferences(activity.getPreferences(Activity.MODE_PRIVATE));
		for (Long id : ids){
			emergencyContacts.add(getSingleEmergencyContact(activity, id));
		}
		
		return emergencyContacts;
	}
}
