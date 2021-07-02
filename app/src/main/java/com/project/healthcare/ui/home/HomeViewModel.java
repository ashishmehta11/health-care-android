package com.project.healthcare.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.project.healthcare.data.BaseData;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeViewModel extends AndroidViewModel {
    private final BaseData baseData;
    private HashMap<String, ArrayList<String>> statesAndCities = new HashMap<>();
    private MutableLiveData<String> selectedState;
    private MutableLiveData<String> selectedCity;
    public HomeViewModel(@NonNull Application application) {
        super(application);
        baseData = new BaseData();
        buildStatesAndCities();
        selectedState = new MutableLiveData<>("Gujarat");
        selectedCity = new MutableLiveData<>(statesAndCities.get(selectedState.getValue()).get(0));
    }

    public void reSetSelectedCity()
    {
        this.selectedCity.setValue(statesAndCities.get(selectedState.getValue()).get(0));
    }


    public MutableLiveData<String> getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(String selectedState) {
        this.selectedState.setValue(selectedState);
    }

    public MutableLiveData<String> getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        this.selectedCity.setValue(selectedCity);
    }

    private void buildStatesAndCities() {
        statesAndCities.put("Gujarat",
                new ArrayList<String>(){
                    {
                        add("Ahmedabad");
                        add("Gandhinagar");
                        add("Rajakot");
                        add("Vadodra");
                        add("Surat");
                        add("Mehsana");
                    }
                });

        statesAndCities.put("Maharashtra",
                new ArrayList<String>(){
                    {
                        add("Mumbai");
                        add("Thane");
                        add("Pune");
                        add("Lonawala");
                    }
                });

        statesAndCities.put("Rajasthan",
                new ArrayList<String>(){
                    {
                        add("Jaipur");
                        add("Udaipur");
                    }
                });

    }

    public BaseData getBaseData() {
        return baseData;
    }

    public HashMap<String, ArrayList<String>> getStatesAndCities() {
        return statesAndCities;
    }
}
