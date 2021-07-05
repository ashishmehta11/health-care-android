package com.project.healthcare.ui.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.databinding.FragmentRegisterBinding;
import com.project.healthcare.ui.MainActivityViewModel;


public class Register extends Fragment {
    MainActivityViewModel viewModel;
    FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        }
        viewModel.getBaseData().setTitleBarName("Registration");
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getBaseData().setTitleBarName("Registration");
    }

}