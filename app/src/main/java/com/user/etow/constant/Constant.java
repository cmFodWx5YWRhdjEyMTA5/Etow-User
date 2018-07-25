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

    String GENERIC_ERROR = "General error, please try again later";
    String SERVER_ERROR = "Fail to connect to server";

    String HOST_SCHEMA = "http://";
    String DOMAIN_NAME = "suusoft.com/eTow/public";
    String HOST = HOST_SCHEMA + DOMAIN_NAME + "/api/v1/";

    String TYPE_PAYMENT_CASH = "type_payment_cash";
    String TYPE_PAYMENT_CARD = "type_payment_card";

    String CURRENT_LOCATION = "CURRENT_LOCATION";

    // Key Intent
    String TYPE_PAYMENT = "TYPE_PAYMENT";
    String IS_TRIP_COMPLETED = "IS_TRIP_COMPLETED";
    String OBJECT_TRIP = "OBJECT_TRIP";
    String IS_DATE_SCHEDULED = "IS_DATE_SCHEDULED";
    String DATE_BOOKING = "DATE_BOOKING";
    String PHONE_NUMBER = "PHONE_NUMBER";
}
