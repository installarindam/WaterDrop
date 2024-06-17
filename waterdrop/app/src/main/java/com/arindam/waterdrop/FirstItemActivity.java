package com.arindam.waterdrop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstItemActivity extends AppCompatActivity {

    private EditText distributorIdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstitemactivity);
        getSupportActionBar().hide();

        distributorIdEditText = findViewById(R.id.distributorIdEditText);
        Button submitButton = findViewById(R.id.submitDistributorIdButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String distributorId = distributorIdEditText.getText().toString().trim();

                if (!distributorId.isEmpty()) {
                    saveDistributorId(distributorId);
                } else {
                    Toast.makeText(FirstItemActivity.this, "Please enter a distributor ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveDistributorId(String distributorId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://waterdrop-74daf-default-rtdb.firebaseio.com/");
        String currentUserId = MainActivity.tempuser;

        if (currentUserId != null) {
            databaseReference.child("users").child(currentUserId).child("distributorId").setValue(distributorId)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(FirstItemActivity.this, "Distributor ID saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(FirstItemActivity.this, "Failed to save Distributor ID", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(FirstItemActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
