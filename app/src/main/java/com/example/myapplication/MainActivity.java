package com.example.myapplication;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;  // Для отладки
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;  // Инициализация Firebase Authentication
    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация полей
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        auth = FirebaseAuth.getInstance();  // Получаем экземпляр Firebase Auth

        // Кнопка входа
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            Log.d("Login", "Login button clicked");  // Проверка, срабатывает ли обработчик
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            Log.d("Login", "Attempting login with email: " + email);  // Отладка email

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Log.d("Login", "Login successful");
                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Log.e("Login", "Login failed: " + errorMessage);
                            Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


        // Текст для перехода к регистрации
        TextView signupText = findViewById(R.id.signupText);
        signupText.setOnClickListener(view -> {
            Log.d("Login", "Signup text clicked");  // Отладка перехода к регистрации
            // Переход на RegisterActivity при клике
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
