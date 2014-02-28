package com.serialcoders.moneymanager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.widget.TextView;

public class FinancialAccount extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_financial_account);
		
		Intent intent = getIntent();
		String passedName = intent.getExtras().getString("FinancialAccountName");
		
		TextView textView = (TextView) findViewById(R.id.fin_account_welcome);
		textView.setText(passedName);
		textView.setGravity(Gravity.CENTER);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.financial_account, menu);
		return true;
	}
	
	public void makeDeposit() {
        Intent in = new Intent(this, DepositActivity.class);
        startActivity(in);
	}
	
	public void makeWithdrawal() {
		Intent in = new Intent(this, WithdrawActivity.class);
		startActivity(in);
	}
	
}
