package com.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.FullPlace;
import com.meghlaxshapplications.travelapp.R;
import com.models.ModelPost;
import com.models.ModelUsers;

import java.util.List;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyHolder>{

    Context context;
    List<ModelPost> postList;
    List<ModelUsers> usersList;

    private DatabaseReference likesRef;
    private DatabaseReference postRef;
    private FirebaseAuth firebaseAuth;

    boolean mProcessLike = false;


    public AdapterPost(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
        firebaseAuth = FirebaseAuth.getInstance();

        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_veritcle_post,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String uid = postList.get(position).getUid();
        String thumbUrl = postList.get(position).getThumbnailUrl();
        String nameplace = postList.get(position).getPlaceName();
        String stateplace = postList.get(position).getState();
        String pLikes = postList.get(position).getPlikes();
        String pId = postList.get(position).getpId();
        String img1 = postList.get(position).getImage1();
        String img2 = postList.get(position).getImage2();
        String img3 = postList.get(position).getImage3();
        String img4 = postList.get(position).getImage4();

        holder.placename.setText(nameplace);
        holder.statename.setText(stateplace);



        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullPlace.class);
//                intent.putExtra("profileimg",postList.get(position).getProfileImage());
//                intent.putExtra("Profilename",postList.get(position).getUsername());
                intent.putExtra("Thumbnail",postList.get(position).getThumbnailUrl());
                intent.putExtra("PlaceName",postList.get(position).getPlaceName());
//                intent.putExtra("tick",postList.get(position).getTick());
                intent.putExtra("pID",postList.get(position).getpId());
                intent.putExtra("img1",postList.get(position).getImage1());
                intent.putExtra("img2",img2);
                intent.putExtra("img3",postList.get(position).getImage3());
                intent.putExtra("img4",postList.get(position).getImage4());
                intent.putExtra("des",postList.get(position).getDescription());
                intent.putExtra("state",postList.get(position).getState());
                intent.putExtra("rate",postList.get(position).getRating());
                intent.putExtra("videobyus",postList.get(position).getVideobyus());
                intent.putExtra("UID",postList.get(position).getUid());
                intent.putExtra("category",postList.get(position).getCategory());
                context.startActivity(intent);
            }
        });

        isLikes(postList.get(position).getpId(), holder.likebtn);

        holder.likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.likebtn.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postList.get(position).getpId())
                            .child(firebaseAuth.getCurrentUser().getUid()).setValue(true);

                }
                else {

                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postList.get(position).getpId())
                            .child(firebaseAuth.getCurrentUser().getUid()).removeValue();


                }
            }
        });










//        setlikes(holder,pId);

//        holder.likebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int pLikes = Integer.parseInt(postList.get(position).getPlikes());
//                mProcessLike = true;
//
//                String postIde = postList.get(position).getpId();
//
//                likesRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        if (mProcessLike){
//
//                            if (snapshot.child(postIde).hasChild(firebaseAuth.getCurrentUser().getUid())){
//
//                                postRef.child(postIde).child("plikes").setValue(""+(pLikes-1));
//                                likesRef.child(postIde).child(firebaseAuth.getCurrentUser().getUid()).removeValue();
//                                mProcessLike = false;
//                            }
//
//                            else {
//
//                                postRef.child(postIde).child("plikes").setValue(""+(pLikes+1));
//                                likesRef.child(postIde).child(firebaseAuth.getCurrentUser().getUid()).setValue("Liked");
//
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });




        try {
            Glide.with(context).load(thumbUrl).placeholder(R.drawable.jai).into(holder.thumbnailImg);



        } catch (Exception e) {
            e.printStackTrace();
        }


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

    private void nLikes(TextView likes, String postId){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String nooflikes = snapshot.getChildrenCount()+" Like";


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void setlikes(MyHolder holder, String postKey) {
//
//        likesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.child(postKey).hasChild(firebaseAuth.getCurrentUser().getUid())){
//
//
//                    holder.likebtn.setImageResource(R.drawable.like);
//
//
//
//                }
//                else {
//
//                    holder.likebtn.setImageResource(R.drawable.dislike);
//
//
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
//
//    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        ImageView thumbnailImg;
        TextView statename, placename;
        private ImageView likebtn;
        private CardView click;

        public MyHolder(@NonNull View itemView) {




            super(itemView);

            thumbnailImg = itemView.findViewById(R.id.thumb_image);
            statename = itemView.findViewById(R.id.stateofplace);
            placename = itemView.findViewById(R.id.nameofplace);
            likebtn = itemView.findViewById(R.id.likebtn);
            click = itemView.findViewById(R.id.click);




        }
    }

    private void getPublisherInfo(ImageView ProfileImage , ImageView tick, TextView name , String userId){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ModelUsers modelUsers = snapshot.getValue(ModelUsers.class);

                Glide.with(context).load(modelUsers.getProfileImage()).into(ProfileImage);
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


}
