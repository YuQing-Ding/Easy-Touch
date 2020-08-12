package com.EasySoftware.easytouch;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * LIST列表，显示所有的包。
 * @author dr
 */
public class InstalledApplicationListActivity extends AppCompatActivity implements
OnItemClickListener {


    private List<String> mPackages = new ArrayList<String>();
    private List<String> mPackages2 = new ArrayList<String>();
    private List<Drawable> mPackages3 = new ArrayList<Drawable>();
    private ImageView icon;
    private MyAppInfo myAppInfos;
    private Context mContext;
    private ImageView imageView;
    private PackageInfo packageInfo;
    private static final String APP_ICON = "app_icon";
    private ArrayAdapter icon_view;
    private ListView lv_app_list;
    private ApplicationInfo pkg;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_list);
        ImageView imageView=findViewById(R.id.iv_app_icon);
        ListView lv_app_list = findViewById(R.id.lv_app_list);

        // 获取所有程序包名称，并且循环显示出来。
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfos = packageManager
                .getInstalledPackages(PackageManager.GET_ACTIVITIES);

        for (PackageInfo packageInfo : packageInfos) {
            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                continue;

            }

            mPackages.add(packageInfo.applicationInfo.loadLabel(packageManager)
                    + "\n" + packageInfo.packageName);
            mPackages2.add((String) packageInfo.applicationInfo.loadLabel(packageManager));
            mPackages3.add(packageInfo.applicationInfo.loadIcon(packageManager));


        }



        PackageInformation packageInformation=new PackageInformation(InstalledApplicationListActivity.this);
        ArrayList<PackageInformation.InfoObject> apps= packageInformation.getInstalledApps();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_app_info, R.id.tv_app_name,
                mPackages2);
        lv_app_list.setAdapter(arrayAdapter);


        CustomAdapterAppList adapterAppList=new CustomAdapterAppList(InstalledApplicationListActivity.this,apps);
        adapterAppList.notifyDataSetChanged();
        lv_app_list.setAdapter(adapterAppList);
        lv_app_list.setOnItemClickListener(this);




        // 列表项，单击事件。




}

    private AdapterView getListView() {
        return null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        // 把选中的包名传过去


        Intent intent = new Intent();
        // 把选中的包名传过去
        intent.putExtra("package_name", mPackages.get(position));
        setResult(1, intent);
        intent.putExtra("package_name2", mPackages2.get(position));
        setResult(1, intent);

        finish();

    }

}