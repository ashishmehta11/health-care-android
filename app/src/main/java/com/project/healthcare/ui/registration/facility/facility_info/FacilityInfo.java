package com.project.healthcare.ui.registration.facility.facility_info;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.data.FacilityType;
import com.project.healthcare.data.ManagedBy;
import com.project.healthcare.databinding.FragmentFacilityInfoBinding;
import com.project.healthcare.ui.MainActivityViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;


public class FacilityInfo extends Fragment implements Observer {
    private static final String TAG = "FacilityInfo";
    private final RecyclerTypeOfFacilityAdapter.FacilityAddedNotifier facilityTypeAddedNotifier = new RecyclerTypeOfFacilityAdapter.FacilityAddedNotifier();
    private final RecyclerSelectedFacilityTypeAdapter.FacilityRemovedNotifier facilityTypeRemovedNotifier = new RecyclerSelectedFacilityTypeAdapter.FacilityRemovedNotifier();
    private final RecyclerSelectedSpecialityTypeAdapter.SpecialityRemovedNotifier specialityRemovedNotifier = new RecyclerSelectedSpecialityTypeAdapter.SpecialityRemovedNotifier();
    private final RecyclerTypeOfSpecialityAdapter.SpecialityAddedNotifier specialityAddedNotifier = new RecyclerTypeOfSpecialityAdapter.SpecialityAddedNotifier();
    MainActivityViewModel viewModel;
    FragmentFacilityInfoBinding binding;
    RecyclerTypeOfFacilityAdapter typeOfFacilityAdapter;
    RecyclerSelectedFacilityTypeAdapter selectedFacilityTypeAdapter;
    RecyclerTypeOfSpecialityAdapter typeOfSpecialityAdapter;
    RecyclerSelectedSpecialityTypeAdapter selectedSpecialityTypeAdapter;
    ArrayAdapter<CharSequence> managedByAdapter;
    ArrayList<FacilityType> facType = new ArrayList<>(Arrays.asList(FacilityType.values()));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_facility_info, container, false);
        }

        facilityTypeAddedNotifier.addObserver(this);
        facilityTypeRemovedNotifier.addObserver(this);
        specialityRemovedNotifier.addObserver(this);
        specialityAddedNotifier.addObserver(this);
        viewModel.getBaseData().setTitleBarName("Establishment Info");
        attachAdapters();
        attachListeners();
        addTextWatchers();
        setData();
        return binding.getRoot();
    }

    private void setData() {
        binding.avgFees.editText.setText(viewModel.getHealthFacility().getAvgPrice());
        binding.managedByName.editText.setText(viewModel.getHealthFacility().getManagedByName());
        int pos = 0;
        for (; pos < ManagedBy.values().length; pos++) {
            if (viewModel.getHealthFacility().getManagedBy() == ManagedBy.values()[pos])
                break;
        }
        pos--;
        binding.spinnerManagedBy.setSelection(pos, true);
        setManagedByLbl();
    }

    private void setManagedByLbl() {
        if (viewModel.getHealthFacility().getManagedBy() == ManagedBy.Community)
            binding.lblManagedByName.setText("Name of the managing community");
        if (viewModel.getHealthFacility().getManagedBy() == ManagedBy.Government)
            binding.lblManagedByName.setText("Name of the managing government body");
        if (viewModel.getHealthFacility().getManagedBy() == ManagedBy.NGO)
            binding.lblManagedByName.setText("Name of the managing NGO");
        if (viewModel.getHealthFacility().getManagedBy() == ManagedBy.Private)
            binding.lblManagedByName.setText("Name of the Owner/Bussiness/Organization");
        if (viewModel.getHealthFacility().getManagedBy() == ManagedBy.Social_Welfare)
            binding.lblManagedByName.setText("Name of the Patron");
    }

    private void addTextWatchers() {
        binding.managedByName.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString();
                viewModel.getHealthFacility().setManagedByName(ss);
                validateAll();
                if (ss.isEmpty())
                    binding.managedByName.txtError.setVisibility(View.GONE);
            }
        });

        binding.avgFees.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString();
                viewModel.getHealthFacility().setAvgPrice(ss);
                validateAll();
                if (ss.isEmpty())
                    binding.avgFees.txtError.setVisibility(View.GONE);
            }
        });
    }

    private boolean checkFacilityType() {
        return !viewModel.getHealthFacility().getTypeOfFacility().isEmpty();
    }

    private boolean checkManagedByName() {
        return viewModel.getHealthFacility().getManagedByName().length() > 2;
    }

    private boolean checkPrice() {
        return Pattern.matches("^[0-9]+$", viewModel.getHealthFacility().getAvgPrice());
    }

    private boolean validateAll() {
        boolean success = true;
        if (!checkPrice()) {
            success = false;
            binding.avgFees.txtError.setVisibility(View.VISIBLE);
            binding.avgFees.txtError.setText("Enter proper details");
            if (binding.avgFees.editText.getText().toString().isEmpty())
                binding.avgFees.txtError.setVisibility(View.GONE);
        } else if (!checkManagedByName()) {
            success = false;
            binding.managedByName.txtError.setVisibility(View.VISIBLE);
            binding.managedByName.txtError.setText("Enter proper details");
            if (binding.managedByName.editText.getText().toString().isEmpty())
                binding.managedByName.txtError.setVisibility(View.GONE);

        } else if (!checkFacilityType()) {
            success = false;
        }
        if (success) {
            viewModel.getHealthFacility().setCompletedStages(2);
            binding.incMoveRight.cardMoveRight.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
        } else {
            viewModel.getHealthFacility().setCompletedStages(1);
            binding.incMoveRight.cardMoveRight.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
        }
        return success;
    }

    private void attachListeners() {
        binding.incMoveLeft.cardMoveLeft.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
        binding.incMoveLeft.btnMoveLeft.setOnClickListener(v -> viewModel.getSelectedBottomNumber().setValue(1));
        binding.incMoveRight.btnMoveRight.setOnClickListener(v -> {
            if (validateAll())
                viewModel.getSelectedBottomNumber().setValue(3);
        });
    }

    private void attachAdapters() {
        prepareSpinner();
        ArrayList<FacilityType> type = new ArrayList<>();
        for (FacilityType ft : facType) {
            boolean has = false;
            for (FacilityType f : viewModel.getHealthFacility().getTypeOfFacility()) {
                if (ft == f) {
                    has = true;
                    break;
                }
            }
            if (!has)
                type.add(ft);
        }
        selectedFacilityTypeAdapter = new RecyclerSelectedFacilityTypeAdapter(facilityTypeRemovedNotifier, viewModel.getHealthFacility().getTypeOfFacility());
        typeOfFacilityAdapter = new RecyclerTypeOfFacilityAdapter(type, facilityTypeAddedNotifier);
        binding.recyclerRegSelectedFacilityTypes.setAdapter(selectedFacilityTypeAdapter);
        binding.recyclerRegTypeOfFacility.setAdapter(typeOfFacilityAdapter);

//        typeOfSpecialityAdapter = new RecyclerTypeOfSpecialityAdapter(Arrays.asList(SpecialityType.values()), specialityAddedNotifier);
//        selectedSpecialityTypeAdapter = new RecyclerSelectedSpecialityTypeAdapter(specialityRemovedNotifier);
//        binding.recyclerRegTypeOfSpeciality.setAdapter(typeOfSpecialityAdapter);
//        binding.recyclerRegSelectedSpecialityTypes.setAdapter(selectedSpecialityTypeAdapter);
    }

    private void prepareSpinner() {
        String[] arr = new String[ManagedBy.values().length];
        for (int i = 0; i < ManagedBy.values().length; i++)
            arr[i] = ManagedBy.toString(ManagedBy.values()[i]);
        managedByAdapter = new ArrayAdapter<>(getActivity()
                , R.layout.spinner_list_item_small_font
                , arr);
        managedByAdapter.setDropDownViewResource(R.layout.spinner_dropdown_small_font);
        binding.spinnerManagedBy.setAdapter(managedByAdapter);
        binding.spinnerManagedBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.getHealthFacility().setManagedBy(ManagedBy.fromString((String) parent.getSelectedItem()));
                setManagedByLbl();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        //Facility Type added
        if (o instanceof RecyclerTypeOfFacilityAdapter.FacilityAddedNotifier) {
            Log.d(TAG, "update: arg : " + FacilityType.toString((FacilityType) arg));
            selectedFacilityTypeAdapter.getList().add((FacilityType) arg);
            selectedFacilityTypeAdapter.notifyDataSetChanged();
            typeOfFacilityAdapter.getList().remove((FacilityType) arg);
            typeOfFacilityAdapter.notifyDataSetChanged();
            viewModel.getHealthFacility().getTypeOfFacility().add((FacilityType) arg);
        }

        //Facility Type removed
        if (o instanceof RecyclerSelectedFacilityTypeAdapter.FacilityRemovedNotifier) {
            typeOfFacilityAdapter.getList().add((FacilityType) arg);
            typeOfFacilityAdapter.notifyDataSetChanged();
            selectedFacilityTypeAdapter.getList().remove((FacilityType) arg);
            selectedFacilityTypeAdapter.notifyDataSetChanged();
            viewModel.getHealthFacility().getTypeOfFacility().remove((FacilityType) arg);
        }

        validateAll();
//        //Speciality Type added
//        if (o instanceof RecyclerTypeOfSpecialityAdapter.SpecialityAddedNotifier) {
//            Log.d(TAG, "update: arg : " + SpecialityType.toString((SpecialityType) arg));
//            selectedSpecialityTypeAdapter.getList().add((SpecialityType) arg);
//            selectedSpecialityTypeAdapter.notifyDataSetChanged();
//            typeOfSpecialityAdapter.getList().remove((SpecialityType) arg);
//            typeOfSpecialityAdapter.notifyDataSetChanged();
//            viewModel.getHealthFacility().getSpecialities().add((SpecialityType) arg);
//        }
//
//        //Speciality Type removed
//        if (o instanceof RecyclerSelectedSpecialityTypeAdapter.SpecialityRemovedNotifier) {
//            typeOfSpecialityAdapter.getList().add((SpecialityType) arg);
//            typeOfSpecialityAdapter.notifyDataSetChanged();
//            selectedSpecialityTypeAdapter.getList().remove((SpecialityType) arg);
//            selectedSpecialityTypeAdapter.notifyDataSetChanged();
//            viewModel.getHealthFacility().getSpecialities().remove((SpecialityType) arg);
//        }

    }
}