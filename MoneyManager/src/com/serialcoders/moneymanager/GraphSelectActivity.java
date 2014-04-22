package com.serialcoders.moneymanager;

import java.util.ArrayList;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Build;

public class GraphSelectActivity extends Activity {
	
	private ParseUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph_select);

		Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT",
                "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        user = ParseUser.getCurrentUser();
        
        findAccounts();
	}
	
	/**
     * updates the account list.
     */
    private void findAccounts() {
        List<ParseObject> accountList;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.whereEqualTo("username", user.getUsername());
        try {
            accountList = query.find();
        } catch (ParseException e) {
            accountList = new ArrayList<ParseObject>();
            Toast.makeText(GraphSelectActivity.this,
                    "Cannot load Accounts: " + e, Toast.LENGTH_LONG).show();
        }

        LinearLayout ll = (LinearLayout) findViewById(R.id.account_list);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        ll.removeAllViews();
        for (final ParseObject a : accountList) {
            AccountButton accountButton = new AccountButton(GraphSelectActivity.this);
            accountButton.setAccount(a.getString("displayName"));
            accountButton.setBalance(a.getDouble("currentBalance"));
            accountButton.setAccountFullName(a.getString("fullName"));

            ll.addView(accountButton, lp);
        }
    }

}
