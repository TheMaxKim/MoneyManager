package com.serialcoders.moneymanager;

import java.text.DecimalFormat;

import android.content.Context;
import android.widget.Button;

public class ActionButton extends Button {

	public ActionButton(Context context) {
		super(context);
		
	}
	
	public interface ActionButtonCallback {
		void refresh();
	}

}
