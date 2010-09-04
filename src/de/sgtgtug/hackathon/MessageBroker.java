package de.sgtgtug.hackathon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
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
	private StringBuffer msgBfr = new StringBuffer();
	
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_broker);
        
        Button sendBtn = (Button) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<BrokerContact> contacts = new ArrayList<BrokerContact>();
				BrokerContact c1 = new BrokerContact("01638792012", "onlythoughtworks@googlemail.com");
				contacts.add(c1);
				sendMessages(contacts);
			}
		});
    }
    
	private void sendMessages(ArrayList<BrokerContact> contacts) {
		String msg = buildMsg();
		
		for (BrokerContact contact : contacts) {
			sendSMS(msgBfr.toString(), contact.sms);
			sendEmail(msgBfr.toString(), contact.email);
		}
		msgBfr = new StringBuffer();
	}
	
    private void sendEmail(String message, String sendTo) {
    	
	}

	private void sendSMS(String message, String sendTo ) {
    	SmsManager smsMngr = SmsManager.getDefault();
    	
    	ArrayList<String> chunkedMessages = smsMngr.divideMessage(message);
        for (String messageChunk : chunkedMessages)
        	smsMngr.sendTextMessage(sendTo, null, messageChunk, null, null);
    	smsMngr.sendTextMessage(sendTo, this.getString(R.string.app_name), message, null, null);
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
    	locBuf.append("I'm at: " + currLoc.toString() + "\n");
    	List<Address> addresses = resolveLocation(currLoc);
    	
    	if (addresses != null) {
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
        return locMngr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
    
    private List<Address> resolveLocation(Location currLoc) {
        Geocoder gCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
          return gCoder.getFromLocation(currLoc.getLatitude(), currLoc.getLongitude(), 1);
        } catch (IOException e) {
        	Log.e(LOG_TAG, "Could not resolve GeoLocation, here is what i know: " + e.getMessage());
        	return null;
        }
    }
    
    public class BrokerContact {
    	public String sms;
    	public String email;
    	
    	public BrokerContact(String sms, String email) {
    		this.sms = sms;
    		this.email = email;
		}
    }
}