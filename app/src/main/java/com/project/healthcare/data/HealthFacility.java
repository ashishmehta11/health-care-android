package com.project.healthcare.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class HealthFacility {
    private String id = "";
    private String name = "", address = "", state = "Andhra Pradesh", city = "", pinCode = "", password = "", establishmentDate = "", managedByName = "", avgPrice = "", about = "";
    private ManagedBy managedBy;
    private ArrayList<String> phoneNumbers = new ArrayList<>();
    private ArrayList<String> emails = new ArrayList<>();
    private int completedStages = 0;
    private ArrayList<FacilityType> affiliations = new ArrayList<>();
    private ArrayList<SpecialityType> specialities = new ArrayList<>();
    private ArrayList<Object> services = new ArrayList<>();

    public HealthFacility() {
        phoneNumbers.add("");
        emails.add("");
    }

    public HealthFacility(String name, String address, String state, String city, String pinCode, String password, String establishmentDate, ArrayList<String> phoneNumbers, ArrayList<String> emails, ArrayList<FacilityType> typeOfFacility, ArrayList<SpecialityType> specialities, ArrayList<Object> services) {
        this.name = name;
        this.address = address;
        this.state = state;
        this.city = city;
        this.pinCode = pinCode;
        this.password = password;
        this.establishmentDate = establishmentDate;
        this.phoneNumbers = phoneNumbers;
        this.emails = emails;
        this.affiliations = typeOfFacility;
        this.specialities = specialities;
        this.services = services;
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

    public static HealthFacility fromJson(JsonObject data) {
        HealthFacility hf = new HealthFacility();
        hf.setId(data.get("id").getAsString());
        hf.setName(data.get("name").getAsString());
        hf.getEmails().set(0, data.get("user_name").getAsString());
        String[] emails = data.get("emails").getAsString().split(";");
        for (String email : emails) {
            hf.getEmails().add(email);
        }
        String[] phoneNumbers = data.get("contact_numbers").getAsString().split(";");
        hf.getPhoneNumbers().clear();
        for (String phone : phoneNumbers) {
            hf.getPhoneNumbers().add(phone);
        }
        hf.setAddress(data.get("address").getAsString());
        hf.setAbout(data.get("about").getAsString());
        hf.setCity(data.get("city").getAsString());
        hf.setState(data.get("state").getAsString());
        hf.setPinCode(data.get("pin_code").getAsString());
        hf.setAvgPrice(data.get("avg_fees").getAsString());
        for (JsonElement s : data.get("affiliations").getAsJsonArray()) {
            hf.getAffiliations().add(FacilityType.fromString(s.getAsString()));
        }
        JsonObject ownership = data.get("ownership").getAsJsonObject();
        hf.setManagedBy(ManagedBy.fromString(ownership.get("id").getAsString()));
        hf.setManagedByName(ownership.get("name").getAsString());
        for (JsonElement s : data.get("speciality").getAsJsonArray()) {
            hf.getSpecialities().add(SpecialityType.fromString(s.getAsString()));
        }
        return hf;
    }

    public ArrayList<SpecialityType> getSpecialities() {
        return specialities;
    }

    public ArrayList<Object> getServices() {
        return services;
    }

    public String getManagedByName() {
        return managedByName;
    }

    public void setManagedByName(String managedByName) {
        this.managedByName = managedByName;
    }

    public ManagedBy getManagedBy() {
        return managedBy;
    }

    public void setManagedBy(ManagedBy managedBy) {
        this.managedBy = managedBy;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public static JsonObject toJson(HealthFacility facility) {
        JsonObject json = new JsonObject();
        json.addProperty("user_name", facility.getEmails().get(0));
        json.addProperty("name", facility.getName());
        json.addProperty("address", facility.getAddress());
        json.addProperty("about", facility.getAbout());
        json.addProperty("city", facility.getCity());
        json.addProperty("state", facility.getState());
        json.addProperty("pin_code", facility.getPinCode());
        json.addProperty("password", facility.getPassword());
        String num = "";
        for (int i = 0; i < facility.getPhoneNumbers().size(); i++) {
            if (i == facility.getPhoneNumbers().size() - 1)
                num += facility.getPhoneNumbers().get(i);
            else {
                num += facility.getPhoneNumbers().get(i) + ";";
            }
        }
        json.addProperty("contact_number", num);
        num = "";
        for (int i = 0; i < facility.getEmails().size(); i++) {
            if (i == facility.getEmails().size() - 1)
                num += facility.getEmails().get(i);
            else {
                num += facility.getEmails().get(i) + ";";
            }
        }
        if (num.isEmpty())
            num = null;
        json.addProperty("emails", num);
        json.addProperty("avg_fees", facility.getAvgPrice());
        JsonArray arr = new JsonArray();
        for (FacilityType t : facility.getAffiliations()) arr.add(FacilityType.toString(t));
        json.add("affiliations", arr);
        arr = new JsonArray();
        for (SpecialityType t : facility.getSpecialities()) arr.add(SpecialityType.toString(t));
        json.add("speciality", arr);
        JsonObject o = new JsonObject();
        o.addProperty("id", ManagedBy.toString(facility.getManagedBy()));
        o.addProperty("name", facility.getManagedByName());
        json.add("ownership", o);
        return json;
    }

    public ArrayList<FacilityType> getAffiliations() {
        return affiliations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
