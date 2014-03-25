package com.serialcoders.moneymanager;

import java.util.ArrayList;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseGeoPoint;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class TransactionActivity extends Activity implements LocationListener {
	Double amount;
	String target;
	String passedName;
	String transactionType; 
	LocationManager mLocationManager;
	ParseGeoPoint transactionLocation;
	ParseUser user;
	ParseObject selectedAccount;
	Double currentAccountBalance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);
		Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT", "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
		
		Intent intent = getIntent();
		passedName = intent.getExtras().getString("FinancialAccountName");
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);		
		Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location == null) {
			location = new Location("default");
		}
		transactionLocation = geoPointFromLocation(location);
        
        user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> accountQuery = ParseQuery.getQuery("Account");
	    List<ParseObject> accountList;
	    accountQuery.whereEqualTo("username", user.getUsername());
	    accountQuery.whereEqualTo("displayName", passedName);
	    try {
	    	accountList = accountQuery.find();
	    } catch (ParseException e) {
	    	accountList = new ArrayList<ParseObject>();
	    	Toast.makeText(this, "Cannot load account information: " + e, Toast.LENGTH_LONG).show();
	    }
	    selectedAccount = accountList.get(0);
	    currentAccountBalance = (Double) selectedAccount.getDouble("currentBalance");
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transaction, menu);
		return true;
	}

	public void checkTransaction(View v) {
		EditText amountText = (EditText) findViewById(R.id.editText1);
        EditText targetText = (EditText) findViewById(R.id.editText2);
        
        if (amountText.getText().toString().equals("")) {
        	Toast.makeText(this, "That's not a valid transaction amount!", Toast.LENGTH_SHORT).show();
        } else {
        
	        amount = Double.parseDouble(amountText.getText().toString());
	        target = targetText.getText().toString();
	        
	        if (amount == 0) {
	        	Toast.makeText(this, "That's not a valid transaction amount!", Toast.LENGTH_SHORT).show();
	        } else if (target.equals("")) {
	        	Toast.makeText(this, "That's not a valid deposit source or withdrawal target!", Toast.LENGTH_SHORT).show();
	        } else {
	        	AlertDialog.Builder builder = new AlertDialog.Builder(TransactionActivity.this);
	        	builder.setCancelable(true);
	        	builder.setTitle("Are you sure you'd like to make this transaction?");
	        	builder.setPositiveButton("Make Transaction", new DialogInterface.OnClickListener() {
	        	  @Override
	        	  public void onClick(DialogInterface dialog, int which) {
	        		  
	    	        	createTransaction(amount, target);
	    	    	    dialog.dismiss();
	    	    		Intent i = new Intent(TransactionActivity.this, FinancialAccountActivity.class);
	    	    		i.putExtra("FinancialAccountName", passedName);
	    	    		startActivity(i);
	        	  }
	        	});
	        	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        	  @Override
	        	  public void onClick(DialogInterface dialog, int which) {
	        	    dialog.dismiss();
	        	  }
	        	});
	        	AlertDialog alert = builder.create();
	        	alert.show();
	        }
    	
        }
	}
	
	public void createTransaction(Double amount, String target) {


        RadioButton rbw = (RadioButton) findViewById(R.id.withdrawRadio);
        
        if (rbw.isChecked()) {
        	amount = -amount;
        	transactionType = "Withdrawal";
        	currentAccountBalance += amount;
        }
        else{
        	transactionType = "Deposit";
        	currentAccountBalance += amount;
        }
        
        
        ParseObject transaction = new ParseObject("Transaction");
		transaction.put("accountFullName", passedName);
		transaction.put("amount", amount);
		transaction.put("target", target);
		transaction.put("userName", user.getUsername());
		transaction.put("transactionType", transactionType);
		transaction.put("transactionLocation", transactionLocation);
		transaction.saveInBackground();
		
		selectedAccount.put("currentBalance", currentAccountBalance);
		selectedAccount.saveInBackground();
		
		Toast.makeText(TransactionActivity.this, "Transaction made!", Toast.LENGTH_LONG).show();
		finish();
		startActivity(getIntent());
	}
	
	private ParseGeoPoint geoPointFromLocation(Location location) {
		  return new ParseGeoPoint(location.getLatitude(), location.getLongitude());
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
