package com.project.healthcare.ui.home.bottom_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.project.healthcare.R;
import com.project.healthcare.data.HomeCovidNewsFlash;
import com.project.healthcare.databinding.FragmentHomeBottomBinding;
import com.project.healthcare.ui.MainActivityViewModel;

import java.util.ArrayList;
import java.util.Map;

public class HomeBottomFragment extends Fragment {

    LinearLayoutManager layoutManagerCases;
    ArrayList<HomeCovidNewsFlash> list = new ArrayList<>();
    private FragmentHomeBottomBinding binding;
    private MainActivityViewModel viewModel;
    private RecyclerCasesAdapter casesAdapter;
    final Handler handler = new Handler();
    private Runnable runnable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_bottom, container, false);
        }
        prepareList();
        setLayoutManager();
        prepareRecyclerCases();
        attachListeners();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setLayoutManager();
        autoScroll();
    }

    private void prepareList() {
        int i = 0;
        for (Map.Entry<String, ArrayList<String>> entry : viewModel.getStatesAndCities().entrySet()) {
            list.add(new HomeCovidNewsFlash(String.valueOf(++i),
                    String.valueOf(++i),
                    String.valueOf(++i),
                    String.valueOf(++i),
                    entry.getKey()));
        }
    }

    private void attachListeners() {
        //Linear Layout Click listener
        binding.linearLayout.setOnClickListener(v -> navigateToCovidScreen());
    }

    private void navigateToCovidScreen() {
    }

    private void setLayoutManager() {
        layoutManagerCases = new LinearLayoutManager(viewModel.getApplication().getApplicationContext()) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                if (viewModel.getApplication().getApplicationContext() == null)
                    return;
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(viewModel.getApplication().getApplicationContext()) {
                    private static final float SPEED = 1f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);

            }
        };
        layoutManagerCases.setOrientation(RecyclerView.HORIZONTAL);
    }


    private void prepareRecyclerCases() {

        casesAdapter = new RecyclerCasesAdapter(list);
        binding.recyclerCases.setLayoutManager(layoutManagerCases);
        binding.recyclerCases.setAdapter(casesAdapter);
        binding.recyclerCases.setSelected(false);
        autoScroll();
    }


    public void autoScroll() {
        final int speedScroll = 0;
        final Handler handler = new Handler();
        runnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (count == casesAdapter.getItemCount())
                    count = 0;
                if (count < casesAdapter.getItemCount()) {
                    binding.recyclerCases.smoothScrollToPosition(++count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}