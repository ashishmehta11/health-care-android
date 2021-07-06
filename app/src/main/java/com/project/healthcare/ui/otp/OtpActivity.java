package com.project.healthcare.ui.otp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.project.healthcare.R;
import com.project.healthcare.databinding.ActivityOtpBinding;
import com.project.healthcare.ui.MainActivity;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {
    ActivityOtpBinding binding;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        initializeCountDownTimer();
        attachClickListener();
        countDownTimer.start();
    }

    private void attachClickListener() {
        binding.resendOtp.setOnClickListener(v -> {
            setVisibility(View.VISIBLE, View.GONE);
            countDownTimer.start();
        });

        binding.cardVerify.setOnClickListener(v -> navigateToMainActivity());
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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