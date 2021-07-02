package com.project.healthcare.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

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
        attachFragment(HomeFragment.class);
    }

    private void attachFragment(Class fc) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fc, null)
                .setReorderingAllowed(true)
                .commitNow();
        setBlueNavMenuCards();
        if (fc == HomeFragment.class)
            binding.navMenuHome.cardView.setCardBackgroundColor(getColor(R.color.transparent_white));
    }

    private void setBlueNavMenuCards() {
        binding.navMenuHome.cardView.setCardBackgroundColor(getColor(R.color.blue));
        binding.navMenuLogin.cardView.setCardBackgroundColor(getColor(R.color.blue));
        binding.navMenuCovid.cardView.setCardBackgroundColor(getColor(R.color.blue));
        binding.navMenuProfile.cardView.setCardBackgroundColor(getColor(R.color.blue));
        binding.navMenuSearch.cardView.setCardBackgroundColor(getColor(R.color.blue));
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

        attachNavMenuClickListener();
    }

    private void attachNavMenuClickListener() {
        // HOME
        binding.navMenuHome.cardView.setOnClickListener(v -> {
            setBlueNavMenuCards();
            binding.navMenuHome.cardView.setCardBackgroundColor(getColor(R.color.transparent_white));
        });

        //LOGIN
        binding.navMenuLogin.cardView.setOnClickListener(v -> {
            setBlueNavMenuCards();
            binding.navMenuLogin.cardView.setCardBackgroundColor(getColor(R.color.transparent_white));
        });

        //PROFILE
        binding.navMenuProfile.cardView.setOnClickListener(v -> {
            setBlueNavMenuCards();
            binding.navMenuProfile.cardView.setCardBackgroundColor(getColor(R.color.transparent_white));
        });

        //SEARCH
        binding.navMenuSearch.cardView.setOnClickListener(v -> {
            setBlueNavMenuCards();
            binding.navMenuSearch.cardView.setCardBackgroundColor(getColor(R.color.transparent_white));
        });

        //COVID
        binding.navMenuCovid.cardView.setOnClickListener(v -> {
            setBlueNavMenuCards();
            binding.navMenuCovid.cardView.setCardBackgroundColor(getColor(R.color.transparent_white));
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }
}