package com.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hendraanggrian.socialview.commons.SocialAdapter;
import com.meghlaxshapplications.travelapp.R;
import com.models.ModelUsers;

import java.util.List;

public class ArrayAdapterUsers extends SocialAdapter<ModelUsers> {

    Context context;
    List<ModelUsers> usersList;

    public ArrayAdapterUsers(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }


    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.verticle_users,parent,false);

        return new MyHolder(view);
    }


    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        String userImage = usersList.get(position).getProfileImage();
        String tick = usersList.get(position).getTick();
        String Uid = usersList.get(position).getUID();
        String username = usersList.get(position).getName();


        Glide.with(context).load(userImage).placeholder(R.drawable.profilethub).into(holder.profileImage);
        holder.profilename.setText(username);
        if (tick.equals("0")){

            holder.Tick.setVisibility(View.VISIBLE);

        }
        else {
            holder.Tick.setVisibility(View.GONE);
        }

    }


    public int getItemCount() {
        return usersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{


        ImageView profileImage , Tick;
        TextView profilename;



        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_avater);
            Tick = itemView.findViewById(R.id.tick_avater);
            profilename = itemView.findViewById(R.id.name_avater);
        }
    }
}
