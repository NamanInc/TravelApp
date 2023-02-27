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
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.travelapp.R;
import com.models.ModelPost;
import com.models.ModelUsers;

import java.util.List;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelAdapterViewHolder> {
    private Context context;

    private List<ModelPost> postList;

    public TravelAdapter(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public TravelAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TravelAdapterViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewpager,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TravelAdapterViewHolder holder, int position) {

        holder.placename.setText(postList.get(position).getPlaceName());
        Glide.with(context).load(postList.get(position).getThumbnailUrl()).into(holder.image);
        Glide.with(context).load(postList.get(position).getProfileImage()).into(holder.profileimage);


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class TravelAdapterViewHolder extends RecyclerView.ViewHolder{
        private KenBurnsView image;
        private TextView placename;
        private ImageView profileimage;

        public TravelAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imagepost);
            placename = itemView.findViewById(R.id.place_name);
            profileimage = itemView.findViewById(R.id.profile_av);


        }







    }


}
