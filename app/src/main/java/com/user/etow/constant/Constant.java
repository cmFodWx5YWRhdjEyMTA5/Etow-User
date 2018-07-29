package com.user.etow.constant;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

public interface Constant {

    String SUCCESS = "success";
    String ERROR = "error";

    int FAIL_CONNECT_CODE = -1;
    int JSON_PARSER_CODE = -10;
    int OTHER_CODE = -20;

    int CODE_HTTP_203 = 203;
    int CODE_HTTP_204 = 204;
    int CODE_HTTP_300 = 300;
    int CODE_HTTP_401 = 401;
    int CODE_HTTP_409 = 409;
    int CODE_HTTP_410 = 410;
    int CODE_HTTP_411 = 411;
    int CODE_HTTP_412 = 412;
    int CODE_HTTP_413 = 413;
    int CODE_HTTP_421 = 421;
    int CODE_HTTP_507 = 507;
    int CODE_HTTP_510 = 510;

    String GENERIC_ERROR = "General error, please try again later";
    String SERVER_ERROR = "Fail to connect to server";

    String HOST_SCHEMA = "http://";
    String DOMAIN_NAME = "suusoft.com/eTow/public";
    String HOST = HOST_SCHEMA + DOMAIN_NAME + "/api/v1/";

    String TYPE_PAYMENT_CASH = "type_payment_cash";
    String TYPE_PAYMENT_CARD = "type_payment_card";

    // Key Intent
    String TYPE_PAYMENT = "TYPE_PAYMENT";
    String IS_TRIP_COMPLETED = "IS_TRIP_COMPLETED";
    String OBJECT_TRIP = "OBJECT_TRIP";
    String IS_DATE_SCHEDULED = "IS_DATE_SCHEDULED";
    String PHONE_NUMBER = "PHONE_NUMBER";
    String IS_UPDATE = "IS_UPDATE";
    String SCHEDULE_DATE = "SCHEDULE_DATE";
    String IS_VEHICLE_NORMAL = "IS_VEHICLE_NORMAL";
}
