/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.xunyuan.xinyu.floatwindow;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.xunyuan.xinyu.R;
import com.xunyuan.xinyu.databinding.ActivityFloatWindowBinding;
import com.xunyuan.xinyu.floatwindow.view.FloatWindowManager;
import com.xunyuan.xinyu.floatwindow.perssion.FloatWindowUtils;
import com.xunyuan.xinyu.notification.MyNotificationManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 悬浮窗Activity
 */
public class FloatWindowActivity extends Activity implements View.OnClickListener {
    private Context context;

    private MyNotificationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFloatWindowBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_float_window);
        binding.setListener(this);
        context = this;
        manager = new MyNotificationManager(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_or_apply:
                if (FloatWindowUtils.manageDrawOverlays(context)) {
                    FloatWindowManager.getInstance(context).showWindow(context);
                }
                break;
            case R.id.btn_dismiss:
                FloatWindowManager.getInstance(context).dismissWindow();
                break;
            case R.id.jump:
                Intent intent = new Intent(context, WindowManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.notification:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    manager.createNotification26(context, manager.createNotificationChannel(), manager.createAction(context, FloatWindowActivity.class));
                }
                break;
            case R.id.cancleNotification:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    manager.cancalNotification(manager.NOTIFICATION_ID);
                }
                break;
            default:
                break;

        }

    }


    //测试方法
    private void fafa() {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText("build.prop", readString("/system/build.prop")));
    }

    /**
     *
     */
    public static String readString(String file) {
        InputStream input = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            input = new FileInputStream(new File(file));
            byte[] buffer = new byte[1024 * 4];
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            output.flush();
            return output.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


}
