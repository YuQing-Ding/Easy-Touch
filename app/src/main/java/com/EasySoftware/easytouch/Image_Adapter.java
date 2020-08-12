package com.EasySoftware.easytouch;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mukeshk on 6/15/2016.
 */
class CustomAdapterAppList extends BaseAdapter {
    private ArrayList<PackageInformation.InfoObject> mObjectApps;
    private Context mContext;

    public CustomAdapterAppList(Context context, ArrayList<PackageInformation.InfoObject> apps) {
        this.mContext = context;
        this.mObjectApps = apps;
    }

    @Override
    public int getCount() {
        return mObjectApps.size();
    }

    @Override
    public Object getItem(int position) {
        return mObjectApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        convertView = inflater.inflate(R.layout.item_app_info, parent, false);
        holder = new ViewHolder();
        holder.appName = (TextView) convertView.findViewById(R.id.tv_app_name);
        holder.appIcon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
        //Set values
        final PackageInformation.InfoObject infoObject = mObjectApps.get(position);
        holder.appName.setText(infoObject.appname);
        holder.appIcon.setImageDrawable(infoObject.icon);
        //Click action for app
        return convertView;
    }

    private class ViewHolder {
        TextView appName;
        TextView appVersion;
        ImageView appIcon;
    }
}
