package com.meghlaxshapplications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.Adapters.AdapterChatList;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.travelapp.R;
import com.models.ModelChat;
import com.models.ModelChatlist;
import com.models.ModelUsers;

import java.util.ArrayList;
import java.util.List;

public class Chats extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private List<ModelChatlist> chatlists;
    private List<ModelUsers> usersList;
    DatabaseReference reference;
    FirebaseUser currentUser;
    AdapterChatList adapterChatList;
    LottieAnimationView loading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        recyclerView = findViewById(R.id.chats_recy);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        chatlists = new ArrayList<>();
        getSupportActionBar().hide();
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);




        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chatlists.clear();
              for (DataSnapshot ds : snapshot.getChildren()){

                  ModelChatlist chatlist = ds.getValue(ModelChatlist.class);
                  chatlists.add(chatlist);

              }
              
              
              load();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void load() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);
                loadChats();

            }
        },3000);



    }

    private void loadChats() {

        usersList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){

                    ModelUsers users = ds.getValue(ModelUsers.class);
                    for (ModelChatlist chatlist : chatlists){

                        if (users.getUID() !=null && users.getUID().equals(chatlist.getId())){

                            usersList.add(users);
                            break;
                        }
                    }

                    adapterChatList = new AdapterChatList(Chats.this,usersList);
                    recyclerView.setAdapter(adapterChatList);
                    for (int i=0; i<usersList.size(); i++){

                        lastMessage(usersList.get(i).getUID());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void lastMessage(String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String theLastMessage = "default";
                for (DataSnapshot ds : snapshot.getChildren()){

                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat == null){
                        continue;
                    }
                    String  sender = chat.getSender();
                    String  reciver = chat.getReceiver();
                    if (sender == null || reciver == null){
                        continue;
                    }

                    if (chat.getReceiver().equals(currentUser.getUid()) && chat.getSender().equals(userId) || chat.getReceiver().equals(userId) && chat.getSender().equals(currentUser.getUid())){

                        theLastMessage = chat.getMessage();




                    }
                }

                adapterChatList.setLastMessageMap(userId,theLastMessage);
                adapterChatList.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}