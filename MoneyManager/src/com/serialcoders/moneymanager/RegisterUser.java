package com.serialcoders.moneymanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * This activity represents the screen where a user can register an account.
 * @author Max
 *
 */
public class RegisterUser extends Activity {
	/**
	 * @param usernameView The textbox for the username to be entered.
	 */
    private EditText usernameView;
    /**
     * @param passwordView The textbox for the password to be entered.
     */
    private EditText passwordView;
    /**
     * @param confirmPasswordView The textbox for the password to be entered again to confirm it.
     */
    private EditText confirmPasswordView;
    /**
     * @param username The user provided username for the new account.
     */
    private String username;
    /**
     * @param password The user provided password for the new account.
     */
    private String password;
    /**
     * @param loadingMessage The loading message to use during login and signup.
     */
    private String loadingMessage = "One Moment";
    
    MediaPlayer mp;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        
        usernameView = (EditText) findViewById(R.id.registerName);
        passwordView = (EditText) findViewById(R.id.registerPassword);
        confirmPasswordView = (EditText) findViewById(R.id.confirmPassword);
        
        mp = MediaPlayer.create(this,R.raw.button_click);
        
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	mp.start();
            	
            	String space = " ";
                String joinMessage = space + getResources().getString(R.string.error_join) + space;
                
                String errorIntro = getResources().getString(R.string.error_intro) + space;
                
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
                dlg.setTitle(loadingMessage);
                dlg.setMessage("Signing up...");
                dlg.show();

                // Set up a new Parse user
                ParseUser user = new ParseUser();
                username = usernameView.getText().toString();
                user.setUsername(username);
                password = passwordView.getText().toString();
                user.setPassword(password);
                user.saveInBackground();
                
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
                            
                            Intent i = new Intent(RegisterUser.this, AddProfilePictureActivity.class);
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

    /**
     * This method is used for logging in the user with their new account after registration.
     * @param newUsername The username provided.
     * @param newPassword The password provided.
     */
    private void login(String newUsername, String newPassword) {
        final ProgressDialog dlg = new ProgressDialog(RegisterUser.this);
        dlg.setTitle(loadingMessage);
        dlg.setMessage("Logging in...");
        dlg.show();

        ParseUser.logInInBackground(newUsername, newPassword, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // The user is logged in.
                    Toast.makeText(RegisterUser.this, "Login Successful!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterUser.this, "Login Failed!" + e.toString(), Toast.LENGTH_LONG).show();
                    // Registration failed. Look at the ParseException to see what happened.
                }
                dlg.dismiss();
            }
        });
    }

    /**
     * Checks if the given textbox is empty.
     * @param etText The textbox to check.
     * @return Whether or not the textbox is empty.
     */
    private boolean isEmpty(EditText etText) {
        return !(etText.getText().toString().trim().length() > 0);
    }

    /**
     * Checks if the password is the same in the password and confirm password textboxes.
     * @param etText1 The first instance of the password.
     * @param etText2 The second instance of the password.
     * @return Returns true if they are equal.
     */
    private boolean isMatching(EditText etText1, EditText etText2) {
        return (etText1.getText().toString().equals(etText2.getText().toString()));
    }

}
