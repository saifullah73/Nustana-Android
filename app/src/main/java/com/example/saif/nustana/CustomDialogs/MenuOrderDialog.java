package com.example.saif.nustana.CustomDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saif.nustana.Menu.MenuItems.ItemInfo;
import com.example.saif.nustana.Order.ShoppingCart.ShoppingCartItem;
import com.example.saif.nustana.R;
import com.example.saif.nustana.User.UserInfo;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuOrderDialog extends Dialog implements android.view.View.OnClickListener {
    private Activity c;
    private View drinks, crusts;
    private TextView orderQuantityView;
    private Button increment, decrement, addToCart;
    private RadioGroup drinkGroup, crustGroup;

    private DatabaseReference mReference;
    private DatabaseReference.CompletionListener listener;

    private int mode;
    private int orderQuantity = 1;
    private String drinkType;
    private String crustType;
    private ShoppingCartItem item;

    public MenuOrderDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_dialog);
        drinkGroup = findViewById(R.id.drinksRadioGroup);
        crustGroup = findViewById(R.id.crustRadioGroup);
        addToCart = findViewById(R.id.oAddToCartButton);
        increment = findViewById(R.id.increment);
        decrement = findViewById(R.id.decrement);
        orderQuantityView = findViewById(R.id.oOrderQuantity);
        drinks = findViewById(R.id.drinksLinearView);
        crusts = findViewById(R.id.crustLinearView);
        addToCart.setOnClickListener(this);

        mReference = FirebaseDatabase.getInstance().getReference("/users/" + UserInfo.userID + "/shoppingCart");
        listener = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                addToCart.setEnabled(true);
                addToCart.setBackgroundColor(c.getResources().getColor(R.color.button_pink));
                if (databaseError == null) {
                    Toast.makeText(c.getApplicationContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(c.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

            }
        };

        if (ItemInfo.itemCategory.equals(c.getString(R.string.burger))) {
            drinks.setVisibility(View.VISIBLE);
            crusts.setVisibility(View.GONE);
            mode = 1;
        } else if (ItemInfo.itemCategory.equals(c.getString(R.string.pizza))) {
            crusts.setVisibility(View.VISIBLE);
            drinks.setVisibility(View.VISIBLE);
            mode = 2;
        } else {
            crusts.setVisibility(View.GONE);
            drinks.setVisibility(View.GONE);
            mode = 3;
        }

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderQuantity != 10) {
                    orderQuantity += 1;
                    orderQuantityView.setText(String.valueOf(orderQuantity));
                }
            }
        });
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderQuantity != 1) {
                    orderQuantity -= 1;
                    orderQuantityView.setText(String.valueOf(orderQuantity));
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        addToCart.setEnabled(false);
        addToCart.setBackgroundColor(c.getResources().getColor(R.color.grey));
        if (mode == 1) {
            getRadioButtonsState(drinkGroup);
            item = new ShoppingCartItem(ItemInfo.itemName, ItemInfo.itemPrice, ItemInfo.itemId, String.valueOf(orderQuantity), drinkType, null);

        } else if (mode == 2) {
            getRadioButtonsState(crustGroup);
            getRadioButtonsState(drinkGroup);
            item = new ShoppingCartItem(ItemInfo.itemName, ItemInfo.itemPrice, ItemInfo.itemId, String.valueOf(orderQuantity), drinkType, crustType);

        } else {
            item = new ShoppingCartItem(ItemInfo.itemName, ItemInfo.itemPrice, ItemInfo.itemId, String.valueOf(orderQuantity), null);
        }
        String key = mReference.push().getKey();
        mReference.child(key).setValue(item, listener);

    }

    private void getRadioButtonsState(RadioGroup group) {

        int buttonId = group.getCheckedRadioButtonId();
        Log.i("lovely", String.valueOf(buttonId));
        // Check which radio button was clicked
        switch (buttonId) {
            case R.id.cocaCola:
                Log.i("lovely", "here");
                drinkType = c.getString(R.string.cocaCola);
                break;
            case R.id.sprite:
                drinkType = c.getString(R.string.sprite);
                break;
            case R.id.fanta:
                drinkType = c.getString(R.string.fanta);
                break;
            case R.id.mineralWater:
                drinkType = c.getString(R.string.mineralWater);
                break;
            case R.id.thinCrust:
                Log.i("lovely", "here2");
                crustType = c.getString(R.string.thinCrust);
                break;
            case R.id.plainCrust:
                crustType = c.getString(R.string.plainCrust);
                break;
        }
    }
}

