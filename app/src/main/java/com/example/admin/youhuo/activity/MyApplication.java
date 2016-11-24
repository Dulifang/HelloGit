package com.example.admin.youhuo.activity;

import android.app.Application;

import com.example.admin.youhuo.view.Loader;

/**
 * Created by admin on 2016/11/23.
 */

public class MyApplication extends Application {
    public Loader loader;

    @Override
    public void onCreate() {
        super.onCreate();
        loader = new Loader();
    }
}
