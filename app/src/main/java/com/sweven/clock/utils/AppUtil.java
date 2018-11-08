package com.sweven.clock.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.sweven.clock.entity.App;

import java.util.ArrayList;

/**
 * Created by Sweven on 2018/9/15.
 * Email:sweventears@Foxmail.com
 */
public class AppUtil {

    private PackageManager packageManager;

    //获取一个包管理器
    public AppUtil(Context context) {
        packageManager = context.getPackageManager();
    }

    /**
     * 获取系统中所有应用信息，
     * 并将应用软件信息保存到list列表中。
     **/
    public ArrayList<App> getAllApp() {
        ArrayList<App> ArrayList = new ArrayList<>();
        App myApp;
        //获取到所有安装了的应用程序的信息，包括那些卸载了的，但没有清除数据的应用程序
        ArrayList<PackageInfo> packageInfos =
                (java.util.ArrayList<PackageInfo>)
                        packageManager.getInstalledPackages
                                (PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo info : packageInfos) {
            myApp = new App();
            //拿到包名
            String packageName = info.packageName;

            //拿到应用程序的信息
            ApplicationInfo appInfo = info.applicationInfo;

            //判断是否为用户应用、是否为桌面应用、是否为本应用
            if (filterApp(appInfo) &&
                    isLaunchApp(packageName) &&
                    !packageName.equals("com.sweven.clock")) {

                Drawable icon;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    //拿到应用程序的原生图标
                    icon = appInfo.loadUnbadgedIcon(packageManager);
                } else {
                    icon = appInfo.loadIcon(packageManager);
                }

                //拿到应用程序的程序名
                String appName = appInfo.loadLabel(packageManager).toString();

                myApp.setPackageName(packageName);
                myApp.setName(appName);
                myApp.setIcon(icon);

                ArrayList.add(myApp);
            }

        }
        return ArrayList;
    }

    /**
     * @param packageName 包名
     * @return 判断是否为可运行APP
     */
    private boolean isLaunchApp(String packageName) {
        Intent launchApp = packageManager.getLaunchIntentForPackage(packageName);
        return launchApp != null;
    }

    /**
     * 判断某一个应用程序是不是系统的应用程序，
     *
     * @param info 应用信息
     * @return 如果是返回true，否则返回false。
     */
    private boolean filterApp(ApplicationInfo info) {
        //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {//系统应用更新了的应用程序
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {//判断是不是用户自己装的应用
            return true;
        }
        return false;
    }

    /**
     * @param apkPath app包名
     * @param context context
     * @return APP图标
     */
    public static Drawable getApkFileIcon(String apkPath, Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            if (appInfo != null) {
                try {
                    appInfo.publicSourceDir = apkPath;
                    return pm.getApplicationIcon(appInfo);
                } catch (OutOfMemoryError e) {
                }
            }
        }
        return null;
    }
}
