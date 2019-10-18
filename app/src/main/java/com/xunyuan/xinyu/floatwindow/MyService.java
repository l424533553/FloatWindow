package com.xunyuan.xinyu.floatwindow;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.xunyuan.xinyu.floatwindow.view.FloatingView;


/**
 * 作者：罗发新
 * 时间：2019/10/17 0017    星期四
 * 邮件：424533553@qq.com
 * 说明：
 */

public class MyService extends Service {
    public static final String ACTION = "action";
    public static final String SHOW = "show";
    public static final String HIDE = "hide";
    private FloatingView mFloatingView;

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = new FloatingView(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getStringExtra(ACTION);
            if (SHOW.equals(action)) {
                mFloatingView.show();
            } else if (HIDE.equals(action)) {
                mFloatingView.hide();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
