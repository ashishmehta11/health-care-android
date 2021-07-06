package com.project.healthcare.utils;

import android.content.Context;

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
}
