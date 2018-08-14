package com.user.etow.data.networking;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.user.etow.BuildConfig;
import com.user.etow.constant.Constant;
import com.user.etow.constant.KeyAPI;
import com.user.etow.data.prefs.DataStoreManager;
import com.user.etow.models.response.ApiResponse;
import com.user.etow.models.response.ApiSuccess;
import com.user.etow.models.response.EstimateCostResponse;

import java.util.concurrent.TimeUnit;

import io.realm.annotations.Ignore;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

public interface EtowService {

    class Creator {
        public static Retrofit newRetrofitInstance() {
            OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
            okBuilder.readTimeout(30, TimeUnit.SECONDS);
            okBuilder.connectTimeout(30, TimeUnit.SECONDS);
            okBuilder.retryOnConnectionFailure(true);
            okBuilder.addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder();
                builder.header(KeyAPI.KEY_AUTHORIZATION, DataStoreManager.getUserToken())
                        .method(original.method(), original.body());
                return chain.proceed(builder.build());
            });
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okBuilder.addInterceptor(interceptor);
            }
            Gson gson = new GsonBuilder()
                    .addDeserializationExclusionStrategy(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getAnnotation(Ignore.class) != null;
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    }).setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();

            return new Retrofit.Builder()
                    .baseUrl(Constant.HOST)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okBuilder.build())
                    .build();
        }
    }

    @FormUrlEncoded
    @POST("user/get-otp")
    Observable<ApiSuccess> getOTP(@Field(KeyAPI.KEY_PHONE) String phone);

    @FormUrlEncoded
    @POST("user/verify-otp")
    Observable<ApiSuccess> verifyOTP(@Field(KeyAPI.KEY_OTP) String otp);

    @FormUrlEncoded
    @POST("user/register")
    Observable<ApiResponse> register(@Field(KeyAPI.KEY_FULL_NAME) String fullName,
                                     @Field(KeyAPI.KEY_EMAIL) String email,
                                     @Field(KeyAPI.KEY_PASSWORD) String password,
                                     @Field(KeyAPI.KEY_PHONE) String phone);

    @FormUrlEncoded
    @POST("user/reset-password")
    Observable<ApiSuccess> resetPassword(@Field(KeyAPI.KEY_EMAIL) String email);

    @FormUrlEncoded
    @POST("user/login")
    Observable<ApiResponse> login(@Field(KeyAPI.KEY_EMAIL) String email,
                                  @Field(KeyAPI.KEY_PASSWORD) String password);

    @FormUrlEncoded
    @POST("user/update-profile")
    Observable<ApiResponse> updateProfile(@Field(KeyAPI.KEY_FULL_NAME) String fullName,
                                          @Field(KeyAPI.KEY_PHONE) String phone,
                                          @Field(KeyAPI.KEY_EMAIL) String email,
                                          @Field(KeyAPI.KEY_PASSWORD) String password,
                                          @Field(KeyAPI.KEY_AVATAR) String avatar);

    @POST("user/logout")
    Observable<ApiSuccess> logout();

    @FormUrlEncoded
    @POST("trip/get-price")
    Observable<EstimateCostResponse> getEstimateCost(@Field(KeyAPI.KEY_DISTANCE) String distance);

    @FormUrlEncoded
    @POST("trip/create")
    Observable<ApiResponse> createTrip(@Field(KeyAPI.KEY_TRIP) String tripInfor);

    @FormUrlEncoded
    @POST("feedback/send")
    Observable<ApiSuccess> sendFeedback(@Field(KeyAPI.KEY_COMMENT) String comment);

    @FormUrlEncoded
    @PUT("trip/update")
    Observable<ApiSuccess> updateTrip(@Field(KeyAPI.KEY_TRIP_ID) int tripId,
                                      @Field(KeyAPI.KEY_STATUS) String status);

    @GET("trip/get-setting-time")
    Observable<ApiResponse> getSetting();
}
