package com.serialcoders.moneymanager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.Parse;
import com.parse.ParseObject;
import com.serialcoders.moneymanager.ActionButton.ActionButtonCallback;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

/**
 * Allows users to view accounts.
 *
 * @author Steven
 */
public class UserAccountActivity extends Activity implements ActionButtonCallback {
    /**
     * The currently logged in user.
     */
    private ParseUser user;
    /**
     * Profile photo of user.
     */
    private ParseFile profilePhotoFile;
    /**
     * view used to show profile picture.
     */
    private ImageView profilePictureView;
    /**
     * layout of the slider drawer.
     */
    private DrawerLayout drawerLayout;
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
    private String displayName = "displayName";
    /**
     * called when the activity is first started.
     *
     * @param saved used to resume application
     */
    protected final void onCreate(final Bundle saved) {
        super.onCreate(saved);

        ScrollView sv = new ScrollView(this);
        setContentView(R.layout.activity_useraccount);

        Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT",
                "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        user = ParseUser.getCurrentUser();

        displayWelcome();
        updateProfilePicture();
        findAccounts();
        createDrawer();
    }
    /**
     * called when the activity resumes.
     */
    protected final void onResume() {
        super.onResume();

        findAccounts();
    }
    /**
     * creates the slider drawer.
     */
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
    /**
     * displays the welcome text.
     */
    private void displayWelcome() {
        TextView textView = (TextView) findViewById(R.id.welcomemessage);
        String username = user.getUsername();
        textView.setText("Welcome to MoneyManager, " + username + "!");
    }
    /**
     * updates the profile picture.
     */
    private void updateProfilePicture() {
        this.profilePictureView =
                (ImageView) this.findViewById(R.id.profilepicture);
        profilePhotoFile = user.getParseFile("profilePhoto");
        if (profilePhotoFile != null) {

            byte[] data;
            try {
                data = profilePhotoFile.getData();
                Bitmap bitmap =
                        BitmapFactory.decodeByteArray(data, 0, data.length);
                profilePictureView.setImageBitmap(bitmap);
            } catch (ParseException e) {
                Toast.makeText(UserAccountActivity.this,
                        "Cannot Show Profile Picture: " + e,
                        Toast.LENGTH_LONG).show();
            }

        }
        
        findViewById(R.id.changepicture)
            .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent i = new Intent(UserAccountActivity.this,
                        AddProfilePictureActivity.class);
                    startActivity(i);
                }
            });
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
            Toast.makeText(UserAccountActivity.this,
                    "Cannot load Accounts: " + e, Toast.LENGTH_LONG).show();
        }

        LinearLayout ll = (LinearLayout) findViewById(R.id.account_list);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        ll.removeAllViews();
        for (final ParseObject a : accountList) {
            AccountButton accountButton = new AccountButton(UserAccountActivity.this);
            accountButton.setAccount(a.getString(displayName));
            accountButton.setBalance(a.getDouble("currentBalance"));
            accountButton.setAccountFullName(a.getString("fullName"));

            ll.addView(accountButton, lp);
        }
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
    public final boolean onOptionsItemSelected(final MenuItem item) {
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
                logOut(view);
            } else if (item.equals(myAccountString)) {
                Intent i = new Intent(UserAccountActivity.this,
                        UserAccountActivity.class);
                startActivity(i);
            } else if (item.equals(spendingReportString)) {
                Intent i = new Intent(UserAccountActivity.this,
                        SpendingReportActivity.class);
                startActivity(i);
            } else if (item.equals(transactionReportString)) {
            	Intent i = new Intent(UserAccountActivity.this,
                    TransactionReportActivity.class);
                startActivity(i);
            } else if (item.equals(transactionMapString)) {
                Intent i = new Intent(UserAccountActivity.this,
                        TransactionMapActivity.class);
                startActivity(i);
            }

            drawerLayout.closeDrawer(drawerList);
        }
    }
    /**
     * launches the CreateAccount Activity.
     *
     * @param v calling view.
     */
    public final void addAccount(final View v) {
        Intent i = new Intent(UserAccountActivity.this, CreateAccount.class);
        startActivity(i);
    }
    
    
    /**
     * log the user out to main screen.
     *
     * @param v calling view.
     */
    public final void logOut(final View v) {
        user.logOut();
        Toast.makeText(UserAccountActivity.this,
                "Successfully logged out!", Toast.LENGTH_LONG).show();
        Intent in = new Intent(UserAccountActivity.this, Login.class);
        startActivity(in);
    }
    

    /**
     * Launchers the RemoveAccount ativity.
     *
     * @param v calling view
     */
    public void removeAccount(final View v) {
        //TODO Add remove account functionality for button
    }
	@Override
	public void refresh() {
		findAccounts();
	}


}
