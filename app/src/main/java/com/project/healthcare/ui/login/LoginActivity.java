package com.project.healthcare.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.databinding.ActivityLoginBinding;
import com.project.healthcare.ui.MainActivity;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    LoginActivityViewModel viewModel;
    ActivityLoginBinding binding;
    boolean isEmailCorrect = false, isPassCorrect = false, isPhone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        attachListeners();
    }

    private void attachListeners() {
        //LOGIN BTN CLICK LISTENER
        binding.cardLogin.setOnClickListener(v -> attemptLogin());

        //REGISTER CLICK LISTENER
        binding.txtRegister.setOnClickListener(v -> navigateToRegister());

        //FORGOT PASS CLICK LISTENER
        binding.txtForgotPassword.setOnClickListener(v -> {
            if (!isPhone && isEmailCorrect) {
                navigateToForgotPassword();
            }
        });

        addTextWatchers();
    }

    private void navigateToRegister() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("register", true);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void navigateToForgotPassword() {
    }


    private void attemptLogin() {
        if (isEmailCorrect && isPassCorrect) {

        }
    }

    private void addTextWatchers() {
        binding.email.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    String emailRegex = "^[a-zA-Z0-9._@\\-]{2,}@[a-zA-Z0-9_]*[.][a-zA-Z.]+$";
                    String numberRegex = "^[0-9]{10}$";
                    if (!Pattern.matches(emailRegex, s.toString())
                            && !Pattern.matches(numberRegex, s.toString())) {
                        setEmailIncorrect();
                        isPhone = false;
                    } else {
                        isPhone = !Pattern.matches(emailRegex, s.toString());
                        setEmailCorrect();
                    }
                } else {
                    setEmailCorrect();
                    isEmailCorrect = false;
                    setLoginBtnColor(R.color.light_blue);
                    setForgotPasswordColor(R.color.light_blue);
                }
            }
        });

        binding.incPass.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 0) {
                    setPasswordCorrect();
                    isPassCorrect = false;
                    setLoginBtnColor(R.color.light_blue);
                } else {
                    if (s.length() >= 6) {
                        setPasswordCorrect();
                    } else {
                        setPasswordIncorrect();
                    }
                }
            }
        });
    }

    private void setLoginBtnColor(int p) {
        binding.cardLogin.setCardBackgroundColor(getResources().getColor(p, getTheme()));
    }

    private void setForgotPasswordColor(int p) {
        binding.txtForgotPassword.setTextColor(getResources().getColor(p, getTheme()));
    }

    private void setPasswordIncorrect() {
        binding.incPass.editText.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.edit_text_bg_error));
        binding.incPass.txtError.setVisibility(View.VISIBLE);
        binding.incPass.txtError.setText("Password must be a least 6 characters long.");
        isPassCorrect = false;
        setLoginBtnColor(R.color.light_blue);
    }

    private void setPasswordCorrect() {
        binding.incPass.editText.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
        binding.incPass.txtError.setVisibility(View.GONE);
        isPassCorrect = true;
        if (isEmailCorrect)
            setLoginBtnColor(R.color.blue);
    }

    private void setEmailCorrect() {
        binding.email.editText.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.edit_text_bg));
        binding.email.txtError.setVisibility(View.GONE);
        setForgotPasswordColor(R.color.blue);
        isEmailCorrect = true;
        if (isPassCorrect)
            setLoginBtnColor(R.color.blue);
    }


    private void setEmailIncorrect() {
        binding.email.editText.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.edit_text_bg_error));
        binding.email.txtError.setVisibility(View.VISIBLE);
        binding.email.txtError.setText("Invalid Email/Phone no.");
        setForgotPasswordColor(R.color.light_blue);
        isEmailCorrect = false;
        setLoginBtnColor(R.color.light_blue);
    }
}