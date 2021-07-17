package com.project.healthcare.api;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.project.healthcare.data.Citizen;
import com.project.healthcare.data.HealthFacility;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class ApiCalls extends Observable {
    private static final ApiCalls instance = new ApiCalls();
    private static final String TAG = "ApiCalls";

    public static ApiCalls getInstance() {
        return instance;
    }

    /***
     * Call id : 1
     * @param state
     * @param city
     */
    public void getFacilitiesByCity(String state, String city) {
        final int callId = 1;
        state = state.replace(' ', '_');
        city = city.replace(' ', '_');
        Log.d(TAG, "facilities by state and city  called ");
        Call<JsonObject> call = ApiCallingObject.getApiCallObject().getFacilities(state, city);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                parseFacilityListResponse(response);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "facilities by state and city :onFailure: " + t.toString());
                setChanged();
                notifyObservers(new ApiCallReturnObjects(new ArrayList<HealthFacility>(), "Error", t.toString(), false, callId));
            }
        });

    }

    private void parseFacilityListResponse(Response<JsonObject> response) {
        printResponseLogs(response, "facilities by state and city :onResponse: ", "facilities by state and city :onResponse: body:", "facilities by state and city :onResponse:error body:");
        ArrayList<HealthFacility> list = new ArrayList<>();
        if (response.code() == 200) {
            try {
                JsonArray arr = response.body().getAsJsonArray("facilities");
                for (int i = 0; i < arr.size(); i++) {
                    JsonObject jObj = arr.get(i).getAsJsonObject();
                    HealthFacility hf = HealthFacility.fromJson(jObj);
                    list.add(hf);
                }
                setChanged();
                notifyObservers(new ApiCallReturnObjects(list, "", "", true, 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setChanged();
            requestNotSuccessful(response, 1);
        }
    }

    private void printResponseLogs(Response<JsonObject> response, String s, String s2, String s3) {
        Log.d(TAG, s + response.toString());
        if (response.body() != null)
            Log.d(TAG, s2 + response.body().toString());
        if (response.errorBody() != null) {
            Log.d(TAG, s3 + response.errorBody().toString());
        }
    }

    /*Log.d(TAG, "facilities by state and city :onResponse: " + response.toString());
                if (response.code() == 200) {
                    ArrayList<HealthFacility> list = new ArrayList<>();
                    for (int i = 0; i < response.body().length(); i++) {
                        try {
                            JSONObject jObj = response.body().getJSONObject(i);
                            HealthFacility hf = HealthFacility.fromJson(jObj);
                            list.add(hf);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setChanged();
                        notifyObservers(new ApiCallReturnObjects(list, "", "", true, 1));
                    }

                }



                  Log.d(TAG, "facilities by state and city :onFailure: " + t.toString());

     */

    /***
     * Call Id =2
     * @param facility
     */
    public void registerFacility(HealthFacility facility) {
        final int callId = 2;
        JsonObject data = HealthFacility.toJson(facility);
        Log.d(TAG, "facility register calling api : data :" + data.toString());
        Call<JsonObject> call = ApiCallingObject.getApiCallObject().registerFacility(data);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                printResponseLogs(response, "facility register :onResponse: ", "facility register :onResponse: body:", "facility register :onResponse:error body:");
                if (response.code() == 201) {
                    setChanged();
                    if (response.body() != null)
                        notifyObservers(new ApiCallReturnObjects(null, "Success", response.body().get("success").getAsString(), true, callId));
                    else
                        notifyObservers(new ApiCallReturnObjects(null, "Success", "user registered successfully", true, callId));
                } else {
                    setChanged();
                    requestNotSuccessful(response, 1);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "register facility :onFailure: " + t.toString());
                setChanged();
                notifyObservers(new ApiCallReturnObjects(null, "Error", t.toString(), false, callId));
            }
        });
    }


    /***
     * Call Id = 3
     * @param citizen
     */
    public void registerCitizen(Citizen citizen) {
        final int callId = 3;
        JsonObject data = citizen.toJson();
        Log.d(TAG, "registerCitizen: data: " + data.toString());
        Call<JsonObject> call = ApiCallingObject.getApiCallObject().registerCitizen(data);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                printResponseLogs(response, "facility citizen :onResponse: ", "facility citizen :onResponse: body:", "facility citizen :onResponse:error body:");
                if (response.code() == 201) {
                    setChanged();
                    notifyObservers(new ApiCallReturnObjects(null, "Success", response.body().get("success").getAsString(), false, callId));
                } else {
                    setChanged();
                    requestNotSuccessful(response, callId);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "register citizen :onFailure: " + t.toString());
                setChanged();
                notifyObservers(new ApiCallReturnObjects(null, "Error", t.toString(), false, callId));
            }
        });
    }


    /***
     * Call ID = 4
     * @param userName
     * @param password
     */
    public void login(String userName, String password) {
        final int callId = 4;
        JsonObject data = new JsonObject();
        data.addProperty("user_name", userName);
        data.addProperty("password", password);

        Call<JsonObject> call = ApiCallingObject.getApiCallObject().login(data);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                printResponseLogs(response, "facility citizen :onResponse: ", "facility citizen :onResponse: body:", "facility citizen :onResponse:error body:");
                if (response.code() == 200) {
                    setChanged();
                    JsonObject data = response.body();
                    notifyObservers(new ApiCallReturnObjects(getLoginResponseData(data), "Success", "Login successful", true, callId));
                } else {
                    setChanged();
                    requestNotSuccessful(response, callId);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "login :onFailure: " + t.toString());
                setChanged();
                notifyObservers(new ApiCallReturnObjects(null, "Error", t.toString(), false, callId));
            }
        });
    }

    private Object getLoginResponseData(JsonObject data) {
        if (data.get("group").getAsString().contains("citizen")) {
            return Citizen.fromJson(data);
        } else {
            HealthFacility facility = HealthFacility.fromJson(data.get("facility").getAsJsonObject());
            facility.setToken(data.get("token").getAsString());
            return facility;

        }
    }


    /***
     * Call ID =5
     * @param token
     */
    public void logout(String token) {
        final int callId = 5;
        Call<JsonObject> call = ApiCallingObject.getApiCallObject().logout(token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                printResponseLogs(response, "logout :onResponse: ", "logout :onResponse: body:", "logout :onResponse:error body:");
                if (response.code() == 200) {
                    setChanged();
                    notifyObservers(new ApiCallReturnObjects(null
                            , "Success", response.body().get("success").getAsString()
                            , true
                            , callId));
                } else {
                    setChanged();
                    requestNotSuccessful(response, callId);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "logout :onFailure: " + t.toString());
                setChanged();
                notifyObservers(new ApiCallReturnObjects(null, "Error", t.toString(), false, callId));
            }
        });
    }

    private void requestNotSuccessful(Response<JsonObject> response, int i) {
        if (response.errorBody() != null) {
            Converter<ResponseBody, JsonObject> converter = ApiCallingObject.getRetrofit()
                    .responseBodyConverter(JsonObject.class, new Annotation[0]);
            try {
                JsonObject errorResp = converter.convert(response.errorBody());
                if (errorResp.has("error"))
                    notifyObservers(new ApiCallReturnObjects(null, "Error",
                            errorResp.get("error").getAsString()
                            , false, i));
                else if (errorResp.has("detail"))
                    notifyObservers(new ApiCallReturnObjects(null, "Error",
                            errorResp.get("detail").getAsString()
                            , false, i));
                else
                    notifyObservers(new ApiCallReturnObjects(null, "Error", response.message(), false, i));
            } catch (IOException e) {
                Log.d(TAG, "requestNotSuccessful: exception trace :" + Arrays.toString(e.getStackTrace()));
                Log.d(TAG, "requestNotSuccessful: exception :" + e.getMessage());
                notifyObservers(new ApiCallReturnObjects(null, "Error",
                        response.message()
                        , false, i));
            }
        } else
            notifyObservers(new ApiCallReturnObjects(null, "Error", response.message(), false, i));
    }

    public static class ApiCallReturnObjects {
        private final Object data;
        private final String title;
        private final String text;
        private final boolean isSuccess;
        private final int callId;

        public ApiCallReturnObjects(Object data, String title, String text, boolean isSuccess, int callId) {
            this.data = data;
            this.title = title;
            this.text = text;
            this.isSuccess = isSuccess;
            this.callId = callId;
        }

        public Object getData() {
            return data;
        }

        public String getTitle() {
            return title;
        }

        public String getText() {
            return text;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public int getCallId() {
            return callId;
        }
    }
}
