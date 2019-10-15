package com.llj.plugin_lib;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author: lilinjie
 * @date: 2019-10-14 17:27
 * @description: 代理Activity
 */
public class ProxyActivity extends Activity {
    private String mRealActivityName;
    private IPlugin mIPlugin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealActivityName = getIntent().getStringExtra(PluginApkConstants.TAG_CLASS_NAME);
        try {
            Class<?> realActivityCls = PluginManager.getInstance().getDexClassLoader().loadClass(mRealActivityName);
            Object realActivity = realActivityCls.newInstance();
            if (realActivity instanceof IPlugin) {
                mIPlugin = (IPlugin) realActivity;
                Bundle b = new Bundle();
                b.putInt(PluginApkConstants.TAG_FROM, IPlugin.FROM_EXTRENAL);
                mIPlugin.attach(this);
                mIPlugin.onCreate(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        mIPlugin.onStart();
        super.onStart();
    }

    @Override
    protected void onResume() {
        mIPlugin.onResume();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        mIPlugin.onRestart();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        mIPlugin.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mIPlugin.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mIPlugin.onDestroy();
        super.onDestroy();
    }

    @Override
    public ClassLoader getClassLoader() {
        ClassLoader classLoader = PluginManager.getInstance().getDexClassLoader();
        if (classLoader != null) {
            return classLoader;
        }
        return super.getClassLoader();
    }

    @Override
    public Resources getResources() {
        Resources resources = PluginManager.getInstance().getResources();
        if (resources != null) {
            return resources;
        }
        return super.getResources();
    }
}
