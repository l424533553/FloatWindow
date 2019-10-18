/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.xunyuan.xinyu.floatwindow.phone;

import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.xunyuan.xinyu.floatwindow.perssion.SetPermission;

import java.lang.reflect.Method;

/**
 * 作者：罗发新
 * 时间：2019/10/17 0017    星期四
 * 邮件：424533553@qq.com
 * 说明：华为手机众多，型号众多，匹配也很复杂
 */
public class HuaweiUtils {
    private static final String TAG = "HuaweiUtils";
    private final static String HUAWEI_PACKAGE = "com.huawei.systemmanager";

    /**
     * 去华为权限申请页面
     */
    public static void applyPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//   ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
//   ComponentName comp = new ComponentName("com.huawei.systemmanager",
//      "com.huawei.permissionmanager.ui.SingleAppActivity");//华为权限管理，跳转到指定app的权限管理位置需要华为接口权限，未解决
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");//悬浮窗管理页面
            intent.setComponent(comp);
            if (getEmuiVersion() == 3.1) {
                //emui 3.1 的适配
                context.startActivity(intent);
            } else {
                //emui 3.0 的适配
                comp = new ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity");//悬浮窗管理页面
                intent.setComponent(comp);
                context.startActivity(intent);
            }
        } catch (SecurityException e) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//   ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            ComponentName comp = new ComponentName("com.huawei.systemmanager",
                    "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理，跳转到本app的权限管理页面,这个需要华为接口权限，未解决
//      ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");//悬浮窗管理页面
            intent.setComponent(comp);
            context.startActivity(intent);
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (ActivityNotFoundException e) {
            //手机管家版本较低 HUAWEI SC-UL10
            //   Toast.makeText(MainActivity.this, "act找不到", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.Android.settings", "com.android.settings.permission.TabItem");//权限管理页面 android4.4
//   ComponentName comp = new ComponentName("com.android.settings","com.android.settings.permission.single_app_activity");//此处可跳转到指定app对应的权限管理页面，但是需要相关权限，未解决
            intent.setComponent(comp);
            context.startActivity(intent);
            e.printStackTrace();
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (Exception e) {
            //抛出异常时提示信息
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    // 华为
    static boolean manageDrawOverlaysForEmui(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setClassName(HUAWEI_PACKAGE, "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");
            if (SetPermission.startSafely(context, intent)) {
                return true;
            }
        }
        // Huawei Honor P6|4.4.4|3.0
        intent.setClassName(HUAWEI_PACKAGE, "com.huawei.notificationmanager.ui.NotificationManagmentActivity");
        intent.putExtra("showTabsNumber", 1);
        if (SetPermission.startSafely(context, intent)) {
            return true;
        }
        intent.setClassName(HUAWEI_PACKAGE, "com.huawei.permissionmanager.ui.MainActivity");
        if (SetPermission.startSafely(context, intent)) {
            return true;
        }
        return false;
    }


    public static boolean checkIsHuaweiRom() {
        return Build.MANUFACTURER.toUpperCase().contains("HUAWEI");
    }

    public static boolean IsHuaweiRom() {
        return !TextUtils.isEmpty(SetPermission.getSystemProperty("ro.build.version.emui"));
    }

    /**
     * @return 获取 emui 版本号
     */
    public static double getEmuiVersion() {
        try {
            String emuiVersion = SetPermission.getSystemProperty("ro.build.version.emui");
            if (!TextUtils.isEmpty(emuiVersion)) {
                assert emuiVersion != null;
                String version = emuiVersion.substring(emuiVersion.indexOf("_") + 1);
                return Double.parseDouble(version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1.0;
    }


    /**
     * 该方法 主要针对 18 <=API <23
     * 主要针对那些特殊厂家的Rom，各Rom检测悬浮框权限的方法都一样
     *
     * @return 通过映射的方法来判断 WRITE_SETTINGS  或 SYSTEM_ALERT_WINDOW 是否已打开
     * AppOpsManager函数在API 18中被Google新增，但在API 19里才被公开
     */
    private static boolean checkOp(Context context, int op) {
        // 19-22
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Method method = AppOpsManager.class.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
                return false;
            }
        } else
            // SDK=18
            return Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

}


