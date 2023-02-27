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

import com.bumptech.glide.Glide;
import com.meghlaxshapplications.travelapp.ChatActivity;
import com.meghlaxshapplications.travelapp.R;
import com.models.ModelChatlist;
import com.models.ModelUsers;

import java.util.HashMap;
import java.util.List;

public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.MyHolder> {

    Context context;
    List<ModelUsers> usersList;
    private HashMap<String , String > lastMessageMap;


    public AdapterChatList(Context context, List<ModelUsers> usersList) {
        this.context = context;
        this.usersList = usersList;
        lastMessageMap = new HashMap<>();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String hisUid = usersList.get(position).getUID();
        String  userImage = usersList.get(position).getProfileImage();
        String username = usersList.get(position).getName();
        String lastMessage = lastMessageMap.get(hisUid);


        holder.name.setText(username);
        if (lastMessage==null || lastMessage.equals("default")){

            holder.lastmessage.setVisibility(View.GONE);
        }
        else {

            holder.lastmessage.setVisibility(View.VISIBLE);
            holder.lastmessage.setText(lastMessage);

        }

        try{

            Glide.with(context).load(userImage).into(holder.profileIv);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (usersList.get(position).getOnlineStatus().equals("online")){

            holder.onlinestatusIv.setVisibility(View.VISIBLE);





        }
        else {

            holder.onlinestatusIv.setVisibility(View.GONE);


        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("uid",hisUid);
                context.startActivity(intent);
            }
        });

    }

    public void setLastMessageMap(String userId , String lastMessage){

        lastMessageMap.put(userId,lastMessage);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView profileIv ,  onlinestatusIv;
        TextView name , lastmessage;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profileIv = itemView.findViewById(R.id.profileivIv);
            onlinestatusIv = itemView.findViewById(R.id.onlineStatusIv);
            name = itemView.findViewById(R.id.nameTv);
            lastmessage = itemView.findViewById(R.id.lastmessageTv);


        }
    }
}
