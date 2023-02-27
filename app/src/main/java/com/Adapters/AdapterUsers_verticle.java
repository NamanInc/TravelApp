package com.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.meghlaxshapplications.travelapp.R;
import com.meghlaxshapplications.travelapp.Userprofile;
import com.models.ModelUsers;

import java.util.List;

public class AdapterUsers_verticle extends RecyclerView.Adapter<AdapterUsers_verticle.MyHolder> {

    Context context;
    List<ModelUsers> usersList;
    LottieAnimationView lottieAnimationView;


    public AdapterUsers_verticle(Context context, List<ModelUsers> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.horizontal_users,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        String userImage = usersList.get(position).getProfileImage();
        String tick = usersList.get(position).getTick();
        String Uid = usersList.get(position).getUID();
        String username = usersList.get(position).getName();
        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(context, Userprofile.class);

                 intent.putExtra("uid",Uid);
                 context.startActivity(intent);

            }
        });


        Glide.with(context).load(userImage).placeholder(R.drawable.profilethub).into(holder.profileImage);
        holder.profilename.setText(username);
        if (tick.equals("0")){

            holder.Tick.setVisibility(View.VISIBLE);

        }
        else {
            holder.Tick.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{


        ImageView profileImage , Tick;
        TextView profilename;



        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_naman);
            Tick = itemView.findViewById(R.id.tick_naman);
            profilename = itemView.findViewById(R.id.name_naman);
        }
    }
}
