package com.project.healthcare.ui.otp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.project.healthcare.R;
import com.project.healthcare.databinding.ActivityOtpBinding;
import com.project.healthcare.ui.MainActivity;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class OtpActivity extends AppCompatActivity {
    ActivityOtpBinding binding;
    private CountDownTimer countDownTimer;
    private boolean allowVerify = false;
    private String phoneNumber;
    private String otp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        binding.incOtp.txtError.setText("Invalid OTP.");
        phoneNumber = getIntent().getStringExtra("phone_number");
        initializeCountDownTimer();
        attachClickListener();
        countDownTimer.start();
    }

    private void attachClickListener() {
        binding.resendOtp.setOnClickListener(v -> {
            setVisibility(View.VISIBLE, View.GONE);
            countDownTimer.start();
            Toast.makeText(this, "New OTP sent!", Toast.LENGTH_SHORT).show();
        });

        binding.cardVerify.setOnClickListener(v -> navigateToMainActivity());

        addTextWatcher();
    }

    private void addTextWatcher() {
        binding.incOtp.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                otp = s.toString();
                if (!s.toString().isEmpty()) {
                    if (!Pattern.matches("^[0-9]{6}$", s.toString())) {
                        validateOtp(R.drawable.edit_text_bg_error, View.VISIBLE, R.color.light_blue, android.R.color.holo_red_light, false);
                    } else {
                        validateOtp(R.drawable.edit_text_bg, View.GONE, R.color.blue, R.color.blue, true);
                    }
                } else {
                    validateOtp(R.drawable.edit_text_bg, View.GONE, R.color.light_blue, android.R.color.holo_red_light, false);
                }
            }
        });
    }

    private void validateOtp(int p, int gone, int p2, int p3, boolean b) {
        binding.incOtp.editText.setBackground(ResourcesCompat.getDrawable(getResources(), p, getTheme()));
        binding.incOtp.txtError.setVisibility(gone);
        binding.cardVerify.setCardBackgroundColor(getColor(p2));
        binding.incOtp.textInputLayout.setCounterTextColor(ColorStateList.valueOf(getColor(p3)));
        allowVerify = b;
    }

    private void navigateToMainActivity() {
        if (allowVerify) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void initializeCountDownTimer() {
        setVisibility(View.VISIBLE, View.GONE);
        countDownTimer = new CountDownTimer(1 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.txtTimer.setText(String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                binding.txtTimer.setText("00:00");
                setVisibility(View.GONE, View.VISIBLE);
            }
        };
    }

    private void setVisibility(int timer, int otp) {
        binding.txtTimer.setVisibility(timer);
        binding.resendOtp.setVisibility(otp);
    }

    @Override
    protected void onStop() {
        countDownTimer.cancel();
        super.onStop();
    }
}