package com.serialcoders.moneymanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class UserAccountActivity extends Activity {
	
	private ParseUser user;
	
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

	    setContentView(R.layout.activity_useraccount);
	    
	    Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
	    
	    user = ParseUser.getCurrentUser();
	    
	    TextView textView = (TextView) findViewById(R.id.welcomemessage);
	    String username = user.getUsername();
	    textView.setText("Welcome to MoneyManager " + username + "!");
	    
	    findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        			
        		user.logOut();
        		
        		Toast.makeText(UserAccountActivity.this, "Successfully logged out!", Toast.LENGTH_LONG).show();
        		Intent in = new Intent(UserAccountActivity.this, Login.class);
				startActivity(in);
				
				
        	}
        });
	}
	
	public void addAccount(View v) {
		Intent i = new Intent(UserAccountActivity.this, CreateAccount.class);
		startActivity(i);
	}
}
