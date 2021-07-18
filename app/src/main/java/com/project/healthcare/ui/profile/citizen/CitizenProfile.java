package com.project.healthcare.ui.profile.citizen;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.healthcare.R;
import com.project.healthcare.api.ApiCalls;
import com.project.healthcare.data.Citizen;
import com.project.healthcare.data.DialogData;
import com.project.healthcare.database.Database;
import com.project.healthcare.databinding.FragmentCitizenProfileBinding;
import com.project.healthcare.ui.MainActivityViewModel;
import com.project.healthcare.utils.Utils;

import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import static com.project.healthcare.api.ApiCalls.CALL_ID_DELETE_USER;
import static com.project.healthcare.api.ApiCalls.CALL_ID_UPDATE_CITIZEN;


public class CitizenProfile extends Fragment implements Observer {
    private final static String TAG = "Citizen Profile";
    private final Utils.GeneralDialog generalDialog = new Utils.GeneralDialog();
    MainActivityViewModel viewModel;
    FragmentCitizenProfileBinding binding;
    String emailRegex = "^[a-zA-Z0-9._@\\-]{2,}@[a-zA-Z0-9_]*[.][a-zA-Z]+[a-zA-Z.]*$", nameRegex = "^[a-zA-Z\\s]*$";
    Dialog dialog;
    Citizen citizen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_citizen_profile, container, false);
        }
        viewModel.getBaseData().setTitleBarName("Citizen Profile");
        viewModel.getBaseData().setFloatingMenuBtnVisibility(View.VISIBLE);
        setData();
        addTextWatchers();
        addClickListener();
        dialog = Utils.buildProgressDialog(requireActivity());
        generalDialog.buildGeneralDialog(requireActivity(), new DialogData("", "", "OK", "", View.GONE));
        generalDialog.binding.footer.dialogBtnFooterPositive.setOnClickListener(v -> generalDialog.dialog.dismiss());
        ApiCalls.getInstance().addObserver(this);
        return binding.getRoot();
    }

    private void addClickListener() {
        binding.cardUpdate.setOnClickListener(v -> attemptUpdate());
        binding.cardDelete.setOnClickListener(v -> attemptDelete());
    }

    private void attemptDelete() {
        Utils.GeneralDialog gd = new Utils.GeneralDialog();
        gd.buildGeneralDialog(requireActivity(), new DialogData("Confirm", "Are you sure you want to delete your account?", "NO", "YES", View.VISIBLE));
        gd.binding.footer.dialogBtnFooterPositive.setOnClickListener(v -> {
            gd.dialog.dismiss();
        });
        gd.binding.footer.dialogBtnFooterNegative.setOnClickListener(v -> {
            ApiCalls.getInstance().deleteUser("Token " + viewModel.getCitizen().getToken());
            gd.dialog.dismiss();
            if (!dialog.isShowing())
                dialog.show();
        });
        gd.dialog.show();
    }

    private void setData() {
        citizen = viewModel.getCitizen();
        binding.name.editText.setText(citizen.getName());
        binding.email.editText.setText(citizen.getUserName());
        binding.phone.editText.setText(citizen.getPhoneNumber());
    }

    private void attemptUpdate() {
        if (validateAll()) {
            Log.d(TAG, "attemptUpdate: token : " + viewModel.getCitizen().getToken());
            ApiCalls.getInstance().updateCitizen("Token " + viewModel.getCitizen().getToken(), citizen);
            if (!dialog.isShowing())
                dialog.show();
        }
    }

    private void navigateToHome() {
        Navigation.findNavController(binding.getRoot()).popBackStack(R.id.homeFragment, false);
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
                citizen.setName(s.toString());
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
                citizen.setUserName(s.toString());
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
                citizen.setPhoneNumber(s.toString());
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
                citizen.setPassword(s.toString());
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
        changePasswordPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.blue));
    }

    private void changeNamePerValidation(int p, int visible, @ColorInt int c) {
        binding.name.editText.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        binding.name.txtError.setText("Invalid name.");
        binding.name.txtError.setVisibility(visible);
        binding.cardUpdate.setCardBackgroundColor(c);
    }

    private void changeEmailPerValidation(int p, int visible, @ColorInt int c) {
        binding.email.editText.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        binding.email.txtError.setText("Invalid email.");
        binding.email.txtError.setVisibility(visible);
        binding.cardUpdate.setCardBackgroundColor(c);
    }

    private void changePhoneNumberPerValidation(int p, int visible, @ColorInt int c) {
        binding.phone.editText.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        binding.phone.txtError.setText("Invalid phone number.");
        binding.phone.txtError.setVisibility(visible);
        binding.cardUpdate.setCardBackgroundColor(c);
    }

    private void changePasswordPerValidation(int p, int visible, @ColorInt int c) {
        binding.password.editText.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        binding.password.txtError.setText("Password must be at least 6 characters long");
        binding.password.txtError.setVisibility(visible);
        binding.cardUpdate.setCardBackgroundColor(c);
    }


    private boolean validateAll() {
        boolean success = true;
        if (!validateNameRegex(citizen.getName()))
            success = false;
        if (!validateEmailRegex(citizen.getUserName()))
            success = false;
        if (binding.password.editText.getText().length() > 0 && binding.password.editText.getText().length() < 6)
            success = false;
        if (!validatePhoneNumberRegex(citizen.getPhoneNumber()))
            success = false;
        if (success) {
            binding.cardUpdate.setCardBackgroundColor(requireActivity().getColor(R.color.blue));
        } else
            binding.cardUpdate.setCardBackgroundColor(requireActivity().getColor(R.color.light_blue));
        return success;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ApiCalls) {
            if (dialog.isShowing())
                dialog.cancel();
            ApiCalls.ApiCallReturnObjects objs = (ApiCalls.ApiCallReturnObjects) arg;
            switch (objs.getCallId()) {
                case CALL_ID_UPDATE_CITIZEN:
                    if (objs.isSuccess()) {
                        //navigateToLogin();
                        viewModel.setCitizen(citizen);
                        Database db = new Database(requireContext());
                        db.deleteUser();
                        db.insertUser(citizen);
                    } else {

                    }
                    generalDialog.binding.getData().setTitleString(objs.getTitle());
                    generalDialog.binding.getData().setTextString(objs.getText());
                    generalDialog.dialog.setOnDismissListener(dialog -> {
                    });
                    generalDialog.dialog.show();
                    break;
                case CALL_ID_DELETE_USER:
                    if (objs.isSuccess()) {
                        //navigateToLogin();
                        Database db = new Database(requireContext());
                        db.deleteUser();
                        navigateToHome();
                    } else {

                    }
                    generalDialog.binding.getData().setTitleString(objs.getTitle());
                    generalDialog.binding.getData().setTextString(objs.getText());
//                    generalDialog.dialog.setOnDismissListener(dialog -> {});
                    generalDialog.dialog.show();
                    break;
            }
        }
    }
}