package com.ustc.music.application;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class App extends Application {
    public static Context sContext;
    public static int sScreenWidth;
    public static int sScreenHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        sScreenWidth = dm.widthPixels;
        sScreenHeight = dm.heightPixels;
    }
}
