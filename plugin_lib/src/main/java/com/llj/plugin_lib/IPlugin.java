package com.llj.plugin_lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author: lilinjie
 * @date: 2019-10-14 15:41
 * @description: 插件化Activity的接口规范
 */
public interface IPlugin {

    /**
     * 内部跳转
     */
    int FROM_INTERNAL = 0;
    /**
     * 作为插件时跳转
     */
    int FROM_EXTRENAL = 1;

    void attach(Activity activity);

    /*以下作为Activity的生命周期,插件Activity本身在作为插件的时候,
      不具备生命周期,由宿主程序中的代理Activity代为管理
     */


    void onCreate(Bundle saveInstanceState);

    void onStart();

    void onResume();

    void onRestart();

    void onPause();

    void onStop();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
