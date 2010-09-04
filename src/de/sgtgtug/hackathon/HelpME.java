package de.sgtgtug.hackathon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class HelpME extends Activity {
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent msgBroker = new Intent(HelpME.this, MessageBroker.class);
        startActivity(msgBroker);
    }
}