package com.project.healthcare.data;

import java.util.ArrayList;

public class HealthFacility {
    private String name = "", address = "", state = "Andhra Pradesh", city, pinCode = "", password = "", establishmentDate = "";
    private ArrayList<String> phoneNumbers = new ArrayList<>();
    private ArrayList<String> emails = new ArrayList<>();
    private int completedStages = 0;

    public HealthFacility() {
        phoneNumbers.add("");
        emails.add("");
    }

    public HealthFacility(String name,
                          String address,
                          String state,
                          String city,
                          String pinCode,
                          String password,
                          String establishmentDate,
                          ArrayList<String> phoneNumbers,
                          ArrayList<String> emails) {
        this.name = name;
        this.address = address;
        this.state = state;
        this.city = city;
        this.pinCode = pinCode;
        this.password = password;
        this.establishmentDate = establishmentDate;
        this.phoneNumbers = phoneNumbers;
        this.emails = emails;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;

    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;

    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(String establishmentDate) {
        this.establishmentDate = establishmentDate;
    }


    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }


    public ArrayList<String> getEmails() {
        return emails;
    }

    public int getCompletedStages() {
        return completedStages;
    }

    public void setCompletedStages(int completedStages) {
        this.completedStages = completedStages;
    }
}