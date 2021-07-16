package com.project.healthcare.ui.registration.facility.service_info;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.api.ApiCalls;
import com.project.healthcare.data.SpecialityType;
import com.project.healthcare.databinding.FragmentServiceInfoBinding;
import com.project.healthcare.ui.MainActivityViewModel;
import com.project.healthcare.ui.login.LoginActivity;
import com.project.healthcare.utils.Utils;

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
    Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = FragmentServiceInfoBinding.inflate(inflater, container, false);
        }
        dialog = Utils.buildProgressDialog(requireContext());
        viewModel.getBaseData().setTitleBarName("Provided Services");
        specialityRemovedNotifier.addObserver(this);
        specialityAddedNotifier.addObserver(this);
        ApiCalls.getInstance().addObserver(this);
        attachAdapters();
        attachListeners();
        addTextWatchers();
        setData();
        return binding.getRoot();
    }

    private void addTextWatchers() {
        binding.about.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString();
                viewModel.getHealthFacility().setAbout(ss);
                validateAll();
                if (ss.length() > 10 || ss.isEmpty())
                    binding.about.txtError.setVisibility(View.GONE);
            }
        });
    }

    private boolean validateAll() {
        boolean success = true;
        if (viewModel.getHealthFacility().getAbout().length() < 10) {
            success = false;
            binding.about.txtError.setVisibility(View.VISIBLE);
            binding.about.txtError.setText("Please tell us a little bit more about establishment");
            if (binding.about.editText.getText().toString().isEmpty())
                binding.about.txtError.setVisibility(View.GONE);
        } else if (!checkSpecialityType()) {
            success = false;
        }
        if (success) {
            if (viewModel.getHealthFacility().getCompletedStages() < 3)
                viewModel.getHealthFacility().setCompletedStages(3);
            binding.cardRegister.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
        } else {
            viewModel.getHealthFacility().setCompletedStages(2);
            binding.cardRegister.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
        }
        return success;
    }


    private boolean checkSpecialityType() {
        return !viewModel.getHealthFacility().getSpecialities().isEmpty();
    }


    private void setData() {
        binding.about.editText.setText(viewModel.getHealthFacility().getAbout());
    }

    private void attachListeners() {
        binding.incMoveLeft.cardMoveLeft.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
        binding.incMoveLeft.btnMoveLeft.setOnClickListener(v -> {
            viewModel.getSelectedBottomNumber().setValue(2);
        });

        binding.cardRegister.setOnClickListener(v -> {
            if (validateAll()) {
                // call api
//                Toast.makeText(requireContext(), "You have been registered successfully!", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(requireActivity(), LoginActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
                ApiCalls.getInstance().registerFacility(viewModel.getHealthFacility());
                if (!dialog.isShowing())
                    dialog.show();
            }
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
            typeOfSpecialityAdapter.getList().remove(arg);
            typeOfSpecialityAdapter.notifyDataSetChanged();
            viewModel.getHealthFacility().getSpecialities().add((SpecialityType) arg);
            validateAll();
        }

        //Speciality Type removed
        if (o instanceof RecyclerSelectedSpecialityTypeAdapter.SpecialityRemovedNotifier) {
            typeOfSpecialityAdapter.getList().add((SpecialityType) arg);
            typeOfSpecialityAdapter.notifyDataSetChanged();
            selectedSpecialityTypeAdapter.getList().remove(arg);
            selectedSpecialityTypeAdapter.notifyDataSetChanged();
            viewModel.getHealthFacility().getSpecialities().remove(arg);
            validateAll();
        }

        if (o instanceof ApiCalls) {
            if (dialog.isShowing())
                dialog.cancel();
            ApiCalls.ApiCallReturnObjects objs = (ApiCalls.ApiCallReturnObjects) arg;
            viewModel.getBaseData().setHomeProgressWheelVisibility(View.GONE);
            switch (objs.getCallId()) {
                case 2:
                    if (objs.isSuccess()) {
                        Toast.makeText(viewModel.getApplication().getApplicationContext(), "You have been registered successfully!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(requireActivity(), LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else {
                        Toast.makeText(viewModel.getApplication().getApplicationContext(), objs.getTitle() + " : -> " + objs.getText(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

    }
}