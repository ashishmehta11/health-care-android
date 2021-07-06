package com.project.healthcare.data;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.project.healthcare.BR;

public class DialogData extends BaseObservable {
    private String titleString, btnText;

    public DialogData(String titleString, String btnText) {
        setTitleString(titleString);
        setBtnText(btnText);
    }

    @Bindable
    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(String titleString) {
        this.titleString = titleString;
        notifyPropertyChanged(BR.titleString);
    }

    @Bindable
    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
        notifyPropertyChanged(BR.btnText);
    }
}
