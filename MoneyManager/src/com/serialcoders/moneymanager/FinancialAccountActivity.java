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
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FinancialAccountActivity extends Activity {

	String passedName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_financial_account);
		
		Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");

		
		Intent intent = getIntent();
		passedName = intent.getExtras().getString("FinancialAccountName");
		
		TextView textView = (TextView) findViewById(R.id.fin_account_welcome);
		textView.setText(passedName);

		
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Transaction");
	    List<ParseObject> transactionList;
        
	    ParseUser user = ParseUser.getCurrentUser();
        query.whereEqualTo("accountFullName", passedName);
        query.whereEqualTo("userName", user);
	    try {
	    	transactionList = query.find();
	    } catch (ParseException e) {
	    	transactionList = new ArrayList<ParseObject>();
	    	Toast.makeText(this, "Cannot load transactions: " + e, Toast.LENGTH_LONG).show();
	    }
	    
	    Double totalBalance = 0.0;
	    
	    for (ParseObject a : transactionList) {
	    	/*textView = new TextView(UserAccountActivity.this);			//Old code for just listing the names of the accounts.
	    	textView.setText(a.getString("displayName"));
	    	LinearLayout ll = (LinearLayout) findViewById(R.id.account_list);
	    	
	    	ll.addView(textView);*/
	    	Button accountButton = new Button(this);
	    	accountButton.setText(Double.toString(a.getDouble("amount")));
	    	accountButton.setBackgroundResource(R.drawable.green_button);
	    	LinearLayout ll = (LinearLayout)findViewById(R.id.transaction_list);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ll.addView(accountButton, lp);
            
            accountButton.setOnClickListener(new View.OnClickListener() {
            	public void onClick(View view) {
            		Button pressedButton = (Button) view;
            	}
            });
            totalBalance += a.getDouble("amount");
	    }
        
		
		TextView balance = (TextView) findViewById(R.id.balance);
		balance.setText("Balance: $" + totalBalance);
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.financial_account, menu);
		return true;
	}
	
	public void makeTransaction(View v) {
        Intent in = new Intent(FinancialAccountActivity.this, TransactionActivity.class);
        in.putExtra("FinancialAccountName", passedName);
        startActivity(in);
	}
	
}
