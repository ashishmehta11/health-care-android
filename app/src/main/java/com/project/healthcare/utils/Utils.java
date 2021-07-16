package com.project.healthcare.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.project.healthcare.R;
import com.project.healthcare.data.DialogData;
import com.project.healthcare.databinding.DialogGeneralBinding;
import com.project.healthcare.ui.MainActivityViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Utils {
    static HashMap<String, ArrayList<String>> cityAndState = new HashMap<String, ArrayList<String>>();

    public Utils(Context context) {
        try {
            InputStream is = context.getAssets().open("list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(jsonString);
            Iterator<?> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next().toString();
                ArrayList<String> values = new ArrayList<>();
                JSONArray arr = json.getJSONArray(key);
                for (int i = 0; i < arr.length(); i++) {
                    values.add(arr.getString(i));
                }
                MainActivityViewModel.statesAndCities.putIfAbsent(key, values);
            }
        } catch (Exception ignore) {

        }
    }

    public static Dialog buildProgressDialog(Context context) {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Dialog_Alert);
        //DialogData dialogData = new DialogData("Registration", "OK");
        View dialogView = LayoutInflater.from(context).inflate(R.layout.progress_wheel, null, false);
        //DialogRegistrationChoiceBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_registration_choice, null, false);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static class GeneralDialog {
        public Dialog dialog;
        public DialogGeneralBinding binding;

        public void buildGeneralDialog(Context context, DialogData dialogData) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Dialog_Alert);
            binding = DialogGeneralBinding.inflate(LayoutInflater.from(context));
            //DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_general, null, false);
            binding.setData(dialogData);
            builder.setView(binding.getRoot());
            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
            binding.header.btnCloseDialog.setOnClickListener(v -> {
                if (dialog.isShowing()) dialog.cancel();
            });
        }
    }

}
