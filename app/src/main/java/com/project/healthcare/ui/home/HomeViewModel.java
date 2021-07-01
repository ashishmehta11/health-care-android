package com.project.healthcare.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.project.healthcare.data.BaseData;


public class HomeViewModel extends AndroidViewModel {
    private final BaseData baseData;
    public HomeViewModel(@NonNull Application application) {
        super(application);
        baseData = new BaseData();
    }

    public BaseData getBaseData() {
        return baseData;
    }
}
