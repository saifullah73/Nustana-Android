package com.example.saif.nustana.Order.OrderItem;

// class for info of extras that may be included with the Order
public class Extras {
    private String drinkType;
    private String crustType;

    public Extras() {
    }

    public void setdrinkType(String drinkType) {
        this.drinkType = drinkType;
    }

    public void setCrustType(String crustType) {
        this.crustType = crustType;
    }

    public String getdrinkType() {

        return drinkType;
    }

    public String getCrustType() {
        return crustType;
    }
}
