package com.hc.baselibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hc.baselibrary.ioc.ViewUtils;
import com.hc.baselibrary.mode.BaseResult;

/**
 * description：
 * <p/>
 * Created by 曾辉 on 2016/10/20.
 * QQ：240336124
 * Email: 240336124@qq.com
 * Version：1.0
 */
public abstract class BaseFragment extends Fragment {

    protected View rootView;
    protected Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        rootView = View.inflate(context,getLayoutId(),null);

        // 加入注解
        ViewUtils.inject(this,rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getLayoutId();

    /**
     * 判断后台返回的数据是否成功
     */
    protected  boolean isNetRequestOk(BaseResult result){
        return result.reason.equals("成功的返回");
    }
}
