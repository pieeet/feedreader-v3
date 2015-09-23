package com.rocdev.android.piet.newsreaderv3;

/**
 *
 * Created by Piet on 25-5-2015.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class NewsReaderService extends Service {

    private NewsReaderApp app;
    private Timer timer;
    private FileIO io;

    @Override
    public void onCreate() {
        Log.d("News reader", "Service created");
        app = (NewsReaderApp) getApplication();
        io = new FileIO(getApplicationContext());
        startTimer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("News reader", "Service started");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //methode moet geimplementeerd maar doet niks
        Log.d("News reader", "Service bound - not used!");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("News reader", "Service destroyed");
        stopTimer();
    }

    private void startTimer() {
        TimerTaak taak = new TimerTaak();
        timer = new Timer(true);
        int delay = 1000 * 60 ;      // 1 minuut
        int interval = 1000 * 60 * 30;   // half uur
        timer.schedule(taak, delay, interval);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void sendNotification(String text) {
        // create the intent for the notification
        Intent notificationIntent = new Intent(this, ItemsActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // create the pending intent
        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, flag);

        // create the variables for the notification
        int icon = R.drawable.ic_launcher_2;
        CharSequence contentTitle = getText(R.string.app_name);

        // create the notification and set its data
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(icon)
                        .setTicker("Nieuwe feed beschikbaar")
                        .setContentTitle(contentTitle)
                        .setContentText(text)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();

        // display the notification
        NotificationManager manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        final int NOTIFICATION_ID = 1;
        manager.notify(NOTIFICATION_ID, notification);
    }

    private class TimerTaak extends TimerTask {

        @Override
        public void run() {
            Log.d("News reader", "Timer task started");

            io.downloadFile();
            Log.d("News reader", "File downloaded");

            RSSFeed newFeed = io.readFile();
            Log.d("News reader", "File read");

            Log.d("News reader", "new feedPubDate: " + newFeed.getPubDateMillis());
            Log.d("News reader", "app feedPubDate" + app.getFeedMillis());

            // if new feed is newer than old feed
            if (newFeed.getPubDateMillis() > app.getFeedMillis()) {
                Log.d("News reader", "Updated feed available.");

                // update app object
                app.setFeedMillis(newFeed.getPubDateMillis());

                // display notification
                sendNotification("Nieuwe feed beschikbaar.");

                //stuur broadcast
                Intent intent = new Intent(RSSFeed.NEW_FEED);
                intent.putExtra("test", "test1 test2");
                sendBroadcast(intent);
            }
            else {
                Log.d("News reader", "Updated feed NOT available.");
            }
        }
    }
}
