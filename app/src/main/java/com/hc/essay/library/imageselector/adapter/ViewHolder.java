package com.hc.essay.library.imageselector.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Darren on 2016/12/21.
 * Email: 240336124@qq.com
 * Description:
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private View mRootView;

    private SparseArray<View> mViews;

    public ViewHolder(View itemView) {
        super(itemView);
        this.mRootView = itemView;
        mViews = new SparseArray<>();
    }

    /**
     * 获取View
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mRootView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public void setImageResource(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
    }

    public void setVisibility(int visible, int... viewIds) {
        for (int viewId : viewIds)
            getView(viewId).setVisibility(visible);
    }

    public void setText(int viewId, String text) {
        TextView itemTv = getView(viewId);
        itemTv.setText(text);
    }

    public void setItemClick(View.OnClickListener listener) {
        mRootView.setOnClickListener(listener);
    }
}
