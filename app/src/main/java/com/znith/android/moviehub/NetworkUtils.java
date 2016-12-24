package com.znith.android.moviehub;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by spidey on 12/24/16.
 */

public class NetworkUtils {

    public static boolean isConnected(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
