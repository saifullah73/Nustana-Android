package com.example.saif.nustana.Order;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.saif.nustana.Menu.CategoryAdapter;
import com.example.saif.nustana.R;

public class OrdersTerminalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_orders);
        setTitle("Orders");

        ViewPager viewPager = findViewById(R.id.soViewpager);

        // Create an adapter that knows which fragment should be shown on each page
        OrdersCategoryAdapter adapter = new OrdersCategoryAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.soTabs);

        tabLayout.setupWithViewPager(viewPager);
    }
}
