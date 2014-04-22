package com.serialcoders.moneymanager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Build;

public class GraphSelectActivity extends Activity implements OnClickListener {
	
	private ParseUser user;
	private List<String> selected;

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
        
        selected =  new ArrayList<String>();
        for (final ParseObject a : accountList) {
            Button accountButton = new Button(GraphSelectActivity.this);
            accountButton.setText("Account name: " + a.getString("displayName")
                    + "   Current Balance: "
                    + DecimalFormat.getCurrencyInstance()
                    .format(a.getDouble("currentBalance")));
            CheckBox checkBox = new CheckBox(GraphSelectActivity.this);
            checkBox.setOnClickListener(this);
            checkBox.setContentDescription(a.getObjectId());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT, 1.0f);
            accountButton.setLayoutParams(param);
            checkBox.setLayoutParams(param);
            
            LinearLayout horizontal = new LinearLayout(GraphSelectActivity.this);
			horizontal.setOrientation(LinearLayout.HORIZONTAL);
			horizontal.addView(accountButton);
			horizontal.addView(checkBox);
            ll.addView(horizontal, lp);
        }
    }
    
    @Override
    public void onClick(View v) {
		if (((CheckBox) v).isChecked()) {
			selected.add((String) v.getContentDescription());
		} else {
			selected.remove((String) v.getContentDescription());
		}
	}
    
    public void generateGraph(View v) {
    	Intent i = new Intent(GraphSelectActivity.this,
                GraphActivity.class);
    	i.putExtra("accounts", (String[]) selected.toArray(new String[0]));
    	Log.d("accounts selected", Arrays.toString(selected.toArray()));
            startActivity(i);
    }

}
