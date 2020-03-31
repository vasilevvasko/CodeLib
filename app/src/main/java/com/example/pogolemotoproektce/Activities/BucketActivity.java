package com.example.pogolemotoproektce.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pogolemotoproektce.Adapters.BucketActivityViewAdapter;
import com.example.pogolemotoproektce.Classes.Book;
import com.example.pogolemotoproektce.Classes.BucketListSingleton;
import com.example.pogolemotoproektce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class BucketActivity extends AppCompatActivity {

    long lastClickTime = 0;

    ArrayList<Book> books;
    TextView txtTotal;
    BucketListSingleton bucketListSingleton;

    Double totalPrice;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket);

        user =  FirebaseAuth.getInstance().getCurrentUser();

        getBucketArray();
        initTabBar();
        initBucketRecyclerView(books);
        setTotal();
    }

    private void initBucketRecyclerView(ArrayList<Book> books) {

        Log.i("HomeActivity", "initRecyclerView()");

        RecyclerView recyclerView = findViewById(R.id.bucket_recycler_view);
        BucketActivityViewAdapter adapter = new BucketActivityViewAdapter(this, books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

    }

    private void initTabBar() {

        Log.i("HomeActivity", "initTabBar()");

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_bucket_action_bar, null);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        findViewById(R.id.bucketBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spamPrevented()) {
                    return;
                }
                finish();
            }
        });
        findViewById(R.id.bucketCheckOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spamPrevented()) {
                    return;
                }

                if(books == null) {
                    Toast.makeText(getApplicationContext(), "You cannot check out with an empty bucket", Toast.LENGTH_LONG).show();
                    return;
                }

                if (user == null ) {
                    Toast.makeText(getApplicationContext(), "You need to be signed in before accessing the ceheck out screen", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (!user.isEmailVerified()) {
                    Toast.makeText(getApplicationContext(), "You need a verified account to access the ceheck out screen", Toast.LENGTH_LONG).show();
                    return;
                }


                Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                intent.putExtra("total", totalPrice);
                startActivity(intent);
            }
        });
    }

    private void getBucketArray() {

        Log.i("HomeActivity", "getBucketArray()");
        bucketListSingleton = BucketListSingleton.getInstance();

        books = bucketListSingleton.getBucketList();
    }

    private void setTotal() {
        txtTotal = findViewById(R.id.txtTotal);
        totalPrice = 0.0;
        for (int i = 0; i < books.size(); i++) {
            totalPrice = totalPrice + books.get(i).getPrice();
        }
        txtTotal.setText(totalPrice + " BGN");
    }

    public void restart(ArrayList<Book> books) {
        onRestart();
        initTabBar();
        initBucketRecyclerView(books);
        setTotal();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
