package com.example.saif.nustana.Menu.MenuItems;

import android.content.Intent;

public final class ItemInfo {
    public static String itemName;
    public static String itemDescription;
    public static String itemPrice;
    public static String itemCategory;
    public static String imageUrl;
    public static String itemId;


    public static void setItemInfo(Item item) {
        itemName = item.getItemName();
        itemDescription = item.getItemDescription();
        itemPrice = item.getItemPrice();
        itemCategory = item.getItemCategory();
        imageUrl = item.getImageUrl();
        itemId = item.itemId;
    }

    public static void setItemInfo(Intent intent) {
        itemName = intent.getStringExtra("itemName");
        itemDescription = intent.getStringExtra("itemDescription");
        itemPrice = intent.getStringExtra("itemPrice");
        itemCategory = intent.getStringExtra("itemCategory");
        imageUrl = intent.getStringExtra("imageUrl");
        itemId = intent.getStringExtra("itemId");
    }

    public static Item toItem() {
        return new Item(itemName, itemDescription, itemPrice, itemCategory, imageUrl, itemId);
    }

}
