package com.example.myapplication;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterViewModel extends ViewModel {

    private FirebaseAuth auth;

    private final MutableLiveData<FirebaseUser> registrationLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<FirebaseUser> getRegistrationLiveData() {
        return registrationLiveData;
    }
  void registerUser(String email, String password, String name) {
        auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Log.d("FireBaseUser", user.getEmail().toString());
                        sendVerificationEmail(auth.getCurrentUser());
                        registrationLiveData.setValue(user);

                    } else {
                    }
                });
    }

    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {

                });
    }
}
