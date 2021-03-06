package com.gs.buluo.app.model;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.RequestBodyBean.NewPaymentRequest;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BillResponse;
import com.gs.buluo.app.bean.ResponseBody.CardResponse;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.bean.WxPayResponse;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;

import java.util.List;

import retrofit2.Callback;

/**
 * Created by hjn on 2016/11/18.
 */
public class MoneyModel {
    public void getWelletInfo(String uid, Callback<BaseResponse<WalletAccount>> callback){
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getWallet(uid).enqueue(callback);
    }


    public void getBillList(String uid,String sortSkip, Callback<BillResponse> callback){
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getBillList(uid,"20",sortSkip).enqueue(callback);
    }

    public void getBillListFirst(String uid, Callback<BillResponse> callback){
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getBillListFirst(uid,"20").enqueue(callback);
    }

    public void getCardList(String uid, Callback<CardResponse> callback){
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getCardList(uid).enqueue(callback);
    }

    public void addBankCard(String uid, String vCode, BankCard card, Callback<BaseResponse<CodeResponse>> callback){
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                addBankCard(uid,vCode,card).enqueue(callback);
    }

    public void deleteCard(String id ,Callback<BaseResponse> callback) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                deleteCard(TribeApplication.getInstance().getUserInfo().getId(),id).enqueue(callback);
    }

    public void createPayment(String password, List<String> ids, String payChannel, String type, Callback<BaseResponse<OrderPayment>> callback) {
        NewPaymentRequest request=new NewPaymentRequest();
        request.orderIds=ids;
        request.payChannel=payChannel;
        request.password = password;
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                createPayment(TribeApplication.getInstance().getUserInfo().getId(),type,request).enqueue(callback);
    }

    public void getPaymentStatus(String payId, Callback<BaseResponse<OrderPayment>> callback) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getPaymentStatus(TribeApplication.getInstance().getUserInfo().getId(),payId).enqueue(callback);
    }

    public void topUpInWx(String price, Callback<BaseResponse<WxPayResponse>> callback){
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                payInWx(TribeApplication.getInstance().getUserInfo().getId(),new ValueRequestBody(price)).enqueue(callback);
    }

    public void getWXRechargeResult(String prepayId, Callback<BaseResponse<CodeResponse>> callback){
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getTopUpResult(TribeApplication.getInstance().getUserInfo().getId(),new ValueRequestBody(prepayId)).enqueue(callback);
    }
}
