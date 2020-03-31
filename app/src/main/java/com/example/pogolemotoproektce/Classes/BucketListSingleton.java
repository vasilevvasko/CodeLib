package com.example.pogolemotoproektce.Classes;


import java.util.ArrayList;



public class BucketListSingleton {

    private static BucketListSingleton instance;
    private ArrayList<Book> bucketList = new ArrayList<>();



    public static BucketListSingleton getInstance() {
        if (instance == null)
            instance = new BucketListSingleton();
        return instance;
    }

    private BucketListSingleton() {
    }

    public ArrayList<Book> getBucketList() {
        return bucketList;
    }

    private boolean checkIfBookExcists(Book book) {
        boolean excists = false;
        for ( int i = 0; i < bucketList.size(); i++) {
            if (book.getTitle().compareTo(bucketList.get(i).getTitle()) == 0) {
                excists = true;
            }
            else {
                excists = false;
            }
        }
        return excists;
    }

    public boolean addToBucket(Book book) {

        if(!checkIfBookExcists(book)){
            bucketList.add(book);
            return true;
        }
        else return false;
        /*if (!bucketList.contains(book)) {
            bucketList.add(book);
        }*/
    }
}
