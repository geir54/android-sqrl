package com.sqrl.android_sqrl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

// TODO:
// Read user list from sqrl.dat

public class loginActivity extends Activity {
	private List<String> users = new ArrayList<String>(); // List of usernames
	private Spinner username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);    		
		
		username = (Spinner) findViewById(R.id.userSpinner);
		
		// Add listener on loginbutton
		 final Button loginButton = (Button) findViewById(R.id.button1);
		 loginButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {	           
	            	String a = username.getSelectedItem().toString();
	            	Toast.makeText(getApplicationContext(), a, Toast.LENGTH_LONG).show();
	            }  });
		 
		// Add listener on new user button button
		 final Button regButton = (Button) findViewById(R.id.button2);
		 regButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {	           
	             // Open new activity to register new user
	            	  Intent a = new Intent(loginActivity.this, newuserActivity.class);
	                  startActivity(a);
	            }  });
		 
		 addUsersToSpinner();
	}
	
	  // adds items to username list (spinner)
	  public void addUsersToSpinner() {	 
		users.add("User 1");
		users.add("User 2");
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, users);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		username.setAdapter(dataAdapter);
	  }

}
