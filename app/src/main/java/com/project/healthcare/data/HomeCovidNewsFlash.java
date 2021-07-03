package com.project.healthcare.data;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class HomeCovidNewsFlash extends BaseObservable {
    private String vaccinated, cases, recovery, deaths, area;

    public HomeCovidNewsFlash(String vaccinated, String cases, String recovery, String deaths, String area) {
        this.vaccinated = vaccinated;
        this.cases = cases;
        this.recovery = recovery;
        this.deaths = deaths;
        this.area = area;
    }

    @Bindable
    public String getVaccinated() {
        return vaccinated;
    }

    @Bindable
    public String getCases() {
        return cases;
    }

    @Bindable
    public String getRecovery() {
        return recovery;
    }

    @Bindable
    public String getDeaths() {
        return deaths;
    }

    @Bindable
    public String getArea() {
        return area;
    }
}
