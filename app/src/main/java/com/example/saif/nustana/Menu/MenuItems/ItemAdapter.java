package com.example.saif.nustana.Menu.MenuItems;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.saif.nustana.R;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    public ItemAdapter(@NonNull Context context, @NonNull List<Item> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_item_view, parent, false);
        }

        Item item = getItem(position);

        TextView imageUrl = listItemView.findViewById(R.id.invisible_item_image_url);
        imageUrl.setText(item.getImageUrl());
        TextView itemId = listItemView.findViewById(R.id.invisible_item_id_view);
        itemId.setText(item.itemId);
        TextView itemName = listItemView.findViewById(R.id.item_name_view);
        itemName.setText(item.getItemName());
        TextView itemDescription = listItemView.findViewById(R.id.item_description_view);
        itemDescription.setText(item.getItemDescription());
        TextView itemPrice = listItemView.findViewById(R.id.item_price_view);
        itemPrice.setText(item.getItemPrice() + " Rs/-");
        ImageView itemImage = listItemView.findViewById(R.id.item_image_view);
        if (item.getImageUrl() != null) {
            Glide.with(itemImage.getContext())
                    .load(item.getImageUrl())
                    .into(itemImage);
        } else {
            itemImage.setImageResource(R.drawable.no_image);
        }

        return listItemView;
    }
}
