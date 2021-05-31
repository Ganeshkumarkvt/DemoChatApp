package com.example.demochatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText username, email, password;
    Button registerbtn;
    FirebaseAuth auth;
    DatabaseReference reference;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();

        registerbtn.setOnClickListener(v -> {
            String txtusername = username.getText().toString();
            String txtemail = email.getText().toString();
            String txtpassword = password.getText().toString();

            if (TextUtils.isEmpty(txtusername) || TextUtils.isEmpty(txtemail) || TextUtils.isEmpty(txtpassword)){
                Toast.makeText(RegisterActivity.this, "All fields need to be filled", Toast.LENGTH_SHORT).show();
            }else if (txtpassword.length() < 6){
                Toast.makeText(RegisterActivity.this, "Password must be atleast 6 characters", Toast.LENGTH_SHORT).show();
            }else {
                register(txtusername, txtemail, txtpassword);
            }
        });
    }

    public void register(String username, String email, String password) {

        auth.createUserWithEmailAndPassword( email, password)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()){
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                String userid = firebaseUser.getUid();

                                reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", userid);
                                hashMap.put("username", username);
                                hashMap.put("status", "offline");
                                hashMap.put("imgurl", "default");
                                hashMap.put("search", username.toLowerCase());

                                reference.setValue(hashMap).addOnCompleteListener(
                                        task1 -> {
                                            if (task1.isSuccessful()){
                                               Intent intent = new Intent( RegisterActivity.this, MainActivity.class);
                                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                               startActivity(intent);
                                               finish();
                                            }
                                        }
                                );

                            }else {
                                Toast.makeText(RegisterActivity.this, "You can't use this email or password for registration", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

    }

}