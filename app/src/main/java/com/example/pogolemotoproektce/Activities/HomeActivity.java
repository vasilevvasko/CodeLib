package com.example.pogolemotoproektce.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.pogolemotoproektce.Adapters.HomeActivityHorizontalViewAdapter;
import com.example.pogolemotoproektce.Adapters.HomeActivityVerticalViewAdapter;
import com.example.pogolemotoproektce.Classes.Book;
import com.example.pogolemotoproektce.Classes.TempReadBook;
import com.example.pogolemotoproektce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeActivity extends AppCompatActivity {

    //Request codes
    private static int SORT_BOOK_BY_AUTHOR = 1;
    private static int READ_REQUEST_CODE = 2;
    private static int PERMISSION_REQUEST_STORAGE = 1000;

    //Item variables
    private ArrayList<Book> Books = new ArrayList<>();
    private ArrayList<Book> LibraryBooks = new ArrayList<>();
    private ArrayList<Book> PopularBooks = new ArrayList<>();

    //Dialog variables
    Dialog dialog;

    //Timer variable
    private long lastClickTime = 0;

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference myUploadRef;

    private FirebaseUser user;

    private final String jsonUrl = "https://api.myjson.com/bins/15iviz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("HomeActivity", "onCreate()");



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseDatabase.getInstance();

        myRef = database.getInstance().getReference().child("Books");
        myUploadRef = database.getInstance().getReference().child("Uploaded Books");

        initTabBar();
        initImageBitmaps();
        requestPermission();
        switchActivity();


        // UNIQUE PIECE OF CODE - Used to enter data in database with unique ID
        /*for (int i = 0; i < Books.size(); i++) {
            String postId = myRef.push().getKey();
            Books.get(i).setPostId(postId);
            myRef.child(postId).setValue(Books.get(i));
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SORT_BOOK_BY_AUTHOR) {
            if (resultCode == Activity.RESULT_OK) {
                String chosenAuthor = data.getStringExtra("chosenAuthor");
                initVereticleRecyclerView(sortByAuthor(chosenAuthor, Books));
            }
         /*   if (resultCode == Activity.RESULT_CANCELED) {
                //for no result
            }*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initImageBitmaps() {

        Log.i("HomeActivity", "initImageBitmaps()");


        /*Book book1 = new Book("https://imgur.com/5qLyBTr.jpg", "Doktor Ofboli", "Vasko Vasilev", 100.0, 321, 2);
        Book book2 = new Book("https://imgur.com/5qLyBTr.jpg", "Zoki Poki", "Vasko Vasilev", 70.0, 231, 3);
        Book book3 = new Book("https://imgur.com/5qLyBTr.jpg", "Sekerna Prikazna", "Vasko Vasilev", 30.0, 12, 1);
        Book book4 = new Book("https://imgur.com/5qLyBTr.jpg", "Pekolot", "Ivan Ivanov", 90.0, 666, 3);
        Book book5 = new Book("https://imgur.com/5qLyBTr.jpg", "Raj", "Ivan Ivanov", 0.0, 420, 4);
        Book book6 = new Book("https://imgur.com/5qLyBTr.jpg", "Kamasutra", "Ivan Ivanov", 0.0, 69, 5);
        Book book7 = new Book("https://imgur.com/5qLyBTr.jpg", "Cistsiliste", "Kiril Kirov", 50.0, 100, 3);

        Books.add(book1);
        Books.add(book2);
        Books.add(book3);
        Books.add(book4);
        Books.add(book5);
        Books.add(book6);
        Books.add(book7);*/


        /*parseJson();*/
        /*parseExampleJson();*/

        myRef = database.getReference("Books");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //remove all books to prevent duplicates
                Books.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Books.add(ds.getValue(Book.class));
                }

                for (int i = 0; i < Books.size(); i++) {
                    if (Books.get(i).getAvailability()) {
                        LibraryBooks.add(Books.get(i));
                    }

                    if (Books.get(i).getDonwloads() > 500) {
                        PopularBooks.add(Books.get(i));
                    }
                }


                initVereticleRecyclerView(Books);
                initFirstHorizontalRecyclerView(PopularBooks);
                initSecondHorizontalRecyclerView(LibraryBooks);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.i("IVANCE", Integer.toString(Books.size()));


    }

    private void initFirstHorizontalRecyclerView(ArrayList<Book> books) {

        Log.i("HomeActivity", "initRecyclerView()");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        HomeActivityHorizontalViewAdapter adapter = new HomeActivityHorizontalViewAdapter(this, books);
        recyclerView.setAdapter(adapter);
    }

    private void initSecondHorizontalRecyclerView(ArrayList<Book> books) {

        Log.i("HomeActivity", "initRecyclerView()");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.home_recycler_view2);
        recyclerView.setLayoutManager(layoutManager);
        HomeActivityHorizontalViewAdapter adapter = new HomeActivityHorizontalViewAdapter(this, books);
        recyclerView.setAdapter(adapter);
    }

    private void initVereticleRecyclerView(ArrayList<Book> books) {

        Log.i("HomeActivity", "initRecyclerView()");

        RecyclerView recyclerView = findViewById(R.id.home_recycler_view3);
        HomeActivityVerticalViewAdapter adapter = new HomeActivityVerticalViewAdapter(this, books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }


    private void initTabBar() {

        Log.i("HomeActivity", "initTabBar()");


        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_home_action_bar, null);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        findViewById(R.id.Bucket).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                user =  FirebaseAuth.getInstance().getCurrentUser();

                if(spamPrevented()) {
                    return;
                }

                if (user == null ) {
                    Toast.makeText(getApplicationContext(), "You need to be signed in before accessing the bucket", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (!user.isEmailVerified()) {
                    Toast.makeText(getApplicationContext(), "You need a verified account to access the bucket!", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), BucketActivity.class);
                startActivity(intent);

            }
        });

        findViewById(R.id.Menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spamPrevented()) {
                    return;
                }
                initDialog();
            }
        });
    }

    private void initDialog() {

        dialog = new Dialog(this, R.style.DialogCustomTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_menu);

        // positioning
        WindowManager.LayoutParams WMLP = dialog.getWindow().getAttributes();
        WMLP.x = -300;   //x position
        WMLP.y = 100;   //y position
        dialog.getWindow().setAttributes(WMLP);

        dialog.getWindow().getAttributes().windowAnimations = R.anim.enter_from_left;
        dialog.show();

        initDialogViews();

    }

    private void initDialogViews() {

        user =  FirebaseAuth.getInstance().getCurrentUser();

        dialog.findViewById(R.id.menuOption1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByPopularity(Books);
                initVereticleRecyclerView(Books);


                dialog.getWindow().getAttributes().windowAnimations = R.anim.exit_to_left;
                dialog.dismiss();
            }
        });


        dialog.findViewById(R.id.menuOption2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByCheapest(Books);
                initVereticleRecyclerView(Books);


                dialog.getWindow().getAttributes().windowAnimations = R.anim.exit_to_left;
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.menuOption3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initVereticleRecyclerView(showOnlyFree(Books));


                dialog.getWindow().getAttributes().windowAnimations = R.anim.exit_to_left;
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.menuLogIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getWindow().getAttributes().windowAnimations = R.anim.exit_to_left;
                dialog.dismiss();

                Intent intent = new Intent(getApplicationContext(), SinginActivity.class);
                startActivity(intent);
            }
        });

        dialog.findViewById(R.id.menuLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getWindow().getAttributes().windowAnimations = R.anim.exit_to_left;
                dialog.dismiss();

                FirebaseAuth.getInstance().signOut();
            }
        });


        if (user == null) {
            dialog.findViewById(R.id.menuLogOut).setElevation(0);
            dialog.findViewById(R.id.menuLogOut).setVisibility(View.INVISIBLE);
            dialog.findViewById(R.id.menuLogOut).setClickable(false);

            dialog.findViewById(R.id.menuLogIn).setElevation(6);
            dialog.findViewById(R.id.menuLogIn).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.menuLogIn).setClickable(true);
        }

        else {
            dialog.findViewById(R.id.menuLogOut).setElevation(6);
            dialog.findViewById(R.id.menuLogOut).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.menuLogOut).setClickable(true);

            dialog.findViewById(R.id.menuLogIn).setElevation(0);
            dialog.findViewById(R.id.menuLogIn).setVisibility(View.INVISIBLE);
            dialog.findViewById(R.id.menuLogIn).setClickable(false);
        }
    }

    private void sortByCheapest(ArrayList<Book> sortBooks) {
        Collections.sort(sortBooks, new Comparator<Book>() {
            @Override
            public int compare(Book book, Book t1) {
                return Double.valueOf(book.getPrice()).compareTo(t1.getPrice());
            }
        });
    }

    private void sortByPopularity(ArrayList<Book> sortBooks) {

        Collections.sort(sortBooks, new Comparator<Book>() {
            @Override
            public int compare(Book book, Book t1) {
                return Integer.valueOf(t1.getDonwloads()).compareTo(book.getDonwloads());
            }
        });
    }

    private ArrayList<Book> showOnlyFree(ArrayList<Book> sortBooks) {
        ArrayList<Book> tempBooks = new ArrayList<Book>();
        for (int i = 0; i < sortBooks.size(); i++) {

            if (sortBooks.get(i).getPrice() == 0) {
                tempBooks.add(sortBooks.get(i));
            }
        }
        return tempBooks;
    }



    private void sortByRating() {

        /*Collections.sort(Books, new Comparator<Book>() {
            @Override
            public int compare(Book book, Book t1) {
                return Integer.valueOf(t1.getRating()).compareTo(book.getRating());
            }
        });*/
    }

    private ArrayList<Book> sortByAuthor(String author, ArrayList<Book> sortBooks) {
        ArrayList<Book> tempBooks = new ArrayList<Book>();
        for (int i = 0; i < sortBooks.size(); i++) {

            if (sortBooks.get(i).getAuthor().compareTo(author) == 0) {
                tempBooks.add(sortBooks.get(i));
            }
        }
        return tempBooks;
    }

