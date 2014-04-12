package com.serialcoders.moneymanager;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.serialcoders.moneymanager.ActionButton.ActionButtonCallback;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity represents a user's financial account in the app.
 * @author Max Kim
 *
 */
public class FinancialAccountActivity extends SliderMenuActivity implements ActionButtonCallback {
    /**
     * @param passedName The name of the current financial account the user is viewing as a string.
     */
    String passedName;
    /**
     * @param currentFinancialAccount The current financial account the user is on as a parse
     * object for retrieval of the data associated with the account.
     */
    ParseObject currentFinancialAccount;
    /**
     * @param accountCurrentBalance The current monetary balance of the financial account.
     */
    Double accountCurrentBalance;
    /**
     * @param financialAccountNameExtra The name of the extra passed in as the name of the selected financial account.
     */
    String financialAccountNameExtra = "FinancialAccountName";
    Double initialBalance;
    ParseUser user;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_financial_account, null, false);
        drawerLayout.addView(contentView, 0);
        //setContentView(R.layout.activity_financial_account);
        
        Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
 
        Intent intent = getIntent();
        passedName = intent.getExtras().getString(financialAccountNameExtra);
        
        TextView textView = (TextView) findViewById(R.id.fin_account_welcome);
        textView.setText(passedName);
        
        user = ParseUser.getCurrentUser();
        refresh();
        
        TextView textViewInitialBalance = (TextView) findViewById(R.id.initialbalance);
        
        textViewInitialBalance.setText("Initial Balance: " + DecimalFormat.getCurrencyInstance().format(initialBalance));
 
   }
        

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.financial_account, menu);
        return true;
    }
    
    /**
     * This method is used to go to the Transaction activity when the user wishes to make a transaction.
     * @param v Current view
     */
    public void makeTransaction(View v) {
        Intent in = new Intent(FinancialAccountActivity.this, TransactionActivity.class);
        in.putExtra(financialAccountNameExtra, passedName);
        startActivity(in);
    }
    
    /**
     * This method is used to go back to the User Account activity when the user wishes to go back.
     * @param v Current view
     */
    public void backToAccounts(View v) {
        Intent in = new Intent(FinancialAccountActivity.this, UserAccountActivity.class);
        startActivity(in);
    }

	@Override
	public void refresh() {
		ParseQuery<ParseObject> accountQuery = ParseQuery.getQuery("Account");
        List<ParseObject> accountList;
        accountQuery.whereEqualTo("username", user.getUsername());
        accountQuery.whereEqualTo("displayName", passedName);
        try {
            accountList = accountQuery.find();
        } catch (ParseException e) {
            accountList = new ArrayList<ParseObject>();
            Toast.makeText(this, "Cannot load account information: " + e, Toast.LENGTH_LONG).show();
        }
        Log.d("size", ""+accountList.size());
        accountCurrentBalance = accountList.get(0).getDouble("currentBalance");
        initialBalance = accountList.get(0).getDouble("initialBalance");
            
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Transaction");
        List<ParseObject> transactionList;
        query.whereEqualTo("accountFullName", passedName);
        query.whereEqualTo("userName", user.getUsername());
        try {
            transactionList = query.find();
        } catch (ParseException e) {
            transactionList = new ArrayList<ParseObject>();
            Toast.makeText(this, "Cannot load transactions: " + e, Toast.LENGTH_LONG).show();
        }
    
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        LinearLayout ll = (LinearLayout) findViewById(R.id.transaction_list);
    	ll.removeAllViews();
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        for (ParseObject a : transactionList) {
           /* Button accountButton = new Button(this);
            Date transactionDate = a.getCreatedAt();
            Double amount = a.getDouble("amount");
            Object transactionType = a.get("transactionType");
            String space = " ";
            String on = " on ";
            if (amount < 0) {
                accountButton.setText(transactionType + space + DecimalFormat.getCurrencyInstance().format(-amount) + on + df.format(transactionDate));
                accountButton.setBackgroundResource(R.drawable.red_button);
            }
            else {
                accountButton.setText(transactionType + space + DecimalFormat.getCurrencyInstance().format(amount) + on + df.format(transactionDate));
                accountButton.setBackgroundResource(R.drawable.green_button);
            }       
            LinearLayout ll = (LinearLayout) findViewById(R.id.transaction_list);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ll.addView(accountButton, lp);
            
            accountButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Button pressedButton = (Button) view;
                }
            });*/
        	//accountCurrentBalance += a.getDouble("amount");
        	ActionButton transactionButton = new TransactionButton(this, a);
            ll.addView(transactionButton, lp);
		
        }

        TextView balance = (TextView) findViewById(R.id.balance);
        String moneyFormatBalance = DecimalFormat.getCurrencyInstance().format(accountCurrentBalance);
        Log.d("balance ", "" + accountCurrentBalance);
        balance.setText("Balance: " + moneyFormatBalance);
        
    }
    
}
