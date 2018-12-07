package com.axon.alex.axonsoft.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Connection {
    /***
     *
     * @param context Activity context
     * @return whether network is available or not
     */
    public static boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            Toast.makeText(context, "нет сети", Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(context, "нет сети", Toast.LENGTH_SHORT).show();
        return false;
    }

}
