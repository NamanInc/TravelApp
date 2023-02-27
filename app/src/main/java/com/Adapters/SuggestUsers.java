package com.Adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.travelapp.R;
import com.models.ModelUsers;

import java.util.List;

public class SuggestUsers extends RecyclerView.Adapter<SuggestUsers.MyHolder> {

    Context context;
    List<ModelUsers> usersList;
    private FirebaseAuth firebaseAuth;


    public SuggestUsers(Context context, List<ModelUsers> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.suggestusers,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        firebaseAuth = FirebaseAuth.getInstance();


        String userImage = usersList.get(position).getProfileImage();
        String tick = usersList.get(position).getTick();
        String Uid = usersList.get(position).getUID();
        String username = usersList.get(position).getName();
        String uid = usersList.get(position).getUID();


        Glide.with(context).load(userImage).placeholder(R.drawable.profilethub).into(holder.profileImage);
        holder.profilename.setText(username);
        if (tick.equals("0")){

            holder.Tick.setVisibility(View.VISIBLE);

        }
        else {
            holder.Tick.setVisibility(View.GONE);
        }

        DatabaseReference referencea  = FirebaseDatabase.getInstance().getReference().child("Follow").child(uid).child("followers");
        referencea.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.numcounts.setText(snapshot.getChildrenCount()+" Followers");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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




    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{


        ImageView profileImage , Tick;
        TextView followtxt;
        CardView followbtn;
        ProgressBar progressBar;
        TextView profilename;
        TextView numcounts;




        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_naman);
            Tick = itemView.findViewById(R.id.tick_naman);
            profilename = itemView.findViewById(R.id.name_naman);
            followbtn = itemView.findViewById(R.id.profile_follow_btn);
            progressBar = itemView.findViewById(R.id.progressbar);
            followtxt = itemView.findViewById(R.id.profile_follow_text);
            numcounts = itemView.findViewById(R.id.followercount);

        }
    }
}
