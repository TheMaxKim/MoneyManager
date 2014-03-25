package com.serialcoders.moneymanager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

public class DatePickerFragment extends DialogFragment
								implements DatePickerDialog.OnDateSetListener {
	
		final Calendar c = Calendar.getInstance();
		Date date;
		EditText editText;
		
		public Dialog onCreateDialog(Bundle SavedInstanceState) {
			editText = (EditText) getActivity().findViewById(getArguments().getInt("EditText"));
			
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		
		public void onDateSet(DatePicker view, int year, int month, int day) {
			c.set(year, month, day);
			date = c.getTime();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			editText.setText(df.format(date));
		}
}
