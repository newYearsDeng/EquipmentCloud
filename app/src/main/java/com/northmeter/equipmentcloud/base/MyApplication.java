package com.northmeter.equipmentcloud.base;

import android.app.Application;

/**
 * Created by dyd on 2018/11/28.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new InitOkGo().initOkGo(this);
    }
}
