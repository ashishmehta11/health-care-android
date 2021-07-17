package com.project.healthcare.ui.otp;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;
import com.project.healthcare.R;
import com.project.healthcare.data.Citizen;
import com.project.healthcare.data.DialogData;
import com.project.healthcare.data.HealthFacility;
import com.project.healthcare.database.Database;
import com.project.healthcare.databinding.ActivityOtpBinding;
import com.project.healthcare.ui.MainActivity;
import com.project.healthcare.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class OtpActivity extends AppCompatActivity {
    ActivityOtpBinding binding;
    private CountDownTimer countDownTimer;
    private boolean allowVerify = false;
    private String phoneNumber;
    private String otp = "";
    private final static String TAG = "OTPActivity";
    Dialog progressDialog;
    Utils.GeneralDialog generalDialog = new Utils.GeneralDialog();
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        binding.incOtp.txtError.setText("Invalid OTP.");
        phoneNumber = "+91" + getIntent().getStringExtra("phone_number");
        progressDialog = Utils.buildProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        generalDialog.buildGeneralDialog(this, new DialogData(
                "Error",
                "",
                "OK",
                "",
                View.GONE
        ));
        generalDialog.binding.footer.dialogBtnFooterPositive.setOnClickListener(v -> generalDialog.dialog.cancel());
        initializeFirebaseAuth();
        initializeCountDownTimer();
        attachClickListener();
        countDownTimer.start();
    }

    private void initializeFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential credential) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                Log.d(TAG, "onVerificationFailed", e);
                if (!generalDialog.dialog.isShowing()) {
                    generalDialog.binding.getData().setTextString("Error");
                    generalDialog.binding.getData().setTextString(e.toString());
                    generalDialog.dialog.show();
                }
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull @NotNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                Log.d(TAG, "onCodeSent:" + s);
                Toast.makeText(getApplicationContext(), "Otp sent to registered number", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = s;
                mResendToken = token;
                // super.onCodeSent(s, forceResendingToken);
            }
        };
        startPhoneNumberVerification();
    }

    private void startPhoneNumberVerification() {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        if (!progressDialog.isShowing())
            progressDialog.show();
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (progressDialog.isShowing())
                        progressDialog.cancel();
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        if (generalDialog.dialog.isShowing())
                            generalDialog.dialog.cancel();
                        generalDialog.binding.getData().setTitleString("Successful");
                        generalDialog.binding.getData().setTextString("You have logged in successfully");
                        generalDialog.dialog.setOnDismissListener(dialog -> {
                            insertUserIntoDB();
                            navigateToMainActivity();
                        });
                        generalDialog.dialog.show();
                        FirebaseUser user = task.getResult().getUser();

                        // Update UI
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.d(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            if (generalDialog.dialog.isShowing())
                                generalDialog.dialog.cancel();
                            generalDialog.binding.getData().setTitleString("Invalid OTP");
                            generalDialog.binding.getData().setTextString("The OTP you entered in incorrect.\nPlease provide a correct OTP.");
                            generalDialog.dialog.show();
                        }
                    }
                });
    }

    private JsonObject intentToJson() {
        JsonObject data = new JsonObject();
        data.addProperty("phone_number", getIntent().getStringExtra("phone_number"));
        data.addProperty("name", getIntent().getStringExtra("name"));
        data.addProperty("user_name", getIntent().getStringExtra("user_name"));
        data.addProperty("token", getIntent().getStringExtra("token"));
        data.addProperty("token", getIntent().getStringExtra("token"));
        data.addProperty("id", getIntent().getStringExtra("id"));
        return data;
    }

    private void insertUserIntoDB() {
        Database db = new Database(getApplicationContext());
        if (getIntent().getStringExtra("user_group").contains("citizen")) {
            db.insertUser(Citizen.fromJson(intentToJson()));
        } else {
            db.insertUser(HealthFacility.fromJson(intentToJson()));
        }
    }

    private void attachClickListener() {
        binding.resendOtp.setOnClickListener(v -> {
            progressDialog.show();
            resendVerificationCode(phoneNumber, mResendToken);
            setVisibility(View.VISIBLE, View.GONE);
            countDownTimer.start();
        });

        binding.cardVerify.setOnClickListener(v -> {
            if (otp.length() == 6) {
                progressDialog.show();
                verifyPhoneNumberWithCode(mVerificationId, otp);
            }
            //navigateToMainActivity();
        });

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