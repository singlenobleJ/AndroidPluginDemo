package com.llj.plugin_module;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.llj.plugin_lib.PluginBaseActivity;

public class MainActivity extends PluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mProxyActivity, Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
