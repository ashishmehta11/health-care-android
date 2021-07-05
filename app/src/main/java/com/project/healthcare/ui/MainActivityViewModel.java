package com.project.healthcare.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.project.healthcare.R;
import com.project.healthcare.data.BaseData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class MainActivityViewModel extends AndroidViewModel {
    private final BaseData baseData;
    private LinkedHashMap<String, ArrayList<String>> statesAndCities = new LinkedHashMap<>();
    private MutableLiveData<String> selectedState;
    private MutableLiveData<String> selectedCity;
    private Application application;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        //new Color.)
        baseData = new BaseData(AppCompatResources.getColorStateList(application.getApplicationContext(), R.color.blue)
                , AppCompatResources.getColorStateList(application.getApplicationContext(), R.color.transparent_white));
        buildStatesAndCities();
        selectedState = new MutableLiveData<>("Gujarat");
        selectedCity = new MutableLiveData<>(statesAndCities.get(selectedState.getValue()).get(0));
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
        statesAndCities.putIfAbsent("Gujarat",
                new ArrayList<String>() {
                    {
                        add("Ahmedabad");
                        add("Gandhinagar");
                        add("Rajakot");
                        add("Vadodra");
                        add("Surat");
                        add("Mehsana");
                    }
                });

        statesAndCities.putIfAbsent("Maharashtra",
                new ArrayList<String>() {
                    {
                        add("Mumbai");
                        add("Thane");
                        add("Pune");
                        add("Lonawala");
                        add("Lonawala");
                    }
                });

        statesAndCities.putIfAbsent("Rajasthan",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
                    }
                });

        statesAndCities.putIfAbsent("Uttar Pradesh",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
                    }
                });

        statesAndCities.putIfAbsent("Delhi",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
                    }
                });

        statesAndCities.putIfAbsent("Uttrakhand",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
                    }
                });

        statesAndCities.putIfAbsent("Goa",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
                    }
                });

        statesAndCities.putIfAbsent("Bihar",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
                    }
                });

        statesAndCities.putIfAbsent("Karnataka",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
                    }
                });

        statesAndCities.putIfAbsent("Karnatak",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
                    }
                });

        statesAndCities.putIfAbsent("Karnata",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
                    }
                });

        statesAndCities.putIfAbsent("Karnat",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
                    }
                });

        statesAndCities.putIfAbsent("Karna",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
                    }
                });

        statesAndCities.putIfAbsent("Tamil Nadu",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
                    }
                });

        statesAndCities.putIfAbsent("Chandhighad",
                new ArrayList<String>() {
                    {
                        add("Jaipur");
                        add("Udaipur");
                        add("Bikaner");
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
