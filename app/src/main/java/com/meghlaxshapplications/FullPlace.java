package com.meghlaxshapplications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapters.AdapterPost;
import com.Adapters.AdapterProducts;
import com.Adapters.AdapterUsers;
import com.Adapters.Fullscreenvideo;
import com.Adapters.SuggestUsers;
import com.Fragments.MapActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.meghlaxshapplications.travelapp.FullImages;
import com.meghlaxshapplications.travelapp.MapsFragment;
import com.meghlaxshapplications.travelapp.R;
import com.meghlaxshapplications.travelapp.Userprofile;
import com.models.ModelPost;
import com.models.ModelProducts;
import com.models.ModelUsers;
import com.notification.Data;
import com.notification.Sender;
import com.notification.Token;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class FullPlace extends AppCompatActivity {
    private ImageView thumbimage,tick,profileimg , likeimg , img1 , img2 , img3 , img4;
    private TextView profile_name,placename , numlike ,stateshow;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private MaterialRatingBar ratebar;
    private TextView textvideo;
    private TextView ratingtext;
    private SimpleExoPlayerView exoPlayerView;
    private RecyclerView recyclerView12;
    private CardView card_exo;
    private RelativeLayout mapviewbtn;
    private TextView des;
    private ProgressBar progressBar;
    private String ratingst , videobyus;
    private List<ModelProducts> productsList;
    private LinearLayout clickprofile;
    private CardView followbtn;
    private TextView followtxt;
    private SimpleExoPlayer exoPlayer;
    private List<ModelUsers> usersList;
    private String categry;
    private String thumbUrl,tickurl,profileimageurl,profilenametxt,placenametxt , pId , img1st , img2st , img3st , img4st , uid;
    private ImageView img1full , img2full , img3full , img4full , videofull;
    private boolean fullscreen = false;

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    // private APIService apiService;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullplace);
        thumbimage = findViewById(R.id.thumImage);
        tick = findViewById(R.id.profiletick);
        profile_name = findViewById(R.id.profilename);
        firebaseAuth = FirebaseAuth.getInstance();
        profileimg = findViewById(R.id.userprofile);
        placename = findViewById(R.id.nameofplace);
        numlike = findViewById(R.id.likenum);
        likeimg = findViewById(R.id.likebtnfull);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mapviewbtn = findViewById(R.id.mapviewbtn);
        getSupportActionBar().hide();
        img1 = findViewById(R.id.img1_show);
        img2 = findViewById(R.id.img2_show);
        img3 = findViewById(R.id.img3_show);
        img4 = findViewById(R.id.img4_show);
        des = findViewById(R.id.description_show);
        stateshow = findViewById(R.id.state_show);
        ratebar = findViewById(R.id.ratebar);
        ratingtext = findViewById(R.id.ratingtext);
        exoPlayerView = findViewById(R.id.exoplayerview);
        textvideo = findViewById(R.id.textvideo);
        card_exo = findViewById(R.id.card_exo);
        clickprofile = findViewById(R.id.profileclick);
        followbtn = findViewById(R.id.followbtnfull);
        followtxt = findViewById(R.id.followtxt);
        progressBar = findViewById(R.id.progressbar12);
        img1full = findViewById(R.id.img1full);
        img2full = findViewById(R.id.img2full);
        img3full = findViewById(R.id.img3full);
        img4full = findViewById(R.id.img4full);
        videofull = findViewById(R.id.video_full);
        recyclerView = findViewById(R.id.recyclerviewproducts);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        productsList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        loadproducts();

        mapviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullPlace.this, MapActivity.class);
                startActivity(intent);
            }
        });







        img1full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullPlace.this, FullImages.class);
                intent.putExtra("img1url",img1st);

                startActivity(intent);
            }
        });

        img2full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullPlace.this, FullImages.class);
                intent.putExtra("img2url",img2st);

                startActivity(intent);
            }
        });

        img3full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullPlace.this, FullImages.class);
                intent.putExtra("img3url",img3st);

                startActivity(intent);
            }
        });

        img4full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullPlace.this, FullImages.class);
                intent.putExtra("img4url",img4st);

                startActivity(intent);
            }
        });

        videofull.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent intent = new Intent(FullPlace.this, Fullscreenvideo.class);
                                             intent.putExtra("videourl",videobyus);
                                             exoPlayer.getPlaybackState();
                                             pausePlayer();

                                             startActivity(intent);




                                         }

                                     });




//
//        Intent intent = new Intent(context, FullPlace.class);
//        intent.putExtra("ProfileImage",postList.get(position).getProfileImage());
//        intent.putExtra("Profilename",postList.get(position).getUsername());
//        intent.putExtra("Thumbnail",postList.get(position).getThumbnailUrl());
//        intent.putExtra("PlaceName",postList.get(position).getPlaceName());
//        intent.putExtra("tick",postList.get(position).getTick());

