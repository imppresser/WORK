package com.example.test1;

import android.app.Activity; 
import android.content.Intent; 
import android.os.Bundle; 
import android.os.Handler; 
 



public class SplashActivity extends Activity {

	 
    private final int SPLASH_DISPLAY_LENGHT = 3000; //—”≥Ÿ»˝√Î 
 
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.splashactivity); 
        new Handler().postDelayed(new Runnable(){ 
 
         @Override
         public void run() { 
             Intent mainIntent = new Intent(SplashActivity.this,ContactsMainActivity.class); 
             SplashActivity.this.startActivity(mainIntent); 
             SplashActivity.this.finish(); 
         } 
             
        }, SPLASH_DISPLAY_LENGHT); 
    } 

}
