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

    int CODE_HTTP_303 = 303;
    int CODE_HTTP_304 = 304;
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

    /*String HOST_SCHEMA = "http://";
    String DOMAIN_NAME = "suusoft.com/eTow/public";*/
    String HOST_SCHEMA = "http://";
    String DOMAIN_NAME = "167.99.195.131/eTow/public";
    String HOST = HOST_SCHEMA + DOMAIN_NAME + "/api/v1/";

    String TYPE_PAYMENT_CASH = "cash";
    String TYPE_PAYMENT_CARD = "card";

    String TYPE_VEHICLE_NORMAL = "normal";
    String TYPE_VEHICLE_FLATBED = "flatbed";

    int IS_SCHEDULE = 1;
    int IS_NOT_SCHEDULE = 0;

    String SCHEDULE_TRIP_STATUS_COMPLETED = "8_1";

    String TYPE_DRIVER = "1";
    String TYPE_USER = "2";

    int IS_DRIVER_FREE = 1;

    // Key Intent
    String TYPE_PAYMENT = "TYPE_PAYMENT";
    String IS_TRIP_COMPLETED = "IS_TRIP_COMPLETED";
    String OBJECT_TRIP = "OBJECT_TRIP";
    String IS_SCHEDULE_TRIP = "IS_SCHEDULE_TRIP";
    String PHONE_NUMBER = "PHONE_NUMBER";
    String VERIFICATION_ID = "mVerificationId";
    String IS_UPDATE = "IS_UPDATE";
    String SCHEDULE_DATE = "SCHEDULE_DATE";
    String IS_VEHICLE_NORMAL = "IS_VEHICLE_NORMAL";
    String GO_TO_UPCOMING_TRIPS = "GO_TO_UPCOMING_TRIPS";

    int TRIP_STATUS_NEW = 1; // Khi user mới booking trip
    int TRIP_STATUS_CANCEL = 2; // Khi user cancel trip
    int TRIP_STATUS_REJECT = 3; // Khi driver reject trip
    int TRIP_STATUS_ACCEPT = 4; // Khi driver accept trip
    int TRIP_STATUS_ARRIVED = 5; // Khi driver đến pick up location
    int TRIP_STATUS_ON_GOING = 6; // Khi driver dang di đến drop off location
    int TRIP_STATUS_JOURNEY_COMPLETED = 7; // Khi driver đến drop off location
    int TRIP_STATUS_COMPLETE = 8; // Khi thanh toán xong

    String PAYMENT_STATUS_PAYMENT_SUCCESS = "success"; // Thanh toán thanh cong
    String PAYMENT_STATUS_PAYMENT_PENDING = "pending"; // Chua thanh toán
    String PAYMENT_STATUS_PAYMENT_FAIL = "fail"; // Thanh toán that bai
}
