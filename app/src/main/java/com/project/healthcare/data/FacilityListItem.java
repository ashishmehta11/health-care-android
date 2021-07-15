package com.project.healthcare.data;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class FacilityListItem extends BaseObservable {

    private final String[] emails;
    private final String[] contacts;
    private final String address;
    private final String name;

    public FacilityListItem(String[] emails, String[] contacts, String address, String name) {
        this.emails = emails;
        this.contacts = contacts;
        this.address = address;
        this.name = name;
    }

    @Bindable
    public String[] getEmails() {
        return emails;
    }

    @Bindable
    public String[] getContacts() {
        return contacts;
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    @Bindable
    public String getName() {
        return name;
    }
}
