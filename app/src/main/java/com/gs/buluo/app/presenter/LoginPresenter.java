package com.gs.buluo.app.presenter;

import android.text.TextUtils;
import android.widget.Button;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressListResponse;
import com.gs.buluo.app.bean.ResponseBody.UserBeanResponse;
import com.gs.buluo.app.bean.SipBean;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.eventbus.SelfEvent;
import com.gs.buluo.app.model.MainModel;
import com.gs.buluo.app.network.BaseSubscriber;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.triphone.LinphoneManager;
import com.gs.buluo.app.triphone.LinphonePreferences;
import com.gs.buluo.app.triphone.LinphoneUtils;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.view.impl.ILoginView;

import org.greenrobot.eventbus.EventBus;
import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    private final MainModel mainModel;
    private String token;

    public LoginPresenter() {
        mainModel = new MainModel();
    }

    public void doLogin(Map<String, String> params, final Button button) {
        //wbn
        setButtonFalse(button);
        mainModel.rxDoLogin(params, new Action1<UserBeanResponse>() {
            @Override
            public void call(UserBeanResponse user) {

                doOnLogin(user);

            }
        }, new BaseSubscriber<UserBeanResponse>() {
            @Override
            public void onCompleted() {
                setButtonTrue(button);
            }

            @Override
            public void onError(Throwable e) {
                setButtonTrue(button);
                super.onError(e);
            }

            @Override
            public void onNext(UserBeanResponse userBeanResponse) {

            }
        });


    }

    private void doOnLogin(UserBeanResponse user) {
        String uid = user.getData().getAssigned();
        token = user.getData().getToken();
        UserInfoEntity entity = new UserInfoEntity();
        entity.setId(uid);
        entity.setToken(token);
        TribeApplication.getInstance().setUserInfo(entity);
        getUserInfo(uid);
        getAddressInfo(uid);
    }

    private void setButtonFalse(Button button) {
        button.setClickable(false);
        button.setText(R.string.sign_in_ing);
    }

    private void setButtonTrue(Button button) {
        button.setText(R.string.sign_in);
        button.setClickable(true);
    }

    public void doVerify(String phone) {
        mainModel.doVerify(phone, new Callback<BaseResponse<CodeResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CodeResponse>> call, Response<BaseResponse<CodeResponse>> response) {
                if (response.body() != null) {
                    BaseResponse res = response.body();
                    if (res.code == 202) {
                        if (isAttach()) mView.dealWithIdentify(202);
                    } else {
                        if (isAttach()) mView.dealWithIdentify(400);
                    }
                } else {
                    if (isAttach()) mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CodeResponse>> call, Throwable t) {
                if (isAttach()) mView.showError(R.string.connect_fail);
            }
        });
    }

    public void getUserInfo(String uid) {
        mainModel.getUserInfo(uid, new TribeCallback<UserInfoEntity>() {
            @Override
            public void onSuccess(Response<BaseResponse<UserInfoEntity>> response) {
                UserInfoEntity entity = response.body().data;
                entity.setSipJson();
                if (!CommonUtils.isLibc64()) {
                    SipBean sip = entity.getSip();
                    if (sip != null)
                        saveCreatedAccount(sip.user, sip.password, null, null, sip.domain, LinphoneAddress.TransportType.LinphoneTransportUdp);
                }
                entity.setToken(token);
                if (entity.getDistrict() != null)
                    entity.setArea(entity.getProvince() + "-" + entity.getCity() + "-" + entity.getDistrict());
                else
                    entity.setArea(entity.getProvince() + "-" + entity.getCity());

                TribeApplication.getInstance().setUserInfo(entity);
                UserInfoDao dao = new UserInfoDao();
                dao.saveBindingId(entity);
                EventBus.getDefault().post(new SelfEvent());
                if (isAttach()) {
                    mView.loginSuccess();
                }
            }

            @Override
            public void onFail(int responseCode, BaseResponse<UserInfoEntity> body) {
                if (isAttach()) {
                    mView.showError(R.string.connect_fail);
                }
            }
        });
    }

    private void getAddressInfo(String assigned) {
        mainModel.getAddressList(assigned, new Callback<UserAddressListResponse>() {
            @Override
            public void onResponse(Call<UserAddressListResponse> call, Response<UserAddressListResponse> response) {
                if (response != null && response.body() != null) {
                    List<UserAddressEntity> list = response.body().data;
                    AddressInfoDao dao = new AddressInfoDao();
                    for (UserAddressEntity address : list) {
                        address.setUid(TribeApplication.getInstance().getUserInfo().getId());
                        address.setArea(address.getProvice(), address.getCity(), address.getDistrict());
                        dao.saveBindingId(address);
                    }
                } else {
                    mView.showError(R.string.connect_fail);
                }

            }

            @Override
            public void onFailure(Call<UserAddressListResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    private void saveCreatedAccount(String username, String password, String prefix, String ha1, String domain, LinphoneAddress.TransportType transport) {
        username = LinphoneUtils.getDisplayableUsernameFromAddress(username);
        domain = LinphoneUtils.getDisplayableUsernameFromAddress(domain);

        String identity = "sip:" + username + "@" + domain;
        try {
            LinphoneAddress address = LinphoneCoreFactory.instance().createLinphoneAddress(identity);
        } catch (LinphoneCoreException e) {
            org.linphone.mediastream.Log.e(e);
        }

        LinphonePreferences.AccountBuilder builder = new LinphonePreferences.AccountBuilder(LinphoneManager.getLc())
                .setUsername(username)
                .setDomain(domain)
                .setHa1(ha1)
                .setPassword(password);

        if (prefix != null) {
            builder.setPrefix(prefix);
        }
        String forcedProxy = "";
        if (!TextUtils.isEmpty(forcedProxy)) {
            builder.setProxy(forcedProxy)
                    .setOutboundProxyEnabled(true)
                    .setAvpfRRInterval(5);
        }

        if (transport != null) {
            builder.setTransport(transport);
        }

//        if (getResources().getBoolean(R.bool.enable_push_id)) {
//            String regId = mPrefs.getPushNotificationRegistrationID();
//            String appId = getString(R.string.push_sender_id);
//            if (regId != null && mPrefs.isPushNotificationEnabled()) {
//                String contactInfos = "app-id=" + appId + ";pn-type=google;pn-tok=" + regId;
//                builder.setContactParameters(contactInfos);
//            }
//        }

        try {
            builder.saveNewAccount();
//            if(!newAccount) {
//                displayRegistrationInProgressDialog();
//            }
//            accountCreated = true;
        } catch (LinphoneCoreException e) {
            org.linphone.mediastream.Log.e(e);
        }
    }
}
