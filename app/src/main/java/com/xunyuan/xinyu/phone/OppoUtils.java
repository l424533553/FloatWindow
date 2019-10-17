package com.xunyuan.xinyu.phone;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.xunyuan.xinyu.perssion.SetPermission;

/**
 * Description:
 *
 * @author Shawn_Dut
 * @since 2018-02-01
 */
public class OppoUtils {

    private static final String TAG = "OppoUtils";


    public static boolean checkIsOppoRom() {
        //https://github.com/zhaozepeng/FloatWindowPermission/pull/26
        return Build.MANUFACTURER.toUpperCase().contains("OPPO");
    }

    public static boolean isOppoRom() {
        //https://github.com/zhaozepeng/FloatWindowPermission/pull/26
        return !TextUtils.isEmpty(SetPermission.getSystemProperty("ro.build.version.opporom"));
    }

    // OPPO
    static boolean manageDrawOverlaysForOppo(Context context) {
        Intent intent = new Intent();
        intent.putExtra("packageName", context.getPackageName());
        // OPPO A53|5.1.1|2.1
        intent.setAction("com.oppo.safe");
        intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.floatwindow.FloatWindowListActivity");
        if (SetPermission.startSafely(context, intent)) {
            return true;
        }
        // OPPO R7s|4.4.4|2.1
        intent.setAction("com.color.safecenter");
        intent.setClassName("com.color.safecenter", "com.color.safecenter.permission.floatwindow.FloatWindowListActivity");
        if (SetPermission.startSafely(context, intent)) {
            return true;
        }
        intent.setAction("com.coloros.safecenter");
        intent.setClassName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");
        return SetPermission.startSafely(context, intent);
    }

    /**
     * oppo ROM 权限申请
     */
    public static void applyOppoPermission(Context context) {
        //merge request from https://github.com/zhaozepeng/FloatWindowPermission/pull/26
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //com.coloros.safecenter/.sysfloatwindow.FloatWindowListActivity
            ComponentName comp = new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");//悬浮窗管理页面
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
