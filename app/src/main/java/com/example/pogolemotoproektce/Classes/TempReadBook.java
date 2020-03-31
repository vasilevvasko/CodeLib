package com.example.pogolemotoproektce.Classes;

import android.util.Log;

public class TempReadBook {

    public TempReadBook() {
    }

    public TempReadBook(String callNumber, String title, String author, String publisher) {
        this.callNumber = callNumber;
        this.title = title;
        this.author = author;
        this.publisher = publisher;

    }


    // Variables
    private String callNumber;
    private String title;
    private String author;
    private String publisher;


    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void logBookInfo() {

        Log.e("BOOK BOOK BOOK", "");
        Log.e("Call Number:", callNumber);
        Log.e("Title:", title);
        Log.e("Author:", author);
        Log.e("Publisher:", publisher);
    }
}
