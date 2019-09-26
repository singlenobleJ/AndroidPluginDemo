package com.llj.androidplugindemo;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.llj.androidplugindemo.hook.AppClassLoaderHelper;
import com.llj.androidplugindemo.hook.HookHelper;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MainActivity extends AppCompatActivity {

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
        findViewById(R.id.btn_start_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOtherApp();
            }
        });
    }

    private void startOtherApp() {
        try {
            ClassLoader dexClassLoader = AppClassLoaderHelper.getClassLoader(MyApplication.sInstance, "/storage/emulated/0/plugin.apk");
            Class<?> targetClass = dexClassLoader.loadClass("com.llj.plugin.MainActivity");
            Intent intent = new Intent(this, targetClass);
            intent.putExtra(HookHelper.TARGET_INTENT_NAME, intent.getComponent().getClassName());
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
