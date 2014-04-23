package com.serialcoders.moneymanager;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GraphActivity extends Activity {

	private ParseUser user;
	private String[] accounts;
	private GraphView graphView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		
		Parse.initialize(this, "f0ZnpLcS3ysYplTiCoBOGKz3jFsdcGX9y5n3GLIT",
                "dZ5kg5BmoWFf5YdCBrDrcjZ7QA4SU5qSg8C151f3");
        user = ParseUser.getCurrentUser();
        Intent intent = getIntent();
        accounts = intent.getExtras().getStringArray("accounts");
		
		graphView = new LineGraphView(this, "Account Balance");
		
		setUpGraph();
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
		layout.addView(graphView);
	}
	
	private void setUpGraph() {
        Calendar cal = Calendar.getInstance();

		ParseQuery<ParseObject> accountQuery = ParseQuery.getQuery("Account");
        ParseObject account;
        double startDay = 0;
        double today = 0;
        for (int i = 0; i < accounts.length; i++) {
	        try {
	            account = accountQuery.get(accounts[i]);
	        } catch (ParseException e) {
	            account = null;
	            Toast.makeText(this, "Cannot load account information: " + e, Toast.LENGTH_LONG).show();
	        }
	        double balance = account.getDouble("initialBalance");
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Transaction");
	        List<ParseObject> transactionList;
	        query.whereEqualTo("userName", user.getUsername());
	        query.whereEqualTo("accountFullName", account.getString("displayName"));
	        try {
	            transactionList = query.find();
	        } catch (ParseException e) {
	            transactionList = new ArrayList<ParseObject>();
	            Toast.makeText(this, "Cannot load transactions: " + e,
	                    Toast.LENGTH_LONG).show();
	        }
	
	        List<GraphViewData> dataList = new ArrayList<GraphViewData>();
	        cal.setTime(account.getCreatedAt());
	        startDay = cal.get(Calendar.DAY_OF_YEAR)
	        		+ cal.get(Calendar.HOUR_OF_DAY)/24
            		+ cal.get(Calendar.MINUTE)/(60*24)
            		+ cal.get(Calendar.SECOND)/(60*60*24);
	        dataList.add(new GraphViewData(startDay, balance));
	        for (ParseObject o : transactionList) {
	        	Log.d("here", "kjblkj");
	            balance += o.getDouble("amount");
	            cal.setTime(o.getCreatedAt());
	            double time = cal.get(Calendar.DAY_OF_YEAR)
		        		+ cal.get(Calendar.HOUR_OF_DAY)/24
	            		+ cal.get(Calendar.MINUTE)/(60*24)
	            		+ cal.get(Calendar.SECOND)/(60*60*24);
	            dataList.add(new GraphViewData(time, balance));
	        }
	        cal = Calendar.getInstance();
	        today = cal.get(Calendar.DAY_OF_YEAR)
	        		+ cal.get(Calendar.HOUR_OF_DAY)/24
            		+ cal.get(Calendar.MINUTE)/(60*24)
            		+ cal.get(Calendar.SECOND)/(60*60*24);
	        dataList.add(new GraphViewData(today, balance));
	        GraphViewDataInterface[] data = dataList.toArray(new GraphViewData[0]);
	        Log.d("data0", data[0].getY()+"");
			float[] color = {i * 50, 255, 255};
			GraphViewSeries balanceSeries = new GraphViewSeries(account.getString("displayName"),
					new GraphViewSeriesStyle(Color.HSVToColor(color), 3), data);
			
			graphView.addSeries(balanceSeries);
			
		}
        graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
        	  @Override
        	  public String formatLabel(double value, boolean isValueX) {
        	    if (isValueX) {
        	    	Calendar cal = Calendar.getInstance();
        	    	cal.set(Calendar.DAY_OF_YEAR, (int) value);
        	    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        	    	return sdf.format(cal.getTime());
        	    }
        	    return DecimalFormat.getCurrencyInstance().format(value);
        	  }
        	});
        graphView.setViewPort(startDay, today - startDay + 1);
        graphView.setScrollable(true);
        graphView.setScalable(true);
        graphView.setShowLegend(true);
        graphView.setLegendAlign(LegendAlign.BOTTOM);
        graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
        graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
        //graphView.getGraphViewStyle().setNumHorizontalLabels(3);
	}
}
