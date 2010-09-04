package de.sgtgtug.hackathon;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

public class PreferencesUtil {

	public final static List<Long> getEmergencyContactIdsFromPreferences(SharedPreferences preferences) {

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

	public final static String getSingleEmergencyContact(ContentResolver cr, long id) {
		Cursor cur = cr.query(Data.CONTENT_URI, new String[] { Phone.NUMBER }, Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'", new String[] { String.valueOf(id) }, null);

		cur.moveToFirst();
		
		int columnIndexForPhoneNumber = cur.getColumnIndex(Phone.NUMBER);
		return cur.getString(columnIndexForPhoneNumber);
	}
	
	public final static List<String> getAllEmergencyContacts(SharedPreferences preferences, ContentResolver cr){
		List<String> emergencyContacts = new ArrayList<String>();
		List<Long> ids = getEmergencyContactIdsFromPreferences(preferences);
		for (Long id : ids){
			emergencyContacts.add(getSingleEmergencyContact(cr, id));
		}
		
		return emergencyContacts;
	}
}
