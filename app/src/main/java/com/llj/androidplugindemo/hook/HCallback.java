package com.llj.androidplugindemo.hook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * @author: lilinjie
 * @date: 2019-09-09 11:12
 * @description:
 */
public class HCallback implements Handler.Callback {
    public static final int LAUNCH_ACTIVITY = 100;
    private Handler mHandler;

    public HCallback(Handler handler) {
        mHandler = handler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == LAUNCH_ACTIVITY) {
            Object r = msg.obj;
            try {
                Intent proxyIntent = (Intent) ReflectUtils.getField(r.getClass(), r, "intent");
                Intent targetIntent = proxyIntent.getParcelableExtra(HookHelper.TARGET_INTENT);
                if (targetIntent != null) {
                    //替换真实的Intent
                    ReflectUtils.setField(r.getClass(), r, "intent", targetIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mHandler.handleMessage(msg);
        return true;
    }
}
