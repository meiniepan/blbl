package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.WxPayResponse;
import com.gs.buluo.app.eventbus.TopupEvent;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.utils.WXPayUtils;
import com.gs.buluo.app.view.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * Created by hjn on 2016/12/29.
 */
public class RechargePanel extends Dialog implements View.OnClickListener {
    Context mContext;
    @Bind(R.id.recharge_integer)
    TextView mInterger;
    @Bind(R.id.recharge_float)
    TextView mFloat;
    @Bind(R.id.recharge_pay_wechat)
    RadioButton rbWeChat;
    @Bind(R.id.recharge_pay_ali)
    RadioButton rbAli;
    @Bind(R.id.recharge_input)
    EditText mInput;

    private OrderBean.PayChannel payMethod;
    private String prepayid;

    public RechargePanel(Context context) {
        super(context, R.style.my_dialog);
        mContext = context;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.recharge_board, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);


        findViewById(R.id.recharge_back).setOnClickListener(this);
        findViewById(R.id.recharge_finish).setOnClickListener(this);

        rbAli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbAli.setChecked(true);
                    rbWeChat.setChecked(false);
                    payMethod = OrderBean.PayChannel.ALIPAY;
                }
            }
        });
        rbWeChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbAli.setChecked(false);
                    rbWeChat.setChecked(true);
                    payMethod = OrderBean.PayChannel.WEICHAT;
                }
            }
        });
        EventBus.getDefault().register(this);
    }

    public void setData(String price) {
        if (price==null)return;
        String[] arrs = price.split("\\.");
        if (arrs.length > 1) {
            mInterger.setText(arrs[0]);
            mFloat.setText(arrs[1]);
        } else {
            mInterger.setText(price);
            mFloat.setText("00");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge_back:
                dismiss();
                break;
            case R.id.recharge_finish:
                String inputNum = mInput.getText().toString().trim();
                if (inputNum.length() <= 0) {
                    ToastUtils.ToastMessage(mContext, "请输入充值金额");
                    return;
                }
                LoadingDialog.getInstance().show(mContext, "充值中", true);
                if (payMethod == OrderBean.PayChannel.WEICHAT) {
                    doRecharge(inputNum);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void rechargeSuccess(TopupEvent event) {
        new MoneyModel().getWXRechargeResult(prepayid, new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                ToastUtils.ToastMessage(mContext, "充值成功");
                dismiss();
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                ToastUtils.ToastMessage(mContext, R.string.recharge_fail);
                dismiss();
            }
        });
    }

    private void doRecharge(String num) {
        new MoneyModel().topUpInWx(num, new TribeCallback<WxPayResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<WxPayResponse>> response) {
                prepayid = response.body().data.prepayid;
                WXPayUtils.getInstance().doPay(response.body().data);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<WxPayResponse> body) {
                LoadingDialog.getInstance().dismissDialog();
                ToastUtils.ToastMessage(mContext, R.string.connect_fail);
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
    }
}
