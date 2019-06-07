package com.hc.essay.library.imageselector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Darren on 2016/12/21.
 * Email: 240336124@qq.com
 * Description:
 */

public abstract class CommonAdapter<DATA> extends RecyclerView.Adapter<ViewHolder> {

    protected List<DATA> mDatas;
    protected Context mContext;
    protected int mLayoutId;

    public CommonAdapter(Context context, List<DATA> datas, int layoutId) {
        mDatas = datas;
        this.mContext = context;
        this.mLayoutId = layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mDatas.get(position), position);
    }

    public abstract void convert(ViewHolder holder, DATA item, int position);


    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
