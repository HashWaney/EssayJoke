package com.hc.essay.library.imageselector;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.hc.essay.library.R;
import com.hc.essay.library.imageselector.adapter.CommonAdapter;
import com.hc.essay.library.imageselector.adapter.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

/**
 * Created by Darren on 2016/12/21.
 * Email: 240336124@qq.com
 * Description:
 */

public class ImageSelectorListAdapter extends CommonAdapter<ImageEntity> {

    private Context mContext;
    //选择图片的集合
    private ArrayList<String> mSelectImages;
    private int mMaxCount;
    public final static int REQUEST_CAMERA = 0x0045;
    private int mMode;

    public ImageSelectorListAdapter(Context context, ArrayList<String> selectImages, int maxCount, int mode) {
        super(context, new ArrayList<ImageEntity>(), R.layout.media_chooser_item);
        this.mContext = context;
        this.mSelectImages = selectImages;
        this.mMaxCount = maxCount;
        this.mMode = mode;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void convert(ViewHolder holder, final ImageEntity item, final int position) {
        if (item == null) {
            holder.setVisibility(View.INVISIBLE, R.id.image, R.id.mask, R.id.media_selected_indicator);
            holder.setVisibility(View.VISIBLE, R.id.camera_tv);
            holder.setItemClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCamera();
                }
            });
        } else {
            holder.setVisibility(View.VISIBLE, R.id.image, R.id.media_selected_indicator);
            holder.setVisibility(View.INVISIBLE, R.id.camera_tv);
            // 显示图片
            ImageView imageView = holder.getView(R.id.image);

            Glide.with(mContext)
                    .load(item.path)
                    .centerCrop()
                    .into(imageView);

            ImageView selectedIndicatorIv = holder.getView(R.id.media_selected_indicator);
            selectedIndicatorIv.setSelected(mSelectImages.contains(item.path));
            if(mSelectImages.contains(item.path)){
                Log.i(TAG, "convert: contains = true ");
            }else {
                Log.i(TAG, "convert: contains = false ");
            }
            Log.i(TAG, "convert: mSelectImages = " + mSelectImages);
            Log.i(TAG, "convert: item.path = " + item.path);
            // 设置选中效果
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectImages.contains(item.path)) {
                        mSelectImages.remove(item.path);
                    } else {
                        // 判断是否到达最大
                        if (mMaxCount == mSelectImages.size()) {
                            Toast.makeText(mContext, "最多只能选择" +
                                    mMaxCount + "张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mSelectImages.add(item.path);
                    }
                    if (mListener != null) {
                        mListener.selector();
                    }
                   notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * 打开相机拍照
     */
    private void openCamera() {
        try {
            File tmpFile = FileUtils.createTmpFile(mContext);
            if (mListener != null) {
                mListener.openCamera(tmpFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "相机打开失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 设置数据
     */
    public void setData(List<ImageEntity> images, boolean showCamera) {
        mDatas.clear();
        if (showCamera) {
            mDatas.add(null);
        }
        mDatas.addAll(images);
        notifyDataSetChanged();
    }


    //设置图片选择监听
    private UpdateSelectListener mListener;
    public void setOnUpdateSelectListener(UpdateSelectListener listener) {
        this.mListener = listener;
    }

    /**
     * 添加一张选中的图片
     *
     * @param image
     */
    public void addSelectImage(String image) {
        mSelectImages.add(image);
    }

    public interface UpdateSelectListener {
        public void selector();

        public void openCamera(File file);
    }
}
