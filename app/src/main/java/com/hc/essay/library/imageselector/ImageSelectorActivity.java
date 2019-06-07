package com.hc.essay.library.imageselector;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.administrator.framelibrary.BaseSkinActivity;
import com.example.administrator.framelibrary.DefaultNavigationBar;
import com.example.administrator.framelibrary.util.StatusBarUtil;
import com.hc.essay.library.R;

import java.io.File;
import java.util.ArrayList;

import static com.hc.essay.library.imageselector.ImageSelectorListAdapter.REQUEST_CAMERA;


public class ImageSelectorActivity extends BaseSkinActivity implements ImageSelectorListAdapter.UpdateSelectListener, View.OnClickListener {
    private static final String TAG = "ImageSelectorActivity";

    // 选择图片的模式 - 多选
    public static final int MODE_MULTI = 0x0011;
    // 选择图片的模式 - 单选
    public static int MODE_SINGLE = 0x0012;
    // 是否显示相机的EXTRA_KEY
    public static final String EXTRA_SHOW_CAMERA = "EXTRA_SHOW_CAMERA";
    // 总共可以选择多少张图片的EXTRA_KEY
    public static final String EXTRA_SELECT_COUNT = "EXTRA_SELECT_COUNT";
    // 原始的图片路径的EXTRA_KEY
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "EXTRA_DEFAULT_SELECTED_LIST";
    // 选择模式的EXTRA_KEY
    public static final String EXTRA_SELECT_MODE = "EXTRA_SELECT_MODE";
    // 返回选择图片列表的EXTRA_KEY
    public static final String EXTRA_RESULT = "EXTRA_RESULT";

    private TextView mSelectPreview;
    //预览
    private TextView mSelectNumTv;
    // 查询所有数据
    private RecyclerView mImageListRc;
    // 加载所有的数据
    private static final int LOADER_TYPE = 0x0021;
    // 图片显示的Adapter
    private ImageSelectorListAdapter mImageAdapter;
    // 拍照临时存放的文件
    private File mTempFile;

    /*****************
     * 获取传递过来的参数
     *****************/
    private int mMode = MODE_MULTI;
    private int mMaxCount = 8;
    private boolean mShowCamera = true;
    private ArrayList<String> mResultList;
    private TextView mSelectFinish;


    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar = new
                DefaultNavigationBar.Builder(this)
                .setTitle("所有图片")
                .build();
        StatusBarUtil.statusBarTintColor(this, Color.parseColor("#261f1f"));
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_select);
        mSelectNumTv = findViewById(R.id.select_num);
        mImageListRc =  findViewById(R.id.image_list_rv);
        mSelectPreview =findViewById(R.id.select_preview);
        mSelectFinish = findViewById(R.id.select_finish);
        mSelectFinish.setOnClickListener(this);

        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_SELECT_MODE, mMode);
        mMaxCount = intent.getIntExtra(EXTRA_SELECT_COUNT, mMaxCount);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, mShowCamera);
        mResultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        if (mResultList == null) {
            mResultList = new ArrayList<>();
        }


        // 初始化本地图片数据
        initImageList();

        //改变现实
        exchangeViewShow();
    }

    //改变布局显示，需要及时更新,每次点击的地方下手
    private void exchangeViewShow() {
        //预览是不是可以点击，显示什么颜色
        if(mResultList.size() > 0){
            //至少选择一张
            mSelectPreview.setEnabled(true);
            mSelectPreview.setOnClickListener(this);
        }else {
            //一张都没有选
            mSelectPreview.setEnabled(false);
            mSelectPreview.setOnClickListener(null);
        }


        //中间图片的张数也要显示

        mSelectNumTv.setText(mResultList.size() + "/" + mMaxCount);
    }

    /**
     * 初始化本地图片数据
     */
    private void initImageList() {
        mImageListRc.setLayoutManager(new GridLayoutManager(this, 4));
        getLoaderManager().initLoader(LOADER_TYPE, null, mLoaderCallback);
    }

    /**
     * 加载图片的CallBack
     */
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = new CursorLoader(ImageSelectorActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? ",
                    new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // 如果有数据变量数据
            if (data != null && data.getCount() > 0) {
                ArrayList<ImageEntity> images = new ArrayList<>();

                // 不断的遍历循环
                while (data.moveToNext()) {
                    String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                    Log.e("TAG", path + " " + name + " " + dateTime);

                    // 判断文件是不是存在
                    if (!pathExist(path)) {
                        continue;
                    }

                    Log.e("TAG", path + " " + name + " " + dateTime);
                    // 封装数据对象
                    ImageEntity image = new ImageEntity(path, name, dateTime);
                    images.add(image);
                }

                // 显示列表数据
                showListData(images);
            }
        }

        /**
         * 判断该路径文件是不是存在
         */
        private boolean pathExist(String path) {
            if (!TextUtils.isEmpty(path)) {
                return new File(path).exists();
            }
            return false;
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /**
     * 显示图片列表数据
     */
    private void showListData(ArrayList<ImageEntity> images) {
        if (mImageAdapter == null) {
            mImageAdapter = new ImageSelectorListAdapter(this, mResultList, mMaxCount, mMode);
            mImageListRc.setAdapter(mImageAdapter);
        }
        mImageAdapter.setData(images, mShowCamera);
        mImageAdapter.setOnUpdateSelectListener(this);
    }

    @Override
    public void selector() {
        exchangeViewShow();
    }

    @Override
    public void openCamera(File file) {
        mTempFile = file;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * 设置返回结果
     */
    private void setResult() {
        Intent data = new Intent();
        data.putStringArrayListExtra(EXTRA_RESULT, mResultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                // notify system the image has change
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTempFile)));
                mResultList.add(mTempFile.getAbsolutePath());
                setResult();
            }
        }
    }

    @Override
    public void onClick(View v) {
        //图片预览
        switch (v.getId()){
            case R.id.select_preview:
                Log.i(TAG, "onClick: 123");
                break;
            case R.id.select_finish:
                Log.i(TAG, "onClick: 456");
                setResult();
                break;
        }
    }


}
