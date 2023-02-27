package com.meghlaxshapplications.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapters.AdapterChat;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.listners.UsersListner;
import com.models.ModelChat;
import com.models.ModelUsers;
import com.notification.APIService;
import com.notification.Data;
import com.notification.Sender;
import com.notification.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatActivity extends AppCompatActivity  implements UsersListner {

    RecyclerView recyclerView;
    ImageView profileIv;
    TextView nametv , statustv , sendbtn;
    EditText messageEt;
    String hisUid;
    FirebaseAuth firebaseAuth;
    String uid;
    FirebaseDatabase firebaseDatabase;
    ValueEventListener seenListener;
    DatabaseReference userrefforseen;
    List<ModelChat> chatList;
    AdapterChat adapterChat;
   private RequestQueue requestQueue;
   private ImageView videocallbtn;
   private ModelUsers users;
   private boolean notify = false;


    String hisImage;

    DatabaseReference userDRef;
    private UsersListner usersListner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        hisUid = getIntent().getStringExtra("uid");

        recyclerView = findViewById(R.id.chat_recyclerview);
        profileIv  = findViewById(R.id.profile_tv);
        nametv = findViewById(R.id.nameTv);
        statustv = findViewById(R.id.userStatTv);
        sendbtn  = findViewById(R.id.sendbtn);
        messageEt = findViewById(R.id.messageEt);
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        videocallbtn = findViewById(R.id.videocallbutton);

        videocallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usersListner.initiateMeeting(users);


            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //apiService  = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);class
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        firebaseDatabase = FirebaseDatabase.getInstance();
        userDRef = firebaseDatabase.getReference("Users");

        Query userQuery = userDRef.orderByChild("UID").equalTo(hisUid);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    String name = ""+ds.child("Name").getValue();
                     hisImage = ""+ds.child("ProfileImage").getValue();
                     String typing = ""+ds.child("typingTo").getValue();

                     if (typing.equals(uid)){
                         statustv.setText("typing....");
                     }
                     else {
                         String  onliineStatus = ""+ds.child("onlineStatus").getValue();


                         if (onliineStatus.equals("online")){
                             statustv.setText(onliineStatus);

                         }
                         else {
                             Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                             calendar.setTimeInMillis(Long.parseLong(onliineStatus));
                             String dateTime = DateFormat.format("hh:mm aa",calendar).toString();
                             statustv.setText("Last seen at : " +dateTime);

                         }


                     }



                    nametv.setText(name);

                     try {
                         Glide.with(getApplicationContext()).load(hisImage).into(profileIv);

                     } catch (Exception e) {
                         e.printStackTrace();
                     }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message = messageEt.getText().toString().trim();
                if (TextUtils.isEmpty(message)){
                    Toast.makeText(ChatActivity.this, "Type a message...", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendmessage(message);
                }

                messageEt.setText("");
            }


        });

        messageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() ==0){

                    checkTypingStatus("noOne");
                }
                else {

                    try {

                        checkTypingStatus(hisUid);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        readmessage();
        seenMessage();









    }

    private void checkTypingStatus(String  typing) {

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("typingTo",typing);
        dbref.updateChildren(hashMap);



    }

    private void checkOnlineStatus(String  status) {

        try {
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            HashMap<String , Object> hashMap = new HashMap<>();
            hashMap.put("onlineStatus",status);
            dbref.updateChildren(hashMap);
        }
        catch (Exception e){

        }





    }

    private void seenMessage() {

        userrefforseen   = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userrefforseen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(uid) && chat.getSender().equals(hisUid)){

                        HashMap<String  , Object>  hashseen = new HashMap<>();
                        hashseen.put("isSeen",true);
                        ds.getRef().updateChildren(hashseen);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readmessage() {

        chatList  = new ArrayList<>();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Chats");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(uid) && chat.getSender().equals(hisUid) || chat.getReceiver().equals(hisUid) && chat.getSender().equals(uid)){
                        chatList.add(chat);
                    }

                    adapterChat = new AdapterChat(ChatActivity.this,chatList,hisImage);
                    adapterChat.notifyDataSetChanged();

                    recyclerView.setAdapter(adapterChat);



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendmessage(String message) {
        String timestamp = String.valueOf(System.currentTimeMillis());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("sender",uid);
        hashMap.put("receiver",hisUid);
        hashMap.put("message",message);
        hashMap.put("timestamp",timestamp);
        hashMap.put("isSeen",false);
        databaseReference.child("Chats").push().setValue(hashMap);


        String msg = message;
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ModelUsers users = snapshot.getValue(ModelUsers.class);
                if (notify){
                    sendNotification(hisUid,users.getName(),message);
                }

                notify = false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Chatlist").child(uid).child(hisUid);
        chatRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()){

                    chatRef1.child("id").setValue(hisUid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("Chatlist").child(hisUid).child(uid);

        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()){

                    chatRef2.child("id").setValue(uid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendNotification(String hisUid, String name, String message) {

        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){

                    Token token = ds.getValue(Token.class);
                    Data data = new Data(uid , name+" : "+message , "New Message",hisUid , R.drawable.notificationiconw , "CHAT");
                    Sender sender = new Sender(data,token.getToken());

                    try {
                        JSONObject senderIsonObj = new JSONObject(new Gson().toJson(sender));
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderIsonObj, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {



                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String  , String > header = new HashMap<>();
                                header.put("Content-Type","application/json");
                                header.put("Authorization","key=AAAA595KiFg:APA91bHa88SbyC6w2Ktvc3PFEbD3iusB1k2hjjYTDXvx3_TwuwcuYFuIqyd6_xlkixwrwDZRDFJJowqX1t1WrcbrsmXO9xs9pGhRI6ADj832ACnbKaGbaZuN2FztSoxV_l2ph527At5r");

                                return header;
                            }
                        };

                        requestQueue.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        checkOnlineStatus("online");

        super.onStart();
    }

    @Override
    protected void onPause() {

        super.onPause();
        String timestamp = String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        checkTypingStatus("noOne");
        userrefforseen.removeEventListener(seenListener);


    }

    private String calculatetimeAgo(String datepost){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            long time = sdf.parse(datepost).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return ""+ago;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onResume() {
        checkOnlineStatus("online");
        super.onResume();
    }

    @Override
    public void initiateMeeting(ModelUsers user) {

    }

    @Override
    public void initialAudioMeeting(ModelUsers users) {

    }
}