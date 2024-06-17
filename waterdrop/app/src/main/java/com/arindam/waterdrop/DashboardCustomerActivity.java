package com.arindam.waterdrop;

import static com.arindam.waterdrop.MainActivity.tempuser;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DashboardCustomerActivity extends AppCompatActivity {

    private TextView selectedDateTextView;
    private TextView selectedTimeTextView;
    private Spinner productSpinner;
    private Spinner quantitySpinner;
    private EditText addressEditText;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_customer);

        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        selectedTimeTextView = findViewById(R.id.selectedTimeTextView);
        productSpinner = findViewById(R.id.productSpinner);
        quantitySpinner = findViewById(R.id.quantitySpinner);
        addressEditText = findViewById(R.id.addressEditText);

        Button datePickerButton = findViewById(R.id.datePickerButton);
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        Button timePickerButton = findViewById(R.id.timePickerButton);
        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        Button placeOrderButton = findViewById(R.id.placeOrderButton);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });

        ArrayAdapter<CharSequence> productAdapter = ArrayAdapter.createFromResource(this, R.array.product_array, android.R.layout.simple_spinner_item);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(productAdapter);

        ArrayAdapter<CharSequence> quantityAdapter = ArrayAdapter.createFromResource(this, R.array.quantity_array, android.R.layout.simple_spinner_item);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(quantityAdapter);



        // Setup Bottom Navigation
        setupBottomNavigation();
        //side menu
        setupNavigationDrawer();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_dashboard);

    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawerLayout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.item1:
                        // Navigate to the first item screen
                        startActivity(new Intent(DashboardCustomerActivity.this, FirstItemActivity.class));
                        break;
                    case R.id.menu_order_tracking:

                        // Assuming you want to pass the username to the OrderTrackCustomerActivity
                        String username = tempuser;

                        Intent intent = new Intent(DashboardCustomerActivity.this, order_track_customer_activity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);

                        break;
                    default:
                        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                        bottomNavigationView.setSelectedItemId(R.id.menu_dashboard);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle Bottom Navigation item clicks
                switch (item.getItemId()) {
                    case R.id.menu_dashboard:
                        // Handle click on Dashboard menu item
                        Toast.makeText(DashboardCustomerActivity.this, "Opening Dashboard", Toast.LENGTH_SHORT).show();
                        // Add your logic to switch to the Dashboard fragment or activity
                        //BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                        //bottomNavigationView.setSelectedItemId(R.id.menu_dashboard);
                        break;
                    case R.id.menu_order_tracking:
                        // Handle click on Order Tracking menu item

                        String username = tempuser;
                        Toast.makeText(DashboardCustomerActivity.this, "Opening Order Tracking for " + username, Toast.LENGTH_SHORT).show();
                        openOrderTrackingActivity(username);
                        break;
                }

                return true;
            }
        });
    }

    private void openOrderTrackingActivity(String username) {
        Intent intent = new Intent(DashboardCustomerActivity.this, order_track_customer_activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                DashboardCustomerActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                        String formattedDate = sdf.format(calendar.getTime());
                        selectedDateTextView.setText(formattedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                DashboardCustomerActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
                        String formattedTime = sdf.format(calendar.getTime());
                        selectedTimeTextView.setText(formattedTime);
                    }
                },
                hour, minute, true);

        timePickerDialog.show();
    }

    private void placeOrder() {
        String selectedDate = selectedDateTextView.getText().toString();
        String selectedTime = selectedTimeTextView.getText().toString();

        if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        String orderId = String.valueOf(System.currentTimeMillis());
        String username = tempuser;

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(username);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userId = snapshot.child("id").getValue(String.class);
                    String fullName = snapshot.child("fullName").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String distributorId = snapshot.child("distributorId").getValue(String.class);
                    if (distributorId == null || distributorId.isEmpty()) {
                        // Distributor ID is not present, show toast
                        Toast.makeText(DashboardCustomerActivity.this, "Set distributor ID", Toast.LENGTH_SHORT).show();
                        return; // Exit method
                    }
                    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child(orderId);
                    orderRef.child("userId").setValue(userId);
                    orderRef.child("fullName").setValue(fullName);
                    orderRef.child("productName").setValue(productSpinner.getSelectedItem().toString());
                    orderRef.child("productQuantity").setValue(quantitySpinner.getSelectedItem().toString());
                    orderRef.child("scheduleDate").setValue(selectedDate);
                    orderRef.child("scheduleTime").setValue(selectedTime);
                    orderRef.child("deliveryAddress").setValue(address);
                    orderRef.child("distributorId").setValue(distributorId);
                    orderRef.child("phoneNumber").setValue(phone)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(DashboardCustomerActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();

                                    // Clear input fields
                                    selectedDateTextView.setText("");
                                    selectedTimeTextView.setText("");
                                    productSpinner.setSelection(0);
                                    quantitySpinner.setSelection(0);
                                    addressEditText.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DashboardCustomerActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                                    Log.e("DashboardCustomer", "Failed to place order: " + e.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DashboardCustomer", "Database Error: " + error.getMessage());
            }
        });
    }

    private void openOrderTrackingActivity() {
        // Assuming you want to pass the username to the OrderTrackCustomerActivity
        String username = tempuser;

        Intent intent = new Intent(DashboardCustomerActivity.this, order_track_customer_activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
