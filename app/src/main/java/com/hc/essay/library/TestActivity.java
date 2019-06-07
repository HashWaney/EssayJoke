package com.hc.essay.library;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.framelibrary.BaseSkinActivity;
import com.hc.baselibrary.permission.PermissionFail;
import com.hc.baselibrary.permission.PermissionHelper;
import com.hc.baselibrary.permission.PermissionSucceed;
import com.hc.essay.library.imageselector.ImageSelector;
import com.hc.essay.library.imageselector.ImageSelectorActivity;

import java.util.ArrayList;


public class TestActivity extends BaseSkinActivity implements View.OnClickListener {

    // 写内存卡权限申请的请求码
    private static final int WRITE_STORAGE_REQUEST_CODE = 0x0011;
    private static final String TAG ="TestActivity" ;

    private final int REQUEST_CODE = 0x0023;
    private ArrayList<String> mImages;
    private TextView mSelectResultTv;
    
    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        // 初始化View
        mSelectResultTv = (TextView) findViewById(R.id.select_result_tv);
    }

    @Override
    protected void initTitle() {
        // 初始化头部
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test_image);
    }

    @Override
    public void onClick(View v) {
    }

    private void initPermission() {
        PermissionHelper.with(this).requestCode(WRITE_STORAGE_REQUEST_CODE)
                .requestPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.READ_EXTERNAL_STORAGE).request();
    }
    //选择图片
    public void selectImage(View v){
        initPermission();

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @PermissionSucceed(requestCode = WRITE_STORAGE_REQUEST_CODE)
    private void imagePermissionSuccess() {
        Log.i(TAG, "imagePermissionSuccess: mImages = " + mImages);
        ImageSelector.create().count(10).multi().origin(mImages).showCamera(true).start(this, REQUEST_CODE);
    }

    @PermissionFail(requestCode = WRITE_STORAGE_REQUEST_CODE)
    private void imagePermissionfail(){
        Toast.makeText(this,"您拒绝了",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE && data != null) {
                mImages = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                String pathStrs = "";
                for(String item:mImages){
                    pathStrs += item+"\n";
                }
                mSelectResultTv.setText(pathStrs);
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.requestPermissionsResult(this ,requestCode, permissions);
    }

}

