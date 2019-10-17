/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.xunyuan.xinyu;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.xunyuan.xinyu.view.FloatWindowManager;
import com.xunyuan.xinyu.perssion.FloatWindowUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * 悬浮窗Activity
 */
public class FloatWindowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_show_or_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FloatWindowUtils.manageDrawOverlays(context)) {
                    FloatWindowManager.getInstance().showWindow(context);
                }
            }
        });

        findViewById(R.id.btn_dismiss).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FloatWindowManager.getInstance().dismissWindow();
                    }
                });
    }


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
