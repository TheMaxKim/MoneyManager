package com.serialcoders.moneymanager;

import java.util.ArrayList;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccount extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_account, menu);
		return true;
	}
	
	public void createAccount(View v) {
		EditText fullNameView = (EditText) findViewById(R.id.full_name);
        EditText displayNameView = (EditText) findViewById(R.id.display_name);
        String fullName = fullNameView.getText().toString();
        String displayName = displayNameView.getText().toString();
        
        ParseUser user = ParseUser.getCurrentUser();
		ParseObject account = new ParseObject("Account");
		account.put("fullName", fullName);
		account.put("displayName", displayName);
		account.put("username", user.get("username"));
		account.saveInBackground();
		
		
		user.add("Accounts", account);
		Toast.makeText(CreateAccount.this, "Account Created", Toast.LENGTH_LONG).show();
		
		Intent i = new Intent(CreateAccount.this, UserAccountActivity.class);
		startActivity(i);
	}

}
