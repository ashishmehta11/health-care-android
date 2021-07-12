package com.project.healthcare.ui.registration.facility.service_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.data.SpecialityType;
import com.project.healthcare.databinding.FragmentServiceInfoBinding;
import com.project.healthcare.ui.MainActivityViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;


public class ServiceInfo extends Fragment implements Observer {
    private final RecyclerSelectedSpecialityTypeAdapter.SpecialityRemovedNotifier specialityRemovedNotifier = new RecyclerSelectedSpecialityTypeAdapter.SpecialityRemovedNotifier();
    private final RecyclerTypeOfSpecialityAdapter.SpecialityAddedNotifier specialityAddedNotifier = new RecyclerTypeOfSpecialityAdapter.SpecialityAddedNotifier();
    FragmentServiceInfoBinding binding;
    MainActivityViewModel viewModel;
    RecyclerTypeOfSpecialityAdapter typeOfSpecialityAdapter;
    RecyclerSelectedSpecialityTypeAdapter selectedSpecialityTypeAdapter;
    ArrayList<SpecialityType> facType = new ArrayList<>(Arrays.asList(SpecialityType.values()));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = FragmentServiceInfoBinding.inflate(inflater, container, false);
        }
        viewModel.getBaseData().setTitleBarName("Provided Services");
        specialityRemovedNotifier.addObserver(this);
        specialityAddedNotifier.addObserver(this);
        attachAdapters();
        attachListeners();
        return binding.getRoot();
    }

    private void attachListeners() {
        binding.incMoveLeft.cardMoveLeft.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
        binding.incMoveLeft.btnMoveLeft.setOnClickListener(v -> {
            viewModel.getSelectedBottomNumber().setValue(2);
        });
    }

    private void attachAdapters() {
        ArrayList<SpecialityType> type = new ArrayList<>();
        for (SpecialityType ft : facType) {
            boolean has = false;
            for (SpecialityType f : viewModel.getHealthFacility().getSpecialities()) {
                if (ft == f) {
                    has = true;
                    break;
                }
            }
            if (!has)
                type.add(ft);
        }
        typeOfSpecialityAdapter = new RecyclerTypeOfSpecialityAdapter(type, specialityAddedNotifier);
        selectedSpecialityTypeAdapter = new RecyclerSelectedSpecialityTypeAdapter(viewModel.getHealthFacility().getSpecialities(), specialityRemovedNotifier);
        binding.recyclerRegTypeOfSpeciality.setAdapter(typeOfSpecialityAdapter);
        binding.recyclerRegSelectedSpecialityType.setAdapter(selectedSpecialityTypeAdapter);
    }

    @Override
    public void update(Observable o, Object arg) {
        //Speciality Type added
        if (o instanceof RecyclerTypeOfSpecialityAdapter.SpecialityAddedNotifier) {
            selectedSpecialityTypeAdapter.getList().add((SpecialityType) arg);
            selectedSpecialityTypeAdapter.notifyDataSetChanged();
            typeOfSpecialityAdapter.getList().remove((SpecialityType) arg);
            typeOfSpecialityAdapter.notifyDataSetChanged();
            viewModel.getHealthFacility().getSpecialities().add((SpecialityType) arg);
        }

        //Speciality Type removed
        if (o instanceof RecyclerSelectedSpecialityTypeAdapter.SpecialityRemovedNotifier) {
            typeOfSpecialityAdapter.getList().add((SpecialityType) arg);
            typeOfSpecialityAdapter.notifyDataSetChanged();
            selectedSpecialityTypeAdapter.getList().remove((SpecialityType) arg);
            selectedSpecialityTypeAdapter.notifyDataSetChanged();
            viewModel.getHealthFacility().getSpecialities().remove((SpecialityType) arg);
        }
    }
}