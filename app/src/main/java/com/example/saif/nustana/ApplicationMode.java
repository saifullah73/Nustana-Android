package com.example.saif.nustana;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public final class ApplicationMode {

    //constants to enable/disable certain features

    public static String currentMode;
    public static String orderStatus;
    public static String ordersViewer;


    public static boolean checkConnectivity(Activity a) {
        ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean checkConnectivity(Context a) {
        ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}
