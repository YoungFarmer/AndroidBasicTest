package com.example.anotherapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.i("appApp", "App attachBaseContext: " + getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("appApp", "App onCreate: " + getApplicationContext());
    }
}
