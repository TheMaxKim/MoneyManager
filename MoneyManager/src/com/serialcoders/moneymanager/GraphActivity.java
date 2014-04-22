package com.serialcoders.moneymanager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GraphActivity extends Activity {

	private ParseUser user;
	private String passedName;
	private GraphView graphView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		
		Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT",
                "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        user = ParseUser.getCurrentUser();
        Intent intent = getIntent();
        passedName = intent.getExtras().getString("accountName");
		
		graphView = new LineGraphView(this, "Account Balance");
		
		setUpGraph();
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
		layout.addView(graphView);
	}
	
	private void setUpGraph() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Transaction");
        List<ParseObject> transactionList;
        query.whereEqualTo("userName", user.getUsername());

        try {
            transactionList = query.find();
        } catch (ParseException e) {
            transactionList = new ArrayList<ParseObject>();
            Toast.makeText(this, "Cannot load transactions: " + e,
                    Toast.LENGTH_LONG).show();
        }

        Double totalWithdrawals = 0.0;
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        LinearLayout ll = (LinearLayout) findViewById(R.id.graph);
        
        Calendar cal = Calendar.getInstance();

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
        Log.d("size", ""+accountList.size());
        double balance = accountList.get(0).getDouble("initialBalance");
        cal.setTime(accountList.get(0).getCreatedAt());
        
        List<GraphViewData> dataList = new ArrayList<GraphViewData>();
        dataList.add(new GraphViewData(cal.get(Calendar.DAY_OF_YEAR), balance));
        for (ParseObject o : transactionList) {
            balance += o.getDouble("amount");
            cal.setTime(o.getCreatedAt());
            dataList.add(new GraphViewData(cal.get(Calendar.DAY_OF_YEAR), balance));
        }
        GraphViewDataInterface[] data = dataList.toArray(new GraphViewData[0]);
        Log.d("data0", data[0].getY()+"");
		GraphViewSeries balanceSeries = new GraphViewSeries(data);
		
		graphView.addSeries(balanceSeries);
	}
}
