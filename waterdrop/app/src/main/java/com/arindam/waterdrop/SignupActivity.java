package com.arindam.waterdrop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText fullNameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Switch userTypeSwitch;
    private EditText addresse;
    private EditText phoneNumber;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getSupportActionBar().hide();

        // Initialize views
        fullNameEditText = findViewById(R.id.fullNameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        userTypeSwitch = findViewById(R.id.userTypeSwitch);
        addresse= findViewById(R.id.addressEditText);
        phoneNumber = findViewById(R.id.phoneNumberEditText);

        Button signupButton = findViewById(R.id.signupButton);
        TextView loginTextView = findViewById(R.id.loginTextView);

        signupButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String address = addresse.getText().toString().trim();
            String phone = phoneNumber.getText().toString().trim();
            String role;

            // Get the value of userTypeSwitch
            if (userTypeSwitch.isChecked()) {
                role = "Customer";
            } else {
                role = "Distributor";
            }

            // Get a reference to the Firebase Realtime Database
            DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://waterdrop-74daf-default-rtdb.firebaseio.com/");
            String fullName_s = fullNameEditText.getText().toString();
            String username_s = usernameEditText.getText().toString();
            String password_s = passwordEditText.getText().toString();
            String address_s = addresse.getText().toString();
            String phone_s = phoneNumber.getText().toString();
            String role_s = role.toString();
            StoringData storingDataInstance = new StoringData(fullName_s, username_s, phone_s, address_s, password_s, role_s);

            reference.child("users").child(username_s).setValue(storingDataInstance)
                    .addOnSuccessListener(aVoid -> {
                        // Perform signup logic here
                        // Replace this with your actual signup logic

                        // Show a toast indicating successful signup
                        Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // Show a toast indicating unsuccessful signup
                        Toast.makeText(SignupActivity.this, "Signup failed", Toast.LENGTH_SHORT).show();
                    });
        });

        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        userTypeSwitch.setOnClickListener(v -> {
            if (userTypeSwitch.isChecked()) {
                userTypeSwitch.setText("Customer");
            } else {
                userTypeSwitch.setText("Distributor");
            }
        });
    }
}
