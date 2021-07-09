package com.project.healthcare.ui.registration.facility.facility_info;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.data.FacilityType;
import com.project.healthcare.databinding.FragmentFacilityInfoBinding;
import com.project.healthcare.ui.MainActivityViewModel;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;


public class FacilityInfo extends Fragment implements Observer {
    private static final String TAG = "FacilityInfo";
    private final RecyclerTypeOfFacilityAdapter.FacilityAddedNotifier facilityTypeAddedNotifier = new RecyclerTypeOfFacilityAdapter.FacilityAddedNotifier();
    private final RecyclerSelectedTypeAdapter.FacilityRemovedNotifier facilityTypeRemovedNotifier = new RecyclerSelectedTypeAdapter.FacilityRemovedNotifier();
    MainActivityViewModel viewModel;
    FragmentFacilityInfoBinding binding;
    RecyclerTypeOfFacilityAdapter typeOfFacilityAdapter;
    RecyclerSelectedTypeAdapter selectedTypeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_facility_info, container, false);
        }
        facilityTypeAddedNotifier.addObserver(this);
        facilityTypeRemovedNotifier.addObserver(this);
        viewModel.getBaseData().setTitleBarName("Establishment Info");
        attachAdapters();
        attachListeners();
        return binding.getRoot();
    }

    private void attachListeners() {
        binding.incMoveLeft.cardMoveLeft.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
        binding.incMoveLeft.btnMoveLeft.setOnClickListener(v -> viewModel.getSelectedBottomNumber().setValue(1));
    }

    private void attachAdapters() {
        typeOfFacilityAdapter = new RecyclerTypeOfFacilityAdapter(Arrays.asList(FacilityType.values()), facilityTypeAddedNotifier);
        selectedTypeAdapter = new RecyclerSelectedTypeAdapter(facilityTypeRemovedNotifier);
        binding.recyclerRegSelectedFacilityTypes.setAdapter(selectedTypeAdapter);
        binding.recyclerRegTypeOfFacility.setAdapter(typeOfFacilityAdapter);
    }

    @Override
    public void update(Observable o, Object arg) {
        //Facility Type added
        if (o instanceof RecyclerTypeOfFacilityAdapter.FacilityAddedNotifier) {
            Log.d(TAG, "update: arg : " + FacilityType.toString((FacilityType) arg));
            selectedTypeAdapter.getList().add((FacilityType) arg);
            selectedTypeAdapter.notifyDataSetChanged();
            typeOfFacilityAdapter.getList().remove((FacilityType) arg);
            typeOfFacilityAdapter.notifyDataSetChanged();
            viewModel.getHealthFacility().getTypeOfFacility().add((FacilityType) arg);
        }

        //Facility Type removed
        if (o instanceof RecyclerSelectedTypeAdapter.FacilityRemovedNotifier) {
            typeOfFacilityAdapter.getList().add((FacilityType) arg);
            typeOfFacilityAdapter.notifyDataSetChanged();
            selectedTypeAdapter.getList().remove((FacilityType) arg);
            selectedTypeAdapter.notifyDataSetChanged();
            viewModel.getHealthFacility().getTypeOfFacility().remove((FacilityType) arg);
        }
    }
}