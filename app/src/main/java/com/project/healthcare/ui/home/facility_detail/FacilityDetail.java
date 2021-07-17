package com.project.healthcare.ui.home.facility_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.data.FacilityType;
import com.project.healthcare.data.HealthFacility;
import com.project.healthcare.data.ManagedBy;
import com.project.healthcare.data.SpecialityType;
import com.project.healthcare.databinding.FragmentFacilityDetailBinding;
import com.project.healthcare.ui.MainActivityViewModel;


public class FacilityDetail extends Fragment {
    MainActivityViewModel viewModel;
    FragmentFacilityDetailBinding binding;
    HealthFacility facility;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        binding = FragmentFacilityDetailBinding.inflate(inflater, container, false);
        facility = (HealthFacility) getArguments().getSerializable("facility");
        setData();
        return binding.getRoot();
    }

    private void setData() {
        String email = "";
        for (int i = 0; i < facility.getEmails().size(); i++) {
            if (i == facility.getEmails().size() - 1)
                email += facility.getEmails().get(i);
            else
                email += facility.getEmails().get(i) + "\n\n";
        }
        String phones = "";
        for (int i = 0; i < facility.getPhoneNumbers().size(); i++) {
            if (i == facility.getPhoneNumbers().size() - 1)
                phones += facility.getPhoneNumbers().get(i);
            else
                phones += facility.getPhoneNumbers().get(i) + "\n\n";
        }
        String affiliations = "";
        for (int i = 0; i < facility.getAffiliations().size(); i++) {
            if (i == facility.getAffiliations().size() - 1)
                affiliations += FacilityType.toString(facility.getAffiliations().get(i));
            else
                affiliations += FacilityType.toString(facility.getAffiliations().get(i)) + ", ";
        }
        String speciality = "";
        for (int i = 0; i < facility.getSpecialities().size(); i++) {
            if (i == facility.getSpecialities().size() - 1)
                speciality += SpecialityType.toString(facility.getSpecialities().get(i));
            else
                speciality += SpecialityType.toString(facility.getSpecialities().get(i)) + "\n\n";
        }
        binding.txtFacilityName.setText(facility.getName());
        binding.txtAbout.setText(facility.getAbout());
        binding.txtAddress.setText(facility.getAddress());
        binding.txtState.setText(facility.getState());
        binding.txtCity.setText(facility.getCity());
        binding.txtPinCode.setText(facility.getPinCode());
        binding.txtEmails.setText(email);
        binding.txtContactNumbers.setText(phones);
        binding.txtEstablishedDate.setText(facility.getEstablishmentDate());
        binding.txtAffiliations.setText(affiliations);
        binding.txtSpecialities.setText(speciality);
        binding.txtAvgCharges.setText(facility.getAvgPrice());
        binding.txtOwner.setText(ManagedBy.toString(facility.getManagedBy()));
        binding.txtOwnerName.setText(facility.getManagedByName());
    }
}