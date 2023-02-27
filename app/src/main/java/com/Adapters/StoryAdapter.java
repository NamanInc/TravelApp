package com.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AddStoryActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.travelapp.R;
import com.meghlaxshapplications.travelapp.Story_activity;
import com.models.ModelStory;
import com.models.ModelUsers;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private Context context;
    private List<ModelStory> mStory;

    public StoryAdapter(Context context, List<ModelStory> mStory) {
        this.context = context;
        this.mStory = mStory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0){
            View view = LayoutInflater.from(context).inflate(R.layout.add_story, parent,false);
            return new StoryAdapter.ViewHolder(view);
        }

        else {

            View view = LayoutInflater.from(context).inflate(R.layout.story_item, parent,false);
            return new StoryAdapter.ViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ModelStory story = mStory.get(position);

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(story.getUid())){

            userInfo(holder , story.getUid() , position);

        }else {

            userInfo(holder , story.getUid() , position);
        }


        if (holder.getAdapterPosition() !=0){

            seenStory(holder , story.getUid());
        }

        if (holder.getAdapterPosition() == 0){
            myStory(holder.addstory_text , holder.story_plus , false);


        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() == 0){
                    myStory( holder.addstory_text , holder.story_plus ,true);
                }
                else {

                    Intent intent = new Intent(context, Story_activity.class);
                    intent.putExtra("userId",story.getUid());
                    context.startActivity(intent);


                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return mStory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView story_photo , story_plus , story_photo_seen;
        public TextView story_username , addstory_text;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            story_photo = itemView.findViewById(R.id.story_photo);
            story_plus = itemView.findViewById(R.id.story_plus);
            story_photo_seen = itemView.findViewById(R.id.story_photo_seen);
            story_username = itemView.findViewById(R.id.story_username);
            addstory_text = itemView.findViewById(R.id.addstorytext);

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0){

            return 0;

        }

        return 1;

    }

    private void userInfo(ViewHolder viewHolder , String userId , int position){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelUsers users = snapshot.getValue(ModelUsers.class);

                    Glide.with(context).load(users.getProfileImage()).into(viewHolder.story_photo);

                if (position !=0){

                    Glide.with(context).load(users.getProfileImage()).into(viewHolder.story_photo_seen);
                    viewHolder.story_username.setText(users.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void myStory(TextView textView , ImageView imageView  , boolean click) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                long timecurrent = System.currentTimeMillis();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    ModelStory story = snapshot.getValue(ModelStory.class);
                    if (timecurrent > story.getTimestart() && timecurrent < story.getTimeend()){

                        count++;
                    }
                }

                if (click){

                   if (count > 0){




                       AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                       alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "View story", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                               Intent intent = new Intent(context, Story_activity.class);
                               intent.putExtra("userId",FirebaseAuth.getInstance().getCurrentUser().getUid());
                               context.startActivity(intent);
                               dialog.dismiss();


                           }
                       });

                       alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Story", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                               Intent intent = new Intent(context, AddStoryActivity.class);
                               context.startActivity(intent);
                               dialog.dismiss();

                           }
                       });

                       alertDialog.show();


                   }

                   else {

                       Intent intent = new Intent(context, AddStoryActivity.class);
                       context.startActivity(intent);

                   }
                }
                else {
                    if (count >0){
                        textView.setText("My Story");
                        imageView.setVisibility(View.GONE);
                    }
                    else {
                        textView.setText("Add Story");
                        imageView.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void  seenStory(ViewHolder viewHolder ,  String userId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story")
                .child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()){

                    if (!snapshot1.child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists() && System.currentTimeMillis() < snapshot1.getValue(ModelStory.class).getTimeend()){
                        i++;
                    }
                }

                if ( i > 0){
                    viewHolder.story_photo.setVisibility(View.VISIBLE);
                    viewHolder.story_photo_seen.setVisibility(View.GONE);


                }
                else {

                    viewHolder.story_photo_seen.setVisibility(View.VISIBLE);
                    viewHolder.story_photo.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
