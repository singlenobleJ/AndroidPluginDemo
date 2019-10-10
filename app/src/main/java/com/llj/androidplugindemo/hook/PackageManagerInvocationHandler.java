package com.llj.androidplugindemo.hook;

import android.content.ComponentName;
import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: lilinjie
 * @date: 2019-10-10 09:18
 * @description:
 */
public class PackageManagerInvocationHandler implements InvocationHandler {
    private Context context;
    private Object obj;

    public PackageManagerInvocationHandler(Context context, Object obj) {
        this.context = context;
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!"getActivityInfo".equals(method.getName())) {
            return method.invoke(obj, args);
        }
        Object activityInfo = method.invoke(obj, args);
        if (activityInfo == null) {
            //说明没有注册
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof ComponentName) {
                    args[i] = new ComponentName(context.getPackageName(), ProxyActivity.class.getName());
                    return method.invoke(obj, args);
                }
            }
        }
        return activityInfo;
    }
}
