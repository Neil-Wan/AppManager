package com.autodesk.appmanager;

public class Application extends android.app.Application {

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        sApplication = this;
    }

}
