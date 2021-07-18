package com.project.healthcare.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IApiCalls {
    @GET("facility/{state}/{city}")
    Call<JsonObject> getFacilitiesByCity(@Path("state") String state, @Path("city") String city);

    @GET("facility/{id}")
    Call<JsonObject> getFacilityById(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @POST("facility-create")
    Call<JsonObject> registerFacility(@Body JsonObject body);


    @Headers("Content-Type: application/json")
    @POST("citizen-create")
    Call<JsonObject> registerCitizen(@Body JsonObject body);

    @POST("citizen-update")
    Call<JsonObject> updateCitizen(@Header("Authorization") String token, @Body JsonObject body);

    @POST("facility-update")
    Call<JsonObject> updateFacility(@Header("Authorization") String token, @Body JsonObject body);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<JsonObject> login(@Body JsonObject body);

    @GET("logout")
    Call<JsonObject> logout(@Header("Authorization") String token);

    @GET("delete")
    Call<JsonObject> delete(@Header("Authorization") String token);
}
