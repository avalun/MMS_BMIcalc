package com.example.mms_bmicalc;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context appContext;
    public boolean isImperial = false;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
}
