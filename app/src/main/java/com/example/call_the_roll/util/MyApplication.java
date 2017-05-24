package com.example.call_the_roll.util;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by 廖智涌 on 2017/5/22.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return  context;
    }

    @Override
    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }
}
