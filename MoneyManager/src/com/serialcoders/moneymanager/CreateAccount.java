package com.serialcoders.moneymanager;

import java.util.ArrayList;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccount extends Activity {
    String fullName;
    String displayName;
    
    ParseUser user;	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		
		Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_account, menu);
		return true;
	}
	
	public void checkAccount(View v) {
		EditText fullNameView = (EditText) findViewById(R.id.full_name);
        EditText displayNameView = (EditText) findViewById(R.id.display_name);
        fullName = fullNameView.getText().toString();
        displayName = displayNameView.getText().toString();
        
        user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        List<ParseObject> accountList;
        
        
        try {
	    	accountList = query.find();
	    } catch (ParseException e) {
	    	accountList = new ArrayList<ParseObject>();
	    }
        
    	boolean accountExists = false;
        
        if ((fullName.equals("")) || (displayName.equals(""))) {
        	Toast.makeText(CreateAccount.this, "Your new account must have both a name and display name!", Toast.LENGTH_SHORT).show();
        } else {
	        for (ParseObject a : accountList) {
	            	String accountFullName = a.getString("fullName");
	            	String accountDisplayName = a.getString("displayName");
	            	if ((fullName.equals(accountFullName))) {
	            		Toast.makeText(CreateAccount.this, "An account with this name already exists!", Toast.LENGTH_LONG).show();
	            		accountExists = true;
	            	} else if ((displayName.equals(accountDisplayName))) {
	            		Toast.makeText(CreateAccount.this, "An account with this display name already exists!", Toast.LENGTH_LONG).show();
	            		accountExists = true;
	            	}
	            }
	        
	        if (!accountExists) {
	    		AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
	        	builder.setCancelable(true);
	        	builder.setTitle("Create an account with this information?");
	        	builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
	        	  @Override
	        	  public void onClick(DialogInterface dialog, int which) {
	    	        	createAccount(fullName, displayName, user);
	    	    	    dialog.dismiss();
	        	  }
	        	});
	        	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        	  @Override
	        	  public void onClick(DialogInterface dialog, int which) {
	        	    dialog.dismiss();
	        	  }
	        	});
	        	AlertDialog alert = builder.create();
	        	alert.show();
	    	
	    	}
        }
	}

	public void createAccount(String fullName, String displayName, ParseUser user) {

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
