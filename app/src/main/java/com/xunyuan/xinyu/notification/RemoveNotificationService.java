package com.xunyuan.xinyu.notification;

import android.annotation.SuppressLint;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * 作者：罗发新
 * 时间：2019/10/18 0018    星期五
 * 邮件：424533553@qq.com
 * 说明：通知清除回调     只能在8.0及以上使用
 * <p>
 * 系统现在可区分通知是由用户清除，还是由应用自己移除。要查看清除通知的方式，
 * 应实现NotificationListenerService类的新onNotificationRemoved()方法
 * <p>
 * AndroidManifest里面不要忘记加配置
 * <p>
 * < AndroidManifest>
 * <service android:name=".service.RemoveNotificationService"
 * android:permission="android.permission.BIND_NOTIFICATION_LISTENER_SERVICE">
 * <p>
 * <intent-filter>
 * <action android:name="android.service.notification.NotificationListenerService" />
 * </intent-filter>
 * </service>
 * </AndroidManifest>
 */
@SuppressLint("Registered")
@RequiresApi(api = Build.VERSION_CODES.O)
public class RemoveNotificationService extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);
        Log.d("onNotificationPosted", sbn.toString());
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap, int reason) {
//        第三个参数reason是REASON_CANCEL还是REASON_LISTENER_CANCEL就可以知道是用户删除还是系统删除了
        super.onNotificationRemoved(sbn, rankingMap, reason);
        Log.d("onNotificationRemoved", sbn.toString());
    }
}
