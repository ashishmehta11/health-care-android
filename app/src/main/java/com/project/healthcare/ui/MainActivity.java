package com.project.healthcare.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.project.healthcare.R;
import com.project.healthcare.databinding.ActivityMainBinding;
import com.project.healthcare.ui.login.LoginActivity;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;
    private static final String TAG = "HomeActivity";
    private NavController navController;
    private final float size = 40;
    private final float smallSize = 24;

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
        addObservers();
        if (getIntent().hasExtra("citizen")) {
            //navController.popBackStack();
            navController.navigate(R.id.register_citizen);
        } else if (getIntent().hasExtra("facility")) {
            viewModel.getSelectedBottomNumber().setValue(1);
        }

        //ApiCalls.getInstance().getFacilitiesByCity();
        //else
        // attachFragment(HomeFragment.class);
    }

    private void addObservers() {
        viewModel.getSelectedBottomNumber().observe(this, integer -> {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.eighty)
                    , getResources().getDimensionPixelSize(R.dimen.eighty));
            FrameLayout.LayoutParams lps = new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.fifty_six)
                    , getResources().getDimensionPixelSize(R.dimen.fifty_six));
//            float size = getResources().getDimension(R.dimen.forty_eight_sp);

            @ColorInt int color = getColor(R.color.transparent_white);
            @ColorInt int colorLight = getColor(R.color.light_blue);
            Log.d(TAG, "addObservers: case 3 : stages " + viewModel.getHealthFacility().getCompletedStages());
            switch (integer) {
                case 1:
                    if (navController.getCurrentDestination().getId() != R.id.recyclerFacilityList) {
                        navController.navigate(R.id.registerFacilityPrimaryInfo);
                        navController.popBackStack(R.id.registerFacilityPrimaryInfo, false);
                        setBottomNavCardsToDefault();
                    }
                    setValuesInc1(lp, color, size);


                    if (viewModel.getHealthFacility().getCompletedStages() == 0) {
                        setValuesInc2(lps, colorLight, smallSize);
                        setValuesInc3(lps, colorLight, smallSize);
                    }
                    if (viewModel.getHealthFacility().getCompletedStages() == 1) {
                        setValuesInc3(lps, colorLight, smallSize);
                    }


                    break;
                case 2:
                    if (viewModel.getHealthFacility().getCompletedStages() >= 1) {
                        if (navController.getCurrentDestination().getId() != R.id.facilityInfo) {
                            navController.navigate(R.id.facilityInfo);
                            navController.popBackStack(R.id.facilityInfo, false);
                        }

                        setBottomNavCardsToDefault();
                        setValuesInc2(lp, color, size);

                        if (viewModel.getHealthFacility().getCompletedStages() < 2) {
                            setValuesInc3(lps, colorLight, smallSize);
                        }
                    }
                    break;
                case 3:

                    if (viewModel.getHealthFacility().getCompletedStages() > 1) {
                        if (navController.getCurrentDestination().getId() != R.id.serviceInfo) {
                            navController.navigate(R.id.serviceInfo);
                            navController.popBackStack(R.id.serviceInfo, false);
                        }
                        setBottomNavCardsToDefault();
                        setValuesInc3(lp, color, size);
                    }
            }
        });
    }

    private void setBottomNavCardsToDefault() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.fifty_six)
                , getResources().getDimensionPixelSize(R.dimen.fifty_six));
