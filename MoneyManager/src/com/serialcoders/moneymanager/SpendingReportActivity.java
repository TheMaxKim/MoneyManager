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

import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
/**
 * Shows the withdrawals within a specified time.
 *
 * @author Steven
 */
public class SpendingReportActivity extends Activity {
    /**
     * dates use to store the interval.
     */
    private Date dateFrom, dateTo;
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
        setContentView(R.layout.activity_spending_report);

        Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT",
                "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        user = ParseUser.getCurrentUser();
        c = Calendar.getInstance();
        dateTo = new Date();
        dateFrom = new Date(0);

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
                bundle.putInt("EditText", R.id.date_from);
                dateFragment.setArguments(bundle);
                dateFragment.show(getFragmentManager(), "datePicker");
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
                    Log.d("error1", e.getMessage());
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
                bundle.putInt("EditText", R.id.date_to);
                dateFragment.setArguments(bundle);
                dateFragment.show(getFragmentManager(), "datePicker");
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
        query.whereLessThan("amount", 0); //only get negative amounts
        Log.d("tag3", dateFrom.toString());
        Log.d("tag3", dateTo.toString());

          // adds a day because of lessThanOrEqualTo having issues
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTo);
        cal.add(Calendar.DATE, 1);
        dateTo = cal.getTime();

        query.whereGreaterThanOrEqualTo("createdAt", dateFrom);
        query.whereLessThanOrEqualTo("createdAt", dateTo);
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
            Button accountButton = new Button(this);
            Date transactionDate = o.getCreatedAt();
            if (o.getDouble("amount") < 0) {
                accountButton.setText(o.get("transactionType") + " "
                     + DecimalFormat.getCurrencyInstance()
                     .format(-o.getDouble("amount")) + " from "
                     + o.get("accountFullName") + "\n on "
                     + df.format(transactionDate));
                accountButton.setBackgroundResource(R.drawable.red_button);
            }
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            ll.addView(accountButton, lp);

            totalWithdrawals += o.getDouble("amount");
        }

        TextView balance = (TextView) findViewById(R.id.total_withdrawals);
        balance.setText("Withdrawals: $" + totalWithdrawals);
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure.
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

}
