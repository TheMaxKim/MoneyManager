package com.serialcoders.moneymanager;

import java.util.ArrayList;

import com.parse.Parse;
import com.parse.ParseUser;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SliderMenuActivity extends FragmentActivity{
	/**
     * The currently logged in user.
     */
    private ParseUser user;
    /**
     * layout of the slider drawer.
     */
	protected DrawerLayout drawerLayout;
    /**
     * used to display drawer list.
     */
    private ListView drawerList;
    /**
     * toggle to open drawer.
     */
    private ActionBarDrawerToggle drawerToggle;
    /**
     * list of items in drawer.
     */
    private ArrayList<String> drawerItems;
    /**
     * used to show items in drawer.
     */
    private ArrayAdapter adapter;
    /**
     * log out string.
     */
    private String logOutString = "Log Out";
    /**
     * my accounts string.
     */
    private String myAccountString = "My Accounts";
    /**
     * spending report string.
     */
    private String spendingReportString = "Spending Report";
    /**
     * transaction report string.
     */
    private String transactionReportString = "Transaction Report";
    /**
     * transaction map string.
     */
    private String transactionMapString = "Transaction Map";
    /**
     * display name string.
     */
    private String graphString = "Account Graph";
    
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slidermenu);
		Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT",
                "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        user = ParseUser.getCurrentUser();
        
		createDrawer();
	}
	private void createDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // Populate list with options
        drawerItems = new ArrayList<String>();
        drawerItems.add(myAccountString);
        drawerItems.add(spendingReportString);
        drawerItems.add(transactionReportString);
        drawerItems.add(transactionMapString);
        drawerItems.add(logOutString);
        drawerItems.add(graphString);
        
        adapter = new ArrayAdapter(this,
                R.layout.draw_list_layout, drawerItems);
        drawerList.setAdapter(adapter);

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace Up caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in completely closed state. */
            public void onDrawerClosed(final View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.title_activity_drawer);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(final View drawerView) {
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
    protected final void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public final void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }
    /**
     * handles events in the drawer.
     *
     * @author Ethan
     */
    private class DrawerItemClickListener
            implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(final AdapterView<?> parent, final View view,
                final int position, final long id) {

            final String item = (String) parent.getItemAtPosition(position);

            if (item.equals(logOutString)) {
            	user.logOut();
                Toast.makeText(SliderMenuActivity.this,
                        "Successfully logged out!", Toast.LENGTH_LONG).show();
                Intent in = new Intent(SliderMenuActivity.this, Login.class);
                startActivity(in);
            } else if (item.equals(myAccountString)) {
                Intent i = new Intent(SliderMenuActivity.this,
                        UserAccountActivity.class);
                startActivity(i);
            } else if (item.equals(spendingReportString)) {
                Intent i = new Intent(SliderMenuActivity.this,
                        SpendingReportActivity.class);
                startActivity(i);
            } else if (item.equals(transactionReportString)) {
            	Intent i = new Intent(SliderMenuActivity.this,
                    TransactionReportActivity.class);
                startActivity(i);
            } else if (item.equals(transactionMapString)) {
                Intent i = new Intent(SliderMenuActivity.this,
                        TransactionMapActivity.class);
                startActivity(i);
            } else if (item.equals(graphString)) {
                Intent i = new Intent(SliderMenuActivity.this,
                        GraphActivity.class);
                i.putExtra("accountName", "bb"); //TODO change
                startActivity(i);
            }

            drawerLayout.closeDrawer(drawerList);
        }
    }
}
