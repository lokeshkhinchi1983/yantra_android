package com.cdlit.assetmaintenanceapp.Utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by rakesh on 15-02-2018.
 */

public class MyApplication extends Application {

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {

        MyApplication application = (MyApplication) context.getApplicationContext();

        return application.refWatcher;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();

      /*  // for enable leakcanary for reales mode
        MultiDex.install(this);
        refWatcher = LeakCanary.install(this);*/


        // for enable-disable leakcanary for debuge mode
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        refWatcher = installLeakCanary();

    }

    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
    }
}