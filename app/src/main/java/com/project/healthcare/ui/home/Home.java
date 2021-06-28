package com.project.healthcare.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.project.healthcare.R;
import com.project.healthcare.databinding.ActivityHomeBinding;

import org.jetbrains.annotations.NotNull;

public class Home extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private HomeViewModel viewModel;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.drawerLayout.setDrawerElevation(0);
        binding.drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent, getTheme()));
        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull @NotNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull @NotNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if(binding.drawerLayout.isDrawerOpen(GravityCompat.END))
                    binding.cardMenu.setVisibility(View.GONE);
                else
                    binding.cardMenu.setVisibility(View.VISIBLE);
            }
        });
        binding.btnMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.END);
            binding.cardMenu.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }
}