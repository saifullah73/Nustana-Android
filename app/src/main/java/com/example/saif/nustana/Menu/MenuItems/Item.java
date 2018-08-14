package com.example.saif.nustana.Menu.MenuItems;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String itemName;
    private String itemDescription;
    private String itemPrice;
    private String itemCategory;
    private String imageUrl;
    public String itemId;  //public to prevent having a getter and thus preventing Firebase to write to database


    public Item(String itemName, String itemDescription, String itemPrice, String itemCategory, String imageUrl) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
        this.imageUrl = imageUrl;
    }

    public Item(String itemName, String itemDescription, String itemPrice, String itemCategory, String imageUrl, String itemId) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
        this.imageUrl = imageUrl;
        this.itemId = itemId;
    }

    public Item(String itemName, String itemDescription, String itemPrice, String itemCategory) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
    }

    public Item() {

    }

    public String getItemCategory() {
        return itemCategory;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Map<String, Object> toMap() {
        // this method exists to convert item to its map and allow easy updating in database.
        HashMap<String, Object> result = new HashMap<>();
        result.put("itemName", getItemName());
        result.put("itemPrice", getItemPrice());
        result.put("itemDescription", getItemDescription());
        result.put("itemCategory", getItemCategory());
        result.put("imageUrl", getImageUrl());

        return result;
    }

    public void setItemInfo() {
        ItemInfo.itemPrice = getItemPrice();
        ItemInfo.itemDescription = getItemDescription();
        ItemInfo.itemId = itemId;
        ItemInfo.imageUrl = getImageUrl();
        ItemInfo.itemName = getItemName();
        ItemInfo.itemCategory = getItemCategory();
    }
}
