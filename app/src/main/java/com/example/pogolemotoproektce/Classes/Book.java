package com.example.pogolemotoproektce.Classes;


public class Book implements Comparable {

    public Book() {
        this.url = "No image URL";
        this.title = "Title unavialable";
        this.author = "Author unavailable";
        this.price = 0;
        this.downloads = 0;
        this.type = "2";
        this.availableAtLibrary = false;
        this.description = "No description available";

    }

    public Book(String url, String title, String author, double price, int donwloads, String type, boolean availableAtLibrary, String description){
        this.url = url;
        this.title = title;
        this.author = author;
        this.price = price;
        this.downloads = donwloads;
        this.type = type;
        this.availableAtLibrary = availableAtLibrary;
        this.description = description;
    }


    // Variables
    private String url;
    private String title;
    private String author;
    private double price;
    private int downloads;
    private String type;
    private String postId;
    private boolean availableAtLibrary;
    private String description;




    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}

    public double getPrice() {return price;}
    public void setPrice(double price) {this.price = price;}

    public int getDonwloads() {return downloads; }
    public void setDonwloads(int donwloads) {this.downloads = donwloads;}

    public String getType() {return type;}
    public void setType(String rating) {this.type = rating;}

    public String getPostId() {return postId;}
    public void setPostId(String postId) {this.postId = postId;}

    public boolean getAvailability() {return availableAtLibrary;}
    public void setAvailability(boolean availableAtLibrary) {this.availableAtLibrary = availableAtLibrary;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String logBookInfo() {
        return  "InfoAboutBook:" + this.getAuthor() + " | " + this.getTitle() + " | " + this.getPrice() + " | " + this.getDonwloads() + " | " + this.getType() + " | " + this.getUrl() + " | " + this.getAvailability() + " | " + this.getDescription();
    }


    @Override
    public int compareTo(Object temp) {
        Book other = (Book) temp;

        if (getPrice() > other.getPrice()) {
            return 1;
        }
        else if (getPrice() < other.getPrice()) {
            return -1;
        }
        else
            return 0;
    }

}