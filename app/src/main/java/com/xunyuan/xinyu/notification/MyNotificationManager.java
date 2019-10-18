package com.xunyuan.xinyu.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.xunyuan.xinyu.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 作者：罗发新
 * 时间：2019/10/18 0018    星期五
 * 邮件：424533553@qq.com
 * 说明：主要描述NotificationManager中的方法和 Notification的使用
 */
//public class MyNotificationManager extends NotificationManager {
//无法继承NotificationManager，因其没有一个构造函数
public class MyNotificationManager {
    public final int NOTIFICATION_ID = 1999;
    /**
     * 远程输入的Key,识别作用
     */
    public final String KEY_REMOTE_INPUT = "KEY_REMOTE_INPUT";
    /**
     * 点击带有replyAction 后输入显示的提示输入信息
     */
    private String INPUT_HINT = "请输入要回复的内容";
    /**
     * replyAction  的title
     */
    private String INPUT_TITLE = "快速回复";

    private NotificationManager notificationManager;

    public MyNotificationManager(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * @param channelId 删除渠道id
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteNotificationChannel(String channelId) {
        notificationManager.deleteNotificationChannel(channelId);
    }

    /**
     * @param groupId 删除消息组
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteNotificationChannelGroup(String groupId) {
        notificationManager.deleteNotificationChannelGroup(groupId);
    }

    /**
     * 进入应用 的通知栏设置页面,进行权限的具体设置
     * EXTRA_APP_PACKAGE 和 EXTRA_CHANNEL_ID 这两个参数必不可少，不然貌似无法进入
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void faf(Activity activity, String channelId) {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
        activity.startActivity(intent);
    }

    /**
     * 移除指定id 的 Notification
     *
     * @param NOTIFICATION_ID 消息的id
     */
    public void cancalNotification(int NOTIFICATION_ID) {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    /**
     * 获取快捷回复中用户输入的字符串
     *
     * @param KEY_REMOTE_INPUT 创建的远程输入中的Key，用于识别哪一个远程输入
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public CharSequence getMessageText(Intent intent, String KEY_REMOTE_INPUT) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            //通过KEY_TEXT_REPLY来获取输入的内容
            return remoteInput.getCharSequence(KEY_REMOTE_INPUT);
        }
        return null;
    }

    /**
     * 创建 NotificationChannel
     * int importance 消息通知的级别，通常包括声音和视觉提醒和振动，常见的类型有如下：
     * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
     * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
     * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
     * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationChannel createNotificationChannel() {

        NotificationChannelGroup channelGroup = new NotificationChannelGroup("GROUP", "GROUP");
        notificationManager.createNotificationChannelGroup(channelGroup);
        String id = "MyService12";
        String description = "my-service12";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(id, description, importance);
        channel.setLightColor(Color.BLUE);
        // 暂时不知怎么用
        channel.enableLights(false);

        channel.enableVibration(true);
        long[] ary = {0, 100, 500, 100, 500, 100, 500, 100, 500, 100};
        channel.setVibrationPattern(ary);

        //设置角标
        channel.setShowBadge(true);
        //设置渠道组
        channel.setGroup("GROUP");
        //暂不明白什么用
        channel.setBypassDnd(true);
        //设置声音
//        channel.setSound(plynkSound, audioAttributes);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        return channel;

    }

    /**
     * 创建一个 Notification.Action
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public Notification.Action createAction(Context context, Class<?> cls) {
        //创建一个远程输入（既：通知栏的快捷回复）
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REMOTE_INPUT)
                .setLabel(INPUT_HINT)
                .build();

        //创建快速回复的动作，并添加remoteInut

        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Icon icon = Icon.createWithResource("", R.mipmap.upload);
            return new Notification.Action.Builder(icon, INPUT_TITLE, pendingIntent)
                    .addRemoteInput(remoteInput)
                    .build();
        } else {
            return new Notification.Action.Builder(R.mipmap.upload, INPUT_TITLE, pendingIntent)
                    .addRemoteInput(remoteInput)
                    .build();
        }

    }

    /**
     * SDk>=26 创建通知栏的方法
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotification26(Context context, NotificationChannel channel, Notification.Action replyAction) {
        //创建一个Notification，并设置title，content，icon等内容
        Notification newMessageNotification = new Notification.Builder(context, channel.getId())
                .setSmallIcon(R.mipmap.love)
                .setContentTitle("女朋友")
                .setContentText("今晚回来吃饭吗")
                //创建 Action ，如快速回复动作
                .addAction(replyAction)
                // 具体功能还未知
                .setColorized(true)
                //会改变 远程输入背景色
                .setColor(Color.RED)
                //具体功能还未知
                .setOngoing(false)
                //超时后将自动关闭
                .setTimeoutAfter(15 * 1000)
                .build();

        //发出通知
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //8.0及以后必须要带上渠道号
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(NOTIFICATION_ID, newMessageNotification);
    }

}





