package com.meghlaxshapplications;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.meghlaxshapplications.travelapp.R;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ProfileSetup extends AppCompatActivity {
    ImageView profileimg;
    public static final int PICK_CODE= 123;
    ImageView plus;
    private Uri mImageuri;
    FirebaseAuth mAuth;
    private EditText txtemail,txtphone,txtname,txtcity;
    private Button nextbtn;
    private String email,name,phone,city , url;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;


    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        getSupportActionBar().hide();
        plus = findViewById(R.id.plus);

        profileimg = findViewById(R.id.profile_image);
        txtname = findViewById(R.id.edit_name);
        txtemail = findViewById(R.id.edit_email);
        txtphone = findViewById(R.id.edit_phone);
        txtcity = findViewById(R.id.edit_city);
        nextbtn = findViewById(R.id.nextbtn);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference  = firebaseStorage.getReference("profileImages");
        progressDialog = new ProgressDialog(this);



        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = txtname.getText().toString().trim();
                email = txtemail.getText().toString().trim();
                phone = txtphone.getText().toString().trim();
                city = txtcity.getText().toString().trim();

                checkfields();






            }
        });


        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickimage();
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    private void checkfields() {
        if (TextUtils.isEmpty(name)){

            Toast.makeText(this, "Please Enter your Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email)){

            Toast.makeText(this, "Please Enter your Email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Enter your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(city)){
            Toast.makeText(this, "Please Enter your City/Town/Village Name ", Toast.LENGTH_SHORT).show();
        }
        else if (phone.length() < 10){

            Toast.makeText(this, "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
        }
        else {

            imageuploading();
        }
    }

    private void imageuploading() {

        progressDialog.setMessage("Loading...");
        progressDialog.show();


       // Toast.makeText(this, "No Problem", Toast.LENGTH_SHORT).show();

        // Start Uploading Image Process......
        String currenttime = String.valueOf(System.currentTimeMillis()+mAuth.getCurrentUser().getUid());

        StorageReference refs = storageReference.child(currenttime);

        refs.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                if (taskSnapshot !=null){

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                             url = uri.toString();

                            savedatabase();

                           // Toast.makeText(ProfileSetup.this, imageurl, Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileSetup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileSetup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void savedatabase() {

        HashMap <String , String > hashMap = new HashMap<>();

        hashMap.put("ProfileImage",url);
        hashMap.put("Name", name);
        hashMap.put("email",email);
        hashMap.put("PhoneNo",phone);
        hashMap.put("City",city);
        hashMap.put("Tick","1");
        hashMap.put("onlineStatus","online");
        hashMap.put("typingTo","noOne");
        hashMap.put("UID",mAuth.getCurrentUser().getUid());


        databaseReference.child(mAuth.getCurrentUser().getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProfileSetup.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileSetup.this,HomeActivity.class);
                startActivity(intent);
                finish();
                progressDialog.dismiss();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileSetup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

//        databaseReference.child(mAuth.getCurrentUser().getUid()).child("ProfileImage").setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(ProfileSetup.this, "Done !", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    private void pickimage() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_CODE);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CODE && resultCode == RESULT_OK && data!=null){
            mImageuri = data.getData();
            profileimg.setImageURI(mImageuri);
            plus.setVisibility(View.INVISIBLE);


        }
    }
}