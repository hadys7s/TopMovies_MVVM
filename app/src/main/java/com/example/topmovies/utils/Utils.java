package com.example.topmovies.utils;

import android.content.Context;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utils {
    public static boolean isInternetAvailable(Context context) {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }
}
