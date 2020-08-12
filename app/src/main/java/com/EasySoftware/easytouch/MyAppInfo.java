package com.EasySoftware.easytouch;

import android.graphics.drawable.Drawable;

public class MyAppInfo {
    private Drawable image;
    private String appName;

    public MyAppInfo(Drawable image, String appName) {
        this.image = image;
        this.appName = appName;
    }
    public MyAppInfo() {

    }

    public static boolean get(int position) {
        return false;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}

