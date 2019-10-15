package com.llj.plugin_lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author: lilinjie
 * @date: 2019-10-14 17:39
 * @description: 插件基础Activity
 */
public abstract class PluginBaseActivity extends AppCompatActivity implements IPlugin {
    private static final String TAG = "PluginBaseActivity";
    protected Activity mProxyActivity;
    private int from = IPlugin.FROM_INTERNAL;

    @Override
    public void attach(Activity proxyActivity) {
        mProxyActivity = proxyActivity;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        if (saveInstanceState != null) {
            from = saveInstanceState.getInt(PluginApkConstants.TAG_FROM);
        }
        if (from == IPlugin.FROM_INTERNAL) {
            super.onCreate(saveInstanceState);
            mProxyActivity = this;
        }

    }

    @Override
    public void onStart() {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onStart();
        } else {
            Log.d(TAG, "onStart: 宿主启动onStart()");
        }
    }

    @Override
    public void onResume() {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onResume();
        } else {
            Log.d(TAG, "onStart: 宿主启动onResume()");
        }
    }

    @Override
    public void onRestart() {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onRestart();
        } else {
            Log.d(TAG, "onStart: 宿主启动onRestart()");
        }
    }

    @Override
    public void onPause() {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onPause();
        } else {
            Log.d(TAG, "onStart: 宿主启动onPause()");
        }
    }

    @Override
    public void onStop() {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onStop();
        } else {
            Log.d(TAG, "onStart: 宿主启动onStop()");
        }
    }

    @Override
    public void onDestroy() {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onDestroy();
        } else {
            Log.d(TAG, "onStart: 宿主启动onDestroy()");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onStart: 宿主启动onActivityResult()");
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (from == IPlugin.FROM_INTERNAL) {
            super.setContentView(layoutResID);
        } else {
            mProxyActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        if (from == IPlugin.FROM_INTERNAL) {
            super.setContentView(view);
        } else {
            mProxyActivity.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (from == IPlugin.FROM_INTERNAL) {
            super.setContentView(view, params);
        } else {
            mProxyActivity.setContentView(view, params);
        }
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        if (from == IPlugin.FROM_INTERNAL) {
            super.addContentView(view, params);
        } else {
            mProxyActivity.addContentView(view, params);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (from == IPlugin.FROM_INTERNAL) {
            return super.findViewById(id);
        } else {
            return mProxyActivity.findViewById(id);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (from == IPlugin.FROM_INTERNAL) {
            super.startActivity(intent);
        } else {
            PluginManager.getInstance().gotoActivity(mProxyActivity, intent.getComponent().getClassName());
        }

    }
}
