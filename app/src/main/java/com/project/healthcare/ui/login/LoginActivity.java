package com.project.healthcare.ui.login;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.project.healthcare.api.ApiCalls;
import com.project.healthcare.data.Citizen;
import com.project.healthcare.data.DialogData;
import com.project.healthcare.data.HealthFacility;
import com.project.healthcare.databinding.ActivityLoginBinding;
import com.project.healthcare.databinding.DialogRegistrationChoiceBinding;
import com.project.healthcare.ui.MainActivity;
import com.project.healthcare.ui.otp.OtpActivity;
import com.project.healthcare.utils.Utils;

import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import static com.project.healthcare.api.ApiCalls.CALL_ID_LOGIN;

public class LoginActivity extends AppCompatActivity implements Observer {
    LoginActivityViewModel viewModel;
    ActivityLoginBinding binding;
    boolean isEmailCorrect = false, isPassCorrect = false, isPhone = false;
    private final static String TAG = "LoginActivity";
    Dialog dialog, progressDialog;
    Utils.GeneralDialog generalDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        generalDialog = new Utils.GeneralDialog();
        generalDialog.buildGeneralDialog(LoginActivity.this, new DialogData(
                "", "", "OK", "CANCEL", View.GONE));
        progressDialog = Utils.buildProgressDialog(LoginActivity.this);
        ApiCalls.getInstance().addObserver(this);

        attachListeners();
    }


    private void attachListeners() {
        //LOGIN BTN CLICK LISTENER
        binding.cardLogin.setOnClickListener(v -> attemptLogin());

        //REGISTER CLICK LISTENER
        binding.txtRegister.setOnClickListener(v -> showDialog());

        //FORGOT PASS CLICK LISTENER
        binding.txtForgotPassword.setOnClickListener(v -> {
            if (!isPhone && isEmailCorrect) {
                navigateToForgotPassword();
            }
        });

        generalDialog.binding.footer.dialogBtnFooterPositive.setOnClickListener(v -> {
            if (generalDialog.dialog.isShowing()) {
                generalDialog.dialog.dismiss();
            }
        });

        addTextWatchers();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_Dialog_Alert);
        DialogData dialogData = new DialogData("Registration", "", "OK", "", View.GONE);
        DialogRegistrationChoiceBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_registration_choice, null, false);
        binding.setData(dialogData);
        builder.setView(binding.getRoot());
        dialog = builder.create();

        binding.radioGroup.check(R.id.radioButtonGeneral);
        binding.header.btnCloseDialog.setOnClickListener(v -> {
            if (dialog.isShowing())
                dialog.cancel();
        });
        binding.footer.dialogBtnFooter.setOnClickListener(v -> {
            if (binding.radioButtonFacility.isChecked()) navigateToRegister("facility");
            else navigateToRegister("citizen");
        });
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        // dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void navigateToRegister(String msg) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(msg, true);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void navigateToForgotPassword() {
    }


    private void attemptLogin() {
        if (isEmailCorrect && isPassCorrect) {

            progressDialog.show();
            ApiCalls.getInstance().login(binding.email.editText.getText().toString(),
                    binding.incPass.editText.getText().toString());
        }
    }

    @Override
    protected void onStop() {
        if (dialog != null && dialog.isShowing())
            dialog.cancel();
//        if (generalDialog.dialog.isShowing())
//            generalDialog.dialog.cancel();
        if (progressDialog.isShowing())
            progressDialog.cancel();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ApiCalls.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    private void navigateToOtp(Object obj) {
        Intent i = new Intent(this, OtpActivity.class);

        if (obj instanceof Citizen) {
            Citizen c = (Citizen) obj;
            i.putExtra("obj", c);
            i.putExtra("phone_number", c.getPhoneNumber());
        } else {
            HealthFacility c = (HealthFacility) obj;
            i.putExtra("obj", c);
            i.putExtra("phone_number", c.getPhoneNumbers().get(0));
        }
        startActivity(i);
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
                    String emailRegex = "^[a-zA-Z0-9._@\\-]{2,}@[a-zA-Z0-9_]*[.][a-zA-Z]+[a-zA-Z.]*$";
                    if (!Pattern.matches(emailRegex, s.toString().trim())) {
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
                if (s.toString().trim().length() <= 0) {
                    setPasswordCorrect();
                    isPassCorrect = false;
                    setLoginBtnColor(R.color.light_blue);
                } else {
                    if (s.toString().trim().length() >= 6) {
                        setPasswordCorrect();
                    } else {
                        setPasswordIncorrect();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        navigateToHome();
        super.onBackPressed();
    }

    private void navigateToHome() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
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
        binding.incPass.txtError.setText("Invalid Password.");
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
        binding.email.txtError.setText("Invalid Email.");
        setForgotPasswordColor(R.color.light_blue);
        isEmailCorrect = false;
        setLoginBtnColor(R.color.light_blue);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ApiCalls) {
            if (progressDialog.isShowing())
                progressDialog.cancel();
            ApiCalls.ApiCallReturnObjects objs = (ApiCalls.ApiCallReturnObjects) arg;
            switch (objs.getCallId()) {
                case CALL_ID_LOGIN:
                    if (objs.isSuccess()) {
                        navigateToOtp(objs.getData());
                    } else {
                        generalDialog.binding.getData().setTitleString(objs.getTitle());
                        generalDialog.binding.getData().setTextString(objs.getText());
                        generalDialog.dialog.setOnDismissListener(dialog -> {
                        });
                        generalDialog.dialog.show();
                        //Toast.makeText(viewModel.getApplication().getApplicationContext(), objs.getTitle() + " : -> " + objs.getText(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}