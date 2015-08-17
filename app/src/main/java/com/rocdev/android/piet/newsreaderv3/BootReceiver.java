package com.rocdev.android.piet.newsreaderv3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Piet on 17-8-2015.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("News reader", "Boot voltooid");
        //start de service
        Intent service = new Intent(context, NewsReaderService.class);
        context.startService(service);
    }
}
