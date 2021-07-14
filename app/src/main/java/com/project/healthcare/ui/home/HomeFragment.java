package com.project.healthcare.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.databinding.FragmentHomeBinding;
import com.project.healthcare.ui.MainActivityViewModel;


public class HomeFragment extends Fragment {
    MainActivityViewModel viewModel;
    FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
            binding.setData(viewModel.getBaseData());
        }
        viewModel.getBaseData().setTitleBarName("Home");
        viewModel.getBaseData().setFloatingMenuBtnVisibility(View.VISIBLE);
        return binding.getRoot();
    }
}