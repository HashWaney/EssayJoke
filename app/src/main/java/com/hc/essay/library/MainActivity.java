package com.hc.essay.library;

import android.view.View;

import com.example.administrator.framelibrary.BaseSkinActivity;
import com.example.administrator.framelibrary.DefaultNavigationBar;
import com.example.administrator.framelibrary.HttpCallBack;
import com.hc.baselibrary.http.HttpUtils;
import com.hc.essay.library.mode.DiscoverListResult;


public class MainActivity extends BaseSkinActivity implements View.OnClickListener {

    @Override
    protected void initData() {
        // 路径和参数是不能让别人反编译的，NDK -> .so  1.列表保存第一次，2.有些是保存最后所有
        HttpUtils.with(this).url("http://www.weather.com.cn/data/sk/101010100.html")
                .cache(true)// 读取缓存
                .execute(
                new HttpCallBack<DiscoverListResult>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(DiscoverListResult result) {
                // 成功回掉这个方法
                //　目前是没有缓存，现在有了数据库，还有了网络引擎
                // 思路  某些接口如果需要缓存请自己带标示
            }
        });

    }


    @Override
    protected void initView() {
        // 初始化 View
        viewById(R.id.test_tv).setOnClickListener(this);
    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar = new
                DefaultNavigationBar.Builder(this)
                .setTitle("投稿")
                .builder();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {

    }
}

