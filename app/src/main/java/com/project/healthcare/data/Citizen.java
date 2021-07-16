package com.project.healthcare.data;

import androidx.databinding.BaseObservable;

import com.google.gson.JsonObject;

public class Citizen extends BaseObservable {
    private String userName = "";
    private String name = "";
    private String phoneNumber = "";
    private String password = "";
    private String token = "";
    private String id = "";



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static Citizen fromJson(JsonObject data) {
        Citizen citizen = new Citizen();
        if (data.has("id"))
            citizen.setId(data.get("id").getAsString());
        if (data.has("user_name"))
            citizen.setUserName(data.get("user_name").getAsString());
        if (data.has("name"))
            citizen.setName(data.get("name").getAsString());
        if (data.has("phone_number"))
            citizen.setPhoneNumber(data.get("phone_number").getAsString());
        if (data.has("password"))
            citizen.setPassword(data.get("password").getAsString());
        if (data.has("token"))
            citizen.setToken(data.get("token").getAsString());
        return citizen;
    }

    public String getId() {
        return id;
    }

    public JsonObject toJson() {
        JsonObject data = new JsonObject();
        data.addProperty("user_name", getUserName());
        data.addProperty("name", getName());
        data.addProperty("password", getPassword());
        data.addProperty("phone_number", getPhoneNumber());
        return data;
    }

    public void setId(String id) {
        this.id = id;
    }
}