/*    private void parseExampleJson() {

        final ArrayList<Book> jsonBooks = new ArrayList<>();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://api.myjson.com/bins/1e448r", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE", response.toString());
                        try {
                            JSONArray array = response.getJSONArray("results");
                            JSONObject jsonObject = null;
                            for (int i = 0; i < array.length(); i++) {

                                jsonObject = array.getJSONObject(i);
                                JSONObject nameObject = jsonObject.getJSONObject("name");
                                String title = nameObject.getString("title");
                                String firstName = nameObject.getString("first");
                                String lastName = nameObject.getString("last");
                                String email = jsonObject.getString("email");

                                JSONObject prictureObject = jsonObject.getJSONObject("picture");
                                String imageUrl = prictureObject.getString("medium");

                                String fullName = title + " " + firstName + " " + lastName;

                                Book jsonBook = new Book(imageUrl, fullName, email, 50.0, 100, 3);
                                jsonBooks.add(jsonBook);

                            }

                            *//*initVereticleRecyclerView(jsonBooks);
                            initFirstHorizontalRecyclerView(jsonBooks);
                            initSecondHorizontalRecyclerView(jsonBooks);*//*

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


    }

    private void parseJson() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, "https://api.myjson.com/bins/15iviz", null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("RESPONSE", response.toString());
                    try {
                        JSONArray array = response.getJSONArray("results");
                        JSONObject jsonObject = null;
                        for (int i = 0; i < array.length(); i++) {

                            jsonObject = array.getJSONObject(i);
                            String imageUrl = jsonObject.getString("url");
                            String title = jsonObject.getString("title");
                            String author = jsonObject.getString("author");
                            double price = jsonObject.getDouble("price");
                            int downloads = jsonObject.getInt("downloads");
                            int rating = jsonObject.getInt("rating");

                            Books.add( new Book(imageUrl, title, author, price, downloads, rating));

                        }

                        initVereticleRecyclerView(Books);
                        initFirstHorizontalRecyclerView(Books);
                        initSecondHorizontalRecyclerView(Books);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
}*/

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }
    }

    private boolean spamPrevented() {
        // preventing spam
        if (SystemClock.elapsedRealtime() - lastClickTime < 300) {
            return true;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return false;
    }

    private void switchActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }
        },0);
    }
}


