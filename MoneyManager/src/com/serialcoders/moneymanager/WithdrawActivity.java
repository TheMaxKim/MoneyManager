package com.serialcoders.moneymanager;

import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.EditText;

public class WithdrawActivity extends Activity {
	private ParseUser user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.withdraw, menu);
		return true;
	}
	
	public void createWithdrawal() {

		
		EditText destinationView = (EditText) findViewById(R.id.editText1);
		EditText amountView = (EditText) findViewById(R.id.editText2);
		
		String destination = destinationView.getText().toString();
		double amount = Double.parseDouble(amountView.getText().toString());
		
		user = ParseUser.getCurrentUser();
	}

}
