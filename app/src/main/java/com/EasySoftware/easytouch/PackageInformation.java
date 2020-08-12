package com.EasySoftware.easytouch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mukeshk on 6/15/2016.
 */
public class PackageInformation {
    private Context mContext;

    public PackageInformation(Context context) {
        mContext = context;
    }

    /*
     * Get all the installed app excluding system apps
     * */
    public ArrayList<InfoObject> getInstalledApps() {
        ArrayList<InfoObject> listObj = new ArrayList<InfoObject>();
        final PackageManager packageManager = mContext.getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo : packages) {
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                InfoObject newInfo = new InfoObject();
                newInfo.appname = applicationInfo.loadLabel(packageManager).toString();
                newInfo.packagename = applicationInfo.packageName;
                newInfo.icon = applicationInfo.loadIcon(packageManager);
                newInfo.launchactivity = packageManager.getLaunchIntentForPackage(applicationInfo.packageName);
                listObj.add(newInfo);
            }
        }
        return listObj;
    }


    class InfoObject {
        public String appname = "";
        public String packagename = "";
        public Intent launchactivity;
        public Drawable icon;
    }
}