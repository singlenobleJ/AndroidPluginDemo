package com.llj.androidplugindemo.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;

import com.llj.androidplugindemo.MyApplication;

/**
 * @author: lilinjie
 * @date: 2019-09-09 14:05
 * @description:
 */
public class InstrumentationProxy extends Instrumentation {
    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String intentName = intent.getStringExtra(HookHelper.TARGET_INTENT_NAME);
        if (!TextUtils.isEmpty(intentName)) {
            Activity activity = super.newActivity(AppClassLoaderHelper.getClassLoader(MyApplication.sInstance, "/storage/emulated/0/plugin.apk"), className, intent);
            try {
                ReflectUtils.setField(ContextThemeWrapper.class, activity, "mResources", AppClassLoaderHelper.getPluginResources(MyApplication.sInstance, "/storage/emulated/0/plugin.apk"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return activity;
        }
        return super.newActivity(cl, className, intent);
    }
}
