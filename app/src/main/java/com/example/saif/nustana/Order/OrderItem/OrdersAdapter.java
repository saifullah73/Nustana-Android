package com.example.saif.nustana.Order.OrderItem;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.saif.nustana.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrdersAdapter extends ArrayAdapter<Order> {
    public static final String TAG = "orderAdapter";
    private DatabaseReference mReference;
    private TextView userNameView;


    public OrdersAdapter(@NonNull Context context, @NonNull List<Order> objects) {
        super(context, 0, objects);
        mReference = FirebaseDatabase.getInstance().getReference("/users");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View orderItemView = convertView;
        if (orderItemView == null) {
            orderItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_item_order_view, parent, false);
        }

        Order orderItem = getItem(position);

        TextView userIdVIew = orderItemView.findViewById(R.id.soUserID);
        userIdVIew.setText(orderItem.getUserId());
        TextView orderIdVIew = orderItemView.findViewById(R.id.soOrderID);
        orderIdVIew.setText(orderItem.orderID.substring(1));
        TextView totalPrice = orderItemView.findViewById(R.id.soTotalPrice);
        totalPrice.setText("Total Price\n=" + orderItem.getTotalPrice() + "RS/-");
        return orderItemView;

    }

}
