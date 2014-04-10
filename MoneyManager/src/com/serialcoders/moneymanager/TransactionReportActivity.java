package com.serialcoders.moneymanager;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.serialcoders.moneymanager.ActionButton.ActionButtonCallback;

import android.app.Activity;
import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class TransactionReportActivity extends Activity implements ActionButtonCallback {
	/**
     * string used to store the name of the date EditText.
     */
    private String editText;
	/**
     * string used to represent the date the withdrawal was created at.
     */
    private String createdAt;
	/**
     * string used to represent the amount of the withdrawal.
     */
    private String amount;
    /**
     * string used to represent the name of the datePicker.
     */
    private String datePicker;
	
    /**
     * date used to store the interval.
     */
    private Date dateFrom;
    /**
     * dates use to store the interval.
     */
    private Date dateTo;
    /**
     * used to edit the dates as needed.
     */
    private Calendar c;
    /**
     * the currently logged in user.
     */
    private ParseUser user;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_report);

        Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT",
                "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        user = ParseUser.getCurrentUser();
        
        c = Calendar.getInstance();
        dateTo = new Date();
        dateFrom = new Date(0);
        editText = getString(R.string.edit_text);
        amount = getString(R.string.withdrawal_amount);
        createdAt = getString(R.string.created_at);
        datePicker = getString(R.string.date_picker);

        setUpListeners();
    }
    /**
     * sets up the listeners on the text fields.
     */
    private void setUpListeners() {
        EditText dateFromText = (EditText) findViewById(R.id.date_from);
        dateFromText.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                DialogFragment dateFragment = new DatePickerFragment();
                Bundle bundle = new Bundle(1);
                bundle.putInt(editText, R.id.date_from);
                dateFragment.setArguments(bundle);
                dateFragment.show(getFragmentManager(), datePicker);
            }
        });
        dateFromText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(final Editable v) {
                DateFormat dateFormat = DateFormat
                        .getDateInstance(DateFormat.SHORT);
                try {
                    dateFrom = dateFormat.parse(v.toString());
                } catch (java.text.ParseException e) {
                    dateFrom = new Date();
                }
                findWithdrawals();
            }

            @Override
            public void beforeTextChanged(final CharSequence s,
                    final int start, final int count,
                    final int after) { }

            @Override
            public void onTextChanged(final CharSequence s, final int start,
                    final int before,
                    final int count) { }

        });

        EditText dateToText = (EditText) findViewById(R.id.date_to);
        dateToText.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                DialogFragment dateFragment = new DatePickerFragment();
                Bundle bundle = new Bundle(1);
                bundle.putInt(editText, R.id.date_to);
                dateFragment.setArguments(bundle);
                dateFragment.show(getFragmentManager(), datePicker);
            }
        });
        dateToText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(final Editable v) {
                DateFormat dateFormat = DateFormat
                        .getDateInstance(DateFormat.SHORT);
                try {
                    dateTo = dateFormat.parse(v.toString());
                    c.setTime(dateTo);
                    c.add(Calendar.DAY_OF_YEAR, 1);
                    dateTo = c.getTime();
                } catch (java.text.ParseException e) {
                    dateTo = new Date();
                    Log.d("error1", e.getMessage());
                }
                findWithdrawals();
            }

            @Override
            public void beforeTextChanged(final CharSequence s,
                    final int start, final int count,
                    final int after) { }

            @Override
            public void onTextChanged(final CharSequence s,
                    final int start, final int before,
                    final int count) { }
        });
    }
    /**
     * updates the list of withdrawals.
     */
    private void findWithdrawals() {
        //Find all Transactions which are withdrawals
        Log.d("tag2", "hit this");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Transaction");
        List<ParseObject> transactionList;
        query.whereEqualTo("userName", user.getUsername());

          // adds a day because of lessThanOrEqualTo having issues
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTo);
        cal.add(Calendar.DATE, 1);
        dateTo = cal.getTime();

        query.whereGreaterThanOrEqualTo(createdAt, dateFrom);
        query.whereLessThanOrEqualTo(createdAt, dateTo);
        try {
            transactionList = query.find();
        } catch (ParseException e) {
            transactionList = new ArrayList<ParseObject>();
            Toast.makeText(this, "Cannot load transactions: " + e,
                    Toast.LENGTH_LONG).show();
        }

        Double totalWithdrawals = 0.0;
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        LinearLayout ll = (LinearLayout) findViewById(R.id.withdrawal_list);
        ll.removeAllViews();
        for (ParseObject o : transactionList) {
            TransactionButton accountButton = new TransactionButton(this, o);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            ll.addView(accountButton, lp);

            totalWithdrawals += o.getDouble(amount);
        }

        TextView balance = (TextView) findViewById(R.id.total_withdrawals);
        balance.setText("Total: $" + totalWithdrawals);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void refresh() {
		findWithdrawals();
		
	}
}
