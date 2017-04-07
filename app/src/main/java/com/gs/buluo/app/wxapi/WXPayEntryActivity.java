package com.gs.buluo.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.eventbus.TopupEvent;
import com.gs.buluo.app.view.widget.LoadingDialog;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hjn on 2016/12/29.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.Base.WX_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        if(baseReq.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){

        }
        LoadingDialog.getInstance().dismissDialog();
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        EventBus.getDefault().post(new TopupEvent());
        LoadingDialog.getInstance().dismissDialog();
        finish();
    }
}
