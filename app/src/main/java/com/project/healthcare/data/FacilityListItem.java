package com.project.healthcare.data;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class FacilityListItem extends BaseObservable {

    private String[] emails, contacts;
    private String address;

    public FacilityListItem(String[] emails, String[] contacts, String address) {
        this.emails = emails;
        this.contacts = contacts;
        this.address = address;
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
}
