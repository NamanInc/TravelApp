package com.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.travelapp.R;
import com.models.ModelChat;

import java.util.HashMap;
import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {


    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<ModelChat> chatList;
    String imageUrl;
    FirebaseUser fUser;

    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.right_chat ,parent , false);
            return new MyHolder(view);


        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.left_chat, parent, false);
            return new MyHolder(view);


        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String message = chatList.get(position).getMessage();


        holder.messageTv.setText(message);
        try {
            Glide.with(context).load(imageUrl).into(holder.profileIv);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.messageTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete the message ?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletemessage(position);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();



                    }
                });

                builder.create().show();


                return true;
            }
        });

        if (position == chatList.size()-1){
            if (chatList.get(position).isSeen()){
                holder.isSeentv.setText("Seen");
            }
            else {
                holder.isSeentv.setText("Delivered");

            }


        }
        else {
            holder.isSeentv.setVisibility(View.GONE);
        }


    }

    private void deletemessage(int position) {

        String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String msgTimestamp = chatList.get(position).getTimestamp();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = dbref.orderByChild("timestamp").equalTo(msgTimestamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    if (ds.child("sender").getValue().equals(myUID)){

                        ds.getRef().removeValue();

//                        HashMap<String , Object> hashMap = new HashMap<>();
//                        hashMap.put("message","This message was deleted....");
//                        ds.getRef().updateChildren(hashMap);
                        Toast.makeText(context, "Message Deleted...", Toast.LENGTH_SHORT).show();

                    }

                    else {

                        Toast.makeText(context, "Can't delete this message.", Toast.LENGTH_SHORT).show();
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(fUser.getUid())){

            return MSG_TYPE_RIGHT;

        }
        else {

            return MSG_TYPE_LEFT;

        }
    }

    class MyHolder extends RecyclerView.ViewHolder{


        ImageView profileIv;
        TextView messageTv , timeTv , isSeentv;
        LinearLayout messagelayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profileIv = itemView.findViewById(R.id.profileIv);
            messageTv = itemView.findViewById(R.id.messagetv);
            isSeentv = itemView.findViewById(R.id.isSeentv);
            messagelayout = itemView.findViewById(R.id.messagelayout);

        }
    }
}
