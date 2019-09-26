package com.llj.hotfix;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import dalvik.system.DexFile;

/**
 * @author: lilinjie
 * @date: 2019-09-10 14:02
 * @description:
 */
public class MyApplication extends Application {
    public static final String HOT_FIX_APK_PATH = "/storage/emulated/0/hotfix.apk";
    public static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        hotfix();
    }

    private void hotfix() {

        //创建热修复文件
        File hotfixFile = new File(HOT_FIX_APK_PATH);
        if (!hotfixFile.exists()) {
            return;
        }
        try {
            //1.反射获取DexPathList中的dexElements数组
            //获取加载APK的PathDexClassLoader
            ClassLoader classLoader = getClassLoader();
            //获取BaseDexClassLoader中的dexPathList
            Field dexPathListField = classLoader.getClass().getSuperclass().getDeclaredField("pathList");
            dexPathListField.setAccessible(true);
            Object dexPathList = dexPathListField.get(classLoader);
            Field dexElementsField = dexPathList.getClass().getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);
            Object[] dexElements = (Object[]) dexElementsField.get(dexPathList);

            //2.创建修复APK的DexFile,并封装成Element
            Class<?> elementClass = dexElements.getClass().getComponentType();
            DexFile dexFile = new DexFile(hotfixFile);
            Constructor<?> constructor = elementClass.getConstructor(DexFile.class, File.class);
            constructor.setAccessible(true);
            Object elementHot = constructor.newInstance(dexFile, hotfixFile);

            //3. 创建一个新的DexElements,合并新的Element和旧的dexElements,新的Element放置在数组第一位,简单理解为数组0位置插入操作
            Object[] newDexElements = (Object[]) Array.newInstance(elementClass, dexElements.length + 1);
            newDexElements[0] = elementHot;
            System.arraycopy(dexElements, 0, newDexElements, 1, dexElements.length);
            //替换原来的dexElements
            dexElementsField.set(dexPathList, newDexElements);
            Log.i("TAG", "热修复注入成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
