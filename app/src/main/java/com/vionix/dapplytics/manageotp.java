package com.vionix.dapplytics;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class manageotp extends AppCompatActivity
{
    EditText otp,otp1,otp2,otp3,otp4,otp5;
    TextView mobileno,resend,countdoun;
    Button verify;
    String getbackendotp;
    ProgressBar pbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageotp);
        otp = findViewById(R.id.otp);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        mobileno = findViewById(R.id.mobileno);
        resend = findViewById(R.id.resend);
        verify = findViewById(R.id.verify);
        pbar = findViewById(R.id.pbar);
        countdoun = findViewById(R.id.countdoun);



        mobileno.setText(String.format(
                "+91-%s",getIntent().getStringExtra("mobile")

        ));
        getbackendotp =getIntent().getStringExtra("backendotp");
        resend.setEnabled(false);
        resend.setTextColor(Color.GRAY);
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdoun.setVisibility(View.VISIBLE);
                countdoun.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                countdoun.setVisibility(View.GONE);
                resend.setEnabled(true);
                resend.setTextColor(Color.BLACK);

            }

        }.start();

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!otp.getText().toString().isEmpty() && !otp1.getText().toString().trim().isEmpty() && !otp2.getText().toString().trim().isEmpty() && !otp3.getText().toString().trim().isEmpty() && !otp4.getText().toString().trim().isEmpty() && !otp5.getText().toString().trim().isEmpty()) {
                    String enterotp = otp.getText().toString()+
                            otp1.getText().toString()+
                            otp2.getText().toString()+
                            otp3.getText().toString()+
                            otp4.getText().toString()+
                            otp5.getText().toString();

                    if(getbackendotp!=null){
                        pbar.setVisibility(View.VISIBLE);
                        verify.setVisibility(View.INVISIBLE);


                        PhoneAuthCredential PhoneAuthCredential = PhoneAuthProvider.getCredential(
                                getbackendotp,enterotp
                        );
                        FirebaseAuth.getInstance().signInWithCredential(PhoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        pbar.setVisibility(View.GONE);
                                        verify.setVisibility(View.VISIBLE);

                                        if(task.isSuccessful()){
                                            FirebaseUser user = task.getResult().getUser();
                                            long creationTimestamp = user.getMetadata().getCreationTimestamp();
                                            long lastSignInTimestamp = user.getMetadata().getLastSignInTimestamp();
                                            if (creationTimestamp == lastSignInTimestamp) {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();

                                            }

                                        }else {
                                            Toast.makeText(com.vionix.dapplytics.manageotp.this, "Enter The Correct OTP", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(com.vionix.dapplytics.manageotp.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(com.vionix.dapplytics.manageotp.this, "Verifying OTP", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(com.vionix.dapplytics.manageotp.this, "Please Enter All Numbers", Toast.LENGTH_SHORT).show();

                }
            }
        });
        numberotpmove();
        TextView resendlabel =findViewById(R.id.resend);
        resendlabel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resend.setEnabled(false);
                resend.setTextColor(Color.GRAY);
                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        countdoun.setVisibility(View.VISIBLE);
                        countdoun.setText("seconds remaining: " + millisUntilFinished / 1000);
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        countdoun.setVisibility(View.GONE);
                        resend.setEnabled(true);
                        resend.setTextColor(Color.BLACK);

                    }

                }.start();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        mobileno.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        com.vionix.dapplytics.manageotp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(com.vionix.dapplytics.manageotp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String Newbackendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                getbackendotp = Newbackendotp;
                                resend.setTextColor(Color.BLACK);
                                Toast.makeText(com.vionix.dapplytics.manageotp.this, "OTP Sent Succussfully", Toast.LENGTH_SHORT).show();

                            }
                        }
                );

            }

        });
    }

    private void numberotpmove() {
        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if (!s.toString().trim().isEmpty()){
                    otp1.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if (!s.toString().trim().isEmpty()){
                    otp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if (!s.toString().trim().isEmpty()){
                    otp3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if (!s.toString().trim().isEmpty()){
                    otp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if (!s.toString().trim().isEmpty()){
                    otp5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if (!s.toString().trim().isEmpty()){
                    verify.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


}