//        intent.putExtra("img1",postList.get(position).getImage1());
//        intent.putExtra("img2",postList.get(position).getImage2());
//        intent.putExtra("img3",postList.get(position).getImage3());
//        intent.putExtra("img4",postList.get(position).getImage4());

        thumbUrl = getIntent().getStringExtra("Thumbnail");
       // tickurl = getIntent().getStringExtra("tick");
       // profileimageurl = getIntent().getStringExtra("profileimg");
        placenametxt = getIntent().getStringExtra("PlaceName");
       // profilenametxt = getIntent().getStringExtra("Profilename");
        ratingst = getIntent().getStringExtra("rate");
        pId = getIntent().getStringExtra("pID");
        img1st = getIntent().getStringExtra("img1");
        img2st = getIntent().getStringExtra("img2");
        img3st = getIntent().getStringExtra("img3");
        img4st = getIntent().getStringExtra("img4");
        videobyus = getIntent().getStringExtra("videobyus");
        uid = getIntent().getStringExtra("UID");
        categry = getIntent().getStringExtra("category");


        des.setText(getIntent().getStringExtra("des"));
        stateshow.setText(getIntent().getStringExtra("state"));
        ratebar.setRating(Float.parseFloat(ratingst));
        ratingtext.setText(ratingst);

        getPublisherInfo(profileimg,tick,profile_name,uid);


        if (uid.equals(firebaseAuth.getCurrentUser().getUid())){

            followbtn.setVisibility(View.GONE);
        }

        checkfollow();

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

                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(FullPlace.this,R.style.BottomSheetDialogtheme);

                            View bottomview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet,(LinearLayout)findViewById(R.id.bottomlinerar));

                            usersList = new ArrayList<>();

                            recyclerView12 = bottomview.findViewById(R.id.suggestrecyclerview);

                            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false);
                            recyclerView12.setLayoutManager(linearLayoutManager1);




                            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    usersList.clear();
                                    for (DataSnapshot ds: snapshot.getChildren()){

                                        ModelUsers modelUsers = ds.getValue(ModelUsers.class);

                                        if (!modelUsers.getUID().equals(fUser.getUid())){
                                            usersList.add(modelUsers);

                                        }

                                        SuggestUsers suggestUsers = new SuggestUsers(getApplicationContext(),usersList);

                                        Collections.shuffle(usersList);


                                        recyclerView12.setAdapter(suggestUsers);


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });






                            bottomSheetDialog.setContentView(bottomview);
                            bottomSheetDialog.show();


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

        clickprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullPlace.this, Userprofile.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });







        loaddata();


        videobyUs();
        likeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeimg.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(pId)
                            .child(firebaseAuth.getCurrentUser().getUid()).setValue(true);

                }
                else {

                    FirebaseDatabase.getInstance().getReference().child("Likes").child(pId)
                            .child(firebaseAuth.getCurrentUser().getUid()).removeValue();


                }
            }
        });
        isLikes(pId,likeimg);
        nrLikes(numlike,pId);



    }

    private void loadsuggestUsers() {




    }

    private void loadproducts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UploadProducts");

//        Query query = reference.orderByChild("Category").equalTo(categry);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                productsList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelProducts modelPost1 = ds.getValue(ModelProducts.class);

                    productsList.add(modelPost1);

                    AdapterProducts adapterPost = new AdapterProducts(getApplicationContext(),productsList);
                    recyclerView.setAdapter(adapterPost);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }

    private void sendNotification(String hisUid, String name) {

        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    Token token = ds.getValue(Token.class);
                    Data data = new Data(firebaseAuth.getCurrentUser().getUid() , name+" Started Following you on Sair Sapata " , "New Follower",hisUid , R.drawable.notificationiconw,"FOLLOW");
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

    private void videobyUs() {

        if (videobyus.equals("1")){

            exoPlayerView.setVisibility(View.GONE);
            textvideo.setVisibility(View.GONE);
            card_exo.setVisibility(View.GONE);
        }
        else {

            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);

                Uri videoUri = Uri.parse(videobyus);
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videoUri,dataSourceFactory,extractorsFactory,null,null);
                exoPlayerView.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(false);


            } catch (Exception e) {

                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void loaddata() {


        Glide.with(FullPlace.this).load(thumbUrl).placeholder(R.drawable.jai).into(thumbimage);
        Glide.with(FullPlace.this).load(profileimageurl).placeholder(R.drawable.jai).into(profileimg);
        Glide.with(FullPlace.this).load(img1st).placeholder(R.drawable.jai).into(img1);
        Glide.with(FullPlace.this).load(img2st).placeholder(R.drawable.jai).into(img2);
        Glide.with(FullPlace.this).load(img3st).placeholder(R.drawable.jai).into(img3);
        Glide.with(FullPlace.this).load(img4st).placeholder(R.drawable.jai).into(img4);

        placename.setText(placenametxt);
        profile_name.setText(profilenametxt);

//        if (tickurl.equals("0")){
//            tick.setVisibility(View.VISIBLE);
//        }else {
//            tick.setVisibility(View.GONE);
//        }
    }

    private void isLikes(String postId , ImageView imageView){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()){

                    imageView.setImageResource(R.drawable.like);
                    imageView.setTag("liked");
                }
                else {

                    imageView.setImageResource(R.drawable.dislike);
                    imageView.setTag("like");
                }
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
    private void getPublisherInfo(ImageView ProfileImage , ImageView tick, TextView name , String userId){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ModelUsers modelUsers = snapshot.getValue(ModelUsers.class);

                Glide.with(FullPlace.this).load(modelUsers.getProfileImage()).into(ProfileImage);
                name.setText(modelUsers.getName());

                String tickst = modelUsers.getTick();

                if (tickst.equals("0")){
                    tick.setVisibility(View.VISIBLE);

                }
                else {
                    tick.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void nrLikes(TextView likes , String postid){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+" likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (exoPlayer!=null){

          pausePlayer();

        }



    }
    @Override
    protected void onPause() {
        if (exoPlayer!=null){
            pausePlayer();

        }

        super.onPause();
    }


    @Override
    protected void onResume() {
        if (exoPlayer!=null){
           exoPlayer.setPlayWhenReady(false);

        }
        // exoPlayer.release();
        super.onResume();
    }

    private void pausePlayer(){
        exoPlayer.setPlayWhenReady(false);
        exoPlayer.getPlaybackState();
    }
    private void startPlayer(){
        exoPlayer.setPlayWhenReady(false);
        exoPlayer.getPlaybackState();
    }
}