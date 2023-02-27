package com.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;

import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hendraanggrian.widget.SocialTextView;
import com.meghlaxshapplications.travelapp.R;
import com.meghlaxshapplications.travelapp.Userprofile;
import com.meghlaxshapplications.travelapp.postCommentsvideo;
import com.models.ModelPost;
import com.models.ModelUsers;
import com.models.ModelVideos;
import com.notification.Data;
import com.notification.Sender;
import com.notification.Token;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ModelVideos> videosList;

    Context context;


    private DatabaseReference likesRef;
    private DatabaseReference postRef;
    private FirebaseAuth firebaseAuth;
    private RequestQueue requestQueue;
    // private APIService apiService;
    boolean notify = false;




    public VideoAdapter(Context context, List<ModelVideos> postList) {
        this.context = context;
        this.videosList = postList;
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {

        View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reels_layout,null);
        view1.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        VideoAdapter.CustomViewHolder viewHolder1 = new VideoAdapter.CustomViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {

        CustomViewHolder holder= (CustomViewHolder) holder1;

        String profileUrl = videosList.get(position).getProfileurl();
        String profilename = videosList.get(position).getUsername();
        String captionst = videosList.get(position).getCaption();
        String uid = videosList.get(position).getUID();

        if (uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

            holder.followbtn.setVisibility(View.GONE);
        }

        requestQueue = Volley.newRequestQueue(context);


        holder.profilename.setText(profilename);
        holder.captionset.setText(captionst);

        if (videosList.get(position).getTick().equals("1")){

            holder.tickwhite.setVisibility(View.GONE);
        }
        else {

            holder.tickwhite.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(profileUrl).placeholder(R.drawable.profilethub).into(holder.profileImage);

        holder.followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (holder.followtxt.getText().equals("Follow")){
                    holder.progressBar.setVisibility(View.VISIBLE);
                  holder.followtxt.setVisibility(View.GONE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseAuth.getCurrentUser().getUid())
                                    .child("following").child(uid).setValue(true);
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).child("followers").child(firebaseAuth.getCurrentUser().getUid()).setValue(true);
                            holder.progressBar.setVisibility(View.GONE);
                            holder.followtxt.setVisibility(View.VISIBLE);



                        }
                    },2000);



                }else if (holder.followtxt.getText().equals("Following")){

                    holder.progressBar.setVisibility(View.VISIBLE);
                   holder.followtxt.setVisibility(View.GONE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseAuth.getCurrentUser().getUid())
                                    .child("following").child(uid).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).child("followers").child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                           holder.progressBar.setVisibility(View.GONE);
                            holder.followtxt.setVisibility(View.VISIBLE);

                        }
                    },2000);



                }


            }
        });

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Userprofile.class);
                intent.putExtra("uid",uid);
                context.startActivity(intent);
            }
        });

        holder.profilename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Userprofile.class);
                intent.putExtra("uid",uid);
                context.startActivity(intent);
            }
        });

        holder.comment_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, postCommentsvideo.class);
                intent.putExtra("postid",videosList.get(position).getpId());
                intent.putExtra("publisherid",videosList.get(position).getUID());

                context.startActivity(intent);
            }
        });





        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseAuth.getCurrentUser().getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(uid).exists()){
                   holder.followtxt.setText("Following");
                }
                else {
                   holder.followtxt.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        try {

//            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//
//            // track selector is used to navigate between
//            // video using a default seekbar.
//            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//
//            // we are adding our track selector to exoplayer.
//            holder.exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
//
//
//            Uri videoUri = Uri.parse(videoUrl);
//            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
//            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//            MediaSource mediaSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);
//            holder.reels_player.setPlayer(holder.exoPlayer);
//            holder.exoPlayer.prepare(mediaSource);
//
//          AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setUsage(C.USAGE_MEDIA)
//                    .setContentType(C.CONTENT_TYPE_MOVIE)
//                    .build();
//          holder.exoPlayer.setAudioAttributes(audioAttributes);
//          holder.exoPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);


          getnrLikes(videosList.get(position).getpId(),holder.likenum);
          getComments(videosList.get(position).getpId(),holder.comment_number);



            isLikes(videosList.get(position).getpId(), holder.likebtn, holder.likeanimation);

            holder.likebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notify = true;



                    if (holder.likebtn.getTag().equals("like")){
                        FirebaseDatabase.getInstance().getReference().child("VideoLikes").child(videosList.get(position).getpId())
                                .child(firebaseAuth.getCurrentUser().getUid()).setValue(true);

                        holder.likeanimation.setVisibility(View.VISIBLE);

                        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
                        database.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                ModelUsers users = snapshot.getValue(ModelUsers.class);
                                if (notify){

                                    if (!uid.equals(firebaseAuth.getCurrentUser().getUid())){

                                        sendNotification(uid,users.getName());

                                    }

                                }

                                notify = false;

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                               holder.likeanimation.setVisibility(View.GONE);



                            }
                        },1200);


                    }
                    else {

                        FirebaseDatabase.getInstance().getReference().child("VideoLikes").child(videosList.get(position).getpId())
                                .child(firebaseAuth.getCurrentUser().getUid()).removeValue();


                    }
                }


            });



            } catch (Exception e) {

            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }



    }

    private void sendNotification(String hisUid, String name) {

        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    Token token = ds.getValue(Token.class);
                    Data data = new Data(firebaseAuth.getCurrentUser().getUid() , name+" Liked Your Reels " , "Reel Like",hisUid , R.drawable.notificationiconw,"FOLLOW");
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

    private void getnrLikes(String postid , TextView likes) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("VideoLikes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getComments(String postid , TextView comments){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PostCommentsVideo").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isLikes(String postId , ImageView imageView , LottieAnimationView likeanimation) {


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("VideoLikes")
                .child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()) {



                    imageView.setImageResource(R.drawable.like);

                    imageView.setTag("liked");
                } else {

                    imageView.setImageResource(R.drawable.dislike);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView profilename;
        ImageView profileImage , tickwhite;
        SocialTextView captionset;
        ImageView likebtn;
        TextView likenum;
        RelativeLayout followbtn;
        TextView followtxt;
        ProgressBar progressBar;
        LottieAnimationView likeanimation;
        ImageView comment_icon;
        TextView comment_number;



        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            profilename = itemView.findViewById(R.id.profile_username);
            profileImage = itemView.findViewById(R.id.profile_imageview);
            captionset = itemView.findViewById(R.id.captios_set);
            likebtn = itemView.findViewById(R.id.likebtnvideo);
            likenum = itemView.findViewById(R.id.likecountvideo);
            followbtn = itemView.findViewById(R.id.profile_follow_btn1);
            followtxt = itemView.findViewById(R.id.text_follow);
            progressBar = itemView.findViewById(R.id.progressbar13);
            tickwhite = itemView.findViewById(R.id.tickwhite);
            comment_icon = itemView.findViewById(R.id.comment_reels);
            comment_number = itemView.findViewById(R.id.comment_reels_number);
            likeanimation = itemView.findViewById(R.id.likeanimation);



        }

    }
}
