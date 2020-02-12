package com.example.memseekandroid.Utils;

import android.app.Application;

import com.kontakt.sdk.android.common.KontaktSDK;

public class CoreApp extends Application {
    private static final String API_KEY ="SXkCMClsgYnKYZQXrCnqVDtOUVIwgOZd";
    @Override
    public void onCreate(){
        super.onCreate();
        initializeDependenscies();
    }
    //Initializing Kontakt SDK. Insert your API key to allow all samples to work correctly
    private void initializeDependenscies(){
        KontaktSDK.initialize(API_KEY);
    }
}
