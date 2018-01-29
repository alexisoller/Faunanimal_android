package com.example.devomac02.faunanimal_android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


public class SplashScreen extends Activity {
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String Mypreferences = "Mypreferences";
    private SharedPreferences preferencessettings;
    private SharedPreferences.Editor preferenceseditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        preferencessettings = getSharedPreferences(Mypreferences, PREFERENCE_MODE_PRIVATE);
        preferenceseditor = preferencessettings.edit();
        preferenceseditor.putString("fragment_info_obj", null);


        preferenceseditor.putBoolean("login", false);
        preferenceseditor.putString("nombre", "");
        preferenceseditor.putString("apellidos", "");
        preferenceseditor.putString("email", "");

        preferenceseditor.commit();

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}

