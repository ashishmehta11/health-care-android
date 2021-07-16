package com.project.healthcare.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.project.healthcare.R;
import com.project.healthcare.data.BaseData;
import com.project.healthcare.data.Citizen;
import com.project.healthcare.data.HealthFacility;
import com.project.healthcare.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class MainActivityViewModel extends AndroidViewModel {
    private final BaseData baseData;
    public static LinkedHashMap<String, ArrayList<String>> statesAndCities = new LinkedHashMap<>();
    private final MutableLiveData<String> selectedState;
    private final MutableLiveData<String> selectedCity;
    private final MutableLiveData<Integer> selectedBottomNumber;
    private final Application application;
    private HealthFacility healthFacility = new HealthFacility();
    private Citizen citizen = new Citizen();
    private ArrayList<HealthFacility> currentShowingList = new ArrayList<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        //new Color.)
        //application.getApplicationContext().getResources().getD
        baseData = new BaseData(AppCompatResources.getColorStateList(application.getApplicationContext(), R.color.blue)
                , AppCompatResources.getColorStateList(application.getApplicationContext(), R.color.transparent_white));
        buildStatesAndCities();
        selectedState = new MutableLiveData<>("Gujarat");
        selectedCity = new MutableLiveData<>(statesAndCities.get(selectedState.getValue()).get(0));
        selectedBottomNumber = new MutableLiveData<>();
    }

    public Application getApplication() {
        return application;
    }

    public void reSetSelectedCity() {
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
        new Utils(application.getApplicationContext());
    }

    public BaseData getBaseData() {
        return baseData;
    }

    public HashMap<String, ArrayList<String>> getStatesAndCities() {
        return statesAndCities;
    }

    public MutableLiveData<Integer> getSelectedBottomNumber() {
        return selectedBottomNumber;
    }

    public HealthFacility getHealthFacility() {
        return healthFacility;
    }

    public ArrayList<HealthFacility> getCurrentShowingList() {
        return currentShowingList;
    }

    public void setCurrentShowingList(ArrayList<HealthFacility> currentShowingList) {
        this.currentShowingList = currentShowingList;
    }

    public void setHealthFacility(HealthFacility healthFacility) {
        this.healthFacility = healthFacility;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }
}

