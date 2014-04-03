package com.serialcoders.moneymanager.test;

import com.parse.ParseUser;
import com.serialcoders.moneymanager.UserAccountActivity;
import android.app.Activity;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;


public class LogOutTest extends ActivityInstrumentationTestCase2<UserAccountActivity>{

	Activity activity;
	Button logOutButton;
	ParseUser user;
	
	public LogOutTest() {
		super(UserAccountActivity.class);

	}
	@UiThreadTest
	protected void setUp() throws Exception {
		
		
		super.setUp();
		
		activity = getActivity();
	
		logOutButton = (Button) activity.findViewById(com.serialcoders.moneymanager.R.id.button_remove_account);
		
	}
	@UiThreadTest
	public void testLogOut() {
			
		logOutButton.performClick();
		SystemClock.sleep(5000);
		assertNull(ParseUser.getCurrentUser());
	}
	
	
}
