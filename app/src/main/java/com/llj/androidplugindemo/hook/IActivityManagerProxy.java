package com.llj.androidplugindemo.hook;

import android.content.Intent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: lilinjie
 * @date: 2019-09-09 10:16
 * @description:
 */
public class IActivityManagerProxy implements InvocationHandler {
    private Object mActivityManager;

    public IActivityManagerProxy(Object activityManager) {
        mActivityManager = activityManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //执行目标方法前,进行Intent的替换
        if ("startActivity".equals(method.getName())) {
            Intent intent;
            int index = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            intent = (Intent) args[index];
            Intent proxyIntent = new Intent();
            String packageName = "com.llj.androidplugindemo";
            proxyIntent.setClassName(packageName, "com.llj.androidplugindemo.hook.ProxyActivity");
            proxyIntent.putExtra(HookHelper.TARGET_INTENT, intent);
            args[index] = proxyIntent;//偷梁换柱
        }
        return method.invoke(mActivityManager, args);
    }
}