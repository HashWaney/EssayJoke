package com.hc.baselibrary.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import com.hc.baselibrary.R;
import com.hc.baselibrary.ioc.ViewUtils;
import com.hc.baselibrary.utils.ScreenUtils;
import com.hc.baselibrary.view.LoadView;


/**
 * Email 240336124@qq.com
 * Created by Darren on 2017/2/12.
 * Version 1.0
 * Description: 整合应用的BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {

    /** 正在加载数据的页面 */
    private View mLoadingView;
    /** WindowManager下的LayoutParams实例 */
    private WindowManager.LayoutParams mlodingParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置布局layout
        setContentView();

        //Log.e("TAG", viewRoot + "");

        // 一些特定的算法，子类基本都会使用的
        ViewUtils.inject(this);

        // 初始化头部
        initTitle();

        // 初始化界面
        initView();

        // 初始化数据
        initData();
    }

    /**
     * TODO initialization system status bar , but build version need greater 19
     */
    protected void initSystemBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(this, true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);

        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintResource(color);
    }

    /**
     * TODO 设置系统顶部栏和程序主题颜色统一
     * @param activity 当前活动Activity实例
     * @param on
     * void
     */
    @TargetApi(19)
    private void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();

        WindowManager.LayoutParams winParams = win.getAttributes();

        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /**
     * 利用代码的方式加载动画布局
     */
    protected void addLoadView(){
        // 新建加载动画对象
        final WindowManager mWM = getWindowManager();
        mlodingParams = new WindowManager.LayoutParams();
        final int statusBarHeight = ScreenUtils.getStatusHeight(this);
        // 获取头部的高度
        final int titleBarHeight = (int) getResources().getDimension(
                R.dimen.title_height);
        // height = 屏幕的高度 - 状态栏的高度 - 头部的高度
        mlodingParams.height =  ScreenUtils.getScreenHeight(this)
                - statusBarHeight - titleBarHeight;
        mlodingParams.width = WindowManager.LayoutParams.MATCH_PARENT;


        //  控制能够响应其他布局
        mlodingParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 效果为背景透明
        mlodingParams.format = PixelFormat.RGBA_8888;
        // 在底部显示
        mlodingParams.gravity = Gravity.BOTTOM;
        // 床架加载动画布局
        mLoadingView = new LoadView(this);
        mWM.addView(mLoadingView, mlodingParams);
    }

    /**
     * TODO 如果有缓存或是数据获取成功了，就把正在加载数据的界面干掉
     */
    protected final void dismissLoadView() {
        if (mLoadingView == null)
            return;
        // mLoadingView.setVisibility(View.GONE);
        final WindowManager mWM = getWindowManager();
        // 移除LoadView
        mWM.removeView(mLoadingView);
        this.mLoadingView = null;
        this.mlodingParams = null;
    }


    // 初始化数据
    protected abstract void initData();

    // 初始化界面
    protected abstract void initView();

    // 初始化头部
    protected abstract void initTitle();

    // 设置布局layout
    protected abstract void setContentView();


    /**
     * 启动Activity
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * findViewById
     *
     * @return View
     */
    protected <T extends View> T viewById(int viewId) {
        return (T) findViewById(viewId);
    }

    // 只能放一些通用的方法，基本每个Activity都需要使用的方法，readDataBase最好不要放进来 ，
    // 如果是两个或两个以上的地方要使用,最好写一个工具类。
    // 为什么？下周末会讲热修复  阿里开源的 divalk层的方法是怎么加载进来的   腾讯使用的分包问题  是性能方面的问题
    // 永远预留一层
}
