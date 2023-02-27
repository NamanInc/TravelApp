package com.meghlaxshapplications.travelapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubFilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

public class Upload_Post extends AppCompatActivity implements View.OnClickListener {

    ImageView post_Image;
    RelativeLayout hide;
    EditText captiontxt;
    Button uploadbtn;
    public static final int CODECODE = 111;
    private Uri imageUri , finalUri;
    String imgUrl,captionst;
    FirebaseAuth firebaseAuth;
    StorageReference reference;
    FirebaseStorage firebaseStorage;
    String timestamp;
    FirebaseDatabase database;
    byte[] data1s;
    private DatabaseReference userref;
    private String profileUrl,username,tick;
    private ProgressDialog progressDialog;
    private ImageView filter1 ,filter2 , filter3,filter4,filter5;
    private Bitmap bitmap;
    private ImageView fiter6;

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__post);






        post_Image = findViewById(R.id.postImage);
        hide = findViewById(R.id.hide);
        captiontxt = findViewById(R.id.caption_post);
        uploadbtn = findViewById(R.id.upload_post_btn);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance();
        userref = database.getReference("Users");
        progressDialog = new ProgressDialog(this);
        filter1 = findViewById(R.id.filter1);
        filter2 = findViewById(R.id.filter2);
        filter3 = findViewById(R.id.filter3);
        filter4 = findViewById(R.id.filter4);
        filter5 = findViewById(R.id.filter5);
        fiter6 = findViewById(R.id.filter6);
        progressDialog.setMessage("Posting");

        pickImage();

        filter1.setOnClickListener(this);
        filter2.setOnClickListener(this);
        filter3.setOnClickListener(this);
        filter4.setOnClickListener(this);
        filter5.setOnClickListener(this);
        fiter6.setOnClickListener(this);










        userref.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    profileUrl = snapshot.child("ProfileImage").getValue().toString();
                    username = snapshot.child("Name").getValue().toString();
                    tick = snapshot.child("Tick").getValue().toString();

                    Toast.makeText(Upload_Post.this, username, Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        timestamp = String.valueOf(System.currentTimeMillis());
        String filepathname = "Post/" + "post_" + timestamp;
        reference = firebaseStorage.getReference("Upload_Post_travel").child(filepathname);



        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }

    private void pickImage() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,CODECODE);


        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captionst = captiontxt.getText().toString();


                 if (imageUri!=null){






                    uploadimage(data1s);
                }

            }
        });




    }

    private void compressImage() {

        if (imageUri !=null){
          //  File file = new File(SiliCompressor.with(Upload_Post.this).compress(FileUtils.getPath(Upload_Post.this,imageUri),new File(Upload_Post.this.getCacheDir() , "temp")));
            String filePath = SiliCompressor.with(Upload_Post.this).compress(FileUtils.getPath(Upload_Post.this,imageUri), new File(Upload_Post.this.getCacheDir(),"temp"), false);



            File file = new File(filePath);
            finalUri = Uri.fromFile(file);

        }
    }

    private void uploadimage( byte[] data) {


        post_Image.setDrawingCacheEnabled(true);
        post_Image.buildDrawingCache();
        bitmap = ((BitmapDrawable) post_Image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        data1s = baos.toByteArray();
        progressDialog.show();

        reference.putBytes(data1s).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imgUrl = String.valueOf(uri);


                        addtodatabase(imgUrl);
                    }
                });
            }
        });



    }

    private void addtodatabase(String imgUrl) {

        HashMap<String,String> hashMap = new HashMap<>();

        hashMap.put("profileImage",profileUrl);
        hashMap.put("Tick",tick);
        hashMap.put("Username",username);
        hashMap.put("PostUrl",imgUrl);
        hashMap.put("Caption",captionst);
        hashMap.put("pID",timestamp);
        hashMap.put("time",timestamp);
        hashMap.put("Uid",firebaseAuth.getCurrentUser().getUid());

        DatabaseReference refdata = FirebaseDatabase.getInstance().getReference("TravelPost");

        refdata.child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODECODE && resultCode == RESULT_OK && data!=null){

            imageUri = data.getData();

            post_Image.setImageURI(imageUri);

            filter1.setImageURI(imageUri);
            filter2.setImageURI(imageUri);
            filter3.setImageURI(imageUri);
            filter4.setImageURI(imageUri);
            filter5.setImageURI(imageUri);
            fiter6.setImageURI(imageUri);



            hide.setVisibility(View.GONE);

            post_Image.setDrawingCacheEnabled(true);
            post_Image.buildDrawingCache();
             bitmap = ((BitmapDrawable) post_Image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
             data1s = baos.toByteArray();

            Bitmap bitmapfilter1 , bitmapfilter2 , bitmapfilter3 , bitmapfilter4 , bitmapfilter5 , bitmapfilter6;

            bitmapfilter1 = ((BitmapDrawable) filter1.getDrawable()).getBitmap();
            Filter myFilter = new Filter();
            myFilter.addSubFilter(new ColorOverlaySubFilter(150, .5f, .2f, .0f));
            Bitmap image = bitmapfilter1.copy(Bitmap.Config.ARGB_8888,true);
            Bitmap outputImage = myFilter.processFilter(image);
            filter1.setImageBitmap(outputImage);

            bitmapfilter2 = ((BitmapDrawable) filter2.getDrawable()).getBitmap();
            Filter myFilter2 = new Filter();
            myFilter2.addSubFilter(new BrightnessSubFilter(40));
            myFilter2.addSubFilter(new ContrastSubFilter(1.8f));
            Bitmap image2 = bitmapfilter2.copy(Bitmap.Config.ARGB_8888,true);
            Bitmap outputImage2 = myFilter2.processFilter(image2);
            filter2.setImageBitmap(outputImage2);


            bitmapfilter3 = ((BitmapDrawable) filter3.getDrawable()).getBitmap();
            Filter myFilter3 = new Filter();
            myFilter3.addSubFilter(new SaturationSubFilter(2f));
            Bitmap image3 = bitmapfilter3.copy(Bitmap.Config.ARGB_8888,true);
            Bitmap outputImage3 = myFilter3.processFilter(image3);
            filter3.setImageBitmap(outputImage3);

            bitmapfilter4 = ((BitmapDrawable) filter4.getDrawable()).getBitmap();
            Filter myFilter4 = new Filter();
            myFilter4.addSubFilter(new ContrastSubFilter(1.8f));
            Bitmap image4 = bitmapfilter4.copy(Bitmap.Config.ARGB_8888,true);
            Bitmap outputImage4 = myFilter4.processFilter(image4);

            filter4.setImageBitmap(outputImage4);

            bitmapfilter5 = ((BitmapDrawable) filter5.getDrawable()).getBitmap();
            Filter myFilter5 = new Filter();
            myFilter5.addSubFilter(new BrightnessSubFilter(39));
            Bitmap image5 = bitmapfilter5.copy(Bitmap.Config.ARGB_8888,true);
            Bitmap ouputImage5 = myFilter5.processFilter(image5);
            filter5.setImageBitmap(ouputImage5);


            bitmapfilter6 = ((BitmapDrawable) fiter6.getDrawable()).getBitmap();
            Filter myFilter6 = new Filter();
            myFilter6.addSubFilter(new VignetteSubFilter(this, 130));
            Bitmap image6 = bitmapfilter6.copy(Bitmap.Config.ARGB_8888,true);
            Bitmap outputImage6 = myFilter6.processFilter(image6);
            fiter6.setImageBitmap(outputImage6);










        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.filter1 :

                Filter myFilter = new Filter();
                myFilter.addSubFilter(new ColorOverlaySubFilter(100, .2f, .2f, .0f));
                Bitmap image = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                Bitmap outputImage = myFilter.processFilter(image);

                post_Image.setImageBitmap(outputImage);
                break;

            case R.id.filter2 :

                Filter myFilter2 = new Filter();
                myFilter2.addSubFilter(new BrightnessSubFilter(35));
                myFilter2.addSubFilter(new ContrastSubFilter(1.5f));
                Bitmap image2 = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                Bitmap outputImage2 = myFilter2.processFilter(image2);
                post_Image.setImageBitmap(outputImage2);

                break;

            case R.id.filter3 :

                Filter myFilter3 = new Filter();
                myFilter3.addSubFilter(new SaturationSubFilter(1.5f));
                Bitmap image3 = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                Bitmap outputImage3 = myFilter3.processFilter(image3);
                post_Image.setImageBitmap(outputImage3);
                break;

            case R.id.filter4 :

                Filter myFilter4 = new Filter();
                myFilter4.addSubFilter(new ContrastSubFilter(1.4f));
                Bitmap image4 = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                Bitmap outputImage4 = myFilter4.processFilter(image4);

                post_Image.setImageBitmap(outputImage4);
                break;

            case R.id.filter5 :

                Filter myFilter5 = new Filter();
                myFilter5.addSubFilter(new BrightnessSubFilter(34));
                Bitmap image5 = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                Bitmap ouputImage5 = myFilter5.processFilter(image5);
                post_Image.setImageBitmap(ouputImage5);

                break;

            case R.id.filter6 :

                Filter myFilter6 = new Filter();
                myFilter6.addSubFilter(new VignetteSubFilter(this, 110));
                Bitmap image6 = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                Bitmap outputImage6 = myFilter6.processFilter(image6);
                post_Image.setImageBitmap(outputImage6);

                break;











        }
        }

    }
