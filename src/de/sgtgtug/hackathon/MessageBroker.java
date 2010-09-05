package de.sgtgtug.hackathon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MessageBroker extends Activity {
	private static String LOG_TAG = "MessageBroker";
	
	public final static String SMS = "sms";
	public final static String EMAIL = "email";
	
	private boolean SEND_SMS = false;
	private boolean SEND_EMAIL = false;
	
	private StringBuffer msgBfr = new StringBuffer();
	
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_broker);
        Intent helpME = getIntent();
        Bundle extras = helpME.getExtras();
        SEND_SMS = extras.getBoolean(SMS);
        SEND_EMAIL = extras.getBoolean(EMAIL);
        
        final List<MessageBroker.BrokerContact> allEmergencyContacts = PreferencesUtil.getAllEmergencyContacts(this);
        
		ArrayList<BrokerContact> contacts = new ArrayList<BrokerContact>();
		BrokerContact c1 = new BrokerContact("5556", "");
		contacts.add(c1);
		sendMessages(allEmergencyContacts);
		finish();
    }
    
	private void sendMessages(List<MessageBroker.BrokerContact> contacts) {
		String msg = buildMsg();
		
		for (MessageBroker.BrokerContact contact : contacts) {
			if(SEND_SMS)
				sendSMS(msgBfr.toString(), contact.sms);
			if(SEND_EMAIL)
				sendEmail(msgBfr.toString(), contact.email);
		}
		msgBfr = new StringBuffer();
	}
	
    private void sendEmail(String message, String sendTo) {
    	final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{sendTo});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Help Me!");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(intent);
	}

	private void sendSMS(String message, String sendTo ) {
    	SmsManager smsMngr = SmsManager.getDefault();
    	
    	ArrayList<String> chunkedMessages = smsMngr.divideMessage(message);
        for (String messageChunk : chunkedMessages)
        	smsMngr.sendTextMessage(sendTo, this.getString(R.string.app_name), messageChunk, null, null);
    }
    
    private String buildMsg() {
    	//Settingsauslesen
    	msgBfr.append("Help Me, \n");
    	msgBfr.append(getEmergencyLocation());
    	return msgBfr.toString();
    }
    
    private String getEmergencyLocation() {
    	StringBuffer locBuf = new StringBuffer();
    	Location currLoc = getCurrentLocation();
    	if(currLoc != null) {
        	Log.i(LOG_TAG, "Current  Location -> Lat: " + currLoc.getLatitude() + "Long: " + currLoc.getLongitude());
    		locBuf.append("I'm at Lat: " + currLoc.getLatitude() + " Long: " + currLoc.getLongitude() + "\n");
    	}
    	
    	List<Address> addresses = resolveLocation(currLoc);
    	if (addresses != null && !addresses.isEmpty()) {
            Address currentAddress = addresses.get(0);
            if (currentAddress.getMaxAddressLineIndex() > 0) {
              for (int i = 0; i < currentAddress.getMaxAddressLineIndex(); i++) {
            	  locBuf.append(currentAddress.getAddressLine(i));
            	  locBuf.append("\n");
              }
            }
            else {
              if (currentAddress.getPostalCode() != null)
            	  locBuf.append(currentAddress.getPostalCode());
            } 
    	}
    	return locBuf.toString();
    }
    
    private Location getCurrentLocation() {
    	LocationManager locMngr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	Location currLoc = null;
    	currLoc = locMngr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	if(currLoc == null) {
    		currLoc = locMngr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	}
    	return currLoc;
    }
    
    private List<Address> resolveLocation(Location currLoc) {
        Geocoder gCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
          return gCoder.getFromLocation(currLoc.getLatitude(), currLoc.getLongitude(), 1);
        } catch (Exception e) {
        	Log.e(LOG_TAG, "Could not resolve GeoLocation, here is what i know: " + e.getMessage());
        	return null;
        }
    }
    
    public static class BrokerContact {
    	public String sms;
    	public String email;
    	
    	public BrokerContact(String sms, String email) {
    		this.sms = sms;
    		this.email = email;
		}
    }
}