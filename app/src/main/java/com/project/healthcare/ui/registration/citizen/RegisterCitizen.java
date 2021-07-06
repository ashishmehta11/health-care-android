package com.project.healthcare.ui.registration.citizen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.databinding.FragmentRegisterCitizenBinding;
import com.project.healthcare.ui.MainActivityViewModel;
import com.project.healthcare.ui.login.LoginActivity;

import java.util.regex.Pattern;


public class RegisterCitizen extends Fragment {
    MainActivityViewModel viewModel;
    FragmentRegisterCitizenBinding binding;
    //    ArrayAdapter<CharSequence> statesAdapter;
//    ArrayAdapter<CharSequence> citiesAdapter;
//    String selectedCity, selectedState = "Gujarat";
    private final static String TAG = "Register Fragment";
    String emailRegex = "^[a-zA-Z0-9._@\\-]{2,}@[a-zA-Z0-9_]*[.][a-zA-Z.]+$";
    private boolean isNameCorrect = false, isEmailCorrect = false, isPhoneCorrect = false, isPasswordCorrect = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_citizen, container, false);
        }
        viewModel.getBaseData().setTitleBarName("Citizen");
        viewModel.getBaseData().setFloatingMenuBtnVisibility(View.VISIBLE);
        addTextWatchers();
        binding.cardLogin.setOnClickListener(v -> attemptRegistration());
        return binding.getRoot();
    }

    private void attemptRegistration() {
        if (isEmailCorrect && isPasswordCorrect && isNameCorrect && isPhoneCorrect) {
            navigateToLogin();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void addTextWatchers() {
        binding.name.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()) {
                    if (!Pattern.matches("^[a-zA-Z\\s]*$", s.toString())) {
                        isNameCorrect = false;
                        binding.name.txtError.setVisibility(View.VISIBLE);
                        binding.name.txtError.setText("Invalid Name.");
                        binding.name.editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_bg, requireActivity().getTheme()));
                        binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
                    } else {
                        isNameCorrect = true;
                        binding.name.txtError.setVisibility(View.GONE);
                        binding.name.editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_bg, requireActivity().getTheme()));
                        if (isPasswordCorrect && isEmailCorrect && isPhoneCorrect)
                            binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
                    }

                } else {
                    isNameCorrect = false;
                    binding.name.txtError.setVisibility(View.GONE);
                    binding.name.editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_bg, requireActivity().getTheme()));
                    binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
                }
            }
        });

        binding.email.editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    if (!Pattern.matches(emailRegex, s.toString())) {
                        isNameCorrect = false;
                        binding.email.txtError.setVisibility(View.VISIBLE);
                        binding.email.txtError.setText("Invalid Email.");
                        binding.email.editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_bg, requireActivity().getTheme()));
                        binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
                    } else {
                        isEmailCorrect = true;
                        binding.email.txtError.setVisibility(View.GONE);
                        binding.email.editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_bg, requireActivity().getTheme()));
                        if (isPasswordCorrect && isNameCorrect && isPhoneCorrect)
                            binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
                    }

                } else {
                    isEmailCorrect = false;
                    binding.email.txtError.setVisibility(View.GONE);
                    binding.email.editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_bg, requireActivity().getTheme()));
                    binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
                }
            }
        });

        binding.phone.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    if (!Pattern.matches("^[0-9]{10}$", s.toString())) {
                        isPhoneCorrect = false;
                        binding.phone.txtError.setVisibility(View.VISIBLE);
                        binding.phone.txtError.setText("Invalid Phone.");
                        binding.phone.editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_bg, requireActivity().getTheme()));
                        binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
                    } else {
                        isPhoneCorrect = true;
                        binding.phone.txtError.setVisibility(View.GONE);
                        binding.phone.editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_bg, requireActivity().getTheme()));
                        if (isPasswordCorrect && isEmailCorrect && isNameCorrect)
                            binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
                    }

                } else {
                    isPhoneCorrect = false;
                    binding.phone.txtError.setVisibility(View.GONE);
                    binding.phone.editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_bg, requireActivity().getTheme()));
                    binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
                }
            }
        });

        binding.password.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    if (s.toString().length() < 6) {
                        isPasswordCorrect = false;
                        binding.password.txtError.setVisibility(View.VISIBLE);
                        binding.password.txtError.setText("Password must be at least 6 characters long.");
                        binding.password.editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_bg, requireActivity().getTheme()));
                        binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
                    } else {
                        isPasswordCorrect = true;
                        binding.password.txtError.setVisibility(View.GONE);
                        binding.password.editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_bg, requireActivity().getTheme()));
                        if (isNameCorrect && isEmailCorrect && isPhoneCorrect)
                            binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
                    }

                } else {
                    isPasswordCorrect = false;
                    binding.password.txtError.setVisibility(View.GONE);
                    binding.password.editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_bg, requireActivity().getTheme()));
                    binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
                }
            }
        });
    }

}