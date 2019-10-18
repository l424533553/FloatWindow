/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.xunyuan.xinyu.floatwindow.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Description:
 */
public class FloatWindowManager {
    private static final String TAG = "FloatWindowManager";
    private static  FloatWindowManager instance;
    private boolean isWindowDismiss = true;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    private FloatWindowView floatView = null;
    private Context mContext;


    public static FloatWindowManager getInstance(Context context) {
        if (instance == null) {
            synchronized (FloatWindowManager.class) {
                if (instance == null) {
                    instance = new FloatWindowManager(context);
                }
            }
        }
        return instance;
    }


    private FloatWindowManager(Context context) {
        //获得WindowManager对象
        mContext = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * 添加悬浮窗
     */
    protected boolean addView(View view, WindowManager.LayoutParams params) {
        try {
            windowManager.addView(view, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 移除悬浮窗
     */
    protected boolean removeView(View view) {
        try {
            windowManager.removeView(view);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新悬浮窗参数
     */
    protected boolean updateView(View view, WindowManager.LayoutParams params) {
        try {
            windowManager.updateViewLayout(view, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public void showWindow(Context context) {
        if (!isWindowDismiss) {
            Log.e(TAG, "view is already added here");
            return;
        }

        isWindowDismiss = false;
        if (windowManager == null) {
            windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }

        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        mParams = new WindowManager.LayoutParams();
        mParams.packageName = context.getPackageName();
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        int mType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mType = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }
        mParams.type = mType;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.x = screenWidth - dp2px(context, 100);
        mParams.y = screenHeight - dp2px(context, 171);


//        ImageView imageView = new ImageView(mContext);
//        imageView.setImageResource(R.drawable.app_icon);
        floatView = new FloatWindowView(context);
        floatView.setParams(mParams);
        floatView.setIsShowing(true);
        windowManager.addView(floatView, mParams);
    }

    public void dismissWindow() {
        if (isWindowDismiss) {
            Log.e(TAG, "window can not be dismiss cause it has not been added");
            return;
        }

        isWindowDismiss = true;
        floatView.setIsShowing(false);
        if (windowManager != null && floatView != null) {
            windowManager.removeViewImmediate(floatView);
        }
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
