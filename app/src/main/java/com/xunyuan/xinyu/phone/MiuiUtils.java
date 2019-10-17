/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.xunyuan.xinyu.phone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.xunyuan.xinyu.perssion.SetPermission;

public class MiuiUtils {
    private static final String TAG = "MiuiUtils";


    /**
     * check if is miui ROM
     */
    public static boolean checkIsMiuiRom() {
        return !TextUtils.isEmpty(SetPermission.getSystemProperty("ro.miui.ui.version.name"));
    }

    /**
     * 获取小米 rom 版本号，获取失败返回 -1
     *
     * @return miui rom version code, if fail , return -1
     */
    public static int getMiuiVersion() {
        String version = SetPermission.getSystemProperty("ro.miui.ui.version.name");
        if (!TextUtils.isEmpty(version)) {
            assert version != null;
            return Integer.parseInt(version.substring(1));
        }
        return -1;
    }

    private static boolean isIntentAvailable(Intent intent, Context context) {
        if (intent == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    // 小米
    static boolean manageDrawOverlaysForMiui(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        if (SetPermission.startSafely(context, intent)) {
            return true;
        }
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        if (SetPermission.startSafely(context, intent)) {
            return true;
        }
        // miui v5 的支持的android版本最高 4.x
        // http://www.romzj.com/list/search?keyword=MIUI%20V5#search_result
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent1.setData(Uri.fromParts("package", context.getPackageName(), null));
            return SetPermission.startSafely(context, intent1);
        }
        return false;
    }
    /**
     * 根据小米的版本号，跳转进入不同的权限申请页面，进行权限申请，比如悬浮窗等高级权限
     */
    public static void applyMiuiPermission(Context context) {
        int versionCode = getMiuiVersion();
        if (versionCode == 5) {
            goToMiuiPermissionActivity_V5(context);
        } else if (versionCode == 6) {
            goToMiuiPermissionActivity_V6(context);
        } else if (versionCode == 7) {
            goToMiuiPermissionActivity_V7(context);
        } else if (versionCode == 8) {
            goToMiuiPermissionActivity_V8(context);
        } else {
            Log.e(TAG, "this is a special MIUI rom version, its version code " + versionCode);
        }
    }

    /**
     * 小米 V5 版本 ROM权限申请
     */
    public static void goToMiuiPermissionActivity_V5(Context context) {
        Intent intent = null;
        String packageName = context.getPackageName();
        intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            Log.e(TAG, "intent is not available!");
        }

        //设置页面在应用详情页面
//        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
//        PackageInfo pInfo = null;
//        try {
//            pInfo = context.getPackageManager().getPackageInfo
//                    (HostInterfaceManager.getHostInterface().getApp().getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            AVLogUtils.e(TAG, e.getMessage());
//        }
//        intent.setClassName("com.android.settings", "com.miui.securitycenter.permission.AppPermissionsEditor");
//        intent.putExtra("extra_package_uid", pInfo.applicationInfo.uid);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (isIntentAvailable(intent, context)) {
//            context.startActivity(intent);
//        } else {
//            AVLogUtils.e(TAG, "Intent is not available!");
//        }
    }

    /**
     * 小米 V6 版本 ROM权限申请
     */
    public static void goToMiuiPermissionActivity_V6(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            Log.e(TAG, "Intent is not available!");
        }
    }

    /**
     * 小米 V7 版本 ROM权限申请
     */
    public static void goToMiuiPermissionActivity_V7(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            Log.e(TAG, "Intent is not available!");
        }
    }

    /**
     * 小米 V8 版本 ROM权限申请
     */
    public static void goToMiuiPermissionActivity_V8(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            Log.e(TAG, "Intent is not available!");
        }
    }
}
