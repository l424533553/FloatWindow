package com.xunyuan.xinyu.floatwindow.perssion;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * 作者：罗发新
 * 时间：2019/10/17 0017    星期四
 * 邮件：424533553@qq.com
 * 说明：主要用于权限设置
 */
public class SetPermission {
    private static final String TAG = SetPermission.class.getName();

    private static final int OP_WRITE_SETTINGS = 23;
    private static final int OP_SYSTEM_ALERT_WINDOW = 24;

    /**
     * 针对 18 <= SDK <=22
     * 正常Rom,悬浮框权限在18-22时是默认开启的，但是有些厂家进行了特殊的修改，默认是关闭的
     *
     * @param op OP_WRITE_SETTINGS = 23 设置权限  和   OP_SYSTEM_ALERT_WINDOW = 24 悬浮框类型
     * @return 检查手机的权限是否打开，一般是高级权限。每个手机厂商的手机Rom可能不一样，但检测方法一样
     */
    public static boolean checkOp(Context context, int op) {
        // SDK 19-22
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


    /**
     * 安全的启动Intent
     */
    public static boolean startSafely(Context context, Intent intent) {
        if (context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } else {
            Log.e(TAG, "Intent is not available! " + intent);
            return false;
        }
    }
    /**
     * 打开应用的信息界面，其包含通知、权限、高级设置等选项。 也可选择卸载或暂停
     */
    public static void jumpDetailsSettings(Context context) {
        //细节设置
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }


    /**
     * @param propName 根据各Rom的配置文件读取 配置信息
     */
    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }


    /**
     * VM 可以直接设置Android 4.3— 4.4的授权状态，其他的版本直接设置不了。
     *
     * @param allowed 是否设置为权限允许
     * @param op      OP_SYSTEM_ALERT_WINDOW 或者 OP_WRITE_SETTINGS
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean setSettingMode(Context context, boolean allowed, int op) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 || Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            @SuppressLint("DiscouragedPrivateApi")
            Method method = AppOpsManager.class.getDeclaredMethod("setMode", int.class, int.class, String.class, int.class);
            method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName(), allowed ? AppOpsManager.MODE_ALLOWED : AppOpsManager
                    .MODE_IGNORED);
            return true;
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return false;
    }

}
