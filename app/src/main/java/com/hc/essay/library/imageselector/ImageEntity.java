package com.hc.essay.library.imageselector;

import android.text.TextUtils;

/**
 * Created by Darren on 2016/12/21.
 * Email: 240336124@qq.com
 * Description: 图片对象
 */

public class ImageEntity {
    public String path;
    public String name;
    public long time;

    public ImageEntity(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ImageEntity) {
            ImageEntity compare = (ImageEntity) o;
            return TextUtils.equals(this.path, compare.path);
        }
        return false;
    }
}
