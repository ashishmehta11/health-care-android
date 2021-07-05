package com.project.healthcare.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.project.healthcare.R;
import com.project.healthcare.databinding.ActivityMainBinding;
import com.project.healthcare.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;
    private static final String TAG = "HomeActivity";
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setData(viewModel.getBaseData());
        binding.drawerLayout.setDrawerElevation(0);
        binding.drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent, getTheme()));
        navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.navView, navController);
        attachListeners();
        if (getIntent().hasExtra("register")) {
            //navController.popBackStack();
            navController.navigate(R.id.register);
        }
        //else
        // attachFragment(HomeFragment.class);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (viewModel.getBaseData().getTitleBarName().contains("Registration")) {
            //navController.navigate(R.id.homeFragment);
            navController.popBackStack(R.id.homeFragment, true);
        }

        if (viewModel.getBaseData().getTitleBarName().contains("Home")) {
            //navController.navigate(R.id.homeFragment);
            finish();
        }
    }

    private void attachListeners() {
        //DRAWER LISTENER
        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END))
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

        attachNavMenuClickListener();
    }

    private void attachNavMenuClickListener() {
        // HOME
        binding.navMenuHome.cardView.setOnClickListener(v -> {
            //navController.popBackStack();
//           navController.navigate(R.id.homeFragment,true);
            navController.popBackStack(R.id.homeFragment, false);
        });

        //LOGIN
        binding.navMenuLogin.cardView.setOnClickListener(v -> {

            startActivity(new Intent(this, LoginActivity.class));

            binding.drawerLayout.closeDrawer(GravityCompat.END);
        });

        //PROFILE

        binding.navMenuProfile.cardView.setOnClickListener(v -> {

            binding.drawerLayout.closeDrawer(GravityCompat.END);
        });

        //SEARCH
        binding.navMenuSearch.cardView.setOnClickListener(v -> {

            binding.drawerLayout.closeDrawer(GravityCompat.END);
        });

        //COVID
        binding.navMenuCovid.cardView.setOnClickListener(v -> {

            binding.drawerLayout.closeDrawer(GravityCompat.END);
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        viewModel.getBaseData().setTitleBarName("Home");
    }
}