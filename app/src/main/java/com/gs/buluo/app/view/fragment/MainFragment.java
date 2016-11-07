package com.gs.buluo.app.view.fragment;


import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.UserInfo;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.MainPresenter;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IMainView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by admin on 2016/11/1.
 */
public class MainFragment extends BaseFragment implements IMainView{
    @Bind(R.id.fragment_main_head)
    Banner mBanner;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        List list=new ArrayList();
        list.add(R.mipmap.page1);
        list.add(R.mipmap.page2);
        list.add(R.mipmap.page3);
        list.add(R.mipmap.page4);
        mBanner.setImageLoader(new FrescoImageLoader());
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImages(list);
        mBanner.setDelayTime(2000);
        mBanner.start();

        mBanner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                ((MainPresenter)mPresenter).getUser(mContext,null);
            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void setUser(UserInfo info) {
        ToastUtils.ToastMessage(mContext,info.getUid()+"");
    }

    @Override
    public void showError() {

    }
}