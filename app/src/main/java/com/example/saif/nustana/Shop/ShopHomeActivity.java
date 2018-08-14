package com.example.saif.nustana.Shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.saif.nustana.ApplicationMode;
import com.example.saif.nustana.Menu.ShopMenuActivity;
import com.example.saif.nustana.Order.OrdersTerminalActivity;
import com.example.saif.nustana.ProfileWindows.ShopProfileActivity;
import com.example.saif.nustana.R;

public class ShopHomeActivity extends AppCompatActivity {

    private Button sProfile, sOrders, sMenu;
    private ImageView i3, i2, i1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);

        sProfile = findViewById(R.id.sProfileButton);
        sMenu = findViewById(R.id.sMenuButton);
        sOrders = findViewById(R.id.sOrdersButton);
        i1 = findViewById(R.id.sProfileImage);
        i2 = findViewById(R.id.sOrdersImage);
        i3 = findViewById(R.id.sMenuImage);

        sProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ShopProfileActivity.class);
                startActivity(i);
                i1.setAlpha(0.5F);
            }
        });
        sOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationMode.ordersViewer = "owner";
                Intent intent = new Intent(getApplicationContext(), OrdersTerminalActivity.class);
                startActivity(intent);
                i2.setAlpha(0.5F);
            }
        });
        sMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ApplicationMode.currentMode = "owner";
                Intent i1 = new Intent(getApplicationContext(), ShopMenuActivity.class);
                startActivity(i1);
                i3.setAlpha(0.5F);
            }
        });

    }

    @Override
    protected void onResume() {
        // restores transparentation to default

        i1.setAlpha(1F);
        i2.setAlpha(1F);
        i3.setAlpha(1F);
        super.onResume();
    }

}
