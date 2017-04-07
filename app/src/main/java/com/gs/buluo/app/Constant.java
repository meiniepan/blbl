package com.gs.buluo.app;

import android.os.Environment;

/**
 * Created by admin on 2016/11/1.
 */
public class Constant {
    public static final String VCODE = "vcode";
    public static final String WIFI = "Wi-Fi";
    public static final String UPDATE_TIME = "UPDATE_TIME";
    public static final String APK_URL = "http://files.buluo-gs.com/packages/app-tribalc-1.0.1.apk";
    public static final String DIR_PATH = Environment.getExternalStorageDirectory().toString() + "/tribe/";
    public static final String DOOR = "door";
    public static final String DOOR_LIST = "door_list";
    public static final String RE_LOGIN = "re_login";
    public static final String LOADING = "登陆中...";
    public static final String LOAD = "登陆";

    public final static class Base{
        public static final String BASE="https://app-services.buluo-gs.com/";
        public static final String BASE_URL="https://dev-app-services.buluo-gs.com:443/tribalc/v1.0/";
//        public static final String BASE_URL="https://dev-app-services.buluo-gs.com:443/tribalc/v1.0/";
//        public static final String BASE_URL="http://123.56.251.131:10086/trib.alc/v1.0/";
        public static final String BASE_IMG_URL="http://dev-app-services.buluo-gs.com/resources/";   //图片地址要加此前缀
        public static final String BASE_ALI_URL="http://buluo-gs-pictures.oss-cn-beijing.aliyuncs.com/";
//        public static final String BASE_ALI_URL="http://http://pictures.buluo-gs.com/";  //阿里云图片地址base
        public static final String WX_ID = "wx1906c6844a4273e2";
    }
    public static final String VERIFICATION = "verificationCode";
    public static final String NICKNAME = "nickname";
    public static final String SEX = "sex";
    public static final String BIRTHDAY = "birthday";
    public static final String EMOTION = "emotion";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String DISTRICT = "district";
    public static final String AREA = "province,city,district";
    public static final String PICTURE = "picture";
    public static final String PHONE = "phone";
    public static final String MALE = "MALE";
    public static final String FEMALE = "FEMALE";
    public static final String SINGLE = "SINGLE";
    public static final String MARRIED = "MARRIED";
    public static final String LOVE = "LOVE";
    public static final String ADDRESS = "address";
    public static final String ADDRESS_ID = "address_id";
    public static final String WALLET_PWD = "wallet_pwd";
    public static final String OLD_PWD = "old_pwd";


    public static final String BILL = "bill_entity";
    public static final String GOODS_ID = "goods_id";
    public static final String ORDER = "order";
    public static final String ORDER_STATUS = "status";
    public static final int REQUEST_ADDRESS = 208;
    public static final String RECEIVER = "receiver";

    public static final String PRICE = "price";
    public static final String TYPE = "type";
    public static final String COMMUNITY_ID = "community_id";
    public static final String COMMUNITY_NAME = "community_name";
    public static final String PROPERTY_MANAGEMENT="property_management";


    public static final String REPAST = "repast";
    public static final String ENTERTAINMENT = "entertainment";
    public static final String ENTERTAINMENT_ALL = "HAIRDRESSING,FITNESS,ENTERTAINMENT,KEEPHEALTHY";
    public static final String SORT_POPULAR = "popularValue,desc";
    public static final String SORT_PERSON_EXPENSE_DESC = "personExpense,desc";
    public static final String SORT_PERSON_EXPENSE_ASC = "personExpense,asc";
    public static final String SORT_COORDINATE_DESC = "coordinate,desc";
    public static final String SERVE_ID = "serve_id";
    public static final String STORE_ID = "store_id";


    public final static class ForIntent{
        public static final int REQUEST_CODE=0;
        public static final int RESULT_CODE=1;
        public static final String FLAG="flag";
        public static final String FROM_ORDER = "fromOrder";
        public static final String COMPANY_FLAG = "company_info";
        public static final String PROPERTY_BEEN = "property_been";
        public static final String MODIFY = "self_modify";
        public static final String COORDINATE = "coordinate";
        public static final String FANCILITY = "facility";
        public static final String SERVE_POSITION = "serve_position";
    }

}
