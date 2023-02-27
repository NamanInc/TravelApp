package com.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.travelapp.R;
import com.meghlaxshapplications.travelapp.Userprofile;
import com.meghlaxshapplications.travelapp.postComments;
import com.models.ModelPostgram;

import java.util.List;



public class SapataAdapter extends RecyclerView.Adapter<SapataAdapter.MyHolder>{

    Context context;
    List<ModelPostgram> postList;

    private DatabaseReference likesRef;
    private DatabaseReference postRef;
    private FirebaseAuth firebaseAuth;

    boolean mProcessLike = false;


    public SapataAdapter(Context context, List<ModelPostgram> postList) {
        this.context = context;
        this.postList = postList;
        firebaseAuth = FirebaseAuth.getInstance();

        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sapatapost,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String profileimageurl = postList.get(position).getProfileImage();
        String posturl = postList.get(position).getPostUrl();
        String usernametxt = postList.get(position).getUsername();
        String time = postList.get(position).getTime();


        Glide.with(context).load(profileimageurl).placeholder(R.drawable.profilethub).into(holder.profileImage);
        Glide.with(context).load(posturl).placeholder(R.drawable.profilethub).into(holder.postImage);
        holder.username.setText(usernametxt);

        String pID = postList.get(position).getpID();

        isLikes(pID , holder.likebtn);
        getComments(pID , holder.number_comment);

        holder.likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.likebtn.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("LikesPost").child(postList.get(position).getpID())
                            .child(firebaseAuth.getCurrentUser().getUid()).setValue(true);

                }
                else {

                    FirebaseDatabase.getInstance().getReference().child("LikesPost").child(postList.get(position).getpID())
                            .child(firebaseAuth.getCurrentUser().getUid()).removeValue();


                }
            }
        });
        nLikes(holder.liketxt,pID);

        holder.commentslinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, postComments.class);
                intent.putExtra("postid",postList.get(position).getpID());
                intent.putExtra("publisherid",postList.get(position).getUid());
                context.startActivity(intent);

            }
        });

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Userprofile.class);
                intent.putExtra("uid",postList.get(position).getUid());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ImageView imageView;
                ImageView likebtn;
                TextView likecount;




                Dialog dialog;
                dialog = new Dialog(context);

                dialog.setContentView(R.layout.fullimageclick);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                imageView = dialog.findViewById(R.id.fullimage);
                likebtn = dialog.findViewById(R.id.likebtndialog);
                likecount = dialog.findViewById(R.id.liketxtdialog);

                Glide.with(context).load(postList.get(position).getPostUrl()).placeholder(R.color.black).into(imageView);

                isLikes(postList.get(position).getpID(),likebtn);

                nLikes(likecount,postList.get(position).getpID());


                likebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                         if (holder.likebtn.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("LikesPost").child(postList.get(position).getpID())
                            .child(firebaseAuth.getCurrentUser().getUid()).setValue(true);

                }
                else {

                    FirebaseDatabase.getInstance().getReference().child("LikesPost").child(postList.get(position).getpID())
                            .child(firebaseAuth.getCurrentUser().getUid()).removeValue();


                }
            }
        });







                dialog.show();
                return false;
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





    }

    private void getComments(String postid , TextView comments){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PostComments").child(postid);
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

    private void isLikes(String postId , ImageView imageView){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("LikesPost")
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("LikesPost")
                .child(postId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String nooflikes = snapshot.getChildrenCount()+"";

                likes.setText(nooflikes);


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

     ImageView profileImage;
     ImageView postImage;
     TextView username, time, number_comment;
     TextView liketxt;
     ImageView likebtn;
     LinearLayout commentslinear;

        public MyHolder(@NonNull View itemView) {




            super(itemView);

            postImage = itemView.findViewById(R.id.post_image);
            profileImage = itemView.findViewById(R.id.profileimage_post);
            username = itemView.findViewById(R.id.profile_name_post);
            time = itemView.findViewById(R.id.post_time);
            likebtn = itemView.findViewById(R.id.likebtn);
            liketxt = itemView.findViewById(R.id.liketxt);
            commentslinear = itemView.findViewById(R.id.comments);
            number_comment = itemView.findViewById(R.id.number_comment);





        }
    }


}
