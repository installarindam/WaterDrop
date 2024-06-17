package com.arindam.waterdrop;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Switch userTypeSwitch;
    public static String tempuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get a reference to the Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://waterdrop-74daf-default-rtdb.firebaseio.com/");

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Find views by their ids
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        userTypeSwitch = findViewById(R.id.userTypeSwitch);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String role = userTypeSwitch.isChecked() ? "Customer" : "Distributor";

                final String username_data = usernameEditText.getText().toString();
                final String password_data = passwordEditText.getText().toString();
                final String role_data= role.toString();


                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(username_data)) {
                            final String getpassword = snapshot.child(username_data).child("password").getValue(String.class);
                            final String getrole = snapshot.child(username_data).child("role").getValue(String.class);

                            if (getpassword != null && getpassword.equals(password_data)) {
                                tempuser = username_data;
                                Toast.makeText(MainActivity.this, "log in success", Toast.LENGTH_SHORT).show();

                                if (getrole != null && getrole.equals(role_data)) {
                                    if (getrole.equals("Customer")) {
                                        Intent intent = new Intent(MainActivity.this, DashboardCustomerActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else if (getrole.equals("Distributor")) {
                                        Intent intent = new Intent(MainActivity.this, DashboardDistributorActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "you are not a " + role_data, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "password not matched", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "username not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });
        //Note: You need to import the necessary classes for Firebase Realtime Database, such as com.google.firebase.database.DatabaseReference, com.google.firebase.database.FirebaseDatabase, com.google.firebase.database.DataSnapshot, com.google.firebase.database.ValueEventListener, and com.google.firebase.database.DatabaseError, if they are not imported already.







                // Forgot Password TextView click listener
        TextView forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add logic for forgot password
            }
        });

        // Signup TextView click listener
        TextView signupTextView = findViewById(R.id.signupTextView);
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add logic for signup
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set text for userTypeSwitch based on its state
        userTypeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userTypeSwitch.isChecked()) {
                    userTypeSwitch.setText("Customer");
                } else {
                    userTypeSwitch.setText("Distributor");
                }
            }
        });
    }
}
