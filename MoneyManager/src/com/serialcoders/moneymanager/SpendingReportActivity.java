package com.serialcoders.moneymanager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class SpendingReportActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spending_report);
		// Show the Up button in the action bar.
		setupActionBar();
		
		ParseUser user = ParseUser.getCurrentUser();
		
		//Find all Transactions which are withdrawals
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Transaction");
	    List<ParseObject> transactionList;
        //query.whereEqualTo("accountFullName", passedName);
        query.whereEqualTo("userName", user);
        query.whereLessThan("amount", 0); //only get negative amounts
	    try {
	    	transactionList = query.find();
	    } catch (ParseException e) {
	    	transactionList = new ArrayList<ParseObject>();
	    	Toast.makeText(this, "Cannot load transactions: " + e, Toast.LENGTH_LONG).show();
	    }
	    /*Double totalBalance = 0.0;
	    if(accountInitialBalance != null){
	    	totalBalance = Double.parseDouble(accountInitialBalance);
	    }*/
	    Double totalWithdrawals = 0.0;
	    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    for (ParseObject o : transactionList) {
	    	Button accountButton = new Button(this);
	    	Date transactionDate = o.getCreatedAt();    	
	    	accountButton.setText(o.get("transactionType") + "   " + Double.toString(o.getDouble("amount")) + " on " + df.format(transactionDate));
	    	accountButton.setBackgroundResource(R.drawable.green_button);
	    	LinearLayout ll = (LinearLayout)findViewById(R.id.withdrawal_list);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ll.addView(accountButton, lp);
            
            totalWithdrawals += o.getDouble("amount");
	    }
	    
		TextView balance = (TextView) findViewById(R.id.total_withdrawals);
		balance.setText("Withdrawals: $" + totalWithdrawals);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spending_report, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
