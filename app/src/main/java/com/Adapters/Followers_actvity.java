package com.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.travelapp.R;
import com.models.ModelUsers;

import java.util.ArrayList;
import java.util.List;

public class Followers_actvity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<ModelUsers> usersList;
    private AdapterUsers adapterUsers;
    private String id;
    private List<String> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_actvity);
        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recyclerview_followers);
        usersList = new ArrayList<>();
        idList = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterUsers = new AdapterUsers(this,usersList);
        recyclerView.setAdapter(adapterUsers);

        getviews();






    }

    private void getviews() {

        String id = getIntent().getStringExtra("userid");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getIntent().getStringExtra("storyid")).child("views");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    idList.add(snapshot1.getKey());
                }
                showusers();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showusers() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){

                    ModelUsers users = snapshot1.getValue(ModelUsers.class);
                    for (String id : idList){
                        if (users.getUID().equals(id)){

                            usersList.add(users);

                        }


                    }
                }

                adapterUsers.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}