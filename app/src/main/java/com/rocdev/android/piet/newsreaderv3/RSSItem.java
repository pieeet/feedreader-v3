package com.rocdev.android.piet.newsreaderv3;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class RSSItem {

    private String title = null;
    private String description = null;
    private String link = null;
    private String pubDate = null;

    private final SimpleDateFormat dateOutFormat =
            new SimpleDateFormat("EEEE h:mm a (MMM d)");

    private final SimpleDateFormat dateInFormat =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

    public void setTitle(String title)     {

        this.title = title;
    }

    public String getTitle() {

        return title;
    }

    public void setDescription(String description)     {

        this.description = description;
    }

    public String getDescription() {

        return description;
    }

    public void setLink(String link) {

        this.link = link;
    }

    public String getLink() {

        return link;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }


    public String getPubDateFormatted() {
        try {
            Date date = dateInFormat.parse(pubDate.trim());
            return dateOutFormat.format(date);
        }
        catch (Exception e) {
            //throw new RuntimeException(e);
            return pubDate;
        }
    }
}