package com.rocdev.android.piet.newsreaderv3;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 *
 * Created by Piet on 25-5-2015.
 */
public class NewsReaderApp extends Application {

    private long feedMillis = -1;

    public void setFeedMillis(long feedMillis) {
        this.feedMillis = feedMillis;
    }

    public long getFeedMillis() {
        return feedMillis;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("News reader", "App started");
        Log.d("News reader", "FeedMillis: " + feedMillis);

        // start service
        Intent service = new Intent(this, NewsReaderService.class);
        startService(service);
    }
}
