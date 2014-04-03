package com.serialcoders.moneymanager;

 import java.text.DateFormat;
 import java.util.Calendar;
 import java.util.Date;

 import android.app.Dialog;
 import android.app.DialogFragment;
 import android.app.DatePickerDialog;
 import android.os.Bundle;
 import android.widget.DatePicker;
 import android.widget.EditText;

 /**
  * Class used to set the text in an editText view
  * to whatever is chosen by the user.
  *
  * @author Steven
  */
 public class DatePickerFragment extends DialogFragment
                 implements DatePickerDialog.OnDateSetListener {
     /**
      * Calendar used to store date chosen by user.
      */
     private static final Calendar CALENDAR = Calendar.getInstance();
     /**
      * date the user has chosen.
      */
     private static Date date;
     /**
      * The editText which the user is writing to.
      */
     private static EditText editText;

     /**
      * Method called upon creation of the DatePickerFragment.
      *
      * @param saved use to resume the app
      * @return a dialog for choosing the date
      */
     public final Dialog onCreateDialog(final Bundle saved) {
         editText = (EditText) getActivity().findViewById(getArguments()
                 .getInt("EditText"));

         final int year = CALENDAR.get(Calendar.YEAR);
         final int month = CALENDAR.get(Calendar.MONTH);
         final int day = CALENDAR.get(Calendar.DAY_OF_MONTH);

         return new DatePickerDialog(getActivity(), this, year, month, day);
     }

     /**
      *  Method called when the date is set by the user.
      *  sets the editText to this date
      *
      *  @param view the datePicker being used
      *  @param year the year chosen
      *  @param month the month chosen
      *  @param day the day chosen
      */
     public final void onDateSet(final DatePicker view, final int year,
             final int month, final int day) {
         CALENDAR.set(year, month, day);
         date = CALENDAR.getTime();
         final DateFormat dateFormat = DateFormat
                 .getDateInstance(DateFormat.SHORT);
         editText.setText(dateFormat.format(date));
     }
 }
