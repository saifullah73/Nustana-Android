package com.example.saif.nustana.User;

import java.net.URL;

public class UserInfo {

    // constant class to retrieve userInfo
    public static String userID;
    public static String userEmail;
    public static String userName;
    public static String userPassword;
    public static String userPhone;
    public static String userAddress;
    public static URL userPhotoUrl;

    public static void setUserID(String userID) {
        UserInfo.userID = userID;
    }

    public static void setUserEmail(String userEmail) {
        UserInfo.userEmail = userEmail;
    }

    public static void setUserName(String userName) {
        UserInfo.userName = userName;
    }

    public static void setUserPassword(String userPassword) {
        UserInfo.userPassword = userPassword;
    }

    public static void setUserPhone(String userPhone) {
        UserInfo.userPhone = userPhone;
    }

    public static void setUserAddress(String userAddress) {
        UserInfo.userAddress = userAddress;
    }

    public static void setUserPhotoUrl(URL userPhotoUrl) {
        UserInfo.userPhotoUrl = userPhotoUrl;
    }
}
