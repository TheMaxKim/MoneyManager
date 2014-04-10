package com.serialcoders.moneymanager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AccountButton extends ActionButton {

	private String account;
	private String accountFullName;
	private double balance;
	
	public AccountButton(Context context) {
		super(context);
		account = "Default";
		balance = 0.0;
		updateText();
        this.setBackgroundResource(R.drawable.green_button);
        setUpListeners();
	}
	
	public void setAccount(String account) {
		this.account = account;
		updateText();
	}
	
	public void setAccountFullName(String accountFullName) {
		this.accountFullName = accountFullName;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
		updateText();
	}
	
	public void updateText() {
		this.setText("Account name: " + account
                + "   Current Balance: "
                + DecimalFormat.getCurrencyInstance()
                .format(balance));
	}
	
	public void setUpListeners() {
		this.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                Button pressedButton = (Button) view;
                String passedAccount = account;
                Intent in = new Intent(getContext(),
                        FinancialAccountActivity.class);
                in.putExtra("FinancialAccountName", passedAccount);
                getContext().startActivity(in);
            }
        });
        this.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
			    builder.setTitle("Are you sure you want to delete " + account +"?");
			    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
				        //query.whereEqualTo("username", user.getUsername());
				        query.whereEqualTo("fullName", accountFullName);
				        List<ParseObject> list;
				        try {
				        	list = query.find();
				        } catch (ParseException e) {
				        	list = new ArrayList<ParseObject>();
				        	Toast.makeText(getContext(),
				                    "Cannot delete Account: " + e, Toast.LENGTH_LONG).show();
				        }
				        if (list.size() > 0) {
				        	list.get(0).deleteInBackground();
				        } else {
				        	Toast.makeText(getContext(),
				                    "Cannot delete Account: ", Toast.LENGTH_LONG).show();
				        }
				        ((ActionButtonCallback)getContext()).refresh();
					}

				});
			    builder.setNegativeButton("Cancel", null);
			    AlertDialog alert = builder.create();
                alert.show();
				return true;
			}
		});
	}

}
