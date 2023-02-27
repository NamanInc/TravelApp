package com.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.models.Comment_model;
import com.models.ModelUsers;

import java.util.List;

public class Comment_adapter extends RecyclerView.Adapter<Comment_adapter.ViewHolder> {

    private Context mContext;
    private List<Comment_model> mComment;

    public Comment_adapter(Context mContext, List<Comment_model> mComment) {
        this.mContext = mContext;
        this.mComment = mComment;
    }

    private FirebaseUser firebaseUser;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.commentlist , parent ,false);

        return new Comment_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Comment_model comment = mComment.get(position);
        holder.comment.setText(comment.getComment());
        getUserInfo(holder.image_profile , holder.username , comment.getPublisher() , holder.tick);

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "h", Toast.LENGTH_SHORT).show();
            }
        });

        holder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Userprofile.class);
                intent.putExtra("uid",comment.getPublisher());
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profile , tick;
        public TextView username , comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.comment_profile);
            tick = itemView.findViewById(R.id.comment_tick);
            username = itemView.findViewById(R.id.comment_username);
            comment = itemView.findViewById(R.id.comment_text);

        }
    }

    private void getUserInfo(ImageView imageView , TextView username , String publisherID , ImageView tick){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(publisherID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelUsers user = snapshot.getValue(ModelUsers.class);

                try {
                    Glide.with(mContext).load(user.getProfileImage()).into(imageView);
                    username.setText(user.getName());

                    if (user.getTick().equals("1")){
                        tick.setVisibility(View.GONE);


                    }
                    else {

                        tick.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
