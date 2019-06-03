package com.hc.essay.library.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.hc.baselibrary.adapter.CommonAdapter;
import com.hc.baselibrary.adapter.ViewHolder;
import com.hc.essay.library.R;
import com.hc.essay.library.WebActivity;
import com.hc.essay.library.mode.NewsListResult;

import java.util.List;

/**
 * description:
 * <p/>
 * Created by 曾辉 on 2016/11/5.
 * QQ：240336124
 * Email: 240336124@qq.com
 * Version：1.0
 */
public class NewslListAdapter extends CommonAdapter<NewsListResult.ResultBean.DataBean> {
    private Context mContext;
    public NewslListAdapter(Context context, List<NewsListResult.ResultBean.DataBean> datas) {

        super(context, datas, R.layout.channel_list_item);
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder viewHolder, final NewsListResult.ResultBean.DataBean item, int position) {
        viewHolder.setText(R.id.channel_text,item.getTitle());
        viewHolder.setText(R.id.channel_topic,item.getAuthor_name());
        String numberDescStr = item.getCategory()+" 订阅 | "+"总帖数 <font color='#FF678D'>"
                +item.getAuthor_name()+"</font>";

        TextView numberDescTv = viewHolder.getView(R.id.channel_update_info);

        numberDescTv.setText(Html.fromHtml(numberDescStr));

        viewHolder.setImageUrl(R.id.channel_icon,item.getThumbnail_pic_s());

//        if(item.isIs_recommend()){
//            viewHolder.setViewVisible(R.id.recommend_label);
//        }else{
//            viewHolder.setViewGone(R.id.recommend_label);
//        }
        TextView detailTv = viewHolder.getView(R.id.action_btn);
        detailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebActivity.runActivity(mContext, "我的Github,欢迎star", item.getUrl());
            }
        });
    }
}
