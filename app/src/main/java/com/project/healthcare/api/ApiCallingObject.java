package com.project.healthcare.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiCallingObject {
    public final static String BASE_URL = "http://192.168.0.101:8000/api/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .client(new OkHttpClient().newBuilder().retryOnConnectionFailure(true).connectTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS).build())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static IApiCalls iApiCalls = retrofit.create(IApiCalls.class);

    public static IApiCalls getApiCallObject() {
        if (iApiCalls == null) {
            if (retrofit == null)
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            iApiCalls = retrofit.create(IApiCalls.class);
        }
        return iApiCalls;
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        return retrofit;
    }
}

