package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.MinePresenter;
import com.gs.buluo.app.view.activity.LoginActivity;
import com.gs.buluo.app.view.activity.SelfActivity;
import com.gs.buluo.app.view.activity.SettingActivity;
import com.gs.buluo.app.view.activity.VerifyActivity;
import com.gs.buluo.app.widget.ChoosePhotoPanel;
import com.gs.buluo.app.widget.pulltozoom.PullToZoomScrollViewEx;

import butterknife.Bind;


/**
 * Created by admin on 2016/11/1.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    SimpleDraweeView mHead;
    LinearLayout llLogin;
    LinearLayout llUnLogin;
    TextView mNick;

    SimpleDraweeView mCover;

    PullToZoomScrollViewEx scrollView;
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.self_zoom_layout, null, false);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.self_content_layout, null, false);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.self_head_layout, null, false);

        scrollView= (PullToZoomScrollViewEx) getActivity().findViewById(R.id.self_scroll_view);
        mHead= (SimpleDraweeView) headView.findViewById(R.id.mine_head);
        mHead.setOnClickListener(this);
        headView.findViewById(R.id.mine_login).setOnClickListener(this);
        headView.findViewById(R.id.mine_register).setOnClickListener(this);
        contentView.findViewById(R.id.mine_verify).setOnClickListener(this);
        zoomView.findViewById(R.id.mine_setting).setOnClickListener(this);
        zoomView.findViewById(R.id.mine_cover).setOnClickListener(this);
        zoomView.findViewById(R.id.rl_head_bg);
        llLogin= (LinearLayout) headView.findViewById(R.id.self_ll_login);
        llUnLogin= (LinearLayout) headView.findViewById(R.id.self_ll_un_login);
        mNick= (TextView) headView.findViewById(R.id.self_nickname);
        mCover= (SimpleDraweeView) zoomView.findViewById(R.id.rl_head_bg);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);

        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);

        if (TribeApplication.getInstance().getUserInfo()!=null){
            setLoginState(true);
        }
    }


    @Override
    protected BasePresenter getPresenter() {
        return new MinePresenter();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.mine_head:
                intent.setClass(getActivity(),SelfActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_login:
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_register:
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_verify:
                intent.setClass(getActivity(),VerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_setting:
                intent.setClass(getActivity(),SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_cover:
                ChoosePhotoPanel window=new ChoosePhotoPanel(getContext(), new ChoosePhotoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String path) {
                        mCover.setImageURI("file://"+path);
                    }
                });
                window.show();
                break;
        }
    }

    public void setLoginState(boolean loginState) {
        if (null==llUnLogin||null==llLogin)return;
        if (loginState){
            llLogin.setVisibility(View.VISIBLE);
            llUnLogin.setVisibility(View.GONE);
            String nickname = TribeApplication.getInstance().getUserInfo().getNickname();
            if (!TextUtils.isEmpty(nickname)){
                mNick.setText(nickname);
            }
        }else {
            llLogin.setVisibility(View.GONE);
            llUnLogin.setVisibility(View.VISIBLE);
        }
    }
}
