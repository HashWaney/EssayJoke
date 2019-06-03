package com.hc.essay.library;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;
import com.example.administrator.framelibrary.BaseSkinActivity;
import com.example.administrator.framelibrary.DefaultNavigationBar;
import com.example.administrator.framelibrary.HttpCallBack;
import com.example.administrator.framelibrary.skin.SkinManager;
import com.example.administrator.framelibrary.skin.SkinResource;
import com.hc.baselibrary.http.HttpUtils;
import com.hc.baselibrary.ioc.ViewById;
import com.hc.baselibrary.permission.PermissionFail;
import com.hc.baselibrary.permission.PermissionHelper;
import com.hc.baselibrary.permission.PermissionSucceed;
import com.hc.essay.library.mode.DiscoverListResult;

import java.io.File;
import java.lang.reflect.Method;


public class MainActivity extends BaseSkinActivity {
    private static final String TAG = "MainActivity";
    // 写内存卡权限申请的请求码
    private static final int WRITE_STORAGE_REQUEST_CODE = 0x0011;

    private boolean isCache = false;


    @Override
    protected void initData() {
        startService();

        initPermission();
        // 路径和参数是不能让别人反编译的，NDK -> .so  1.列表保存第一次，2.有些是保存最后所有
        HttpUtils.with(this).url("http://www.weather.com.cn/data/sk/101010100.html")
                .cache(isCache)// 读取缓存
                .execute(
                new HttpCallBack<DiscoverListResult>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(DiscoverListResult result) {
                Log.i(TAG, "onSuccess: result = " + result.getWeatherinfo().getCity());
                // 成功回掉这个方法
                //　目前是没有缓存，现在有了数据库，还有了网络引擎
                // 思路  某些接口如果需要缓存请自己带标示
            }
        });




    }

    private void startService() {
        startService(new Intent(this ,MessageService.class));
        startService(new Intent(this ,GuardService.class));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            startService(new Intent(this ,JobWakeUpService.class));
        }
    }


    @Override
    protected void initView() {
        // 初始化 View
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

    public void skin(View view){
        // 从服务器上下载

        String SkinPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                +File.separator +"red.skin";
        // 换肤
        int result = SkinManager.getInstance().loadSkin(SkinPath);
    }

    public void skin1(View view){
        // 恢复默认
        int result = SkinManager.getInstance().restoreDefault();
    }


    public void skin2(View view){
        // 跳转
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void changeSkin(SkinResource skinResource) {
        // 做一些第三方的改变
        Toast.makeText(this,"换肤了",Toast.LENGTH_SHORT).show();
    }

    private void initPermission() {
        PermissionHelper.with(this).requestCode(WRITE_STORAGE_REQUEST_CODE)
                .requestPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.READ_EXTERNAL_STORAGE).request();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @PermissionSucceed(requestCode = WRITE_STORAGE_REQUEST_CODE)
    private void createSql() {
        Log.i(TAG, "createSql: isCache = true");
        isCache = true;
    }



    @PermissionFail(requestCode = WRITE_STORAGE_REQUEST_CODE)
    private void createSqlFail(){
        Toast.makeText(this,"您拒绝了创建数据库",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.requestPermissionsResult(this ,requestCode, permissions);
    }
}

