package com.meghlaxshapplications.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class choosetype extends AppCompatActivity {
    private Button uploadreels , uploadplaces , upload_post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosetype);

        uploadreels = findViewById(R.id.uploadreels);
        uploadplaces  = findViewById(R.id.uploadplace);
        getSupportActionBar().hide();
        upload_post = findViewById(R.id.upload_post);

        upload_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(choosetype.this,Upload_Post.class);
                startActivity(intent);
            }
        });



        uploadreels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(choosetype.this,Upload_reels.class);
                startActivity(intent);
                finish();

            }
        });

        uploadplaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(choosetype.this,Upload_data.class);
                startActivity(intent);
                finish();
            }
        });
    }
}