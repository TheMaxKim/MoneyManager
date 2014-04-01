package com.serialcoders.moneymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 
 * Android activity for making a splash screen.
 * 
 * @author Yuh Meei
 *
 */
public class SplashScreen extends Activity {
 
    /**
     * @param splashTimeOut splash screen timer
     */
    private static int splashTimeOut = 2000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
 
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, Login.class);
                startActivity(i);
                finish();
            }
        }, splashTimeOut);
    }
 
}
