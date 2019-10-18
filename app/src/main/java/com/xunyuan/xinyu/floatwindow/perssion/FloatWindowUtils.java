package com.xunyuan.xinyu.floatwindow.perssion;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;


import com.xunyuan.xinyu.floatwindow.phone.HuaweiUtils;
import com.xunyuan.xinyu.floatwindow.phone.MeizuUtils;
import com.xunyuan.xinyu.floatwindow.phone.MiuiUtils;
import com.xunyuan.xinyu.floatwindow.phone.OppoUtils;
import com.xunyuan.xinyu.floatwindow.phone.Qiku360Utils;
import com.xunyuan.xinyu.floatwindow.phone.VivoUtlis;

import java.lang.reflect.Method;

/**
 * 作者：罗发新
 * 时间：2019/10/17 0017    星期四
 * 邮件：424533553@qq.com
 * 说明：主要用于悬浮窗权限的
 */
public class FloatWindowUtils {
    private static final String TAG = FloatWindowUtils.class.getName();
    private static final int OP_WRITE_SETTINGS = 23;
    private static final int OP_SYSTEM_ALERT_WINDOW = 24;


    /**
     * 打开 悬浮窗启动 功能
     * 在6.0及以后会打开页面让用户自己打开
     */
    public static boolean manageDrawOverlays(Context context) {
        if (!canDrawOverlays(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                openDrawOverSetting(context);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检测  悬浮窗权限。 都在AndroidManifest.xml 中配置android.permission.SYSTEM_ALERT_WINDOW
     * Android 6.0 (SDK=23)及以后google进行统一处理
     * 大于18即4.4的一般需要通过映射的方法来判断
     */
    public static boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return SetPermission.checkOp(context, OP_SYSTEM_ALERT_WINDOW);
        } else {
            return true;
        }
    }

    /**
     * 检查超过23  6.0 版本的权限问题( 该方法 值得商榷，听说 还是有一些问题在某些8.0 机子上)
     * AppOpsManager.MODE_ALLOWED —— 表示授予了权限并且重新打开了应用程序
     * AppOpsManager.MODE_IGNORED —— 表示授予权限并返回应用程序
     * AppOpsManager.MODE_ERRORED —— 表示当前应用没有此权限
     * AppOpsManager.MODE_DEFAULT —— 表示默认值，有的手机厂商Rom默认开启或者关闭。
     * 如默认开启：开启权限后会返回值 MODE_DEFAULT 而不是 MODE_ALLOWED
     * 如默认关闭：关闭权限后返回值 MODE_DEFAULT 而不是 MODE_ERRORED，开启后返回类型 MODE_ALLOWED
     */
    private boolean checkOverM(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            if (appOpsMgr == null)
                return false;
            int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                    .getPackageName());
            return Settings.canDrawOverlays(context) || mode == AppOpsManager.MODE_IGNORED || mode == AppOpsManager.MODE_ALLOWED;
        } else {
            //默认返回值 AppOpsManager.MODE_IGNORED 和 AppOpsManager.MODE_ALLOWED
            return Settings.canDrawOverlays(context);
        }


    }

    /**
     * SDK>=23时，判断是否授权了悬浮窗的权限。
     * 该映射方法对应等价  Settings.canDrawOverlays(this) 方法
     */
    private boolean commonROMPermissionCheck(Context context) {
        Boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class clazz = Settings.class;
                Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                result = (Boolean) canDrawOverlays.invoke(null, context);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
        return result;
    }

    /**
     * 根据不通的Rom进入 不同的权限页面进行设置
     */
    public static void openDrawOverSetting(Context context) {
        if (MiuiUtils.checkIsMiuiRom()) {
            MiuiUtils.applyMiuiPermission(context);
        } else if (HuaweiUtils.checkIsHuaweiRom()) {
            HuaweiUtils.applyPermission(context);
        } else if (MeizuUtils.checkIsMeizuRom()) {
            MeizuUtils.applyPermission(context);
        } else if (OppoUtils.checkIsOppoRom()) {
            OppoUtils.applyOppoPermission(context);
        } else if (VivoUtlis.isVivoRom()) {
            VivoUtlis.manageDrawOverlaysForVivo(context);
        } else if (Qiku360Utils.checkIs360Rom()) {
            Qiku360Utils.applyPermission(context);
        }
    }

}
