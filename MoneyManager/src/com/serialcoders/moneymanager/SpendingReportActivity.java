package com.serialcoders.moneymanager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class SpendingReportActivity extends Activity {
	
	Date dateFrom, dateTo;
	Calendar c;
	ParseUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spending_report);
		
		user = ParseUser.getCurrentUser();
		dateTo = new Date();
		dateFrom = new Date(0);
		
		setUpListeners();
	}
	
	private void setUpListeners() {
		EditText dateFromText = (EditText) findViewById(R.id.date_from);
		dateFromText.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogFragment dateFragment = new DatePickerFragment();
				Bundle bundle = new Bundle(1);
				bundle.putInt("EditText", R.id.date_from);
				dateFragment.setArguments(bundle);
			    dateFragment.show(getFragmentManager(), "datePicker");
			}
		});
		dateFromText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable v) {
				DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
				try {
					dateFrom = dateFormat.parse(v.toString());
				} catch (java.text.ParseException e) {
					dateFrom = new Date();
					Log.d("error1", e.getMessage());
//					Toast.makeText(getContext(), "Please try entering a new date" + e, Toast.LENGTH_LONG).show();
				}
				findWithdrawals();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) { }

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) { }
			
		});
		
		EditText dateToText = (EditText) findViewById(R.id.date_to);
		dateToText.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogFragment dateFragment = new DatePickerFragment();
				Bundle bundle = new Bundle(1);
				bundle.putInt("EditText", R.id.date_to);
				dateFragment.setArguments(bundle);
			    dateFragment.show(getFragmentManager(), "datePicker");
			}
		});
		dateToText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable v) {
				DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
				try {
					dateTo = dateFormat.parse(v.toString());
				} catch (java.text.ParseException e) {
					dateTo = new Date();
					Log.d("error1", e.getMessage());
//					Toast.makeText(getContext(), "Please try entering a new date" + e, Toast.LENGTH_LONG).show();
				}
				findWithdrawals();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) { }

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) { }
			
		});
	}
	
	private void findWithdrawals() {
		//Find all Transactions which are withdrawals
		Log.d("tag2", "hit this");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Transaction");
	    List<ParseObject> transactionList;
        query.whereEqualTo("userName", user);
        query.whereLessThan("amount", 0); //only get negative amounts
        Log.d("tag3", dateFrom.toString());
        Log.d("tag3", dateTo.toString());
        query.whereGreaterThanOrEqualTo("createdAt", dateFrom);
        query.whereLessThanOrEqualTo("createdAt", dateTo);
	    try {
	    	transactionList = query.find();
	    } catch (ParseException e) {
	    	transactionList = new ArrayList<ParseObject>();
	    	Toast.makeText(this, "Cannot load transactions: " + e, Toast.LENGTH_LONG).show();
	    }
	    
	    Double totalWithdrawals = 0.0;
	    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    LinearLayout ll = (LinearLayout)findViewById(R.id.withdrawal_list);
    	ll.removeAllViews();
	    for (ParseObject o : transactionList) {
	    	Button accountButton = new Button(this);
	    	Date transactionDate = o.getCreatedAt();
	    	accountButton.setText(o.get("transactionType") + "   " + Double.toString(o.getDouble("amount")) + " from " + o.get("accountFullName") + "\n on " + df.format(transactionDate));
	    	accountButton.setBackgroundResource(R.drawable.green_button);
	    	
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ll.addView(accountButton, lp);
            
            totalWithdrawals += o.getDouble("amount");
	    }
	    
		TextView balance = (TextView) findViewById(R.id.total_withdrawals);
		balance.setText("Withdrawals: $" + totalWithdrawals);
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
