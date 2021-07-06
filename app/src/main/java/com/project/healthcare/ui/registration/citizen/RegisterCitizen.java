package com.project.healthcare.ui.registration.citizen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.databinding.FragmentRegisterCitizenBinding;
import com.project.healthcare.ui.MainActivityViewModel;


public class RegisterCitizen extends Fragment {
    MainActivityViewModel viewModel;
    FragmentRegisterCitizenBinding binding;
    ArrayAdapter<CharSequence> statesAdapter;
    ArrayAdapter<CharSequence> citiesAdapter;
    String selectedCity, selectedState = "Gujarat";
    private final static String TAG = "Register Fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_citizen, container, false);
        }
        viewModel.getBaseData().setTitleBarName("Citizen");
        return binding.getRoot();
    }

}