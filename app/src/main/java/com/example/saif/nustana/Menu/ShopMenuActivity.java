package com.example.saif.nustana.Menu;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.saif.nustana.ApplicationMode;
import com.example.saif.nustana.Menu.MenuItems.itemEditor;
import com.example.saif.nustana.Order.ShoppingCart.ShoppingCart;
import com.example.saif.nustana.R;

public class ShopMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_menu);
        setTitle("Menu");

        ViewPager viewPager = findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);
        FloatingActionButton button = findViewById(R.id.floating_button);

        if (ApplicationMode.currentMode.equals("owner") || ApplicationMode.currentMode.equals("visitor")) {
            // show newItem button to user
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), itemEditor.class);
                    startActivity(i);
                }
            });
        } else {
            // hide newItem button from user
            button = findViewById(R.id.floating_button);
            button.hide();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_menu, menu);
        MenuItem mi = menu.findItem(R.id.goToCart);
        if (ApplicationMode.currentMode.equals("owner") || ApplicationMode.currentMode.equals("visitor")) {
            mi.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goToCart:
                Intent profileIntent = new Intent(getApplicationContext(), ShoppingCart.class);
                startActivity(profileIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
