package com.example.saif.nustana.Order.ShoppingCart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saif.nustana.R;
import com.example.saif.nustana.User.UserInfo;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ShoppingCartAdapter extends ArrayAdapter<ShoppingCartItem> {

    DatabaseReference mReference;


    public ShoppingCartAdapter(@NonNull Context context, @NonNull List<ShoppingCartItem> objects) {
        super(context, 0, objects);
        mReference = FirebaseDatabase.getInstance().getReference("/users/" + UserInfo.userID + "/shoppingCart");
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_shopping_cart_item, parent, false);
        }

        final ShoppingCartItem item = getItem(position);
        TextView itemDatabaseKey = listItemView.findViewById(R.id.sInvisibleKey);
        itemDatabaseKey.setText(item.itemDatabaseKey);
        TextView itemName = listItemView.findViewById(R.id.sItemNameView);
        itemName.setText(item.getName());
        TextView itemQuantity = listItemView.findViewById(R.id.sItemQuantityView);
        itemQuantity.setText("Quantity:" + item.getQuantity());
        TextView itemPrice = listItemView.findViewById(R.id.sItemPriceView);
        itemPrice.setText(item.getPrice() + " Rs/-");

        // cancel button removes item from database
        final ImageView cancelView = listItemView.findViewById(R.id.sCancelItem);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingCartAdapter.this.remove(getItem(position));
                cancelView.setEnabled(false);
                mReference.child(item.itemDatabaseKey).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                        } else {
                            Toast.makeText(getContext(), "Unable to remove at the moment.Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        return listItemView;
    }


}

