package com.project.healthcare.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.project.healthcare.R;
import com.project.healthcare.databinding.ActivityHomeBinding;
import com.project.healthcare.ui.home.fragments.HomeFragment;


public class Home extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private HomeViewModel viewModel;
    private static final String TAG = "HomeActivity";
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setData(viewModel.getBaseData());
        binding.drawerLayout.setDrawerElevation(0);
        binding.drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent, getTheme()));
        fragmentManager= getSupportFragmentManager();
        attachListeners();
        attachFragment();
    }

    private void attachFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment.class,null)
                .setReorderingAllowed(true)
                .commitNow();
        binding.navMenuHome.cardView.setCardBackgroundColor(getColor(R.color.transparent_white));
    }

    private void attachListeners()
    {
        //DRAWER LISTENER
        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull  View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull  View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if(binding.drawerLayout.isDrawerOpen(GravityCompat.END))
                    binding.cardMenu.setVisibility(View.GONE);
                else
                    binding.cardMenu.setVisibility(View.VISIBLE);
            }
        });

        //BTN MENU LISTENER
        binding.btnMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.END);
            binding.cardMenu.setVisibility(View.GONE);
        });

        //BTN NAV MENU CLOSE
        binding.btnNavMenuClose.setOnClickListener(v -> binding.drawerLayout.closeDrawer(GravityCompat.END));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }
}