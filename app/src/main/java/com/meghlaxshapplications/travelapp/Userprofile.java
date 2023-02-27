package com.meghlaxshapplications.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapters.AdapterPost;
import com.Adapters.AdapterUsers_verticle;
import com.Adapters.SapataAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.models.ModelPost;
import com.models.ModelPostgram;
import com.models.ModelUsers;
import com.notification.APIService;
import com.notification.Data;
import com.notification.Sender;
import com.notification.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Userprofile extends AppCompatActivity {
    String uid;
    private ImageView profileimage;
    private TextView username;
    private TextView userbio , numfollowing , numfollowers , numposts;
    private ImageView tick;
    private FirebaseAuth firebaseAuth;
    private CardView followbtn;
    private TextView followtxt;
    private ProgressBar progressBar;
    private RecyclerView followers_recyclerview , post_recyclerview;
    private List<String> idlist;
    private List<ModelUsers> usersList;
    private AdapterUsers_verticle adapter;
    private List<ModelPost> postList;
    private AdapterPost adapterPost;
    private ImageView messagebtn;
    private RequestQueue requestQueue;
    private RecyclerView sapatapostrecyclerview;
   // private APIService apiService;
    boolean notify = false;
    private List<ModelPostgram> postgrams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        getSupportActionBar().hide();

        Window window = getWindow();

        uid = getIntent().getStringExtra("uid");

        Log.d("Naman",uid);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        followers_recyclerview = findViewById(R.id.recyclerview_followers_profile);
        post_recyclerview = findViewById(R.id.postRecyclerview1);
        messagebtn = findViewById(R.id.message_btn_profile);
        LinearLayoutManager hinkinglayoutmanager = new LinearLayoutManager(Userprofile.this,LinearLayoutManager.HORIZONTAL,true);
        hinkinglayoutmanager.setStackFromEnd(true);
         hinkinglayoutmanager.setReverseLayout(true);
         postgrams = new ArrayList<>();
         sapatapostrecyclerview = findViewById(R.id.travelpost);

         GridLayoutManager gridlayout=  new GridLayoutManager(this,3);
         sapatapostrecyclerview.setLayoutManager(gridlayout);


        postList = new ArrayList<>();
        post_recyclerview.setLayoutManager(hinkinglayoutmanager);
     //   apiService  = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);
        requestQueue = Volley.newRequestQueue(getApplicationContext());



        idlist = new ArrayList<>();







        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        profileimage = findViewById(R.id.profile_image_profile);
        username = findViewById(R.id.profile_name_profile);
        tick = findViewById(R.id.tick);
        followbtn = findViewById(R.id.profile_follow_btn);
        followtxt = findViewById(R.id.profile_follow_text);
        numfollowers = findViewById(R.id.num_followers);
        numfollowing = findViewById(R.id.num_following);
        numposts  = findViewById(R.id.num_posts);


        usersList = new ArrayList<>();
        followers_recyclerview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,true);
        linearLayoutManager1.setStackFromEnd(true);

        followers_recyclerview.setLayoutManager(linearLayoutManager1);

        adapter = new AdapterUsers_verticle(this,usersList);
        followers_recyclerview.setAdapter(adapter);

        messagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Userprofile.this,ChatActivity.class);
                intent.putExtra("uid",uid);

                startActivity(intent);
            }
        });



// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);




