package com.example.pogolemotoproektce.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pogolemotoproektce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SinginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    EditText singInEmailEditText, singInPasswordEditText, createAccountEmailEditText, createAccountPasswordEditText;
    Button registerButton, singInButton;

    ProgressBar progressBar;

    long lastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);


        initTabBar();

        mAuth = FirebaseAuth.getInstance();

        createAccountEmailEditText = (EditText) findViewById(R.id.CreateAccountEmailEditText);
        createAccountPasswordEditText = (EditText) findViewById(R.id.CreateAccountPasswordEditText);
        singInEmailEditText = (EditText) findViewById(R.id.SingInEmailEditText);
        singInPasswordEditText = (EditText) findViewById(R.id.SingInPasswordEditText);

        registerButton = (Button) findViewById(R.id.CreateAccountButton);
        singInButton = (Button) findViewById(R.id.SingInButton);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spamPrevented()) {
                    return;
                }
                registerUser();
            }
        });

        singInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spamPrevented()) {
                    return;
                }
                userSingIn();
            }
        });
    }

    private void registerUser() {


        String email = createAccountEmailEditText.getText().toString().trim();
        String password = createAccountPasswordEditText.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (email.isEmpty()) {
            createAccountEmailEditText.setError("Email is required");
            createAccountEmailEditText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            createAccountEmailEditText.setError("Please enter a valid email");
            createAccountEmailEditText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (password.isEmpty()) {
            createAccountPasswordEditText.setError("Password is required");
            createAccountPasswordEditText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (password.length() < 6) {
            createAccountPasswordEditText.setError("Minimum length of password is 6 characters");
            createAccountPasswordEditText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "Email is already registered!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    }

    private void userSingIn() {

        String email = singInEmailEditText.getText().toString().trim();
        String password = singInPasswordEditText.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (email.isEmpty()) {
            singInEmailEditText.setError("Email is required");
            singInEmailEditText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            singInEmailEditText.setError("Please enter a valid email");
            singInEmailEditText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (password.isEmpty()) {
            singInPasswordEditText.setError("Password is required");
            singInPasswordEditText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (password.length() < 6) {
            singInPasswordEditText.setError("Minimum length of password is 6");
            singInPasswordEditText.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {

                        user = FirebaseAuth.getInstance().getCurrentUser();

                        if (!user.isEmailVerified()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SinginActivity.this);
                            builder.setMessage("Your account is not verified!");
                            builder.setTitle("Acount Verification");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Verify Account", new DialogInterface.OnClickListener() {
                               @Override
                            public void onClick(DialogInterface dialog, int which) {
                                   user.sendEmailVerification();
                                   Toast.makeText(getApplicationContext(), "An verification email has been sent!", Toast.LENGTH_SHORT).show();
                                   finish();
                               }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Welcome back! ", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private boolean validationCheck(String email, String password) {

        boolean validationSucesfull = true;

        if (email.isEmpty()) {
            createAccountEmailEditText.setError("Email is required");
            createAccountEmailEditText.requestFocus();
            validationSucesfull = false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            createAccountEmailEditText.setError("Please enter a valid email");
            createAccountEmailEditText.requestFocus();
            validationSucesfull = false;
        }

        if (password.isEmpty()) {
            createAccountPasswordEditText.setError("Password is required");
            createAccountPasswordEditText.requestFocus();
            validationSucesfull = false;
        }

        if (password.length() < 6) {
            createAccountPasswordEditText.setError("Minimum length of password is 6");
            createAccountPasswordEditText.requestFocus();
            validationSucesfull = false;
        }

        return validationSucesfull;
    }

    private void initTabBar() {

        Log.i("SingleProductActivity", "initTabBar()");

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_product_action_bar, null);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


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

    private boolean spamPrevented() {
        // preventing spam
        if (SystemClock.elapsedRealtime() - lastClickTime < 300) {
            return true;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return false;
    }
}
