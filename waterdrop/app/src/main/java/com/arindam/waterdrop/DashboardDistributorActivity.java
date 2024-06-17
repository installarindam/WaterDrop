package com.arindam.waterdrop;

import static com.arindam.waterdrop.MainActivity.tempuser;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DashboardDistributorActivity extends AppCompatActivity {

    private ListView ordersListView;
    private OrderAdapter orderAdapter;
    private static DatabaseReference ordersRef;
    private List<Order> orders;
    private List<Order> originalOrders; // Preserve the original list
    private Spinner sortSpinner;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_distributor);

        setupNavigationDrawer();

        ordersListView = findViewById(R.id.ordersListView);
        sortSpinner = findViewById(R.id.sortSpinner);








        orders = new ArrayList<>();
        originalOrders = new ArrayList<>(); // Preserve the original orders list
        orderAdapter = new OrderAdapter(this, orders);
        ordersListView.setAdapter(orderAdapter);


        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();
                originalOrders.clear();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    order.setOrderKey(orderSnapshot.getKey());
                    orders.add(order);
                    originalOrders.add(order);
                }

                Collections.sort(orders, new Comparator<Order>() {
                    SimpleDateFormat databaseDateFormat = new SimpleDateFormat("MM-dd-yyyy");
                    SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    @Override
                    public int compare(Order o1, Order o2) {
                        try {
                            Date date1 = databaseDateFormat.parse(o1.getScheduleDate());
                            Date date2 = databaseDateFormat.parse(o2.getScheduleDate());
                            String formattedDate1 = displayDateFormat.format(date1);
                            String formattedDate2 = displayDateFormat.format(date2);
                            return formattedDate2.compareTo(formattedDate1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });

                Collections.reverse(orders); // Reverse the order after sorting

                orderAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashboardDistributorActivity.this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
            }
        });

        ordersListView.setOnItemClickListener((parent, view, position, id) -> {
            Order order = orders.get(position);
            // Handle order item click here...
        });

        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this, R.array.sort_options, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = parentView.getItemAtPosition(position).toString();
                handleSortOption(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do nothing on query submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchOrders(newText);
                return true;
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle other action bar items...
        return super.onOptionsItemSelected(item);
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

                        Toast.makeText(DashboardDistributorActivity.this, "Item 1 Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_item2:
                        Intent intent = new Intent(DashboardDistributorActivity.this, DistributerIdShareActivity.class);
                        //intent.putExtra("distributorId", "<tempuser>"); // Replace "<tempuser>" with your actual Distributor ID
                        startActivity(intent);

                        Toast.makeText(DashboardDistributorActivity.this, "Item 2 Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    private void handleSortOption(String selectedOption) {
        switch (selectedOption) {
            case "All":
                orderAdapter.clear();
                orderAdapter.addAll(originalOrders);
                Collections.reverse(orders);
                orderAdapter.notifyDataSetChanged();

                break;
            case "This Month":
                filterOrdersByMonth();
                break;
            case "Today":
                filterOrdersByToday();
                break;
            case "Order Placed":
            case "Order Delivered":
            case "Order Confirmed":
            case "Order Dispatched":
            case "Order Cancelled":
                filterOrdersByStatus(selectedOption);
                break;
        }
    }

    private void filterOrdersByMonth() {
        List<Order> filteredOrders = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);

        for (Order order : originalOrders) {
            try {
                Date orderDate = new SimpleDateFormat("dd/MM/yyyy").parse(formatDate(order.getScheduleDate()));
                Calendar orderCal = Calendar.getInstance();
                orderCal.setTime(orderDate);

                int orderMonth = orderCal.get(Calendar.MONTH) + 1; // Months are zero-based, so add 1
                int orderYear = orderCal.get(Calendar.YEAR);

                if (orderYear == currentYear && orderMonth == currentMonth) {
                    filteredOrders.add(order);

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        orderAdapter.clear();
        orderAdapter.addAll(filteredOrders);
        Collections.reverse(orders);
        orderAdapter.notifyDataSetChanged();
    }

    private String formatDate(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/20yy");
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate; // return original date in case of an error
        }
    }

    private void filterOrdersByToday() {
        List<Order> filteredOrders = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(new Date());

        for (Order order : originalOrders) {
            if (formatDate(order.getScheduleDate()).equals(currentDate)) {
                filteredOrders.add(order);
            }
        }

        orderAdapter.clear();
        orderAdapter.addAll(filteredOrders);
        Collections.reverse(orders);
        orderAdapter.notifyDataSetChanged();
    }

    private void filterOrdersByStatus(String status) {
        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : originalOrders) {
            if (order.getOrderStatus().equals(status)) {
                filteredOrders.add(order);
            }
        }

        orderAdapter.clear();
        orderAdapter.addAll(filteredOrders);
        Collections.reverse(orders);
        orderAdapter.notifyDataSetChanged();
    }

    private void searchOrders(String query) {
        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : originalOrders) {
            if (order.getFullName().toLowerCase().contains(query.toLowerCase())
                    || order.getDeliveryAddress().toLowerCase().contains(query.toLowerCase())
                    || order.getScheduleDate().toLowerCase().contains(query.toLowerCase())
                    || order.getProductName().toLowerCase().contains(query.toLowerCase())) {
                filteredOrders.add(order);
            }
        }

        orderAdapter.clear();
        orderAdapter.addAll(filteredOrders);
        orderAdapter.notifyDataSetChanged();
    }

    static class Order {
        private String userId;
        private String fullName;
        private String productName;
        private String productQuantity;
        private String scheduleDate;
        private String scheduleTime;
        private String deliveryAddress;
        private String phoneNumber;
        private String orderStatus;
        private String orderKey;

        public Order() {
            this.orderStatus = "Order Placed"; // Set a default order status if not present
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductQuantity() {
            return productQuantity;
        }

        public void setProductQuantity(String productQuantity) {
            this.productQuantity = productQuantity;
        }

        public String getScheduleDate() {
            return scheduleDate;
        }

        public void setScheduleDate(String scheduleDate) {
            this.scheduleDate = scheduleDate;
        }

        public String getScheduleTime() {
            return scheduleTime;
        }

        public void setScheduleTime(String scheduleTime) {
            this.scheduleTime = scheduleTime;
        }

        public String getDeliveryAddress() {
            return deliveryAddress;
        }

        public void setDeliveryAddress(String deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderKey() {
            return orderKey;
        }

        public void setOrderKey(String orderKey) {
            this.orderKey = orderKey;
        }
    }

    class OrderAdapter extends ArrayAdapter<Order> {

        public OrderAdapter(@NonNull Context context, @NonNull List<Order> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_item, parent, false);

                holder = new ViewHolder();
                holder.cardView = convertView.findViewById(R.id.orderCard);
                holder.customerFullName = convertView.findViewById(R.id.customerFullNameTextView);
                holder.productDetails = convertView.findViewById(R.id.productDetailsTextView);
                holder.deliveryAddress = convertView.findViewById(R.id.deliveryAddressTextView);
                holder.scheduleDetails = convertView.findViewById(R.id.scheduleDetailsTextView);
                holder.userId = convertView.findViewById(R.id.userIdTextView);
                holder.orderStatusSpinner = convertView.findViewById(R.id.orderStatusSpinner);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Order order = getItem(position);

            holder.customerFullName.setText("Name: " + order.getFullName());
            holder.productDetails.setText("Product: " + order.getProductName() + ", " + order.getProductQuantity());
            holder.deliveryAddress.setText("Delivery Address: " + order.getDeliveryAddress());
            holder.scheduleDetails.setText("Schedule: " + formatDate(order.getScheduleDate()) + ", Time: " + order.getScheduleTime());
            holder.userId.setText("User ID: " + order.getUserId());

            ArrayAdapter<CharSequence> orderStatusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.order_status_array, android.R.layout.simple_spinner_item);
            orderStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.orderStatusSpinner.setAdapter(orderStatusAdapter);
            holder.orderStatusSpinner.setSelection(orderStatusAdapter.getPosition(order.getOrderStatus()));

            holder.orderStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String newStatus = parentView.getItemAtPosition(position).toString();
                    String currentStatus = order.getOrderStatus();

                    // Check if the status change is valid
                    if (isValidStatusChange(currentStatus, newStatus)) {
                        // Update the order status in Firebase based on the order key
                        updateOrderStatusInFirebase(order.getOrderKey(), newStatus);
                    } else {
                        // Display a toast indicating the invalid status change
                        Toast.makeText(getContext(), "Invalid status change: " + currentStatus + " to " + newStatus, Toast.LENGTH_SHORT).show();
                        // Reset the spinner to the original status
                        holder.orderStatusSpinner.setSelection(orderStatusAdapter.getPosition(currentStatus));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Do nothing here
                }
            });

            return convertView;
        }

        class ViewHolder {
            CardView cardView;
            TextView customerFullName;
            TextView productDetails;
            TextView deliveryAddress;
            TextView scheduleDetails;
            TextView userId;
            Spinner orderStatusSpinner;
        }
    }

    // Function to update order status in Firebase
    private void updateOrderStatusInFirebase(String orderKey, String newStatus) {
        DatabaseReference orderRef = ordersRef.child(orderKey);

        // Update the order status in Firebase
        orderRef.child("orderStatus").setValue(newStatus, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    // Order status updated successfully
                    //Toast.makeText(DashboardDistributorActivity.this, "Order status updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Error updating order status
                    //Toast.makeText(DashboardDistributorActivity.this, "Error updating order status", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Function to check if the status change is valid
    private boolean isValidStatusChange(String currentStatus, String newStatus) {
        // Define the valid status transitions
        List<String> validTransitions = Arrays.asList(
                "Order Placed", "Order Confirmed", "Order Dispatched", "Order Delivered"
        );

        // Check if the transition is valid
        int currentIndex = validTransitions.indexOf(currentStatus);
        int newIndex = validTransitions.indexOf(newStatus);

        return currentIndex != -1 && newIndex != -1 && newIndex >= currentIndex;
    }
}
