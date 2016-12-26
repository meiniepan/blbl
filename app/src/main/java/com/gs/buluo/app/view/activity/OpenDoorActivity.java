package com.gs.buluo.app.view.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.RequestBodyBean.OpenDoorRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.network.OpenDoorService;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.RippleView;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenDoorActivity extends BaseActivity implements RippleView.RippleStateListener, View.OnClickListener {

    @Bind(R.id.open_door_rippleView)
    RippleView mRippleView;
    @Bind(R.id.open_door_text)
    TextView mTextView;
    @Bind(R.id.open_door_down)
    ImageView mImageView;
    @Bind(R.id.open_door_lock)
    ImageView mLockImg;
    private View mRootView;
    private View lockView;

    private static final String TAG = "OpenDoorActivity";
    private Bitmap mBitmap;
    public Context mContext;
    public Handler mHandler=new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what==1) {
                mLockImg.setVisibility(View.INVISIBLE);
                mTextView.setVisibility(View.VISIBLE);
                mRippleView.stopRipple();
            }
        }
    };

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mRippleView.setRippleStateListener(this);
        mTextView.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mRootView = getRootView();
        mContext=this;
        lockView=findViewById(R.id.lock_view);

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra(Constant.PICTURE);
        mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        mRootView.setBackground(new BitmapDrawable(mBitmap));

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        int leftMargin = CommonUtils.getScreenWidth(this)/2 - DensityUtils.dip2px(this,50);
        int topMargin =  (int)(CommonUtils.getScreenHeight(this)/4*3) - DensityUtils.dip2px(this,50);
        lp.setMargins(leftMargin,topMargin, 0, 0);
        lockView.setLayoutParams(lp);
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_open_door;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_door_text:
                mRippleView.startRipple();
                final OpenDoorRequestBody requestBody = new OpenDoorRequestBody();
                requestBody.value="gate_01@dyc.bj";
                TribeRetrofit.getInstance().createApi(OpenDoorService.class).postOpenDoor(TribeApplication.getInstance().getUserInfo().getId(),requestBody).enqueue(new Callback<BaseCodeResponse>() {
                    @Override
                    public void onResponse(Call<BaseCodeResponse> call, Response<BaseCodeResponse> response) {
                        switch (response.body().code) {
                            case 200:
                                mLockImg.setVisibility(View.VISIBLE);
                                mTextView.setVisibility(View.INVISIBLE);

                                mHandler.sendEmptyMessageDelayed(1,3000);

                                break;

                            case 403:
                                mTextView.setText("开锁失败");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseCodeResponse> call, Throwable t) {
                        ToastUtils.ToastMessage(mContext,"网络请求失败");
                    }
                });
                break;
            case R.id.open_door_down:
                finish();
                overridePendingTransition(R.anim.around_alpha, R.anim.around_alpha_out);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.around_alpha, R.anim.around_alpha_out);
    }

    @Override
    public void startRipple() {

    }

    @Override
    public void stopRipple() {

    }

    @Override
    public void onRippleUpdate(ValueAnimator animation) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
