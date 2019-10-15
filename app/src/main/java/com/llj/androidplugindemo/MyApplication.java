package com.llj.androidplugindemo;

import android.app.Application;
import android.content.Context;

import com.llj.plugin_lib.PluginManager;

/**
 * @author: lilinjie
 * @date: 2019-09-09 11:26
 * @description:
 */
public class MyApplication extends Application {
    public static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        PluginManager.getInstance().init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        try {
//            HookHelper.hookAMS();
//            HookHelper.hookPMS(this);
//            HookHelper.hookHandler();
//            HookHelper.hookInstrumentation();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
