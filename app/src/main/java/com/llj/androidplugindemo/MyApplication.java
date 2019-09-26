package com.llj.androidplugindemo;

import android.app.Application;

import com.llj.androidplugindemo.hook.HookHelper;

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
        try {
            HookHelper.hookAMS();
            HookHelper.hookHandler();
            HookHelper.hookInstrumentation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
