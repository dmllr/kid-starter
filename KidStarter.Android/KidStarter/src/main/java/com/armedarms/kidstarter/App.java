package com.armedarms.kidstarter;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;

public class App extends Application {
    private static Context context;
    private static App app;

    public static Context getAppContext() {
        return App.context;
    }

    public static App getApp() {
        return app;
    }

    public boolean isUserParent = false;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        App.context = getApplicationContext();
        ActiveAndroid.initialize(this);
    }
}
