package com.example.pogolemotoproektce.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pogolemotoproektce.R;

public class CheckoutActivity extends AppCompatActivity {

    long lastClickTime = 0;

    EditText nameEditText;
    EditText surnameEditText;
    EditText addressEditText;
    EditText apartmentEditText;
    EditText cityEditText;
    EditText regionEditText;
    EditText postalCodeEditText;

    EditText cardNumberEditText;
    EditText cardHolderNameEditText;
    EditText exparationDateEditText;
    EditText securityCodeEditText;

    Double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Intent intent = getIntent();
        total = getIntent().getDoubleExtra("total", 0.0);

        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        apartmentEditText = findViewById(R.id.apartmentEditText);
        cityEditText = findViewById(R.id.cityEditText);
        regionEditText = findViewById(R.id.regionEditText);
        postalCodeEditText = findViewById(R.id.postalCodeEditText);
        cardNumberEditText = findViewById(R.id.cardEditText);
        cardHolderNameEditText = findViewById(R.id.cardNameEditText);
        exparationDateEditText = findViewById(R.id.cardExpirationDateEditText);
        securityCodeEditText = findViewById(R.id.cardSecurityCodeEditText);


        initTabBar();
    }



    private void initTabBar() {

        Log.i("CheckOutActivity", "initTabBar()");

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_checkout_action_bar, null);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
                builder.setNegativeButton("Cancel Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Negative Button
                    }
                });
                builder.setPositiveButton("Confirm Purchase", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Purchase successful", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setMessage(nameEditText.getText() + " " + surnameEditText.getText() + "\n" + addressEditText.getText() + " " + apartmentEditText.getText() + " " + cityEditText.getText() + " " + regionEditText.getText() + " " + postalCodeEditText.getText() + "\n" + total + "BGN" );
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
