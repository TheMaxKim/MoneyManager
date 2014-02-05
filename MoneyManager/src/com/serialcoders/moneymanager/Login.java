package com.serialcoders.moneymanager;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	EditText username;
	EditText password;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    
    public void login(View view) {
    	EditText username = (EditText)findViewById(R.id.username);
    	EditText password = (EditText)findViewById(R.id.password);
    	if (username.getText().toString().equals("admin") &&
    		password.getText().toString().equals("pass123")) {
    	    	Toast.makeText(getApplicationContext(), "Success!", 
    	    	      Toast.LENGTH_SHORT).show();
    	} else {
    	      Toast.makeText(getApplicationContext(), "Incorrect Login Credentials", 
    	    	      Toast.LENGTH_SHORT).show();
    	}
    }
    
}
