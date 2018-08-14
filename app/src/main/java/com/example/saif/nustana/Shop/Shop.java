package com.example.saif.nustana.Shop;

public class Shop {
    private String shopName;
    private String shopNumber;
    private String shopAddress;
    private String shopDescription;
    private String shopAbout;

    public Shop() {
        //empty constructor for firebase to map data values into shop object
    }

    public Shop(String shopName, String shopNumber, String shopAddress, String shopDescription, String shopAbout) {
        this.shopName = shopName;
        this.shopNumber = shopNumber;
        this.shopAddress = shopAddress;
        this.shopDescription = shopDescription;
        this.shopAbout = shopAbout;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public String getShopAbout() {
        return shopAbout;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public void setShopAbout(String shopAbout) {
        this.shopAbout = shopAbout;
    }
}
