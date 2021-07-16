package com.project.healthcare.ui.registration.citizen;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.api.ApiCalls;
import com.project.healthcare.databinding.FragmentRegisterCitizenBinding;
import com.project.healthcare.ui.MainActivityViewModel;
import com.project.healthcare.ui.login.LoginActivity;
import com.project.healthcare.utils.Utils;

import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;


public class RegisterCitizen extends Fragment implements Observer {
    MainActivityViewModel viewModel;
    FragmentRegisterCitizenBinding binding;
    private final static String TAG = "Register Fragment";
    String emailRegex = "^[a-zA-Z0-9._@\\-]{2,}@[a-zA-Z0-9_]*[.][a-zA-Z]+[a-zA-Z.]*$", nameRegex = "^[a-zA-Z\\s]*$";
    Dialog dialog;

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
        dialog = Utils.buildProgressDialog(requireActivity());
        ApiCalls.getInstance().addObserver(this);
        return binding.getRoot();
    }

    private void attemptRegistration() {
        if (validateAll()) {
            ApiCalls.getInstance().registerCitizen(viewModel.getCitizen());
            if (!dialog.isShowing())
                dialog.show();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        ApiCalls.getInstance().deleteObserver(this);
        super.onDestroyView();
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
                viewModel.getCitizen().setName(s.toString());
                validateName(s.toString());
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
                viewModel.getCitizen().setUserName(s.toString());
                validateEmail(s.toString());
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
                viewModel.getCitizen().setPhoneNumber(s.toString());
                validatePhoneNumber(s.toString());
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
                viewModel.getCitizen().setPassword(s.toString());
                validatePassword(s.toString());
            }
        });

    }

    private boolean validateNameRegex(String s) {
        return Pattern.matches(nameRegex, s);
    }

    private boolean validateEmailRegex(String s) {
        return Pattern.matches(emailRegex, s);
    }

    private boolean validatePhoneNumberRegex(String ss) {
        return Pattern.matches("^[0-9]{10}$", ss);
    }


    private void validateName(String ss) {
        if (!ss.isEmpty()) {
            if (!validateNameRegex(ss)) {
                changeNamePerValidation(R.drawable.edit_text_bg_error, View.VISIBLE, requireActivity().getColor(R.color.light_blue));
                return;
            } else {
                if (validateAll())
                    changeNamePerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.blue));
                else
                    changeNamePerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));

                return;
            }
        }
        changeNamePerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));
    }

    private void validateEmail(String ss) {
        if (!ss.isEmpty()) {
            if (!validateEmailRegex(ss)) {
                changeEmailPerValidation(R.drawable.edit_text_bg_error, View.VISIBLE, requireActivity().getColor(R.color.light_blue));
                return;
            } else {
                if (validateAll())
                    changeEmailPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.blue));
                else
                    changeEmailPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));

                return;
            }
        }
        changeEmailPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));
    }

    private void validatePhoneNumber(String ss) {
        if (!ss.isEmpty()) {
            if (!validatePhoneNumberRegex(ss)) {
                changePhoneNumberPerValidation(R.drawable.edit_text_bg_error, View.VISIBLE, requireActivity().getColor(R.color.light_blue));
                return;
            } else {
                if (validateAll())
                    changePhoneNumberPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.blue));
                else
                    changePhoneNumberPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));

                return;
            }
        }
        changePhoneNumberPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));
    }

    private void validatePassword(String ss) {
        if (!ss.isEmpty()) {
            if (viewModel.getCitizen().getPassword().length() < 6) {
                changePasswordPerValidation(R.drawable.edit_text_bg_error, View.VISIBLE, requireActivity().getColor(R.color.light_blue));
                return;
            } else {
                if (validateAll())
                    changePasswordPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.blue));
                else
                    changePasswordPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));

                return;
            }
        }
        changePasswordPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));
    }

    private void changeNamePerValidation(int p, int visible, @ColorInt int c) {
        binding.name.editText.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        binding.name.txtError.setText("Invalid name.");
        binding.name.txtError.setVisibility(visible);
        binding.cardLogin.setCardBackgroundColor(c);
    }

    private void changeEmailPerValidation(int p, int visible, @ColorInt int c) {
        binding.email.editText.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        binding.email.txtError.setText("Invalid email.");
        binding.email.txtError.setVisibility(visible);
        binding.cardLogin.setCardBackgroundColor(c);
    }

    private void changePhoneNumberPerValidation(int p, int visible, @ColorInt int c) {
        binding.phone.editText.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        binding.phone.txtError.setText("Invalid phone number.");
        binding.phone.txtError.setVisibility(visible);
        binding.cardLogin.setCardBackgroundColor(c);
    }

    private void changePasswordPerValidation(int p, int visible, @ColorInt int c) {
        binding.password.editText.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        binding.password.txtError.setText("Password must be at least 6 characters long");
        binding.password.txtError.setVisibility(visible);
        binding.cardLogin.setCardBackgroundColor(c);
    }


    private boolean validateAll() {
        boolean success = true;
        if (!validateNameRegex(viewModel.getHealthFacility().getName()))
            success = false;
        if (!validateEmailRegex(viewModel.getCitizen().getUserName()))
            success = false;
        if (viewModel.getCitizen().getPassword().length() < 6)
            success = false;

        if (success) {
            binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
        } else
            binding.cardLogin.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
        return success;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ApiCalls) {
            if (dialog.isShowing())
                dialog.cancel();
            ApiCalls.ApiCallReturnObjects objs = (ApiCalls.ApiCallReturnObjects) arg;
            switch (objs.getCallId()) {
                case 3:
                    if (objs.isSuccess()) {
                        navigateToLogin();
                        Toast.makeText(viewModel.getApplication().getApplicationContext(), objs.getTitle() + " : -> " + objs.getText(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(viewModel.getApplication().getApplicationContext(), objs.getTitle() + " : -> " + objs.getText(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}