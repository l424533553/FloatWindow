package com.xunyuan.xinyu.perssion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.xunyuan.xinyu.phone.HuaweiUtils;
import com.xunyuan.xinyu.phone.MeizuUtils;
import com.xunyuan.xinyu.phone.MiuiUtils;
import com.xunyuan.xinyu.phone.OppoUtils;
import com.xunyuan.xinyu.phone.Qiku360Utils;
import com.xunyuan.xinyu.phone.VivoUtlis;

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
