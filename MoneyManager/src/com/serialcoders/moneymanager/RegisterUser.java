package com.serialcoders.moneymanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.app.ActionBar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterUser extends Activity {

	private EditText usernameView;
    private EditText passwordView;
    private EditText confirmPasswordView;
   
    private String username;
    private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);

		Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        
        usernameView = (EditText) findViewById(R.id.registerName);
        passwordView = (EditText) findViewById(R.id.registerPassword);
        confirmPasswordView = (EditText) findViewById(R.id.confirmPassword);
        
    
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            	String joinMessage = " " + getResources().getString(R.string.error_join)+ " ";
            	
            	String errorIntro = getResources().getString(R.string.error_intro) + " ";
            	
               // Validate the sign up data
                boolean validationError = false;
                StringBuilder validationErrorMessage =
                        new StringBuilder(errorIntro);
                if (isEmpty(usernameView)) {
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.error_blank_username));
                }
                if (isEmpty(passwordView)) {
                    if (validationError) {
                        validationErrorMessage.append(joinMessage);
                    }
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.error_blank_password));
                }
                if (!isMatching(passwordView, confirmPasswordView)) {
                    if (validationError) {
                        validationErrorMessage.append(joinMessage);
                    }
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(
                            R.string.error_password_mismatch)); 
                }
                //validationErrorMessage.append(getResources().getString(R.string.error_end));

                // If there is a validation error, display the error
                if (validationError) {
                    Toast.makeText(RegisterUser.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                // Set up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(RegisterUser.this);
                dlg.setTitle("One Moment");
                dlg.setMessage("Signing up...");
                dlg.show();

                // Set up a new Parse user
                ParseUser user = new ParseUser();
                username = usernameView.getText().toString();
                user.setUsername(username);
                password = passwordView.getText().toString();
                user.setPassword(password);
                
                // Call the Parse signup method
                user.signUpInBackground(new SignUpCallback() {

                	@Override
                    public void done(ParseException e) {
                        dlg.dismiss();
                        if (e != null) {
                            // Show the error message
                            Toast.makeText(RegisterUser.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                        	login(username, password);
                        	
							Intent i = new Intent(RegisterUser.this, UserAccountActivity.class);
							startActivity(i);
                        	
                        
                        }
                    }
                });
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_user, menu);
		return true;
	}

	private void login(String username, String password) {
    	final ProgressDialog dlg = new ProgressDialog(RegisterUser.this);
        dlg.setTitle("One Moment");
        dlg.setMessage("Logging in...");
        dlg.show();

    	ParseUser.logInInBackground(username, password, new LogInCallback() {
    		@Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // The user is logged in.
                	Toast.makeText(RegisterUser.this, "Login Successful!", Toast.LENGTH_LONG).show();
                } else {
                	Toast.makeText(RegisterUser.this, "Login Failed!"+e.toString(), Toast.LENGTH_LONG).show();
                    // Registration failed. Look at the ParseException to see what happened.
                }
                dlg.dismiss();
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isMatching(EditText etText1, EditText etText2) {
        if (etText1.getText().toString().equals(etText2.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

}
