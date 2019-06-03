package com.hc.essay.library.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.administrator.framelibrary.HttpCallBack;
import com.hc.baselibrary.base.BaseFragment;
import com.hc.baselibrary.http.HttpUtils;
import com.hc.baselibrary.ioc.ViewById;
import com.hc.essay.library.R;
import com.hc.essay.library.adapter.NewslListAdapter;
import com.hc.essay.library.banner.BannerAdapter;
import com.hc.essay.library.banner.BannerView;
import com.hc.essay.library.banner.BannerViewPager;
import com.hc.essay.library.mode.NewsListResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/3.
 */
public class FindFragment extends BaseFragment implements BannerViewPager.BannerItemClickListener {

    private static final String TAG = "MainActivity";

    @ViewById(R.id.list_view)
    private ListView mListView;

    @ViewById(R.id.root_view)
    private ViewGroup mRootView;

    @ViewById(R.id.banner_view)
    private BannerView mBannerView;

    List<String> bannerList;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        // 数据访问的引擎 用得第三方

        Map<String, Object> params = new HashMap<>();
        params.put("key","c429552ff2d965929082148f13344f06");
        params.put("iid", 6152551759L);
        params.put("channel", 360);
        params.put("aid", 7);

        HttpUtils.with(context).url("http://v.juhe.cn/toutiao/index?type=top").addParams(params).execute(new HttpCallBack<NewsListResult>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(NewsListResult result) {
                if (isNetRequestOk(result)) {
                    Log.i(TAG, "onSuccess: result = " + result.reason );
                    mListView.setAdapter(new NewslListAdapter(context,
                            result.getResult().getData()));

                    // 获取到接口返回数据 初始化广告位
//                    initBanner(result.getData().getRotate_banner().getBanners());
                    bannerList = new ArrayList<>();
                    for (int i = 0; i <5; i ++){
                        bannerList.add(result.getResult().getData().get(i).getThumbnail_pic_s());
                    }
                    initBanner(bannerList);
                }
            }
        });
    }

    /**
     * 初始化广告位
     *
     * @param banners
     */
    private void initBanner(final List<String> banners) {

        mBannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                ImageView bannerIv = null;
                if (convertView == null) {
                    bannerIv = new ImageView(context);
                    bannerIv.setScaleType(ImageView.ScaleType.FIT_XY);
                } else {
                    bannerIv = (ImageView) convertView;
                    Log.e(TAG, "界面复用" + convertView);
                }
                // 利用第三方的工具加载图片  Glide
                String imagePath = banners.get(position);
                Glide.with(context).load(imagePath).
                        // 加载默认图片
                                placeholder(R.drawable.banner_default).into(bannerIv);
                return bannerIv;
            }

            @Override
            public int getCount() {
                return banners.size();
            }


        });

        // 开启滚动
        mBannerView.startRoll();

        // 设置条目点击监听
        mBannerView.setOnBannerItemClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    public void click(int position) {
        Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
    }
}
