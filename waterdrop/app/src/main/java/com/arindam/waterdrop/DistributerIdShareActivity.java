package com.arindam.waterdrop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class DistributerIdShareActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distributer_id_dist);

        // Setup navigation drawer
        setupNavigationDrawer();

        // Get the Distributor ID from intent
        String distributorId = MainActivity.tempuser;

        // Find the TextView to display the value
        TextView textView = findViewById(R.id.textView);

        // Display the value
        if (distributorId != null) {
            textView.setText(distributorId);
        }
    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawerLayout_dist);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_item1:
                        // Handle item 1 click
                        Intent intent = new Intent(DistributerIdShareActivity.this, DashboardDistributorActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_item2:
                        // Handle item 2 click
                        // Intent intent = new Intent(DistributerIdShareActivity.this, DistributerIdShareActivity.class);
                        // intent.putExtra("distributorId", "<tempuser>"); // Replace "<tempuser>" with your actual Distributor ID
                        // startActivity(intent);
                        // Toast.makeText(DistributerIdShareActivity.this, "Item 2 Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the selected item to the ActionBarDrawerToggle,
        // This will handle the drawer toggle icon click events
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
