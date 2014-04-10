package com.serialcoders.moneymanager;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.serialcoders.moneymanager.ActionButton.ActionButtonCallback;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TransactionButton extends ActionButton {
	
	private String id;
	public TransactionButton(Context context) {
		super(context);
	}
	
	public TransactionButton(Context context, ParseObject o) {
		this(context);
		this.id = o.getObjectId();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		
		if (o.getDouble("amount") < 0) {
			setText(o.get("transactionType") + " "
                     + DecimalFormat.getCurrencyInstance()
                        .format(-o.getDouble("amount")) + " from "
                     + o.get("accountFullName") + "\n on "
                     + df.format(o.getCreatedAt()));
			setBackgroundResource(R.drawable.red_button);
		} else {
			setText(o.get("transactionType") + " "
                     + DecimalFormat.getCurrencyInstance()
                        .format(o.getDouble("amount")) + " to "
                     + o.get("accountFullName") + "\n on "
                     + df.format(o.getCreatedAt()));
			setBackgroundResource(R.drawable.green_button);
		}
		this.setUpListeners();
	}
	
	public void setUpListeners() {
		
        this.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
			    builder.setTitle("Are you sure you want to delete this transaction?");
			    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						final ProgressDialog dlg = new ProgressDialog(getContext());
		                dlg.setTitle("One Moment");
		                dlg.setMessage("Deleting...");
		                dlg.show();
						ParseQuery<ParseObject> query = ParseQuery.getQuery("Transaction");
				        //query.whereEqualTo("username", user.getUsername());
						query.getInBackground(id, new GetCallback<ParseObject>() {

							@Override
							public void done(ParseObject object,
									ParseException e) {
								if (object != null) {
									object.deleteInBackground(new DeleteCallback() {

										@Override
										public void done(ParseException e) {
											((ActionButtonCallback)getContext()).refresh();
											dlg.dismiss();
										}
									});
									
								}
								
							}
							
						});
				        
					}

				});
			    builder.setNegativeButton("Cancel", null);
			    AlertDialog alert = builder.create();
                alert.show();
				return true;
			}
		});
	}

}
