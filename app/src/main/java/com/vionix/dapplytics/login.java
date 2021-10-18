package com.vionix.dapplytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {

    Button sendotp ,withoutlogin;
    EditText etphone;
    ProgressBar pbar ,withoutloginpbar;
    private static final String TAG = "my tag";
    private  final static  int RC_SIGN_IN = 124;
    private FirebaseAuth mAuth;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    public void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(login.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            getlocation();
        } else {
            ActivityCompat.requestPermissions(login.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        if (ActivityCompat.checkSelfPermission(login.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(login.this, new String[]{Manifest.permission.SEND_SMS}, 44);
        }
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        withoutloginpbar.setVisibility(View.INVISIBLE);
        withoutlogin.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etphone = findViewById(R.id.etPhone);
        sendotp = findViewById(R.id.sendotp);
        pbar = findViewById(R.id.pbar);
        withoutloginpbar = findViewById(R.id.withoutloginpbar);
        withoutlogin = findViewById(R.id.withoutlogin);




        withoutlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withoutloginpbar.setVisibility(View.VISIBLE);
                withoutlogin.setVisibility(View.INVISIBLE);
                Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        mAuth = FirebaseAuth.getInstance();



        //used to keep login
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            // User is signed in
            Intent i = new Intent(login.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");

            //Login with number
            sendotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!etphone.getText().toString().trim().isEmpty()) {
                        if ((etphone.getText().toString().trim()).length() == 10) {
                            pbar.setVisibility(View.VISIBLE);
                            sendotp.setVisibility(View.INVISIBLE);

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" + etphone.getText().toString(),
                                    60,
                                    TimeUnit.SECONDS,
                                    login.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            pbar.setVisibility(View.GONE);
                                            sendotp.setVisibility(View.VISIBLE);

                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            pbar.setVisibility(View.GONE);
                                            sendotp.setVisibility(View.VISIBLE);
                                            Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            pbar.setVisibility(View.GONE);
                                            sendotp.setVisibility(View.VISIBLE);

                                            Intent intent = new Intent(getApplicationContext(), manageotp.class);
                                            intent.putExtra("mobile", etphone.getText().toString());
                                            intent.putExtra("backendotp", backendotp);
                                            startActivity(intent);
                                        }
                                    }
                            );
                        } else {
                            Toast.makeText(login.this, "Please Enter Correct number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(login.this, "Enter mobile number", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }


    }


}