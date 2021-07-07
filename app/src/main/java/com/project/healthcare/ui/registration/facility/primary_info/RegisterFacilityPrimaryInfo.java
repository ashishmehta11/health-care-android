package com.project.healthcare.ui.registration.facility.primary_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.databinding.FragmentRegisterFacilityPrimaryInfoBinding;
import com.project.healthcare.ui.MainActivityViewModel;


public class RegisterFacilityPrimaryInfo extends Fragment {

    FragmentRegisterFacilityPrimaryInfoBinding binding;
    MainActivityViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_facility_primary_info, container, false);
        }

        viewModel.getBaseData().setTitleBarName("Primary Info");
        viewModel.getSelectedBottomNumber().setValue(1);
        return binding.getRoot();
    }
}