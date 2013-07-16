package com.autodesk.appmanager;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;

public class App {
    public static final int NOT_INSTALLED = 0;
    public static final int UP_TO_DATE = 1;
    public static final int HAS_UPDATE = 2;

    private final String mPackageName;
    private final String mDisplayName;
    private final String mDownloadUrl;
    private PackageInfo mPackageInfo;
    private int mLatestVersionCode;
    private Drawable mIcon;

    public App(String packageName, String displayName, String downloadUrl) {
        mPackageName = packageName;
        mDisplayName = displayName;
        mDownloadUrl = downloadUrl;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setInfo(PackageManager pm, PackageInfo info) {
        mPackageInfo = info;
        mIcon = info.applicationInfo.loadIcon(pm);
    }

    public int getVersionCode() {
        return mPackageInfo.versionCode;
    }

    public Signature getSignature() {
        Signature[] signatures = mPackageInfo.signatures;
        return signatures[0];
    }

    public int getInstallStatus() {
        if (mPackageInfo == null) {
            return NOT_INSTALLED;
        }
        if (mPackageInfo.versionCode < mLatestVersionCode) {
            return HAS_UPDATE;
        }
        return UP_TO_DATE;
    }

    public Drawable getIcon() {
        return mIcon;
    }
}
