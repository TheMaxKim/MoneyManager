package com.serialcoders.moneymanager;

import java.util.logging.Logger;

import org.apache.commons.logging.Log;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.Parse;
import com.parse.RequestPasswordResetCallback;


public class ResetPasswordActivity extends Activity {
	
	private String accountEmail = null;
	private Button button_forgot_password;
	private EditText forgotpw_email;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        
        //getActionBar().setDisplayShowTitleEnabled(false);
        
        button_forgot_password = (Button) findViewById(R.id.resetPassword);
                      
        button_forgot_password.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				forgotpw_email = (EditText) findViewById(R.id.forgot_password_userEmail);
				accountEmail = forgotpw_email.getText().toString();
				checkEmailID();
				
				
				
			}
		});
        
	}
	
	protected void checkEmailID() {
	    // TODO Auto-generated method stub
	    if (TextUtils.isEmpty(accountEmail)) {
	        forgotpw_email.setError(getString(R.string.error_field_required));
	    }
	    else
	        forgotPassword(accountEmail);
	}
	
	
	public void forgotPassword(String accountEmail){
		ParseUser.requestPasswordResetInBackground(accountEmail, new UserForgotPasswordCallBack());
	}
	
	private class UserForgotPasswordCallBack extends RequestPasswordResetCallback{
		public UserForgotPasswordCallBack(){
			super();
			
		}

		@Override
		public void done(ParseException e) {
			if (e == null) {
				LayoutInflater layoutInflater 
		        = (LayoutInflater)getBaseContext()
		         .getSystemService(LAYOUT_INFLATER_SERVICE);  
		       View popupView = layoutInflater.inflate(R.layout.pop_up_window, null);  
		                final PopupWindow popupWindow = new PopupWindow(
		                  popupView, 
		                  LayoutParams.WRAP_CONTENT,  
		                        LayoutParams.WRAP_CONTENT);  
		                
		                Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
		                btnDismiss.setOnClickListener(new Button.OnClickListener(){

		        @Override
		        public void onClick(View v) {
		         // TODO Auto-generated method stub
		         popupWindow.dismiss();
		        }});
		                popupWindow.showAsDropDown(button_forgot_password, 50, -30);
	        } else {
	            Toast.makeText(getApplicationContext(), "Failed to sent link to your email for reset Password", Toast.LENGTH_LONG).show();
	            System.out.println(e);
	            

	        }
		}
	}
	
	
}

