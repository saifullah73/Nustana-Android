package com.example.saif.nustana.Order.ShoppingCart;

import com.example.saif.nustana.Order.OrderItem.Extras;

// class for shoppingCart items

public class ShoppingCartItem {
    public String itemDatabaseKey;
    private String itemId;
    private Extras extra;
    private String quantity;
    private String name;
    private String price;

    public ShoppingCartItem(String name, String price, String itemId, String quantity, Extras extra) {
        this.itemId = itemId;
        this.extra = extra;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public ShoppingCartItem(String name, String price, String itemId, String quantity, String drinks, String crust) {
        this.name = name;
        this.price = price;
        this.itemId = itemId;
        this.quantity = quantity;
        Extras extra = new Extras();
        extra.setCrustType(crust);
        extra.setdrinkType(drinks);
        this.extra = extra;
    }

    public ShoppingCartItem() {
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {

        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public String getItemId() {
        return itemId;
    }

    public Extras getExtra() {
        return extra;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setExtra(Extras extra) {
        this.extra = extra;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }
}

