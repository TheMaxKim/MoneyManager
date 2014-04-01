package com.serialcoders.moneymanager;

import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class AdminAccountActivity extends Activity {
	
	private ParseUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_account);
		
		user = ParseUser.getCurrentUser();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_account, menu);
		return true;
	}
	/**
         * logs out the admin
         * 
         * @param v current view
         */
	public void logOut(View v) {
		user.logOut();
		
		Toast.makeText(AdminAccountActivity.this, "Successfully logged out!", Toast.LENGTH_LONG).show();
		Intent in = new Intent(AdminAccountActivity.this, Login.class);
		startActivity(in);
	}

}
