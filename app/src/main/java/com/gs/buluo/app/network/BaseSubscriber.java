package com.gs.buluo.app.network;


import android.util.Log;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.utils.ToastUtils;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by hjn on 2017/3/20.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {
    private static final String TAG = "Network";

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            //http error
            if (((HttpException) e).code() == 401) {  //token 过期，重新登录
                ToastUtils.ToastMessage(TribeApplication.getInstance().getApplicationContext(), R.string.login_again);
            }
        } else if (e instanceof IOException) {
            ToastUtils.ToastMessage(TribeApplication.getInstance().getApplicationContext(), R.string.convert_fail);
        } else if (e instanceof ApiException) {  //服务器返回异常
            ApiException exception = (ApiException) e;
            Log.e(TAG, "onError:  " + ((ApiException) e).getDisplayMessage());
            Log.e(TAG, "onError:  " + (exception.getCode()));

            if (exception.getCode() == 409) {
                //forbidden

            } else if (exception.getCode() == 404) {
                //not find
            } else if (exception.getCode() == 401) {
                ToastUtils.ToastMessage(TribeApplication.getInstance().getApplicationContext(), R.string.wrong_verify);
            } else {
                ToastUtils.ToastMessage(TribeApplication.getInstance().getApplicationContext(), R.string.connect_fail);
            }
        }
    }

    @Override
    public void onNext(T t) {

    }
}
