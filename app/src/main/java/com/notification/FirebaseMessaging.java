package com.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.meghlaxshapplications.travelapp.ChatActivity;
import com.meghlaxshapplications.travelapp.R;
import com.meghlaxshapplications.travelapp.Userprofile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FirebaseMessaging extends FirebaseMessagingService {
    private Bitmap image;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        SharedPreferences sp = getSharedPreferences("SP_USER",MODE_PRIVATE);
        String savedcurrentUser = sp.getString("Current_USERID","None");

        String sent = remoteMessage.getData().get("sent");
        String user = remoteMessage.getData().get("users");

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser!=null && sent.equals(fUser.getUid())){
            if (!savedcurrentUser.equals(user)){
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

                    sendOAndAboveNotification(remoteMessage);
                }
                else {

                    try {
                        URL url = new URL("https://firebasestorage.googleapis.com/v0/b/travel-app-aaf79.appspot.com/o/WeUploadImages%2FCopy%20of%20Black%20and%20Yellow%20Modern%20Social%20Media%20Marketing%20Trends%20Presentation.png?alt=media&token=e23eaa33-3b46-48ba-8c5d-04fa8079de47");

                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch(IOException e) {
                        System.out.println(e);
                    }
                    sendNormalNotification(remoteMessage);

                }
            }
        }
    }

    private void sendNormalNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String type = remoteMessage.getData().get("TYPE");

        if (type.equals("CHAT")){
            Uri uri;


            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int i = Integer.parseInt(user.replaceAll("[\\D]",""));
            Intent intent = new Intent(this, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("uid",user);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);
            Uri defsoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(Integer.parseInt(icon))
                    .setContentText(body)
                    .setContentTitle(title)
                    .setAutoCancel(true)


                    .setSound(defsoundUri)
                    .setContentIntent(pIntent);
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            int j = 0;
            if (i>0){
                j=i;
            }

            notificationManager.notify(j,builder.build());

        }else if (type.equals("FOLLOW")){






            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int i = Integer.parseInt(user.replaceAll("[\\D]",""));
            Intent intent = new Intent(this, Userprofile.class);
            Bundle bundle = new Bundle();
            bundle.putString("uid",user);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);
            Uri defsoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(Integer.parseInt(icon))
                    .setContentText(body)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setSound(defsoundUri)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(image))
                    .setContentIntent(pIntent);
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            int j = 0;
            if (i>0){
                j=i;
            }

            notificationManager.notify(j,builder.build());
        }






    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendOAndAboveNotification(RemoteMessage remoteMessage) {


        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String type = remoteMessage.getData().get("TYPE");

        if (type.equals("CHAT")){

            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int i = Integer.parseInt(user.replaceAll("[\\D]",""));
            Intent intent = new Intent(this, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("uid",user);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);
            Uri defsoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            OreoAndAboveNotification notification1 = new OreoAndAboveNotification(this);
            Notification.Builder builder = notification1.getONotifications(title,body,pIntent,defsoundUri,icon);



            int j = 0;
            if (i>0){
                j=i;
            }

            notification1.getManager().notify(j,builder.build());

        }
        else if (type.equals("FOLLOW")){

            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int i = Integer.parseInt(user.replaceAll("[\\D]",""));
            Intent intent = new Intent(this, Userprofile.class);
            Bundle bundle = new Bundle();
            bundle.putString("uid",user);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);
            Uri defsoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            OreoAndAboveNotification notification1 = new OreoAndAboveNotification(this);
            Notification.Builder builder = notification1.getONotifications(title,body,pIntent,defsoundUri,icon);



            int j = 0;
            if (i>0){
                j=i;
            }

            notification1.getManager().notify(j,builder.build());

        }



    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            updateToken(s);
        }
    }

    private void updateToken(String tokenRefresh) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(tokenRefresh);
        reference.child(user.getUid()).setValue(token);


    }
}
