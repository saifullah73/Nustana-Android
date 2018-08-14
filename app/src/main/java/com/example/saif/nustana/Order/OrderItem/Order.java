package com.example.saif.nustana.Order.OrderItem;

import com.example.saif.nustana.Order.ShoppingCart.ShoppingCartItem;

import java.util.List;

public class Order {
    private List<ShoppingCartItem> allItems;
    private String userId;
    private String totalPrice;
    public String orderID;

    public Order() {
    }

    public Order(List<ShoppingCartItem> allItems, String userId, String totalPrice) {
        this.allItems = allItems;
        this.userId = userId;
        this.totalPrice = totalPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ShoppingCartItem> getAllItems() {
        return allItems;
    }

    public String getUserId() {
        return userId;
    }


    public void setAllItems(List<ShoppingCartItem> allItems) {
        this.allItems = allItems;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
