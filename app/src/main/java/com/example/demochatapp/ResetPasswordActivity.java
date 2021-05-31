package com.example.demochatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.analytics.FirebaseAnalytics;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText SendEmail;
    Button btn_reset;

    private FirebaseAnalytics mFirebaseAnalytics;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SendEmail = findViewById(R.id.send_email);
        btn_reset = findViewById(R.id.resetbtn);
        firebaseAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(v -> {

            String email = SendEmail.getText().toString();
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this, "Please check your email and reset your password", Toast.LENGTH_SHORT).show();
                        }else{
                            String error = Objects.requireNonNull(task.getException()).getMessage();
                            Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
            );

        });

    }
}