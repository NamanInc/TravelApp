package com.meghlaxshapplications.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullImages extends AppCompatActivity {
    private ImageView imageView;
    String img1, img2 , img3 , img4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_images);
        getSupportActionBar().hide();
        imageView = findViewById(R.id.image_full);
        Intent intent = getIntent();
       img1 =  intent.getStringExtra("img1url");
       img2 =  intent.getStringExtra("img2url");
       img3 =  intent.getStringExtra("img3url");
       img4 =  intent.getStringExtra("img4url");

       if (intent.getStringExtra("img1url")!=null){
           Glide.with(this).load(img1).into(imageView);

       }
       else if (intent.getStringExtra("img2url")!=null){
           Glide.with(this).load(img2).into(imageView);
       }
       else if (intent.getStringExtra("img3url")!=null){

           Glide.with(this).load(img3).into(imageView);

       }

       else if (intent.getStringExtra("img4url")!=null){

           Glide.with(this).load(img4).into(imageView);

       }

       }




    }
