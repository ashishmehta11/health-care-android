package com.project.healthcare.api;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IApiCalls {
    @GET("facility/{state}/{city}")
    Call<JsonArray> getFacilities(@Path("state") String state, @Path("city") String city);
}
