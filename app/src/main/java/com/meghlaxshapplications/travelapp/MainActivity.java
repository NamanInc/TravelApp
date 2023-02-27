package com.meghlaxshapplications.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.meghlaxshapplications.HomeActivity;
import com.meghlaxshapplications.ProfileSetup;

public class MainActivity extends AppCompatActivity {
    Button getStarted;

    FirebaseAuth firebaseAuth ;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getStarted = findViewById(R.id.getstarted);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();



        }
        else {

        }





        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoogleLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}