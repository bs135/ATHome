package com.chipfc.android.things.athome;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Go fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onResume() {
        super.onResume();

        List<App> installedApps = getAllApplications(getApplicationContext(), false);
        AppAdapter appAdapter = new AppAdapter(installedApps);

        for (App app : installedApps) {
            Log.d(TAG, "Installed App : " + app.getName() + " => PackageName" + app.getPackageName());
        }

        recyclerView.setAdapter(appAdapter);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private List<App> getAllApplications(Context context, boolean includeSystemApps) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);

        List<App> installedApps = new ArrayList<>();

        for (PackageInfo pkgInfo : packages) {
            if (pkgInfo.versionName == null) {
                continue;
            }

            if (pkgInfo.packageName.equalsIgnoreCase("com.chipfc.android.things.athome")) {
                continue;
            }

            boolean isSystemApp = ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);

            if (pkgInfo.packageName.equalsIgnoreCase("com.android.documentsui")
                    || pkgInfo.packageName.equalsIgnoreCase("com.android.deskclock")) {
                isSystemApp = false;
            }

            if (!includeSystemApps && isSystemApp) {
                continue;
            }

            App newApp = new App();

            newApp.setPackageName(pkgInfo.packageName);
            newApp.setName(pkgInfo.applicationInfo.loadLabel(packageManager).toString());
            newApp.setIcon(pkgInfo.applicationInfo.loadIcon(packageManager));

            installedApps.add(newApp);

        }

        return installedApps;
    }
}
