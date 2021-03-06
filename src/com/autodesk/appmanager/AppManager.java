package com.autodesk.appmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class AppManager {

    private static AppManager sInstance = new AppManager();

    private AppManager() {

    }

    public static AppManager getInstance() {
        return sInstance;
    }

    private final App[] mApps = new App[] { /*
                                             * new App("com.sketchbook", "SBM"), new App("com.adsk.sketchbook.galaxy",
                                             * "SBM Samsung"), new App("com.adsk.sketchbookhd", "SBP"), new
                                             * App("com.adsk.sketchbookhd.galaxy", "SBP Samsung"), new
                                             * App("pixlr.OMatic", "Pixlr OMatic"),
                                             */
    new App("com.pixlr.express", "Pixlr Express") };

    public App[] getApps() {
        return mApps;
    }

    public void install(Activity context, App app, String apkPath) {
        PackageInfo pInfo = Application.getApplication().getPackageManager()
                .getPackageArchiveInfo(apkPath, PackageManager.GET_SIGNATURES);

        boolean canNotReplace = !app.getSignature().equals(pInfo.signatures[0]);

        if (canNotReplace) {
            uninstall(context, app);
        }

        // Intent intent = new Intent(Intent.ACTION_VIEW);
        // intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        // context.startActivity(intent);
    }

    public void uninstall(Activity context, App app) {
        Uri packageURI = Uri.parse("package:" + app.getPackageName());
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        // context.startActivity(uninstallIntent);
        context.startActivityForResult(uninstallIntent, 1);
    }
}
