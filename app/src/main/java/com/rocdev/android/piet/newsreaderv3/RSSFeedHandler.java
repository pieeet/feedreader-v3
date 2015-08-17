package com.rocdev.android.piet.newsreaderv3;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * Created by Piet on 7-3-2015.
 */
class RSSFeedHandler extends DefaultHandler {
    private RSSFeed feed;
    private RSSItem item;
    private boolean feedTitleHasBeenRead;
    private boolean feedPubDateHasBeenRead;
    private boolean isTitle;
    private boolean isFeedTitle1;
    private boolean isFeedTitle2;
    private boolean isItemDescription;
    private boolean isFeedDescription;
    private boolean isLink;
    private boolean isPubDate;



    public RSSFeed getFeed() {
        return feed;
    }

    @Override
    public void startDocument() throws SAXException {
        feed = new RSSFeed();
        feedTitleHasBeenRead = false;
        feedPubDateHasBeenRead = false;
        isTitle = false;
        isFeedTitle1 = false;
        isFeedTitle2 = false;
        isItemDescription = false;
        isFeedDescription = false;
        isLink = false;
        isPubDate = false;

    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        switch (qName) {
            case "item":
                item = new RSSItem();
                break;
            case "title":
                isTitle = true;
                if (!isFeedTitle1) {
                    isFeedTitle1 = true;
                } else if (!isFeedTitle2) {
                    isFeedTitle2 = true;
                }
                break;
            case "description":
                if (!isFeedDescription) {
                    isFeedDescription = true;
                } else {
                    isItemDescription = true;
                }
                break;
            case "guid":
                isLink = true;
                return;
            case "pubDate":
                isPubDate = true;
                break;

        }
    }

    @Override
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException
    {
        if (qName.equals("item")) {
            feed.addItem(item);
        }
    }

    @Override
    public void characters(char ch[], int start, int length)
    {
        String s = new String(ch, start, length);
        if (isTitle) {
            if (isFeedTitle1 && isFeedTitle2) {
                if (!feedTitleHasBeenRead) {
                    feed.setTitle(s);
                    feedTitleHasBeenRead = true;
                }  else {
                    item.setTitle(s);
                }
            }
            isTitle = false;
        }
        else if (isLink) {
            item.setLink(s);
            isLink = false;
        }
        else if (isItemDescription) {
            item.setDescription(s);
            isItemDescription = false;
        }
        else if (isPubDate) {
            if (!feedPubDateHasBeenRead) {
                feed.setPubDate(s);
                feedPubDateHasBeenRead = true;
            }
            item.setPubDate(s);
            isPubDate = false;
        }
    }
}
