package com.serialcoders.moneymanager.test;

import com.serialcoders.moneymanager.R;
import com.serialcoders.moneymanager.SpendingReportActivity;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class FindWithdrawalsTest extends ActivityInstrumentationTestCase2<SpendingReportActivity>{

	Activity activity;
	EditText dateTo, dateFrom;
	LinearLayout list;
	
	public FindWithdrawalsTest() {
		super(SpendingReportActivity.class);

	}
	@UiThreadTest
	protected void setUp() throws Exception {
		
		
		super.setUp();
		
		activity = getActivity();
		
		
		
		dateTo = (EditText) activity.findViewById(com.serialcoders.moneymanager.R.id.date_to);
		dateFrom = (EditText) activity.findViewById(com.serialcoders.moneymanager.R.id.date_from);
		
		list = (LinearLayout) activity.findViewById(R.id.withdrawal_list);
	}
	@UiThreadTest
	public void testFindWithdrawals() {
		
		
		dateTo.setText("03/31/2014");
		dateFrom.setText("01/28/2014");
		
		//SystemClock.sleep(7000); // need to allow time for parse to stuff.
		
		Button button0 = (Button) list.getChildAt(0);
		assertEquals(button0.getText(), "Withdrawal $250.00 from test #1\n on 03/24/2014 07:00:09 PM");
	}
	
	@UiThreadTest
	public void testError() {
		
		
		dateTo.setText("03/31/2014");
		dateFrom.setText("this should do nothing!");
		
		//SystemClock.sleep(7000); // need to allow time for parse to stuff.
		
		Button button0 = (Button) list.getChildAt(0);
		assertEquals(button0, null);
	}
	@UiThreadTest
	public void testFindWithdrawals2() {
		
		
		dateTo.setText("3/26/14");
		dateFrom.setText("3/25/14");
		
		//SystemClock.sleep(7000); // need to allow time for parse to stuff.
		
		Button button0 = (Button) list.getChildAt(0);
		assertEquals(button0.getText(), "Withdrawal $8.00 from test #1\n on 03/25/2014 02:41:34 PM");
	}
	
	
	
	
}
