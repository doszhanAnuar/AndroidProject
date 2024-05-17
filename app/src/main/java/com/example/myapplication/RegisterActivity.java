package com.example.myapplication;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.mindrot.jbcrypt.BCrypt;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailField, passwordField, nameField, verificationCode;
    private CardView register, verification;

    private RegisterViewModel viewModel;


    private Button registerButton, verButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        setView();
        setListeners();
    }

    private void setView() {
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        nameField = findViewById(R.id.name);
        registerButton = findViewById(R.id.registerButton);
        register = findViewById(R.id.registerCard);
        verification = findViewById(R.id.enterCode);
        verButton = findViewById(R.id.verButton);
        verificationCode = findViewById(R.id.verification_code);
    }


    private void setListeners() {
        viewModel.getRegistrationLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if (user != null) {
                    Toast.makeText(RegisterActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Log.d("Login", "Login successful");
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        registerButton.setOnClickListener(view -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String name = nameField.getText().toString().trim();
            if (password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "The password should be at least 6 characters long.", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.registerUser(email, password, name);
            }
        });
    }
}