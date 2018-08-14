package com.example.saif.nustana.Order.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.saif.nustana.ApplicationMode;
import com.example.saif.nustana.CustomDialogs.OrderInfoDialog;
import com.example.saif.nustana.Order.OrderItem.Order;
import com.example.saif.nustana.Order.OrderItem.OrdersAdapter;
import com.example.saif.nustana.R;
import com.example.saif.nustana.User.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class PendingFragment extends Fragment {
    private ListView listView;
    private OrdersAdapter ordersAdapter;
    private ChildEventListener mChildEventListener;
    private Query mReference;

    public PendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.list_with_empty_view, container, false);
        listView = rootView.findViewById(R.id.items_list);
        View emptyView = rootView.findViewById(R.id.empty_view);


        listView.setEmptyView(emptyView);
        List<Order> items = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(getActivity(), items);
        listView.setAdapter(ordersAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ApplicationMode.orderStatus = "pending";
                TextView tv = view.findViewById(R.id.soOrderID);
                TextView tv1 = view.findViewById(R.id.soUserID);
                // gives orderId and userId to dialog to allow it to retrieve it from database
                OrderInfoDialog od = new OrderInfoDialog(getActivity(), tv.getText().toString(), tv1.getText().toString());
                od.show();
            }
        });
        return rootView;
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Order orderItem = dataSnapshot.getValue(Order.class);
                    orderItem.orderID = dataSnapshot.getKey();
                    ordersAdapter.add(orderItem);

                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // reattaching listener update list in UI
                    ordersAdapter.clear();
                    detachDatabaseReadListener();
                    attachDatabaseReadListener();
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onPause() {
        ordersAdapter.clear();
        detachDatabaseReadListener();
        super.onPause();

    }

    @Override
    public void onStart() {
        mReference = FirebaseDatabase.getInstance().getReference("orders/pending");
        if (ApplicationMode.ordersViewer.equals("customer")) {
            //String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mReference = mReference.orderByChild("userId").equalTo(UserInfo.userID);
        }
        attachDatabaseReadListener();
        super.onStart();
    }
}
