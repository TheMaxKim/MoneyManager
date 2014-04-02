package com.serialcoders.moneymanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * This activity represents the login screen of the financial app.
 * @author Max Kim
 *
 */
public class Login extends Activity {
	/**
	 * @param usernameView The textbox for the username to be entered.
	 */
    private EditText usernameView;
    /**
     * @param passwordView The textbox for the password to be entered.
     */
    private EditText passwordView;
    /**
     * @param buttonLogin The login button for the login screen.
     */
    private Button buttonLogin;
    /**
     * @param buttonRegister The register button for the login screen to make a new account.
     */
    private Button buttonRegister;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        
        ParseObject.registerSubclass(ParseUser.class);
        Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        ParseAnalytics.trackAppOpened(getIntent());
        
        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                
                final ProgressDialog dlg = new ProgressDialog(Login.this);
                dlg.setTitle("One Moment");
                dlg.setMessage("Logging in...");
                dlg.show();

                // Try to login
                String username = usernameView.getText().toString();
                String password = passwordView.getText().toString();

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            // The user is logged in.
                            Toast.makeText(Login.this, "Successfully Logged in!", Toast.LENGTH_LONG).show();
                            Intent i;
                            if (ParseUser.getCurrentUser().getUsername().equals("admin")) {
                                i = new Intent(Login.this, AdminAccountActivity.class);
                            } else {
                                i = new Intent(Login.this, UserAccountActivity.class);
                            }
                            startActivity(i);
                        } else {
                            Toast.makeText(Login.this, "Failed to login " + e.toString(), Toast.LENGTH_LONG).show();
                            // Signup failed. Look at the ParseException to see what happened.
                        }
                        dlg.dismiss();
                    }
                });
            }
        });
        
        // Sign up button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Goto sign up screen
                Intent in = new Intent(Login.this, RegisterUser.class);
                startActivity(in);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
}
