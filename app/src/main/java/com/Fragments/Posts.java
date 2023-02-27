package com.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.Adapters.AdapterPost;
import com.Adapters.StoryAdapter;
import com.Adapters.TravelPostAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.Chats;
import com.meghlaxshapplications.travelapp.R;
import com.meghlaxshapplications.travelapp.Upload_Post;
import com.models.ModelPost;
import com.models.ModelPostgram;
import com.models.ModelStory;

import java.util.ArrayList;
import java.util.List;


public class Posts extends Fragment {


    private DatabaseReference databaseReference , ref ,hikingref;
    RecyclerView postRecyclerView;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private List<ModelPostgram> postList;
    private RecyclerView storyrecycler;
    private StoryAdapter storyAdapter;
    private List<ModelStory> storiesList;
    private List<String> list;
    private ImageView imageView , chatbtn;


    TravelPostAdapter adapterPost ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        postRecyclerView = view.findViewById(R.id.recyclerviewpost);
        storyrecycler = view.findViewById(R.id.storyrecycler);
        storyrecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext() , RecyclerView.HORIZONTAL ,false);
        storyrecycler.setLayoutManager(linearLayoutManager);
        imageView = view.findViewById(R.id.uploadpost_camer);
        storiesList = new ArrayList<>();
        storyAdapter = new StoryAdapter(getContext() ,storiesList);
        storyrecycler.setAdapter(storyAdapter);
        list = new ArrayList<>();
        chatbtn = view.findViewById(R.id.chatbtn);
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Chats.class);
                startActivity(intent);
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    list.add(snapshot1.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Upload_Post.class);
                startActivity(intent);
            }
        });




        firebaseAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("TravelPost");
        readStory();






        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,true);
        layoutManager.setStackFromEnd(true);
        postList = new ArrayList<>();
        postRecyclerView.setLayoutManager(layoutManager);




        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelPostgram modelPost = ds.getValue(ModelPostgram.class);

                    postList.add(modelPost);

                    adapterPost = new TravelPostAdapter(getActivity(),postList);
                    postRecyclerView.setAdapter(adapterPost);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    private void readStory(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long currenttime = System.currentTimeMillis();
                storiesList.clear();
                storiesList.add(new ModelStory("",0,0,"",firebaseAuth.getCurrentUser().getUid()));


                for (String id : list){

                    int countstory = 0;
                    ModelStory modelStory = null;
                    for (DataSnapshot snapshot1 : snapshot.child(id).getChildren()){
                        modelStory = snapshot1.getValue(ModelStory.class);
                        if (currenttime > modelStory.getTimestart() && currenttime < modelStory.getTimeend()){
                            countstory++;
                        }
                    }

                    if (countstory>0){

                        storiesList.add(modelStory);

                    }

                }




                storyAdapter.notifyDataSetChanged();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}