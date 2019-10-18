package com.xunyuan.xinyu.floatwindow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xunyuan.xinyu.R;
import com.xunyuan.xinyu.floatwindow.MyService;

/**
 * 实验WindowManager  这个类的
 */
public class WindowManagerActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_manager);

        findViewById(R.id.btn_show).setOnClickListener(this);
        findViewById(R.id.btn_hide).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MyService.class);
        switch (v.getId()) {
            case R.id.btn_show:
                intent.putExtra(MyService.ACTION, MyService.SHOW);
                break;
            case R.id.btn_hide:
                intent.putExtra(MyService.ACTION, MyService.HIDE);
                break;
        }
        startService(intent);
    }

}
