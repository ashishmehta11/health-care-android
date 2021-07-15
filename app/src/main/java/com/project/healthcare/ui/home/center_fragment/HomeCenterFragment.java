package com.project.healthcare.ui.home.center_fragment;

import android.os.Bundle;
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
import com.project.healthcare.api.ApiCalls;
import com.project.healthcare.data.HealthFacility;
import com.project.healthcare.databinding.FragmentHomeCenterBinding;
import com.project.healthcare.ui.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


public class HomeCenterFragment extends Fragment implements Observer {
    FragmentHomeCenterBinding binding;
    MainActivityViewModel viewModel;
    RecyclerCitiesAdapter recyclerCitiesAdapter;
    RecyclerFacilityListAdapter recyclerFacilityListAdapter;
    ArrayAdapter<CharSequence> statesAdapter;
    RecyclerCitiesAdapter.SelectedCityNotifier notifierObj = new RecyclerCitiesAdapter.SelectedCityNotifier();
    private static final String TAG = "HomeCenterFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        notifierObj.addObserver(this);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_center, container, false);
            binding.setData(viewModel.getBaseData());
        }
        prepareSpinner();
        prepareRecyclerCities(viewModel.getStatesAndCities().get(viewModel.getSelectedCity().getValue()));
        prepareRecyclerFacilityList(viewModel.getCurrentShowingList());
        attachObservers();
        ApiCalls.getInstance().getFacilitiesByCity(viewModel.getSelectedState().getValue(), viewModel.getSelectedCity().getValue());
        viewModel.getBaseData().setHomeProgressWheelVisibility(View.VISIBLE);
        viewModel.getBaseData().setLblHomeNoDataVisibility(View.GONE);
        ApiCalls.getInstance().addObserver(this);
        return binding.getRoot();
    }

    private void prepareRecyclerFacilityList(ArrayList<HealthFacility> l) {
        recyclerFacilityListAdapter = new RecyclerFacilityListAdapter(l);
        binding.recyclerFacilityList.setAdapter(recyclerFacilityListAdapter);
    }

    private void attachObservers() {
        viewModel.getSelectedState().observe(getViewLifecycleOwner(), s -> {
            viewModel.reSetSelectedCity();
            ApiCalls.getInstance().getFacilitiesByCity(viewModel.getSelectedState().getValue(), viewModel.getSelectedCity().getValue());
            viewModel.getBaseData().setHomeProgressWheelVisibility(View.VISIBLE);
            viewModel.getBaseData().setLblHomeNoDataVisibility(View.GONE);
            prepareRecyclerCities(viewModel.getStatesAndCities().get(s));
        });


    }

    private void prepareRecyclerCities(List<String> list) {
        recyclerCitiesAdapter = new RecyclerCitiesAdapter(list, viewModel.getSelectedCity().getValue(), notifierObj);
        binding.recyclerCities.setAdapter(recyclerCitiesAdapter);
    }

    private void prepareSpinner() {
        statesAdapter = new ArrayAdapter<>(getActivity()
                , R.layout.spinner_list_item
                , viewModel.getStatesAndCities().keySet().toArray(new String[0]));
        statesAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        binding.spinnerStates.setAdapter(statesAdapter);
        binding.spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: position : "+position);
                int i=0;
                for(Map.Entry<String,ArrayList<String>> e : viewModel.getStatesAndCities().entrySet()){
                    if(i++ == position) {
                        viewModel.setSelectedState(e.getKey());
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        ApiCalls.getInstance().deleteObserver(this);
        super.onDestroyView();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof RecyclerCitiesAdapter.SelectedCityNotifier) {
            viewModel.setSelectedCity(arg.toString());
            Log.d(TAG, "update: here");
            ApiCalls.getInstance().getFacilitiesByCity(viewModel.getSelectedState().getValue(), viewModel.getSelectedCity().getValue());
        }
        if (o instanceof ApiCalls) {
            ApiCalls.ApiCallReturnObjects objs = (ApiCalls.ApiCallReturnObjects) arg;
            viewModel.getBaseData().setHomeProgressWheelVisibility(View.GONE);
            switch (objs.getCallId()) {
                case 1:
                    if (objs.isSuccess()) {
                        ArrayList<HealthFacility> healthFacilities = (ArrayList<HealthFacility>) objs.getData();
                        viewModel.setCurrentShowingList(healthFacilities);
                        if (healthFacilities.isEmpty())
                            viewModel.getBaseData().setLblHomeNoDataVisibility(View.VISIBLE);
                        else
                            viewModel.getBaseData().setLblHomeNoDataVisibility(View.GONE);
                        Log.d(TAG, "update: inside case 1 : objs.scueess");
                        prepareRecyclerFacilityList(healthFacilities);
                    } else {
                        viewModel.getBaseData().setLblHomeNoDataVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }
}