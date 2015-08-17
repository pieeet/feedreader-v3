package com.rocdev.android.piet.newsreaderv3;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ItemsActivity extends ActionBarActivity
        implements OnItemClickListener {

    private RSSFeed feed;
    private FileIO io;
    private NewsReaderApp app;

    private TextView titleTextView;
    private ListView itemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        app = (NewsReaderApp) getApplication();
        io = new FileIO(this.getApplicationContext());
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        itemsListView = (ListView) findViewById(R.id.itemsListView);
        itemsListView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        long feedPubDateMillis;
        //get feed from app object
        feedPubDateMillis = app.getFeedMillis();
        if (feedPubDateMillis == -1) {
            //downloaden, lezen en vertonen
            new DownloadFeed().execute();
        }
        else if (feed == null) {
            //lezen en vertonen
            new ReadFeed().execute();
        }
        else {
            //alleen vertonen
            updateDisplay();
        }
    }

    private class DownloadFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            io.downloadFile();
            return null;
        }

        // parameter == return doInBackground methode
        @Override
        protected void onPostExecute(Void result) {
            Log.d("News reader", "Feed downloaded: " + new Date());
            new ReadFeed().execute();
        }
    }

    private class ReadFeed extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            feed = io.readFile();
            return null;
        }

        // This is executed after the feed has been read
        @Override
        protected void onPostExecute(Void result) {
            Log.d("News reader", "Feed read: " + new Date());
            app.setFeedMillis(feed.getPubDateMillis());
            // update the display for the activity
            ItemsActivity.this.updateDisplay();
        }
    }

    private void updateDisplay() {
        if (feed == null) {
            titleTextView.setText("Unable to get RSS feed");
            return;
        }

        // set the title for the feed
        titleTextView.setText(feed.getTitle());

        // get the items for the feed
        ArrayList<RSSItem> items = feed.getAllItems();

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data =
                new ArrayList<>();
        for (RSSItem item : items) {
            HashMap<String, String> map = new HashMap<>();
            map.put("date", item.getPubDateFormatted());
            map.put("title", item.getTitle());
            data.add(map);
        }

        // create the resource, from, and to variables 
        int resource = R.layout.listview_item;
        String[] from = {"date", "title"};
        int[] to = {R.id.pubDateTextView, R.id.titleTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);

        Log.d("News reader", "Feed displayed: " + new Date());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        // get the item at the specified position
        RSSItem item = feed.getItem(position);

        // create an intent
        Intent intent = new Intent(this, ItemActivity.class);

        intent.putExtra("pubdate", item.getPubDateFormatted());
        intent.putExtra("title", item.getTitle());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("link", item.getLink());

        this.startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }
}