package com.example.myapplication;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import org.mindrot.jbcrypt.BCrypt;
import com.example.myapplication.UserDatabase;
public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private EditText emailField, passwordField, nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        nameField = findViewById(R.id.name);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(view -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String name = nameField.getText().toString().trim();

            // Хэширование пароля
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

            registerUser(email, hashedPassword, name);
        });

        Button resendVerificationEmail = findViewById(R.id.resendVerificationEmail);
        resendVerificationEmail.setOnClickListener(view -> {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                sendVerificationEmail(user);
            } else {
                Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(String email, String password, String name) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        saveUserData(user, name, password);
                        Toast.makeText(RegisterActivity.this, "Registration successful. Please verify your email.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserData(FirebaseUser user, String name, String hashedPassword) {
        if (user != null) {
            DatabaseReference usersRef = database.getReference("users").child(user.getUid());
            usersRef.child("name").setValue(name);
            usersRef.child("email").setValue(user.getEmail());
            usersRef.child("hashedPassword").setValue(hashedPassword);
        }
    }

    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}