package com.meghlaxshapplications.travelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.Person;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.Adapters.AdapterUsers;
import com.Adapters.AdapterUsers_verticle;
import com.Adapters.ArrayAdapterUsers;
import com.github.tcking.giraffecompressor.GiraffeCompressor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hendraanggrian.socialview.commons.Mention;
import com.hendraanggrian.socialview.commons.MentionAdapter;
import com.hendraanggrian.widget.SocialAutoCompleteTextView;
import com.hendraanggrian.widget.SocialEditText;
import com.iceteck.silicompressorr.SiliCompressor;
import com.models.ModelPost;
import com.models.ModelUsers;
import com.util.RealPathUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static com.github.tcking.giraffecompressor.GiraffeCompressor.TYPE_FFMPEG;
import static com.github.tcking.giraffecompressor.GiraffeCompressor.getContext;

public class Upload_reels extends AppCompatActivity {
    private ImageView plus;
    private VideoView reelvideo;
    private SocialAutoCompleteTextView captiontxt;
    private Button uploadbtn;
    private RelativeLayout picktbtn;
    public static final int CODE = 12;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private Uri videoUri;
    String profileUrl,tick,username, captionst,timestamp;
    private ProgressDialog progressDialog;
    String videoUrl , compressedUri;
    private AdapterUsers_verticle adapterUsers;
    private List<ModelUsers> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_reels);

        getSupportActionBar().hide();

        plus = findViewById(R.id.plusicon);
        reelvideo  = findViewById(R.id.reelPlay);
        captiontxt = findViewById(R.id.caption);
        uploadbtn = findViewById(R.id.upload_reels_btn);
        picktbtn = findViewById(R.id.pickvideobtn);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading.....");
        GiraffeCompressor.init(this);

        loadmention();



        reference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    profileUrl = snapshot.child("ProfileImage").getValue().toString();
                    username = snapshot.child("Name").getValue().toString();
                    tick = snapshot.child("Tick").getValue().toString();

                    Toast.makeText(Upload_reels.this, username, Toast.LENGTH_SHORT).show();
                    


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        picktbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pichvideo();
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captionst = captiontxt.getText().toString().trim();
                checkutills();
            }
        });






    }

    private void loadmention() {
        usersList = new ArrayList<>();

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){

                    ModelUsers modelUsers = ds.getValue(ModelUsers.class);

                    if (!modelUsers.getUID().equals(fUser.getUid())){
                        usersList.add(modelUsers);

                    }

//                    ArrayAdapter<ModelUsers> adapter = new ArrayAdapter<ModelUsers>(getApplicationContext(),R.layout.verticle_users);
//
//                    adapterUsers = new AdapterUsers_verticle(getApplicationContext(),usersList);usersList

                    ArrayAdapter<ModelUsers> arrayAdapter  = new ArrayAdapter<ModelUsers>(getApplicationContext(), R.layout.verticle_users);

                    arrayAdapter.add(new ModelUsers(usersList.get(0).getName(),usersList.get(0).getProfileImage(),"","","",""));


                    captiontxt.setMentionAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkutills() {

        if (TextUtils.isEmpty(captionst)){
            Toast.makeText(this, "Please Enter a best Caption for your video", Toast.LENGTH_SHORT).show();
        }

        else {

            uploadvideotostorage();
        }


    }

    private void uploadvideotostorage() {
        progressDialog.show();

        if (videoUri!=null){

            timestamp = String.valueOf(System.currentTimeMillis());
            String filepathname = "Post/" + "post_" + timestamp;

            firebaseStorage = FirebaseStorage.getInstance();
            storageReference = firebaseStorage.getReference("Reels").child(filepathname);
            storageReference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            videoUrl = String.valueOf(uri);

                            addtodatabse();

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });





        }
    }

    private void addtodatabse() {

        HashMap<String  , String> hashMap = new HashMap<>();
        hashMap.put("videoUrl",videoUrl);
        hashMap.put("tick",tick);
        hashMap.put("Caption",captionst);
        hashMap.put("Username",username);
        hashMap.put("profileurl",profileUrl);
        hashMap.put("pId",timestamp);
        hashMap.put("UID",firebaseAuth.getCurrentUser().getUid());


        DatabaseReference refdata = FirebaseDatabase.getInstance().getReference("TravelReels");
        refdata.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(Upload_reels.this, "Reels Uploaded Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Upload_reels.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });




    }

    private void pichvideo() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent,CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE && resultCode == RESULT_OK && data!=null){

            reelvideo.setVisibility(View.VISIBLE);
            plus.setVisibility(View.GONE);

            videoUri = data.getData();

            reelvideo.setVideoURI(videoUri);



            reelvideo.start();


            String path = RealPathUtil.getRealPath(Upload_reels.this,videoUri);

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

            new CompressVideo().execute("false",videoUri.toString(),file.getPath());




            Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            Log.d("hagu",path);

         //   compressvideo(path);


        }
    }

    private void startsillicompress() {











    }

    private void compressvideo(String path) {
        //String  enviro = getFilesDir().getAbsolutePath();
        String currenttime = String.valueOf(System.currentTimeMillis());
        String output = "/storage/emulated/0/Download/" + currenttime + ".mp4";



        progressDialog.show();

        GiraffeCompressor.init(Upload_reels.this);


        GiraffeCompressor.create(TYPE_FFMPEG) //two implementations: mediacodec and ffmpeg,default is mediacodec
                .input(path) //set video to be compressed
                .output(output) //set compressed video output
                .bitRate(2073600).ready().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GiraffeCompressor.Result>() {
            @Override
            public void onCompleted() {

                Toast.makeText(Upload_reels.this, "Complete", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(Upload_reels.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }

            @Override
            public void onNext(GiraffeCompressor.Result result) {
                String msg = String.format("compress completed \ntake time:%s \nout put file:%s", result.getCostTime(), result.getOutput());
                msg = msg + "\ninput file size:"+ Formatter.formatFileSize(getApplication(),path.length());
                msg = msg + "\nout file size:"+ Formatter.formatFileSize(getApplication(),new File(result.getOutput()).length());
                Log.d("hhhhhhh",msg);

            }
        });


    }


    private class CompressVideo extends AsyncTask<String , String , String > {

        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(Upload_reels.this,"","Compressing.....");

        }

        @Override
        protected String doInBackground(String... strings) {
            String videoPath = null;

            try {
                Uri uri = Uri.parse(strings[1]);
                videoPath = SiliCompressor.with(Upload_reels.this).compressVideo(uri,strings[2]);
            } catch (URISyntaxException e) {


                e.printStackTrace();
            }
            return videoPath;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();


            File file = new File(s);

            Uri compressedUri = Uri.fromFile(file);
            reelvideo.setVideoURI(compressedUri);
            reelvideo.start();


        }
    }
}