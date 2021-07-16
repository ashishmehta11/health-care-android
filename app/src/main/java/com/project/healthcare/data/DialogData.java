package com.project.healthcare.data;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.project.healthcare.BR;

public class DialogData extends BaseObservable {
    private String titleString, btnPositiveText, textString, btnNegativeText;
    private int negativeBtnVisibility = View.GONE;

    public DialogData(String titleString, String textString, String btnPositiveText, String btnNegativeText, int vis) {
        setTitleString(titleString);
        setBtnPositiveText(btnPositiveText);
        setBtnNegativeText(btnNegativeText);
        setTextString(textString);
        setNegativeBtnVisibility(vis);
    }

    @Bindable
    public int getNegativeBtnVisibility() {
        return negativeBtnVisibility;
    }

    public void setNegativeBtnVisibility(int negativeBtnVisibility) {
        this.negativeBtnVisibility = negativeBtnVisibility;
        notifyPropertyChanged(BR.negativeBtnVisibility);
    }

    @Bindable
    public String getBtnNegativeText() {
        return btnNegativeText;
    }

    public void setBtnNegativeText(String btnNegativeText) {
        this.btnNegativeText = btnNegativeText;
        notifyPropertyChanged(BR.btnNegativeText);
    }

    @Bindable
    public String getTextString() {
        return textString;
    }

    public void setTextString(String textString) {
        this.textString = textString;
        notifyPropertyChanged(BR.textString);
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
    public String getBtnPositiveText() {
        return btnPositiveText;
    }

    public void setBtnPositiveText(String btnPositiveText) {
        this.btnPositiveText = btnPositiveText;
        notifyPropertyChanged(BR.btnPositiveText);
    }
}