// finally change the color
//        window.setStatusBarColor(ContextCompat.getColor(Userprofile.this,R.color.barcolor));

        loadmyposts();




        loaduserdata();

        getfollowers();
        getfollowing();
        getnumberofPOst();

        getUserfollowers();
        loadsapatapost();


        if (uid.equals(firebaseAuth.getCurrentUser().getUid())){


            followtxt.setText("Edit Profile");
        }
        else {

            checkfollow();


        }







        followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (followtxt.getText().equals("Follow")){
                    progressBar.setVisibility(View.VISIBLE);
                    followtxt.setVisibility(View.GONE);
                    notify = true;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseAuth.getCurrentUser().getUid())
                                    .child("following").child(uid).setValue(true);
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).child("followers").child(firebaseAuth.getCurrentUser().getUid()).setValue(true);
                            progressBar.setVisibility(View.GONE);
                            followtxt.setVisibility(View.VISIBLE);

                            DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
                            database.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    ModelUsers users = snapshot.getValue(ModelUsers.class);
                                    if (notify){
                                        sendNotification(uid,users.getName());
                                    }

                                    notify = false;

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }
                    },2000);



                }else if (followtxt.getText().equals("Following")){

                    progressBar.setVisibility(View.VISIBLE);
                    followtxt.setVisibility(View.GONE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseAuth.getCurrentUser().getUid())
                                    .child("following").child(uid).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).child("followers").child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                            progressBar.setVisibility(View.GONE);
                            followtxt.setVisibility(View.VISIBLE);

                        }
                    },2000);



                }


            }
        });



    }

    private void loadsapatapost() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TravelPost");
        Query query = reference.orderByChild("Uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postgrams.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelPostgram myPosts = ds.getValue(ModelPostgram.class);
                    postgrams.add(myPosts);

                  //  Collections.shuffle(postgrams);

                    SapataAdapter sapataAdapter = new SapataAdapter(Userprofile.this,postgrams);
                    sapatapostrecyclerview.setAdapter(sapataAdapter);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

//    private void sendNotification(String hisUid, String name) {
//
//        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
//        Query query = allTokens.orderByKey().equalTo(hisUid);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds : snapshot.getChildren()){
//
//                    Token token = ds.getValue(Token.class);
//                    Data data = new Data(firebaseAuth.getCurrentUser().getUid() , name+" Started Following you on Sair Sapata " , "New Message",hisUid , R.drawable.notificationiconw,"FOLLOW");
//                    Sender sender = new Sender(data,token.getToken());
//                    apiService.sendNotification(sender)
//                            .enqueue(new Callback<Response>() {
//                                @Override
//                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//                                    Toast.makeText(Userprofile.this, ""+response.message(), Toast.LENGTH_SHORT).show();
//                                }
//
//                                @Override
//                                public void onFailure(Call<Response> call, Throwable t) {
//
//                                }
//                            });
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void sendNotification(String hisUid, String name) {

        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    Token token = ds.getValue(Token.class);
                    Data data = new Data(firebaseAuth.getCurrentUser().getUid() , name+" Started Following you on Sair Sapata " , "New Message",hisUid , R.drawable.notificationiconw,"FOLLOW");
                    Sender sender = new Sender(data,token.getToken());

                    try {
                        JSONObject senderIsonObj = new JSONObject(new Gson().toJson(sender));
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderIsonObj, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {



                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String  , String > header = new HashMap<>();
                                header.put("Content-Type","application/json");
                                header.put("Authorization","key=AAAA595KiFg:APA91bHa88SbyC6w2Ktvc3PFEbD3iusB1k2hjjYTDXvx3_TwuwcuYFuIqyd6_xlkixwrwDZRDFJJowqX1t1WrcbrsmXO9xs9pGhRI6ADj832ACnbKaGbaZuN2FztSoxV_l2ph527At5r");

                                return header;
                            }
                        };

                        requestQueue.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadmyposts() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = reference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelPost myPosts = ds.getValue(ModelPost.class);
                    postList.add(myPosts);

                    adapterPost = new AdapterPost(Userprofile.this,postList);
                    post_recyclerview.setAdapter(adapterPost);



                }
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
                    for (String id : idlist){
                        if (users.getUID().equals(id)){

                            usersList.add(users);

                        }


                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserfollowers() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(uid).child("followers");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                idlist.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                     idlist.add(snapshot.getKey());
                }

                showusers();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getnumberofPOst() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()){

                    ModelPost modelPost = snapshot1.getValue(ModelPost.class);
                    if (modelPost.getUid().equals(uid)){
                        i++;

                    }
                }
                numposts.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getfollowing() {

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numfollowing.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getfollowers() {

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numfollowers.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkfollow() {



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseAuth.getCurrentUser().getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(uid).exists()){
                    followtxt.setText("Following");
                }
                else {
                    followtxt.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loaduserdata() {

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("UID").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){

                    String name = ""+ds.child("Name").getValue();
                    String profileImageUrl = ""+ds.child("ProfileImage").getValue();
                    String Tick = ""+ds.child("Tick").getValue();

                    username.setText(name);
                    Glide.with(Userprofile.this).load(profileImageUrl).placeholder(R.drawable.profilethub).into(profileimage);

                    if (Tick.equals("1")){

                        tick.setVisibility(View.GONE);


                    }
                    else {
                        tick.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}