package com.project.healthcare.ui.registration.facility.primary_info;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.healthcare.R;
import com.project.healthcare.databinding.FragmentRegisterFacilityPrimaryInfoBinding;
import com.project.healthcare.ui.MainActivityViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Pattern;


public class RegisterFacilityPrimaryInfo extends Fragment {

    FragmentRegisterFacilityPrimaryInfoBinding binding;
    MainActivityViewModel viewModel;
    private static final String TAG = "PrimaryInfo";
    String emailRegex = "^[a-zA-Z0-9._@\\-]{2,}@[a-zA-Z0-9_]*[.][a-zA-Z.]+$";
    String nameRegex = "^[a-zA-Z\\s]*$";
    String pinCodeRegex = "^[0-9]{6}$";
    String phoneNumberRegex = "^[0-9]{10}$";
    ArrayAdapter<CharSequence> statesAdapter, cityAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_facility_primary_info, container, false);
        }

        viewModel.getBaseData().setTitleBarName("Primary Info");
        prepareStateSpinner();
        prepareCitySpinner();
        attachListeners();
        addTextWatchers();
        setEditTextData();
        return binding.getRoot();
    }

    private void setEditTextData() {
        binding.dateOfEstablishment.editText.setText(viewModel.getHealthFacility().getEstablishmentDate());
        binding.name.editText.setText(viewModel.getHealthFacility().getName());
        binding.password.editText.setText(viewModel.getHealthFacility().getPassword());
        binding.pinCode.editText.setText(viewModel.getHealthFacility().getPinCode());
        binding.address.editText.setText(viewModel.getHealthFacility().getAddress());
        int statePos = -1;
        for (Map.Entry<String, ArrayList<String>> e : viewModel.getStatesAndCities().entrySet()) {
            statePos++;
            if (e.getKey().contains(viewModel.getHealthFacility().getState())) break;
        }
        if (statePos > -1) {
            binding.spinnerState.setSelection(statePos, true);
            prepareCitySpinner();
        }
        int cityPos = 0;
        for (; cityPos < viewModel.getStatesAndCities().get(viewModel.getHealthFacility().getState()).size(); cityPos++) {
            if (viewModel.getStatesAndCities().get(viewModel.getHealthFacility().getState()).get(cityPos).contains(viewModel.getHealthFacility().getCity()))
                break;
        }
        cityPos--;
        if (cityPos > -1)
            binding.spinnerCity.setSelection(cityPos, true);
        binding.email.editText.setText(viewModel.getHealthFacility().getEmails().get(0));
        for (int i = 1; i < viewModel.getHealthFacility().getEmails().size(); i++) {
            addEmailTextBoxes();
        }
        binding.phone.editText.setText(viewModel.getHealthFacility().getPhoneNumbers().get(0));
        for (int i = 1; i < viewModel.getHealthFacility().getPhoneNumbers().size(); i++) {
            addPhoneTextBoxes();
        }
    }

    private void prepareStateSpinner() {
        statesAdapter = new ArrayAdapter<>(getActivity()
                , R.layout.spinner_list_item_small_font
                , viewModel.getStatesAndCities().keySet().toArray(new String[0]));
        statesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_small_font);
        binding.spinnerState.setAdapter(statesAdapter);
        binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int i = 0;
                for (Map.Entry<String, ArrayList<String>> e : viewModel.getStatesAndCities().entrySet()) {
                    if (i++ == position) {
                        viewModel.getHealthFacility().setState(e.getKey());
                        prepareCitySpinner();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void prepareCitySpinner() {
        cityAdapter = new ArrayAdapter<>(getActivity()
                , R.layout.spinner_list_item_small_font
                , viewModel.getStatesAndCities().get(viewModel.getHealthFacility().getState()).toArray(new String[0]));
        cityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_small_font);
        binding.spinnerCity.setAdapter(cityAdapter);
        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.getHealthFacility().setCity(viewModel.getStatesAndCities().get(viewModel.getHealthFacility().getState()).get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void attachListeners() {
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog startTime = new DatePickerDialog(requireActivity(), (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            binding.dateOfEstablishment.editText.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
            viewModel.getHealthFacility().setEstablishmentDate(dayOfMonth + "-" + monthOfYear + "-" + year);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        binding.dateOfEstablishment.editText.setOnClickListener(v -> {
            startTime.show();
        });
        binding.dateOfEstablishment.editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                startTime.show();
        });

        binding.incMoveRight.btnMoveRight.setOnClickListener(v -> {
            if (viewModel.getHealthFacility().getCompletedStages() > 0) {
                if (viewModel.getHealthFacility().getCompletedStages() < 1)
                    viewModel.getHealthFacility().setCompletedStages(1);
                viewModel.getSelectedBottomNumber().setValue(2);
            }
        });

        //Add More Emails
        binding.btnAddEmails.setOnClickListener(v -> {
            viewModel.getHealthFacility().getEmails().add("");
            addEmailTextBoxes();
        });

        //Add More Phone numbers
        binding.btnAddPhone.setOnClickListener(v -> {
            viewModel.getHealthFacility().getPhoneNumbers().add("");
            addPhoneTextBoxes();
        });
    }

    private void addPhoneTextBoxes() {
        final int tag = viewModel.getHealthFacility().getPhoneNumbers().size() - 1;
        LinearLayout.LayoutParams lp;
        lp = new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 4;
        LinearLayout ll = new LinearLayout(requireActivity());
        ll.setTag(tag);
        ll.setWeightSum(5);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.setPadding(0, 16, 0, 0);
        View txtBox = getLayoutInflater().inflate(R.layout.text_field_number, null, false);
        txtBox.setLayoutParams(lp);
        lp = new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        lp.gravity = Gravity.CENTER;
        ImageButton delete = new ImageButton(requireActivity());
        delete.setBackgroundResource(android.R.color.transparent);
        delete.setLayoutParams(lp);
        delete.setPadding(8, 8, 8, 8);
        delete.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_delete));
        delete.setOnClickListener(v1 -> {
            binding.phoneLinearLayout.removeView(ll);
            viewModel.getHealthFacility().getPhoneNumbers().remove(tag);
            validateName(viewModel.getHealthFacility().getName());
        });
        ll.addView(txtBox);
        ll.addView(delete);
        EditText ed = txtBox.findViewById(R.id.editText);
        TextView tv = txtBox.findViewById(R.id.txtError);
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString().trim();
                viewModel.getHealthFacility().getPhoneNumbers().set(tag, ss);
                validatePhone(ss, ed, tv);
            }
        });
        binding.phoneLinearLayout.addView(ll);
        ed.setText(viewModel.getHealthFacility().getPhoneNumbers().get(tag));
    }

    private void addEmailTextBoxes() {
        final int tag = viewModel.getHealthFacility().getEmails().size() - 1;
        LinearLayout.LayoutParams lp;
        lp = new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 4;
        LinearLayout ll = new LinearLayout(requireActivity());
        ll.setTag(tag);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.setPadding(0, 16, 0, 0);
        ll.setWeightSum(5);
        View txtBox = getLayoutInflater().inflate(R.layout.text_field, null, false);
        txtBox.setLayoutParams(lp);
        lp = new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        lp.gravity = Gravity.CENTER;
        ImageButton delete = new ImageButton(requireActivity());
        delete.setBackgroundResource(android.R.color.transparent);
        delete.setLayoutParams(lp);
        delete.setPadding(8, 8, 8, 8);
        delete.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_delete));
        delete.setOnClickListener(v1 -> {
            binding.emailsLinearLayout.removeView(ll);
            viewModel.getHealthFacility().getEmails().remove(tag);
            validateName(viewModel.getHealthFacility().getName());
        });
        ll.addView(txtBox);
        ll.addView(delete);
        EditText ed = txtBox.findViewById(R.id.editText);
        TextView tv = txtBox.findViewById(R.id.txtError);
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString().trim();
                viewModel.getHealthFacility().getEmails().set(tag, ss);
                validateEmails(ss, ed, tv);
            }
        });
        binding.emailsLinearLayout.addView(ll);
        ed.setText(viewModel.getHealthFacility().getEmails().get(tag));
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
                String ss = s.toString().trim();
                viewModel.getHealthFacility().setName(ss);
                validateName(ss);
            }
        });
        binding.address.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString().trim();
                viewModel.getHealthFacility().setAddress(ss);
                validateAddress(ss);
            }
        });
        binding.pinCode.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString().trim();
                viewModel.getHealthFacility().setPinCode(ss);
                validatePinCode(ss);
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
                String ss = s.toString().trim();
                viewModel.getHealthFacility().setPassword(ss);
                validatePassword(ss);
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
                String ss = s.toString().trim();
                viewModel.getHealthFacility().getEmails().set(0, ss);
                validateEmails(ss, binding.email.editText, binding.email.txtError);
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
                String ss = s.toString().trim();
                viewModel.getHealthFacility().getPhoneNumbers().set(0, ss);
                validatePhone(ss, binding.phone.editText, binding.phone.txtError);
            }
        });
    }

    private boolean validateAll() {
        boolean success = true;
        if (!validateNameRegex(viewModel.getHealthFacility().getName()))
            success = false;
        if (viewModel.getHealthFacility().getAddress().length() < 3)
            success = false;
        if (!validatePinCodeRegex(viewModel.getHealthFacility().getPinCode()))
            success = false;
        if (viewModel.getHealthFacility().getPassword().length() < 6)
            success = false;
        for (int i = 0; i < viewModel.getHealthFacility().getEmails().size(); i++)
            if (!validateEmailRegex(viewModel.getHealthFacility().getEmails().get(i)))
                success = false;
        for (int i = 0; i < viewModel.getHealthFacility().getPhoneNumbers().size(); i++)
            if (!validatePhoneNumberRegex(viewModel.getHealthFacility().getPhoneNumbers().get(i)))
                success = false;
        if (viewModel.getHealthFacility().getEstablishmentDate().isEmpty())
            success = false;

        if (success)
            viewModel.getHealthFacility().setCompletedStages(1);
        else
            viewModel.getHealthFacility().setCompletedStages(0);
        return success;
    }

    private void validateEmails(String ss, EditText ed, TextView tv) {
        if (!ss.isEmpty()) {
            if (!validateEmailRegex(ss)) {
                changeEmailPerValidation(ed, tv, R.drawable.edit_text_bg_error, View.VISIBLE, requireActivity().getColor(R.color.light_blue));
                return;
            } else {
                if (validateAll())
                    changeEmailPerValidation(ed, tv, R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.blue));
                else
                    changeEmailPerValidation(ed, tv, R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));
                return;
            }
        }
        changeEmailPerValidation(ed, tv, R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));
    }

    private void validatePhone(String ss, EditText ed, TextView tv) {
        if (!ss.isEmpty()) {
            if (!validatePhoneNumberRegex(ss)) {
                changePhonePerValidation(ed, tv, R.drawable.edit_text_bg_error, View.VISIBLE, requireActivity().getColor(R.color.light_blue));
                return;
            } else {
                if (validateAll())
                    changePhonePerValidation(ed, tv, R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.blue));
                else
                    changePhonePerValidation(ed, tv, R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));
                return;
            }
        }
        changePhonePerValidation(ed, tv, R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));

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

    private void validateAddress(String ss) {
        if (!ss.isEmpty()) {
            if (ss.length() < 3) {
                changeAddressPerValidation(R.drawable.edit_text_bg_error, View.VISIBLE, requireActivity().getColor(R.color.light_blue));
                return;
            } else {
                if (validateAll())
                    changeAddressPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.blue));
                else
                    changeAddressPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));

                return;
            }
        }
        changeAddressPerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));

    }

    private void validatePinCode(String ss) {
        if (!ss.isEmpty()) {
            if (!validatePinCodeRegex(ss)) {
                changePinCodePerValidation(R.drawable.edit_text_bg_error, View.VISIBLE, requireActivity().getColor(R.color.light_blue));
                return;
            } else {
                if (validateAll())
                    changePinCodePerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.blue));
                else
                    changePinCodePerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));

                return;
            }
        }
        changePinCodePerValidation(R.drawable.edit_text_bg, View.GONE, requireActivity().getColor(R.color.light_blue));

    }

    private void validatePassword(String ss) {
        if (!ss.isEmpty()) {
            if (ss.length() < 6) {
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
        binding.name.txtError.setText("Invalid Name.");
        binding.name.txtError.setVisibility(visible);
        binding.incMoveRight.cardMoveRight.setCardBackgroundColor(c);
    }

    private void changeEmailPerValidation(EditText ed, TextView tv, int p, int visible, @ColorInt int c) {
        ed.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        tv.setText("Invalid Email.");
        tv.setVisibility(visible);
        binding.incMoveRight.cardMoveRight.setCardBackgroundColor(c);
    }

    private void changePhonePerValidation(EditText ed, TextView tv, int p, int visible, @ColorInt int c) {
        ed.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        tv.setText("Invalid Phone Number.");
        tv.setVisibility(visible);
        binding.incMoveRight.cardMoveRight.setCardBackgroundColor(c);
    }

    private void changeAddressPerValidation(int p, int visible, @ColorInt int c) {
        binding.address.editText.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        binding.address.txtError.setText("Address required.");
        binding.address.txtError.setVisibility(visible);
        binding.incMoveRight.cardMoveRight.setCardBackgroundColor(c);
    }

    private void changePinCodePerValidation(int p, int visible, @ColorInt int c) {
        binding.pinCode.editText.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        binding.pinCode.txtError.setText("Invalid Pin Code.");
        binding.pinCode.txtError.setVisibility(visible);
        binding.incMoveRight.cardMoveRight.setCardBackgroundColor(c);
    }

    private void changePasswordPerValidation(int p, int visible, @ColorInt int c) {
        binding.password.editText.setBackground(ResourcesCompat.getDrawable(getResources(), p, requireContext().getTheme()));
        binding.password.txtError.setText("Password must be at least 6 characters long.");
        binding.password.txtError.setVisibility(visible);
        binding.incMoveRight.cardMoveRight.setCardBackgroundColor(c);
    }


    private boolean validateEmailRegex(String s) {
        return Pattern.matches(emailRegex, s);
    }

    private boolean validatePhoneNumberRegex(String s) {
        return Pattern.matches(phoneNumberRegex, s);
    }

    private boolean validatePinCodeRegex(String s) {
        return Pattern.matches(pinCodeRegex, s);
    }

    private boolean validateNameRegex(String s) {
        return Pattern.matches(nameRegex, s);
    }
}