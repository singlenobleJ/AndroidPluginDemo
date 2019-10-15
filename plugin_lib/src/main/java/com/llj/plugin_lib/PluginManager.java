package com.llj.plugin_lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * @author: lilinjie
 * @date: 2019-10-14 15:48
 * @description: 插件管理器
 */
public class PluginManager {

    @SuppressLint("StaticFieldLeak")
    private static volatile PluginManager sInstance;
    private Context mContext;
    private PackageInfo mPackageInfo;
    private DexClassLoader mDexClassLoader;
    private Resources mResources;

    private PluginManager() {
    }

    public static PluginManager getInstance() {
        if (sInstance == null) {
            synchronized (PluginManager.class) {
                if (sInstance == null) {
                    sInstance = new PluginManager();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public void loadPluginApk(String apkPath) {
        mPackageInfo = mContext.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (mPackageInfo == null) {
            throw new RuntimeException("插件加载失败!");
        }
        File outFile = mContext.getDir("odex", Context.MODE_PRIVATE);
        mDexClassLoader = new DexClassLoader(apkPath, outFile.getAbsolutePath(), null, mContext.getClassLoader());
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            addAssetPathMethod.setAccessible(true);
            addAssetPathMethod.invoke(assetManager, apkPath);
            mResources = new Resources(assetManager, mContext.getResources().getDisplayMetrics(), mContext.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DexClassLoader getDexClassLoader() {
        return mDexClassLoader;
    }

    public Resources getResources() {
        return mResources;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }

    public void gotoActivity(Context context, String realActivityClassName) {
        Intent intent = new Intent(context, ProxyActivity.class);
        intent.putExtra(PluginApkConstants.TAG_CLASS_NAME, realActivityClassName);
        context.startActivity(intent);
    }
}
