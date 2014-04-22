package com.serialcoders.moneymanager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
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
public class UserAccountActivity extends SliderMenuActivity implements ActionButtonCallback {
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
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_useraccount, null, false);
        drawerLayout.addView(contentView, 0); 

        ScrollView sv = new ScrollView(this);
        //setContentView(R.layout.activity_useraccount);
        
        Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT",
                "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        user = ParseUser.getCurrentUser();

        displayWelcome();
        updateProfilePicture();
        findAccounts();
    }
    /**
     * called when the activity resumes.
     */
    protected final void onResume() {
        super.onResume();

        findAccounts();
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
