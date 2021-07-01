package com.project.healthcare.data;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.project.healthcare.BR;

public class BaseData extends BaseObservable {
    private String titleBarName = "Home";

    @Bindable
    public String getTitleBarName() {
        return titleBarName;
    }

    public void setTitleBarName(String titleBarName) {
        this.titleBarName = titleBarName;
        notifyPropertyChanged(BR.titleBarName);
    }
}
