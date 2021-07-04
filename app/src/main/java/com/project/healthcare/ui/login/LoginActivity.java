package com.project.healthcare.ui.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    LoginActivityViewModel viewModel;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    }
}