package com.gs.buluo.app.model;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.RequestBodyBean.AuthorityRequest;
import com.gs.buluo.app.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.app.bean.RequestBodyBean.PhoneUpdateBody;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UploadAccessBody;
import com.gs.buluo.app.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressListResponse;
import com.gs.buluo.app.bean.ResponseBody.UserBeanResponse;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.network.BaseSubscriber;
import com.gs.buluo.app.network.MainApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.network.rxApis;

import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.MD5;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import retrofit2.Callback;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/3.
 */
public class MainModel {             //登录数据同步,上传，验证码
    public void doLogin(Map<String, String> params, Callback<UserBeanResponse> callback) {
        LoginBody bean = new LoginBody();
        bean.phone = params.get(Constant.PHONE);
        bean.verificationCode = params.get(Constant.VERIFICATION);
        TribeRetrofit.getInstance().createApi(MainApis.class).
                doLogin(bean).enqueue(callback);
    }
    //rxjava 登陆
    public void rxDoLogin(Map<String, String> params, Action1<UserBeanResponse> action1, BaseSubscriber<UserBeanResponse> subscriber) {
        LoginBody bean = new LoginBody();
        bean.phone = params.get(Constant.PHONE);
        bean.verificationCode = params.get(Constant.VERIFICATION);
        TribeRetrofit.getInstance().createApi(rxApis.class).
                doLogin(bean).
                subscribeOn(Schedulers.newThread()).
                subscribeOn(Schedulers.io()).
                doOnNext(action1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    public void doVerify(String phone, Callback<BaseResponse<CodeResponse>> callback) {
        TribeRetrofit.getInstance().createApi(MainApis.class).
                doVerify(new ValueRequestBody(phone)).enqueue(callback);
    }

    public void getUserInfo(String uid, Callback<BaseResponse<UserInfoEntity>> callback) {
        TribeRetrofit.getInstance().createApi(MainApis.class).
                getUser(uid).enqueue(callback);
    }

    public void updateUser(String id, String key, String value, CommonCallback<String> callback) {
        RequestParams params = new RequestParams(Constant.Base.BASE_URL + "persons/" + id + "/" + key);
        params.setHeader("Content-Type", "application/json");
        params.setHeader("Accept", "application/json");
        if (TribeApplication.getInstance().getUserInfo()!=null&&TribeApplication.getInstance().getUserInfo().getToken()!=null){
            params.setHeader("Authorization", TribeApplication.getInstance().getUserInfo().getToken());
        }
        params.setAsJsonContent(true);
        if (key.equals(Constant.AREA)) {
            String str[] = value.split("-");
            String province = str[0];
            String city = str[1];
            String district = str[2];
            params.addBodyParameter(Constant.PROVINCE, province);
            params.addBodyParameter(Constant.CITY, city);
            params.addBodyParameter(Constant.DISTRICT, district);
        } else {
            params.addBodyParameter(key, value);
        }
        x.http().request(HttpMethod.PUT, params, callback);
    }

    public void getAddressList(String uid, Callback<UserAddressListResponse> callback) {
        TribeRetrofit.getInstance().createApi(MainApis.class).
                getDetailAddressList(uid).enqueue(callback);
    }


    public void uploadFile(File file, String name, String type, Callback<UploadAccessResponse> callback) {
        UploadAccessBody body = new UploadAccessBody();
        body.key = name;
        body.contentType = type;
        try {
            body.contentMD5 = MD5.md5(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
//            body.contentMD5 = "98d8826e6308556a4a0ed87e265e2573";
        TribeRetrofit.getInstance().createApi(MainApis.class).
                getUploadUrl(TribeApplication.getInstance().getUserInfo().getId(), body).enqueue(callback);
    }

    public void doAuthentication(String name, String sex, long birthday, String idNo, Callback<BaseResponse<UserInfoEntity>> callback) {
        AuthorityRequest request = new AuthorityRequest();
        request.birthday = birthday + "";
        request.idNo = idNo;
        request.name = name;
        request.personSex = sex;
        TribeRetrofit.getInstance().createApi(MainApis.class).
                doAuthentication(TribeApplication.getInstance().getUserInfo().getId(), request).enqueue(callback);
    }

    public void updatePhone(String phone, String code, Callback<BaseResponse<CodeResponse>> callback) {
        PhoneUpdateBody body = new PhoneUpdateBody();
        body.phone = phone;
        body.verificationCode = code;
        TribeRetrofit.getInstance().createApi(MainApis.class).
                updatePhone(TribeApplication.getInstance().getUserInfo().getId(), body).enqueue(callback);
    }
}
