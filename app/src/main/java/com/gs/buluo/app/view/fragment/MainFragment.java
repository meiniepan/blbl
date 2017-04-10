package com.gs.buluo.app.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.CaptureActivity;
import com.gs.buluo.app.view.activity.DoorListActivity;
import com.gs.buluo.app.view.activity.GoodsListActivity;
import com.gs.buluo.app.view.activity.PropertyActivity;
import com.gs.buluo.app.view.activity.ServeActivity;
import com.gs.buluo.app.view.activity.VisitorListActivity;
import com.gs.buluo.app.view.impl.IMainView;
import com.gs.buluo.app.view.widget.AlphaScrollView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by admin on 2016/11/1.
 */
public class MainFragment extends BaseFragment implements IMainView, View.OnClickListener, AlphaScrollView.OnAlphaScrollListener {
    @Bind(R.id.fragment_main_head)
    Banner mBanner;
    @Bind(R.id.main_scroll)
    AlphaScrollView scrollView;
    @Bind(R.id.main_text)
    TextView title;

    View titleBar;
    View line;
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        List list=new ArrayList();
        mBanner.setImageLoader(new FrescoImageLoader(true));
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        list.add(R.mipmap.main_p0);
        list.add(R.mipmap.main_p1);
        list.add(R.mipmap.main_p2);
        list.add(R.mipmap.main_p3);
        list.add(R.mipmap.main_p4);
        mBanner.setImages(list);
        mBanner.setDelayTime(3000);
        mBanner.start();

        scrollView.setScrollListener(this);
        titleBar =getActivity().findViewById(R.id.main_title);
        line =getActivity().findViewById(R.id.main_split);
        line.setVisibility(View.GONE);
        titleBar.getBackground().setAlpha(0);
        title.setVisibility(View.GONE);

        getActivity().findViewById(R.id.shopping).setOnClickListener(this);
        getActivity().findViewById(R.id.shopping_area).setOnClickListener(this);
        getActivity().findViewById(R.id.food).setOnClickListener(this);
        getActivity().findViewById(R.id.food_area).setOnClickListener(this);
        getActivity().findViewById(R.id.fun).setOnClickListener(this);
        getActivity().findViewById(R.id.fun_area).setOnClickListener(this);
        getActivity().findViewById(R.id.main_property).setOnClickListener(this);
        getActivity().findViewById(R.id.scan).setOnClickListener(this);
        getActivity().findViewById(R.id.main_open).setOnClickListener(this);
        getActivity().findViewById(R.id.main_booking).setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void showError(int res) {

    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.shopping:
                intent.setClass(getActivity(), GoodsListActivity.class);
                startActivity(intent);
                break;
            case R.id.shopping_area:
                intent.setClass(getActivity(), GoodsListActivity.class);
                startActivity(intent);
                break;
            case R.id.food:
                intent.setClass(getActivity(), ServeActivity.class);
                intent.putExtra(Constant.TYPE,Constant.REPAST);
                startActivity(intent);
                break;
            case R.id.food_area:
                intent.setClass(getActivity(), ServeActivity.class);
                intent.putExtra(Constant.TYPE,Constant.REPAST);
                startActivity(intent);
                break;
            case R.id.fun:
                intent.setClass(getActivity(), ServeActivity.class);
                intent.putExtra(Constant.TYPE,Constant.ENTERTAINMENT_ALL);
                startActivity(intent);
                break;
            case R.id.fun_area:
                intent.setClass(getActivity(), ServeActivity.class);
                intent.putExtra(Constant.TYPE,Constant.ENTERTAINMENT_ALL);
                startActivity(intent);
                break;
            case R.id.main_property:
                if (!checkUser(getActivity()))return;
                intent.setClass(getActivity(), PropertyActivity.class);
                startActivity(intent);
                break;
            case R.id.scan:
                if (!checkUser(getActivity()))return;
                intent.setClass(getActivity(), CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.main_booking:
                if (!checkUser(getActivity()))return;
                if (checkQualification()) return;
                intent.setClass(getActivity(), VisitorListActivity.class);
                startActivity(intent);
                break;
            case R.id.main_open:
                if (!checkUser(getActivity()))return;
                if (checkQualification()) return;
                intent.setClass(getActivity(),DoorListActivity.class);
                startActivity(intent);
                break;
        }
    }

    public boolean checkQualification() {
        if (TribeApplication.getInstance().getUserInfo().getIdNo()==null){
            ToastUtils.ToastMessage(getContext(),getString(R.string.no_identify));
            return true;
        }
        if (TribeApplication.getInstance().getUserInfo().getCompanyID()==null){
            ToastUtils.ToastMessage(getContext(),getString(R.string.no_company_bind));
            return true;
        }
        return false;
    }

    @Override
    public void onScroll(int scrollY) {
        if(scrollY < DensityUtils.dip2px(getActivity(),28)){
            titleBar.getBackground().setAlpha(0);
            line.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
        }else if(scrollY >= DensityUtils.dip2px(getActivity(),28) && scrollY < DensityUtils.dip2px(getContext(),240)){
            titleBar.getBackground().setAlpha(255* scrollY/  DensityUtils.dip2px(getContext(),240));
        }else{
            titleBar.getBackground().setAlpha(255);
            line.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
        }
    }

}
