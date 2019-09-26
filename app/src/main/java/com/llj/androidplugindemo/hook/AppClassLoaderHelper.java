package com.llj.androidplugindemo.hook;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * @author: lilinjie
 * @date: 2019-09-09 11:53
 * @description:
 */
public class AppClassLoaderHelper {
    private static final Map<String, ClassLoader> classLoaderCache = new HashMap<>();
    private static final Map<String, Resources> resourcesCache = new HashMap<>();

    public static ClassLoader getClassLoader(Context context, String pluginPath) {
        if (classLoaderCache.containsKey(pluginPath)) {
            return classLoaderCache.get(pluginPath);
        }
        String dexFilePath = context.getCacheDir().getAbsolutePath();
        DexClassLoader dexClassLoader = new DexClassLoader(pluginPath, dexFilePath, null, context.getClassLoader());
        classLoaderCache.put(pluginPath, dexClassLoader);
        return dexClassLoader;
    }

    public static Resources getPluginResources(Context context, String pluginPath) {
        if (resourcesCache.containsKey(pluginPath)) {
            return resourcesCache.get(pluginPath);
        }
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, pluginPath);
            Resources superRes = context.getResources();
            Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
            resourcesCache.put(pluginPath, resources);
            return resources;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
