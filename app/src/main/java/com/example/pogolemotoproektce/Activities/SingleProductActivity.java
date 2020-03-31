package com.example.pogolemotoproektce.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pogolemotoproektce.Classes.Book;
import com.example.pogolemotoproektce.Classes.BucketListSingleton;
import com.example.pogolemotoproektce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class SingleProductActivity extends AppCompatActivity {

    long lastClickTime = 0;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String image;
    private String author;
    private double price;
    private String title;
    private int downloads;
    private String type;
    private boolean availability;
    private String descriptiom;

    Book book = new Book();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("SingleProductActivity", "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        fetchDataFromIntent();
        initTabBar();
        initLayoutViews();

    }

    private void fetchDataFromIntent() {

        Log.i("SingleProductActivity", "fetchDataFromIntent()");

        Gson gson = new Gson();
        String arrayAsString = getIntent().getExtras().getString("Book");
        book = gson.fromJson(arrayAsString , Book.class);

        image = book.getUrl();
        title = book.getTitle();
        author = book.getAuthor();
        price = book.getPrice();
        downloads = book.getDonwloads();
        type = book.getType();
        availability = book.getAvailability();
        descriptiom = book.getDescription();

    }

    private void initTabBar() {

        Log.i("SingleProductActivity", "initTabBar()");

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_product_action_bar, null);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        TextView textView = findViewById(R.id.productTitle);
        textView.setText(title);

        findViewById(R.id.productBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spamPrevented()) {
                    return;
                }
                finish();
            }
        });


}

    private void initLayoutViews() {

        Log.i("SingleProductActivity", "initLayoutViews()");

        ImageView imageView = findViewById(R.id.productImage);
        Glide.with(getApplicationContext()).asBitmap().load(image).into(imageView);

        TextView authorTextView = findViewById(R.id.productAuthor);
        authorTextView.setText(author);


        findViewById(R.id.sortByAuthor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(spamPrevented()) {
                    return;
                }
                Intent returnIntent = new Intent(getApplicationContext(), HomeActivity.class);
                returnIntent.putExtra("chosenAuthor", author);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        TextView productTextView = findViewById(R.id.productInfo);
        productTextView.setText(descriptiom);

        TextView priceTextView = findViewById(R.id.productPrice);
        priceTextView.setText(price + " BGN");

        if (availability) {
            findViewById(R.id.availableAtPanitza).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.availableAtPanitza).setVisibility(View.INVISIBLE);
        }

        findViewById(R.id.productAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(spamPrevented()) {
                    return;
                }

                if (user == null) {
                    Toast.makeText(getApplicationContext(), "You need to be singed in to add an item to the bucket!", Toast.LENGTH_LONG).show();
                }

                else if (!user.isEmailVerified()) {
                    Toast.makeText(getApplicationContext(), "You need a verified account to add an item to the bucket!", Toast.LENGTH_LONG).show();
                }
                else {
                    BucketListSingleton bucketListSingleton = BucketListSingleton.getInstance();
                    if (bucketListSingleton.addToBucket(book)) {
                        Toast.makeText(getApplicationContext(), "Adding item to bucket!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Item is already in the bucket!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean spamPrevented() {
        // preventing spam
        if (SystemClock.elapsedRealtime() - lastClickTime < 300) {
            return true;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return false;
    }

}
