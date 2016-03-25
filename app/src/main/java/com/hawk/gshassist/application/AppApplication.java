package com.hawk.gshassist.application;

import android.app.Application;

/**
 * 在此写用途
 * Created by hawk on 2016/3/15.
 */
public class AppApplication extends Application{
    private static AppApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
    public static AppApplication getInstance(){
        return instance;
    }
}
