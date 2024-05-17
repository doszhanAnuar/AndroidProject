package com.example.myapplication;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private TextView signupText;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setObservers();
        setView();
    }
    private void setView() {
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        signupText = findViewById(R.id.signupText);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            Log.d("Login", "Login button clicked");  // Проверка, срабатывает ли обработчик
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            viewModel.login(email, password);
        });
        signupText.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    private void setObservers() {
        viewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if (user != null) {
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Log.d("Login", "Login successful");
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}