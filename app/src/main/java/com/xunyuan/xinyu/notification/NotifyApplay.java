package com.xunyuan.xinyu.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.xunyuan.xinyu.R;

import java.util.ArrayList;

/**
 * 作者：罗发新
 * 时间：2019/10/18 0018    星期五
 * 邮件：424533553@qq.com
 * 说明：通知栏应用类。
 */
public class NotifyApplay {
    NotificationManager manager = null;


    NotificationCompat.Builder builder;

    private void initNotify(Context context) {
        //消息通知栏管理器
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            String channelId = "channelId";
            String channelName = "channelName";
            //通知渠道
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            // 开启指示灯，如果设备有的话
            channel.enableLights(true);
            // 设置角标，时间真假测试。
            channel.setShowBadge(true);
            // 设置指示灯颜色
            channel.setLightColor(ContextCompat.getColor(context, R.color.colorPrimary));
            // 是否在久按桌面图标时显示此渠道的通知
            channel.setShowBadge(true);
            // 设置是否应在锁定屏幕上显示此频道的通知
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PRIVATE);
            // 设置绕过免打扰模式
            channel.setBypassDnd(true);
            // 8.0及以上需要设置渠道
            manager.createNotificationChannel(channel);

            /***************************************************************/
            String groupId = "HuaWei";
            String groupName = "HuaWeiName";
            String groupDownloadId = "HuaWeiDownload";
            String groupDownloadName = "HuaWeiDownloadName";

            //渠道组
            ArrayList<NotificationChannelGroup> groups = new ArrayList<>();

            NotificationChannelGroup group = new NotificationChannelGroup(groupId, groupName);
            groups.add(group);
            NotificationChannelGroup group_download = new NotificationChannelGroup(groupDownloadId, groupDownloadName);
            groups.add(group_download);
            manager.createNotificationChannelGroups(groups);
            // 有了组之后，直接把渠道丢相应组里面就行了，比如我把刚才的channelId这个组丢到groupId所在的组中
            channel.setGroup(groupId);

            /**   第三步  *********************************************/
            // 最后只要在原来NotificationCompat.Builder的参数里面加上这个渠道Id即可
            builder = new NotificationCompat.Builder(context, channelId);
            // 设置条数
            builder.setNumber(10);
            // 通知超时时间，超过了就自动消失
            builder.setTimeoutAfter(60 * 1000);

        } else {
            //当sdk版本小于26
            Notification notification = new NotificationCompat.Builder(context)
                    // 这个icon不管用不用必须加！！！！！
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            manager.notify(1, notification);
        }
    }

    /**
     * 在后天应用中启动 服务。
     * 调用完startForegroundService之后，我们就需要立即设置前台服务通知了
     */
    private void startService(Activity activity, Intent intent) {
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            //5秒内需要调用showStartForeground（）,否则ANR
            activity.startForegroundService(intent);
        } else {
            activity.startService(intent);
        }
    }

    /**
     * 8.0开启前台服务
     */
    public void showStartForeground(Service service, String ticker, String title, String content, int color, int smallIcon, int largeIcon, int id) {
        NotificationCompat.Builder builder = null;
        /*NotificationCompat.Builder builder = getSimpleBuilder(
                ticker,
                title,
                content,
                color,
                smallIcon,
                largeIcon,
                NotificationUtils.channelDownloadId,
                new Intent());
                */

        builder.setOngoing(true);
        builder.setAutoCancel(false);
        builder.setColor(Color.WHITE);
        // 启动前台服务，比8.0之前额外多了startForeground 方法
        service.startForeground(id, builder.build());
    }

    /**
     * 8.0关闭前台服务
     */
    public void hideStartForeground(Service service, int id) {
        // 和普通的Service 额外多了stopForeground 方法
        service.stopForeground(true);
        manager.cancel(id);
    }

    /**
     * 测试环境
     */
    private void test() {
        //

    }
}
