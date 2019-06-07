package com.hc.essay.library.imageselector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Darren on 2016/12/21.
 * Email: 240336124@qq.com
 * Description:
 */

public abstract class MultiCommonAdapter<DATA> extends CommonAdapter<DATA> {
    private MultiSupport mMultiSupport;

    public MultiCommonAdapter(Context context, List list, MultiSupport multiSupport) {
        super(context, list, -1);
        this.mMultiSupport = multiSupport;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return mMultiSupport.getLayoutId(mDatas.get(position), position);
    }
}
