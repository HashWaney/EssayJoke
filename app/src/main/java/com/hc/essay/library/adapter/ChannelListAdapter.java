package com.hc.essay.library.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;
import com.hc.baselibrary.adapter.CommonAdapter;
import com.hc.baselibrary.adapter.ViewHolder;
import com.hc.essay.library.R;
import com.hc.essay.library.mode.ChannelListResult;

import java.util.List;

/**
 * description:
 * <p/>
 * Created by 曾辉 on 2016/11/5.
 * QQ：240336124
 * Email: 240336124@qq.com
 * Version：1.0
 */
public class ChannelListAdapter extends CommonAdapter<ChannelListResult.DataBean.CategoriesBean.ChannelListBean> {
    public ChannelListAdapter(Context context, List<ChannelListResult.DataBean.CategoriesBean.ChannelListBean> datas) {
        super(context, datas, R.layout.channel_list_item);
    }

    @Override
    protected void convert(ViewHolder viewHolder, ChannelListResult.DataBean.CategoriesBean.ChannelListBean item, int position) {
        viewHolder.setText(R.id.channel_text,item.getName());
        viewHolder.setText(R.id.channel_topic,item.getIntro());
        String numberDescStr = item.getSubscribe_count()+" 订阅 | "+"总帖数 <font color='#FF678D'>"
                +item.getTotal_updates()+"</font>";

        TextView numberDescTv = viewHolder.getView(R.id.channel_update_info);

        numberDescTv.setText(Html.fromHtml(numberDescStr));

        viewHolder.setImageUrl(R.id.channel_icon,item.getIcon_url());

        if(item.isIs_recommend()){
            viewHolder.setViewVisible(R.id.recommend_label);
        }else{
            viewHolder.setViewGone(R.id.recommend_label);
        }
    }
}
