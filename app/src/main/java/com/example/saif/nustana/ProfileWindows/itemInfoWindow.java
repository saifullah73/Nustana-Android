package com.example.saif.nustana.ProfileWindows;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.saif.nustana.CustomDialogs.MenuOrderDialog;
import com.example.saif.nustana.Menu.MenuItems.ItemInfo;
import com.example.saif.nustana.R;

public class itemInfoWindow extends AppCompatActivity {

    private TextView itemName, itemPrice, itemCategory, itemDescription, extraInfo;
    private ImageView itemImage;
    private Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        getSupportActionBar().hide();
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.item_info_window);
        ItemInfo.setItemInfo(getIntent());

        itemImage = findViewById(R.id.oItemImageView);
        itemName = findViewById(R.id.oItemNameView);
        itemPrice = findViewById(R.id.oItemPriceView);
        itemCategory = findViewById(R.id.oItemCategoryView);
        itemDescription = findViewById(R.id.oItemDescriptionView);
        orderButton = findViewById(R.id.orderButton);
        extraInfo = findViewById(R.id.oExtraInfo);
        itemName.setText(ItemInfo.itemName);
        itemPrice.setText(ItemInfo.itemPrice);
        itemDescription.setText(ItemInfo.itemDescription);
        itemCategory.setText(ItemInfo.itemCategory);
        if (!ItemInfo.imageUrl.isEmpty()) {
            Glide.with(itemImage.getContext())
                    .load(ItemInfo.imageUrl)
                    .into(itemImage);

        }
        if (ItemInfo.itemCategory.equals(getString(R.string.burger))) {
            // extraInfo input also required
            extraInfo.setVisibility(View.VISIBLE);
            extraInfo.setText("Fries and Drinks included");
        } else if (ItemInfo.itemCategory.equals(getString(R.string.pizza))) {
            // extraInfo input also required
            extraInfo.setVisibility(View.VISIBLE);
            extraInfo.setText("Drinks included");
        } else {
            // extraInfo input not required
            extraInfo.setVisibility(View.GONE);
        }

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuOrderDialog orderDialog = new MenuOrderDialog(itemInfoWindow.this);
                orderDialog.show();
            }
        });
    }
}
