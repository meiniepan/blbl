package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.RequestBodyBean.CommonRequestBody;
import com.gs.buluo.app.bean.RequestBodyBean.NewReserveRequest;
import com.gs.buluo.app.bean.RequestBodyBean.ReserveRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.ReserveDetailResponse;
import com.gs.buluo.app.bean.ResponseBody.ReserveResponse;
import com.gs.buluo.app.bean.ResponseBody.ServeDetailResponse;
import com.gs.buluo.app.bean.ResponseBody.ServeResponse;
import com.gs.buluo.app.bean.ResponseBody.SimpleCodeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hjn on 2016/11/11.
 */
public interface ReserveService {
    @GET("reservations?type=owner")
    Call<ReserveResponse> getReserviceList(
//            @Query("status") String status,
            @Query("limitSize") int limitSize,
            @Query("me") String myId ,
            @Query("sortSkip") String sortSkip);

    @GET("reservations?type=owner")
    Call<ReserveResponse> getReserviceListFirst(
//            @Query("status") String status,
            @Query("me") String myId ,
            @Query("limitSize") int limitSize);


    @GET("reservations/{id}")
    Call<ReserveDetailResponse> getReserveDetail(@Path("id") String reserveId, @Query("me") String myId);

    @PUT("reservations/{id}/status")
    Call<SimpleCodeResponse> cancelReserve(@Path("id")String id,@Query("me") String myId, @Body ReserveRequestBody body);

    @POST("reservations")
    Call<SimpleCodeResponse> createReserve(@Query("me")String myId, @Body NewReserveRequest body);
}

