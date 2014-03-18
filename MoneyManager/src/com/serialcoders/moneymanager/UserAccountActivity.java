package com.serialcoders.moneymanager;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;


public class UserAccountActivity extends Activity {
	
	private ParseUser user;
	private ParseFile profilePhotoFile;
	private ImageView profilePictureView;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private ArrayList<String> drawerItems;
	private ArrayAdapter adapter;
	String logOutString = "Log Out";
	String myAccountString = "My Accounts";
	String spendingReportString = "Spending Report";
		
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		ScrollView sv = new ScrollView(this);
	    setContentView(R.layout.activity_useraccount);
	    
	    Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
	    
	    user = ParseUser.getCurrentUser();
	    
	    TextView textView = (TextView) findViewById(R.id.welcomemessage);
	    String username = user.getUsername();
	    textView.setText("Welcome to MoneyManager, " + username + "!");
	    
	    List<ParseObject> accountList;
	    ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
	    query.whereEqualTo("username", user.getUsername());
	    try {
	    	accountList = query.find();
	    } catch (ParseException e) {
	    	accountList = new ArrayList<ParseObject>();
	    	Toast.makeText(UserAccountActivity.this, "Cannot load Accounts: " + e, Toast.LENGTH_LONG).show();
	    }
	    
	    this.profilePictureView = (ImageView)this.findViewById(R.id.profilepicture);
	    profilePhotoFile = user.getParseFile("profilePhoto");
	    if (profilePhotoFile != null){
	    	
	    	byte[] data;
			try {
				data = profilePhotoFile.getData();
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		    	profilePictureView.setImageBitmap(bitmap);
			} catch (ParseException e) {
				Toast.makeText(UserAccountActivity.this, "Cannot Show Profile Picture: " + e, Toast.LENGTH_LONG).show();
			}
	    	
	    }
	    
	   
        findViewById(R.id.changepicture).setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent i = new Intent(UserAccountActivity.this, AddProfilePictureActivity.class);
				startActivity(i);	
			}
		});
	    
	    
	    
	    for (ParseObject a : accountList) {
	    	/*textView = new TextView(UserAccountActivity.this);			//Old code for just listing the names of the accounts.
	    	textView.setText(a.getString("displayName"));
	    	LinearLayout ll = (LinearLayout) findViewById(R.id.account_list);
	    	
	    	ll.addView(textView);*/
	    	Button accountButton = new Button(UserAccountActivity.this);
	    	accountButton.setText(a.getString("displayName"));
	    	accountButton.setBackgroundResource(R.drawable.green_button);
	    	LinearLayout ll = (LinearLayout)findViewById(R.id.account_list);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ll.addView(accountButton, lp);
            
            accountButton.setOnClickListener(new View.OnClickListener() {
            	public void onClick(View view) {
            		Button pressedButton = (Button) view;
            		String passedAccount = pressedButton.getText().toString();
            		Intent in = new Intent(UserAccountActivity.this, FinancialAccountActivity.class);
            		in.putExtra("FinancialAccountName", passedAccount);
            		startActivity(in);
            	}
            });
	    }
	    
	    findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        			
        		user.logOut();
        		
        		Toast.makeText(UserAccountActivity.this, "Successfully logged out!", Toast.LENGTH_LONG).show();
        		Intent in = new Intent(UserAccountActivity.this, Login.class);
				startActivity(in);

        	}
        });
		
		
	    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        
        // Populate list with options
		drawerItems = new ArrayList<String>();
		drawerItems.add(myAccountString);
		drawerItems.add(spendingReportString);
		drawerItems.add(logOutString);
		adapter = new ArrayAdapter(this, R.layout.draw_list_layout, drawerItems);
		drawerList.setAdapter(adapter);

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.title_activity_drawer);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.drawer_title);
            }
        };
        
        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        // Set the list's click listener
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
	    
	    
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        return true;
    }
	
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

			final String item = (String) parent.getItemAtPosition(position);

			if (item.equals(logOutString)) {
				user.logOut();
        		Toast.makeText(UserAccountActivity.this, "Successfully logged out!", Toast.LENGTH_LONG).show();
        		Intent in = new Intent(UserAccountActivity.this, Login.class);
				startActivity(in);
			} else if (item.equals(myAccountString)) {
				Intent i = new Intent(UserAccountActivity.this, UserAccountActivity.class);
				startActivity(i);
			} else if (item.equals(spendingReportString)){
				Intent i = new Intent(UserAccountActivity.this, SpendingReportActivity.class);
				startActivity(i);
			} 
			
			drawerLayout.closeDrawer(drawerList);
		}
	}
    
	public void addAccount(View v) {
		Intent i = new Intent(UserAccountActivity.this, CreateAccount.class);
		startActivity(i);
	}

	
	public void removeAccount(View v) {
		//TODO Add remove account functionality for button
	}
	
	public void viewReport(View v) {
		Intent in = new Intent(UserAccountActivity.this, SpendingReportActivity.class);
		startActivity(in);
	}

}
