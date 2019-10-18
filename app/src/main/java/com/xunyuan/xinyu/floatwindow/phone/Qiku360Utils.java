/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.xunyuan.xinyu.floatwindow.phone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.xunyuan.xinyu.floatwindow.perssion.SetPermission;

public class Qiku360Utils {
    private static final String TAG = "Qiku360Utils";

    public static boolean checkIs360Rom() {
        //fix issue https://github.com/zhaozepeng/FloatWindowPermission/issues/9
        return Build.MANUFACTURER.toUpperCase().contains("QIKU")
                || Build.MANUFACTURER.contains("360");
    }

    /**
     * 去360手机权限申请页面进行 权限手动申请
     */
    public static void applyPermission360(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.Settings$OverlaySettingsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 进入360权限管理页面
     */
    static boolean manageDrawOverlaysForQihu(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.Settings$OverlaySettingsActivity");
        if (SetPermission.startSafely(context, intent)) {
            return true;
        }
        intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
        return SetPermission.startSafely(context, intent);
    }

    /**
     * 去360权限申请页面
     */
    public static void applyPermission(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.Settings$OverlaySettingsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                Log.e(TAG, "can't open permission page with particular name, please use " +
                        "\"adb shell dumpsys activity\" command and tell me the name of the float window permission page");
            }
        }
    }

    private static boolean isIntentAvailable(Intent intent, Context context) {
        if (intent == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
}
