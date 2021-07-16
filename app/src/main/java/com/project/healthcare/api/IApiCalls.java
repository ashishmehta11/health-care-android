package com.project.healthcare.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IApiCalls {
    @GET("facility/{state}/{city}")
    Call<JsonObject> getFacilities(@Path("state") String state, @Path("city") String city);

    @Headers("Content-Type: application/json")
    @POST("facility-create")
    Call<JsonObject> registerFacility(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("citizen-create")
    Call<JsonObject> registerCitizen(@Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<JsonObject> login(@Body JsonObject body);
}
