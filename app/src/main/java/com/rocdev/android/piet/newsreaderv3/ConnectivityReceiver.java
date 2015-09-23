package com.rocdev.android.piet.newsreaderv3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by piet on 23-09-15.
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("news reader", "dataconnectie veranderd");
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Intent service = new Intent(context, NewsReaderService.class);
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("news reader", "connectie");
            context.startService(service);
        } else {
            Log.d("news reader", "geen connectie");
            context.stopService(service);
        }
    }
}
