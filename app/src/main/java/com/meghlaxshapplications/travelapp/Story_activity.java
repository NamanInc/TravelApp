package com.meghlaxshapplications.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapters.Followers_actvity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.models.ModelStory;
import com.models.ModelUsers;

import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class Story_activity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    int counter = 0;
    long pressTime = 0L;
    long limit = 500L;


    StoriesProgressView storiesProgressView;
    ImageView image , story_photo;
    TextView story_username;


    List<String> images;
    List<String> storyids;
    String userid;

    RelativeLayout r_seen;
    TextView seen_number;
    ImageView story_delete;

   final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN :
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;

                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_activity);
        getSupportActionBar().hide();
        r_seen = findViewById(R.id.r_seen);
        seen_number = findViewById(R.id.story_views);
        story_delete = findViewById(R.id.delete_story);



        storiesProgressView = findViewById(R.id.stories);
        image = findViewById(R.id.image_story1);
        story_photo = findViewById(R.id.profile_story);
        story_username = findViewById(R.id.story_username1);

        r_seen.setVisibility(View.GONE);

        userid = getIntent().getStringExtra("userId");

        if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

            r_seen.setVisibility(View.VISIBLE);
        }

        getStories(userid);
        userInf(userid);

        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });

        reverse.setOnTouchListener(onTouchListener);

        View skip = findViewById(R.id.skip123);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });

        skip.setOnTouchListener(onTouchListener);


        r_seen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Story_activity.this, Followers_actvity.class);
                intent.putExtra("id",userid);
                intent.putExtra("storyid",storyids.get(counter));
                intent.putExtra("title","views");

                startActivity(intent);
            }
        });


        story_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userid).child(storyids.get(counter));
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(Story_activity.this, "Story Deleted", Toast.LENGTH_SHORT).show();

                    }
                });
            }



        });


    }

    @Override
    public void onNext() {
        Glide.with(getApplicationContext()).load(images.get(++counter)).into(image);
        addView(storyids.get(counter));
        seenNumber(storyids.get(counter));





    }

    @Override
    public void onPrev() {

        if ((counter - 1) < 0) return;
            Glide.with(getApplicationContext()).load(images.get(--counter)).into(image);
        seenNumber(storyids.get(counter));

    }

    @Override
    public void onComplete() {
        finish();

    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        storiesProgressView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        storiesProgressView.resume();
        super.onResume();
    }

    private void getStories(String userid) {
        images = new ArrayList<>();
        storyids = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                images.clear();
                storyids.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){

                    ModelStory story = snapshot1.getValue(ModelStory.class);
                    long timecurrent = System.currentTimeMillis();
                    if (timecurrent > story.getTimestart() && timecurrent < story.getTimeend()){
                        images.add(story.getImageurl());
                        storyids.add(story.getStoryid());

                    }
                }

                storiesProgressView.setStoriesCount(images.size());
                storiesProgressView.setStoryDuration(5000L);
                storiesProgressView.setStoriesListener(Story_activity.this);
                storiesProgressView.startStories(counter);

                Glide.with(Story_activity.this).load(images.get(counter)).into(image);

                addView(storyids.get(counter));
                seenNumber(storyids.get(counter));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void addView(String storyid){
        FirebaseDatabase.getInstance().getReference("Story").child(userid)
                .child(storyid).child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);


    }


    private void userInf(String userid){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelUsers users = snapshot.getValue(ModelUsers.class);
                Glide.with(getApplicationContext()).load(users.getProfileImage()).into(story_photo);
                story_username.setText(users.getName());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seenNumber(String  storyid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userid).child(storyid).child("views");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                seen_number.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}