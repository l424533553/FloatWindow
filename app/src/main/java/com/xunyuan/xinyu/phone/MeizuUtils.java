/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.xunyuan.xinyu.phone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.xunyuan.xinyu.perssion.SetPermission;

import java.lang.reflect.Field;

public class MeizuUtils {
    private static final String TAG = "MeizuUtils";

    public static boolean checkIsMeizuRom() {
        //return Build.MANUFACTURER.contains("Meizu");
        String meizuFlymeOSFlag = SetPermission.getSystemProperty("ro.build.display.id");
        if (meizuFlymeOSFlag == null || meizuFlymeOSFlag.length() == 0) {
            return false;
        } else
            return meizuFlymeOSFlag.contains("flyme") || meizuFlymeOSFlag.toLowerCase().contains("flyme");
    }

    // 魅族
    static boolean manageDrawOverlaysForFlyme(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
        intent.putExtra("packageName", context.getPackageName());
        return SetPermission.startSafely(context, intent);
    }

    /**
     * 去魅族权限申请页面
     */
    public static void applyPermission(Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
//            intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");//remove this line code for fix flyme6.3
            intent.putExtra("packageName", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                Log.e(TAG, "获取悬浮窗权限, 打开AppSecActivity失败, " + Log.getStackTraceString(e));
                // 最新的魅族flyme 6.2.5 用上述方法获取权限失败, 不过又可以用下述方法获取权限了
                commonROMPermissionApplyInternal(context);
            } catch (Exception eFinal) {
                Log.e(TAG, "获取悬浮窗权限失败, 通用获取方法失败, " + Log.getStackTraceString(eFinal));
            }
        }
    }

    /**
     * 功能暂时未明确，可能是一种进入权限设置的方法
     */
    public static void commonROMPermissionApplyInternal(Context context) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = Settings.class;
        Field field = clazz.getDeclaredField("");

        Intent intent = new Intent(field.get(null).toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

}
