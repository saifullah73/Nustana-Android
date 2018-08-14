package com.example.saif.nustana.Order.ShoppingCart;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saif.nustana.ApplicationMode;
import com.example.saif.nustana.Menu.ShopMenuActivity;
import com.example.saif.nustana.Order.OrderItem.Order;
import com.example.saif.nustana.R;
import com.example.saif.nustana.User.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart extends AppCompatActivity {
    public static final String TAG = "shoppingCart";

    private ListView listView;
    private ShoppingCartAdapter adapter;
    private DatabaseReference readReference;
    private DatabaseReference writeReference;
    private ChildEventListener mChildEventListener;
    private Button orderButton;
    private TextView totalPriceView;
    private View labelView;
    private List<ShoppingCartItem> allItems;
    private int totalPrice;
    private FloatingActionButton floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        setTitle("My Cart");
        // creates a pending reference to put the new Order in.
        writeReference = FirebaseDatabase.getInstance().getReference("/orders/pending");
        readReference = FirebaseDatabase.getInstance().getReference("/users/" + UserInfo.userID + "/shoppingCart");
        orderButton = findViewById(R.id.finalOrderButton);
        listView = findViewById(R.id.sItemsList);
        totalPriceView = findViewById(R.id.soTotalPrice);
        labelView = findViewById(R.id.sLabelView);
        floatingButton = findViewById(R.id.addButton);

        labelView.setVisibility(View.GONE);
        View emptyView = findViewById(R.id.sEmptyView);
        allItems = new ArrayList<ShoppingCartItem>();
        listView.setEmptyView(emptyView);
        orderButton.setVisibility(View.GONE);
        orderButton.setBackgroundColor(getResources().getColor(R.color.grey));

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // opens menu
                ApplicationMode.currentMode = "customer";
                Intent i1 = new Intent(getApplicationContext(), ShopMenuActivity.class);
                startActivity(i1);
                finish();
            }
        });

        //creating an empty list view to set up the adapter
        //later on the onChildAdded() method in attachDatabaseReadListener() keeps updating our adapter
        List<ShoppingCartItem> items = new ArrayList<>();
        adapter = new ShoppingCartAdapter(getApplicationContext(), items);
        listView.setAdapter(adapter);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ApplicationMode.checkConnectivity(ShoppingCart.this)) {
                    // only allow order if internet is available
                    Order order = new Order(allItems, UserInfo.userID, String.valueOf(totalPrice));
                    writeReference.push().setValue(order, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                readReference.removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            // clears view
                                            adapter.clear();
                                            labelView.setVisibility(View.GONE);
                                        }
                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Your Order Has been placed", Toast.LENGTH_SHORT).show();
                            } else {
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    // if a new child is added UI is changed to reflect ordering
                    labelView.setVisibility(View.VISIBLE);
                    orderButton.setVisibility(View.VISIBLE);
                    orderButton.setEnabled(true);
                    orderButton.setBackgroundColor(getResources().getColor(R.color.button_pink));
                    ShoppingCartItem item = dataSnapshot.getValue(ShoppingCartItem.class);
                    // keeps updating total price on each addition of item
                    totalPrice += Integer.parseInt(item.getQuantity()) * Integer.parseInt(item.getPrice());
                    totalPriceView.setText("Total= " + String.valueOf(totalPrice) + "Rs/-");
                    // adds new item to ShoppingCartItem array
                    allItems.add(item);
                    item.itemDatabaseKey = dataSnapshot.getKey();
                    adapter.add(item);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    // clears and refreshed the UI, and if Items still remain OnChildAdded is called for appropriate modification
                    adapter.clear();
                    allItems.clear();
                    totalPrice = 0;
                    detachDatabaseReadListener();
                    attachDatabaseReadListener();
                    labelView.setVisibility(View.GONE);
                    orderButton.setEnabled(false);
                    orderButton.setBackgroundColor(getResources().getColor(R.color.grey));

                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            readReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            readReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onPause() {
        adapter.clear();
        allItems.clear();
        detachDatabaseReadListener();
        super.onPause();
    }

    @Override
    public void onStart() {
        attachDatabaseReadListener();
        super.onStart();
    }
}
