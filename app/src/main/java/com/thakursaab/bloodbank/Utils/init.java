package com.thakursaab.bloodbank.Utils;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

public class init extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //FAST ANDROID NETWORKING INIT
        AndroidNetworking.initialize(getApplicationContext());
    }
}
