package com.project.healthcare.api;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.project.healthcare.data.HealthFacility;

import java.util.ArrayList;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCalls extends Observable {
    private static final ApiCalls instance = new ApiCalls();
    private static final String TAG = "ApiCalls";

    public static ApiCalls getInstance() {
        return instance;
    }

    // Call id : 1
    public void getFacilitiesByCity(String state, String city) {
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
                notifyObservers(new ApiCallReturnObjects(new ArrayList<HealthFacility>(), "Error", t.toString(), false, 1));
            }
        });
    }

    private void parseFacilityListResponse(Response<JsonObject> response) {
        Log.d(TAG, "facilities by state and city :onResponse: " + response.toString());
        if (response.body() != null)
            Log.d(TAG, "facilities by state and city :onResponse: body:" + response.body().toString());
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
            notifyObservers(new ApiCallReturnObjects(list, "Error", "Not found", false, 1));
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

    public void registerFacility(HealthFacility facility) {
        JsonObject data = HealthFacility.toJson(facility);
        Log.d(TAG, "facility register calling api : data :" + data.toString());
        Call<JsonObject> call = ApiCallingObject.getApiCallObject().registerFacility(data);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "facility register :onResponse: " + response.toString());
                if (response.body() != null)
                    Log.d(TAG, "facility register :onResponse: body:" + response.body().toString());
                if (response.code() == 200) {
                    setChanged();
                    notifyObservers(new ApiCallReturnObjects(null, "Success", "Registered Successfully", true, 2));
                } else if (response.code() == 400) {
                    setChanged();
                    notifyObservers(new ApiCallReturnObjects(null, "Error", "Fill form correctly", false, 2));
                } else {
                    setChanged();
                    notifyObservers(new ApiCallReturnObjects(null, "Error", response.message(), false, 2));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "register facility :onFailure: " + t.toString());
                setChanged();
                notifyObservers(new ApiCallReturnObjects(null, "Error", t.toString(), false, 2));
            }
        });
    }

    public static class ApiCallReturnObjects {
        private final Object data;
        private final String failureTitle;
        private final String failureText;
        private final boolean isSuccess;
        private final int callId;

        public ApiCallReturnObjects(Object data, String failureTitle, String failureText, boolean isSuccess, int callId) {
            this.data = data;
            this.failureTitle = failureTitle;
            this.failureText = failureText;
            this.isSuccess = isSuccess;
            this.callId = callId;
        }

        public Object getData() {
            return data;
        }

        public String getFailureTitle() {
            return failureTitle;
        }

        public String getFailureText() {
            return failureText;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public int getCallId() {
            return callId;
        }
    }
}