//        float size = getResources().getDimension(R.dimen.thirty_two_sp);
        @ColorInt int color = getColor(R.color.blue);
        setValuesInc1(lp, color, smallSize);
        setValuesInc2(lp, color, smallSize);
        setValuesInc3(lp, color, smallSize);
    }

    private void setValuesInc1(FrameLayout.LayoutParams lp, @ColorInt int color, float txtSize) {
        binding.includeBottomNav.inc1.card.setCardBackgroundColor(color);
        binding.includeBottomNav.inc1.card.setLayoutParams(lp);
        binding.includeBottomNav.inc1.textView.setTextSize(txtSize);
    }

    private void setValuesInc2(FrameLayout.LayoutParams lp, @ColorInt int color, float txtSize) {
        binding.includeBottomNav.inc2.card.setCardBackgroundColor(color);
        binding.includeBottomNav.inc2.card.setLayoutParams(lp);
        binding.includeBottomNav.inc2.textView.setTextSize(txtSize);
    }

    private void setValuesInc3(FrameLayout.LayoutParams lp, @ColorInt int color, float txtSize) {
        binding.includeBottomNav.inc3.card.setCardBackgroundColor(color);
        binding.includeBottomNav.inc3.card.setLayoutParams(lp);
        binding.includeBottomNav.inc3.textView.setTextSize(txtSize);
    }


    @Override
    public void onBackPressed() {
        switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
            case R.id.register_citizen:
            case R.id.registerFacilityPrimaryInfo:
                navController.popBackStack(R.id.homeFragment, false);
                return;
            case R.id.facilityInfo:
                viewModel.getSelectedBottomNumber().setValue(1);
                return;
            case R.id.serviceInfo:
                viewModel.getSelectedBottomNumber().setValue(2);
                return;
            case R.id.homeFragment:
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
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    binding.cardMenu.setVisibility(View.GONE);
                    binding.includeBottomNav.btnMenu.setVisibility(View.INVISIBLE);
                } else {
                    if (Objects.requireNonNull(navController.getCurrentDestination()).getId() != R.id.registerFacilityPrimaryInfo ||
                            Objects.requireNonNull(navController.getCurrentDestination()).getId() != R.id.facilityInfo ||
                            Objects.requireNonNull(navController.getCurrentDestination()).getId() != R.id.serviceInfo)
                        binding.cardMenu.setVisibility(View.VISIBLE);
                    binding.includeBottomNav.btnMenu.setVisibility(View.VISIBLE);
                }
            }
        });

        //BTN MENU LISTENER
        binding.btnMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.END);
            binding.cardMenu.setVisibility(View.GONE);
        });

        //BTN NAV MENU CLOSE
        binding.btnNavMenuClose.setOnClickListener(v -> binding.drawerLayout.closeDrawer(GravityCompat.END));

        attachNavMenuListener();
        attachBottomNavMenuListener();
    }

    private void attachBottomNavMenuListener() {
        binding.includeBottomNav.btnMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.END);
            binding.includeBottomNav.btnMenu.setVisibility(View.INVISIBLE);
        });

        binding.includeBottomNav.card1.setOnClickListener(v -> {
            viewModel.getSelectedBottomNumber().setValue(1);
        });

        binding.includeBottomNav.card2.setOnClickListener(v -> {
            viewModel.getSelectedBottomNumber().setValue(2);
        });

        binding.includeBottomNav.card3.setOnClickListener(v -> {
            viewModel.getSelectedBottomNumber().setValue(3);

        });
    }

    private void attachNavMenuListener() {
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable @org.jetbrains.annotations.Nullable Bundle arguments) {
//                if(destination.getId()==R.id.homeFragment){
//                    Log.d(TAG, "onDestinationChanged: inside homeFragment");
//                    viewModel.getBaseData().setTitleBarName("Home");
//                }
//                if(destination.getId()==R.id.register){
//                    Log.d(TAG, "onDestinationChanged: inside register");
//                    viewModel.getBaseData().setTitleBarName("Registration");
//                }
            }
        });
        // HOME
        binding.navMenuHome.cardView.setOnClickListener(v -> {
            //navController.popBackStack();
//           navController.navigate(R.id.homeFragment,true);
//            Log.d(TAG, "attachNavMenuClickListener: hm"+navController.getBackStackEntry(R.id.homeFragment));
//            Log.d(TAG, "attachNavMenuClickListener: reg"+navController.getBackStackEntry(R.id.register));
            navController.popBackStack(R.id.homeFragment, false);
            binding.drawerLayout.closeDrawer(GravityCompat.END);
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
    }
}