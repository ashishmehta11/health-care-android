package com.project.healthcare.data;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.project.healthcare.BR;

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


    public void setVaccinated(String vaccinated) {
        this.vaccinated = vaccinated;
        notifyPropertyChanged(BR.vaccinated);
    }

    public void setCases(String cases) {
        this.cases = cases;
        notifyPropertyChanged(BR.cases);
    }

    public void setRecovery(String recovery) {
        this.recovery = recovery;
        notifyPropertyChanged(BR.recovery);
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
        notifyPropertyChanged(BR.deaths);
    }

    public void setArea(String area) {
        this.area = area;
        notifyPropertyChanged(BR.area);
    }
}
