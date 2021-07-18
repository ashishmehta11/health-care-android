package com.project.healthcare.data;

import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.project.healthcare.BR;

public class BaseData extends BaseObservable {
    private static final String TAG = "BaseData";
    private String titleBarName = "Home";
    private final ColorStateList blue;
    private final ColorStateList transparentWhite;
    private ColorStateList home;
    private ColorStateList login;
    private ColorStateList search;
    private ColorStateList covid;
    private ColorStateList profile;


    private int floatingMenuBtnVisibility = View.GONE, registerBottomNavVisibility = View.GONE;
    private int lblHomeNoDataVisibility = View.GONE, homeProgressWheelVisibility = View.VISIBLE;

    public BaseData(ColorStateList blue, ColorStateList transparentWhite) {
        this.blue = blue;
        this.transparentWhite = transparentWhite;
        setBlue();
    }

    private void setBlue() {
        setHome(blue);
        setLogin(blue);
        setProfile(blue);
        setSearch(blue);
        setCovid(blue);
    }


    @Bindable
    public String getTitleBarName() {
        return titleBarName;
    }

    public void setTitleBarName(String titleBarName) {
        this.titleBarName = titleBarName;
        Log.d(TAG, "setTitleBarName: " + this.titleBarName);
        setUIChanges();
        notifyPropertyChanged(BR.titleBarName);
    }

    private void setUIChanges() {
        int visibilityMenuBtn = View.VISIBLE, visibilityBottomMenu = View.GONE;
        if (titleBarName.contains("Citizen") || titleBarName.contains("Healthcare Facility")) {
            setBlue();
            setLogin(transparentWhite);
        }
        if (titleBarName.contains("Home")) {
            setBlue();
            setHome(transparentWhite);
        }
        if (titleBarName.contains("Primary Info") || titleBarName.contains("Establishment Info") || titleBarName.contains("Provided Services")) {
            visibilityMenuBtn = View.GONE;
            visibilityBottomMenu = View.VISIBLE;
            setBlue();
            setLogin(transparentWhite);
        }

        if (titleBarName.contains("Facility Profile")) {
            visibilityMenuBtn = View.GONE;
            visibilityBottomMenu = View.VISIBLE;
            setBlue();
            setProfile(transparentWhite);
        }

        if (titleBarName.contains("Citizen Profile")) {
            setBlue();
            setProfile(transparentWhite);
        }
        setFloatingMenuBtnVisibility(visibilityMenuBtn);
        setRegisterBottomNavVisibility(visibilityBottomMenu);
    }


    @Bindable
    public ColorStateList getHome() {
        return home;
    }

    public void setHome(ColorStateList home) {
        this.home = home;
        notifyPropertyChanged(BR.home);
    }

    @Bindable
    public ColorStateList getLogin() {
        return login;
    }

    public void setLogin(ColorStateList login) {
        this.login = login;
        notifyPropertyChanged(BR.login);
    }

    @Bindable
    public ColorStateList getSearch() {
        return search;
    }

    public void setSearch(ColorStateList search) {
        this.search = search;
        notifyPropertyChanged(BR.search);
    }

    @Bindable
    public ColorStateList getCovid() {
        return covid;

    }

    public void setCovid(ColorStateList covid) {
        this.covid = covid;
        notifyPropertyChanged(BR.covid);
    }

    @Bindable
    public ColorStateList getProfile() {
        return profile;
    }

    public void setProfile(ColorStateList profile) {
        this.profile = profile;
        notifyPropertyChanged(BR.profile);
    }

    @Bindable
    public int getFloatingMenuBtnVisibility() {
        return floatingMenuBtnVisibility;
    }

    public void setFloatingMenuBtnVisibility(int floatingMenuBtnVisibility) {
        this.floatingMenuBtnVisibility = floatingMenuBtnVisibility;
        notifyPropertyChanged(BR.floatingMenuBtnVisibility);
    }

    @Bindable
    public int getRegisterBottomNavVisibility() {
        return registerBottomNavVisibility;
    }

    public void setRegisterBottomNavVisibility(int registerBottomNavVisibility) {
        this.registerBottomNavVisibility = registerBottomNavVisibility;
        notifyPropertyChanged(BR.registerBottomNavVisibility);
    }

    @Bindable
    public int getLblHomeNoDataVisibility() {
        return lblHomeNoDataVisibility;
    }

    public void setLblHomeNoDataVisibility(int lblHomeNoDataVisibility) {
        this.lblHomeNoDataVisibility = lblHomeNoDataVisibility;
        notifyPropertyChanged(BR.lblHomeNoDataVisibility);
    }

    @Bindable
    public int getHomeProgressWheelVisibility() {
        return homeProgressWheelVisibility;
    }

    public void setHomeProgressWheelVisibility(int homeProgressWheelVisibility) {
        this.homeProgressWheelVisibility = homeProgressWheelVisibility;
        notifyPropertyChanged(BR.homeProgressWheelVisibility);
    }
}
