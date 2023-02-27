package com.meghlaxshapplications.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapters.Comment_adapter;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.models.Comment_model;
import com.models.ModelUsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class postComments extends AppCompatActivity {
    private EditText addcomment;
    private ImageView image_profile;
    private TextView post;

    String postId;
    String publisherid;


    FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private Comment_adapter commentAdapter;
    private List<Comment_model> commentModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);
        getSupportActionBar().hide();

        addcomment = findViewById(R.id.addcomment);
        image_profile = findViewById(R.id.image_profile);
        post = findViewById(R.id.post_comment);
        recyclerView = findViewById(R.id.comment_recyclerview);
        commentModelList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new Comment_adapter(postComments.this,commentModelList);
        recyclerView.setAdapter(commentAdapter);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postid");
        publisherid = intent.getStringExtra("publisherid");

        readcomment();


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addcomment.getText().toString().equals("")){

                    Toast.makeText(postComments.this, "Please write a Comment", Toast.LENGTH_SHORT).show();

                }
                else {

                    addComment();
                }
            }
        });

        getImage();


    }

    private void addComment() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PostComments").child(postId);

        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("comment", addcomment.getText().toString());
        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.push().setValue(hashMap);

        addcomment.setText("");
    }

    private void getImage(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelUsers user = snapshot.getValue(ModelUsers.class);
                Glide.with(getApplicationContext()).load(user.getProfileImage()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readcomment(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PostComments").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentModelList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){

                    Comment_model comment_model = snapshot1.getValue(Comment_model.class);
                    commentModelList.add(comment_model);


                }

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}