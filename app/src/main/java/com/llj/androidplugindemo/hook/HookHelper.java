package com.llj.androidplugindemo.hook;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * @author: lilinjie
 * @date: 2019-09-09 10:46
 * @description:
 */
public class HookHelper {
    public static final String TAG = "HookHelper";
    public static final String TARGET_INTENT = "target_intent";
    public static final String TARGET_INTENT_NAME = "target_intent_name";

    public static void hookAMS() throws Exception {
        Object defaultSingleton;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Class<?> activityManagerCls = Class.forName("android.app.ActivityManager");
            defaultSingleton = ReflectUtils.getField(activityManagerCls, null, "IActivityManagerSingleton");
        } else {
            Class<?> activityManagerCls = Class.forName("android.app.ActivityManagerNative");
            defaultSingleton = ReflectUtils.getField(activityManagerCls, null, "gDefault");
        }
        Class<?> clazz = Class.forName("android.util.Singleton");
        Field mInstanceField = ReflectUtils.getField(clazz, "mInstance");
        Object activityManager = mInstanceField.get(defaultSingleton);
        Class<?> cls = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{cls}, new IActivityManagerInvocationHandler(activityManager));
        mInstanceField.set(defaultSingleton, proxy);
    }

    public static void hookPMS(Context context) throws Exception {
        // 兼容AppCompatActivity报错问题
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
        sCurrentActivityThreadField.setAccessible(true);
        Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);
        Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
        sPackageManagerField.setAccessible(true);
        Object sPackageManager = sPackageManagerField.get(null);
        Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
        Object proxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader(),
                new Class<?>[]{iPackageManagerInterface},
                new PackageManagerInvocationHandler(context, sPackageManager));
        sPackageManagerField.set(sCurrentActivityThread, proxy);
        PackageManager pm = context.getPackageManager();
        Field mPmField = pm.getClass().getDeclaredField("mPM");
        mPmField.setAccessible(true);
        mPmField.set(pm, proxy);
    }

    public static void hookHandler() throws Exception {
        Class<?> activityThreadCls = Class.forName("android.app.ActivityThread");
        Object currentActivityThread = ReflectUtils.getField(activityThreadCls, null, "sCurrentActivityThread");
        Field mHField = ReflectUtils.getField(activityThreadCls, "mH");
        Handler mH = (Handler) mHField.get(currentActivityThread);
        ReflectUtils.setField(Handler.class, mH, "mCallback", new HCallback(mH));

    }

    public static void hookInstrumentation() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Object currentActivityThread = ReflectUtils.getField(activityThreadClass, null, "sCurrentActivityThread");
            ReflectUtils.setField(activityThreadClass, currentActivityThread, "mInstrumentation", new InstrumentationProxy());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
