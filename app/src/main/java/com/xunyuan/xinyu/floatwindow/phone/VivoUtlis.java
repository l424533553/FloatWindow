package com.xunyuan.xinyu.floatwindow.phone;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.xunyuan.xinyu.floatwindow.perssion.SetPermission;


/**
 * 作者：罗发新
 * 时间：2019/10/17 0017    星期四
 * 邮件：424533553@qq.com
 * 说明：
 */
public class VivoUtlis {


    public static boolean isVivoRom() {
        //https://github.com/zhaozepeng/FloatWindowPermission/pull/26
        return !TextUtils.isEmpty(SetPermission.getSystemProperty("ro.vivo.os.version"));
    }

    // VIVO
    public static boolean manageDrawOverlaysForVivo(Context context) {
        // 不支持直接到达悬浮窗设置页，只能到 i管家 首页
        Intent intent = new Intent("com.iqoo.secure");
        intent.setClassName("com.iqoo.secure", "com.iqoo.secure.MainActivity");
        // com.iqoo.secure.ui.phoneoptimize.SoftwareManagerActivity
        // com.iqoo.secure.ui.phoneoptimize.FloatWindowManager
        return SetPermission.startSafely(context, intent);
    }
}
