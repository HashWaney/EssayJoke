package com.hc.essay.library;

import android.view.View;
import android.widget.Toast;

import com.example.administrator.framelibrary.BaseSkinActivity;


public class TestActivity extends BaseSkinActivity implements View.OnClickListener {
    
    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        // 初始化View
    }

    @Override
    protected void initTitle() {
        // 初始化头部
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, 2/0 + "测试", Toast.LENGTH_LONG).show();
        // Activity   启动流程
    }

}

