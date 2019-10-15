package com.llj.androidplugindemo;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.llj.androidplugindemo.hook.AppClassLoaderHelper;
import com.llj.androidplugindemo.hook.HookHelper;
import com.llj.plugin_lib.PluginManager;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });
        findViewById(R.id.btn_load_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPluginApk();
                Toast.makeText(MainActivity.this, "插件加载成功", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_start_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPluginApk();
            }
        });
    }

    private void loadPluginApk() {
        String apkPath = AssetUtils.copyAssetToCache(this, "plugin.apk");
        PluginManager.getInstance().loadPluginApk(apkPath);
    }

    private void startPluginApk() {
        String pluginEnterActivityName = PluginManager.getInstance().getPackageInfo().activities[0].name;
        Log.d(TAG, "pluginEnterActivityName：" + pluginEnterActivityName);
        PluginManager.getInstance().gotoActivity(this, pluginEnterActivityName);
    }

    private void startOtherApp() {
        try {
            String pluginPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin.apk";
            ClassLoader dexClassLoader = AppClassLoaderHelper.getClassLoader(MyApplication.sInstance, pluginPath);
            Class<?> targetClass = dexClassLoader.loadClass("com.llj.plugin.MainActivity");
            Intent intent = new Intent(this, targetClass);
            intent.putExtra(HookHelper.TARGET_INTENT_NAME, intent.getComponent().getClassName());
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
