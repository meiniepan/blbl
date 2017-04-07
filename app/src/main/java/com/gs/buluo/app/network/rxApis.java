package com.gs.buluo.app.network;

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

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Solang on 2017/3/29.
 */

public interface rxApis {

    @GET("persons/{id}")
    Observable<BaseResponse<UserInfoEntity>> getUser(
            @Path("id") String uid);

    @POST("persons/login")
    Observable<UserBeanResponse> doLogin(@Body LoginBody params);

    @POST("verifications/phone")
    Observable<BaseResponse<CodeResponse>> doVerify(@Body ValueRequestBody phone);



    @GET("persons/{id}/addresses")
    Observable<UserAddressListResponse> getDetailAddressList(
            @Path("id") String uid);

    @POST("oss_authorization/picture")
    Observable<UploadAccessResponse> getUploadUrl(@Query("me")String id, @Body UploadAccessBody body);

    @POST("persons/{id}/authentication")
    Observable<BaseResponse<UserInfoEntity>> doAuthentication(@Path("id") String id, @Body AuthorityRequest request);

    @PUT("persons/{id}/phone")
    Observable<BaseResponse<CodeResponse>> updatePhone(@Path("id") String id, @Body PhoneUpdateBody body);
}
