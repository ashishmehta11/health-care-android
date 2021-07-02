package com.project.healthcare.ui.home.fragments.center_fragment;

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
import com.project.healthcare.databinding.FragmentHomeCenterBinding;
import com.project.healthcare.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


public class HomeCenterFragment extends Fragment implements Observer {
    FragmentHomeCenterBinding binding;
    HomeViewModel viewModel;
    RecyclerCitiesAdapter recyclerCitiesAdapter;
    RecyclerFacilityListAdapter recyclerFacilityListAdapter;
    ArrayAdapter<CharSequence> statesAdapter;
    private static final String TAG = "HomeCenterFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_center, container, false);
        }
        prepareSpinner();
        prepareRecyclerCities(viewModel.getStatesAndCities().get(viewModel.getSelectedCity().getValue()));
        prepareRecyclerFacilityList();
        attachObservers();
        return binding.getRoot();
    }

    private void prepareRecyclerFacilityList() {
        recyclerFacilityListAdapter = new RecyclerFacilityListAdapter();
        binding.recyclerFacilityList.setAdapter(recyclerFacilityListAdapter);
    }

    private void attachObservers() {
        viewModel.getSelectedState().observe(getViewLifecycleOwner(), s -> {
            viewModel.reSetSelectedCity();
            prepareRecyclerCities(viewModel.getStatesAndCities().get(s));
        });
    }

    private void prepareRecyclerCities(List<String> list) {
        recyclerCitiesAdapter = new RecyclerCitiesAdapter(list, viewModel.getSelectedCity().getValue());
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
    public void update(Observable o, Object arg) {
        if(o instanceof RecyclerCitiesAdapter.SelectedCityNotifier){
            viewModel.setSelectedCity(arg.toString());
        }
    }
}