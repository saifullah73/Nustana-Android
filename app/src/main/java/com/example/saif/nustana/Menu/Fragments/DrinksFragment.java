package com.example.saif.nustana.Menu.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.saif.nustana.ApplicationMode;
import com.example.saif.nustana.Menu.MenuItems.Item;
import com.example.saif.nustana.Menu.MenuItems.ItemAdapter;
import com.example.saif.nustana.Menu.MenuItems.itemEditor;
import com.example.saif.nustana.ProfileWindows.itemInfoWindow;
import com.example.saif.nustana.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DrinksFragment extends Fragment {
    private DatabaseReference mReference;
    private ChildEventListener mChildEventListener;

    private ListView listView;
    private ItemAdapter adapter;

    public DrinksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_with_empty_view, container, false);

        listView = rootView.findViewById(R.id.items_list);
        View emptyView = rootView.findViewById(R.id.empty_view);

        if (!ApplicationMode.checkConnectivity(getActivity())) {
            TextView emptyTitle = rootView.findViewById(R.id.empty_title_text);
            emptyTitle.setText("Please check your internet connection");

        }
        listView.setEmptyView(emptyView);
        mReference = FirebaseDatabase.getInstance().getReference("/items/" + getString(R.string.drink));

        //creating an empty list view to set up the adapter
        //later on the onChildAdded() method in attachDatabaseReadListener() keeps updating our adapter
        List<Item> items = new ArrayList<>();
        adapter = new ItemAdapter(getActivity(), items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView itemNameView = view.findViewById(R.id.item_name_view);
                TextView itemDescriptionView = view.findViewById(R.id.item_description_view);
                TextView itemPriceView = view.findViewById(R.id.item_price_view);
                TextView itemIdVIew = view.findViewById(R.id.invisible_item_id_view);
                TextView imageUrl = view.findViewById(R.id.invisible_item_image_url);
                if (ApplicationMode.currentMode.equals("owner") || ApplicationMode.currentMode.equals("visitor")) {
                    // allow owner to update Item
                    Intent intent = new Intent(getContext(), itemEditor.class);
                    intent.putExtra(getString(R.string.imageUrl), imageUrl.getText().toString());
                    intent.putExtra(getString(R.string.itemName), itemNameView.getText().toString());
                    intent.putExtra(getString(R.string.itemPrice), itemPriceView.getText().toString().split(" ")[0]); // separates the RS/- part
                    intent.putExtra(getString(R.string.itemDescription), itemDescriptionView.getText().toString());
                    intent.putExtra(getString(R.string.itemCategory), getString(R.string.drink));
                    intent.putExtra(getString(R.string.itemId), itemIdVIew.getText().toString());
                    startActivity(intent);
                } else {
                    // allow user to view ItemInfo
                    Intent intent = new Intent(getContext(), itemInfoWindow.class);
                    intent.putExtra(getString(R.string.imageUrl), imageUrl.getText().toString());
                    intent.putExtra(getString(R.string.itemName), itemNameView.getText().toString());
                    intent.putExtra(getString(R.string.itemPrice), itemPriceView.getText().toString().split(" ")[0]); // separates the RS/- part
                    intent.putExtra(getString(R.string.itemDescription), itemDescriptionView.getText().toString());
                    intent.putExtra(getString(R.string.itemCategory), getString(R.string.drink));
                    intent.putExtra(getString(R.string.itemId), itemIdVIew.getText().toString());
                    startActivity(intent);

                }
            }
        });

        return rootView;
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Item item = dataSnapshot.getValue(Item.class);
                    item.itemId = dataSnapshot.getKey();
                    adapter.add(item);

                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    //reattaching updates list in UI
                    adapter.clear();
                    detachDatabaseReadListener();
                    attachDatabaseReadListener();
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // reattaching the listener updates our list in UI
                    adapter.clear();
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
        adapter.clear();
        detachDatabaseReadListener();
        super.onPause();
    }

    @Override
    public void onStart() {
        attachDatabaseReadListener();
        super.onStart();
    }
}
