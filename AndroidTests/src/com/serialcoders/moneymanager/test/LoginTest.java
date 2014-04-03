package com.serialcoders.moneymanager.test;

import com.parse.ParseUser;
import com.serialcoders.moneymanager.Login;
import android.app.Activity;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;

public class LoginTest extends ActivityInstrumentationTestCase2<Login>{

	Activity activity;
	EditText usernameView, passwordView;
	Button loginButton;
	ParseUser user;
	
	public LoginTest() {
		super(Login.class);

	}
	@UiThreadTest
	protected void setUp() throws Exception {
		
		
		super.setUp();
		
		activity = getActivity();
		
		
		
		usernameView = (EditText) activity.findViewById(com.serialcoders.moneymanager.R.id.username);
		passwordView = (EditText) activity.findViewById(com.serialcoders.moneymanager.R.id.password);
		
		loginButton = (Button) activity.findViewById(com.serialcoders.moneymanager.R.id.buttonLogin);
	}
	@UiThreadTest
	public void testLogin() {
		
		usernameView.setText("max");
		passwordView.setText("hello");
		
		loginButton.performClick();
		SystemClock.sleep(5000);
		assertEquals(ParseUser.getCurrentUser().getUsername(), "max");
	}
	
	
}
