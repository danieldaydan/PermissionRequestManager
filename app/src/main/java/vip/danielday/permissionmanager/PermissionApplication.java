package vip.danielday.permissionmanager;

import android.app.Application;

/**
 * Created by daidan on 2018/4/28.
 */

public class PermissionApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(CustomExceptionHandler.getInstance(this));
    }
}
