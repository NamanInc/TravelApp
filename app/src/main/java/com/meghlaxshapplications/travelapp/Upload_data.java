package com.meghlaxshapplications.travelapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class Upload_data extends AppCompatActivity {
    private Spinner states, categorysp;
    String[] city, category_h;
    ImageView thumbnailimg, plusimage, reelIcon;
    TextView hidetxt, reelText, postbtn;
    LinearLayout linearLayout, lreel;
    public static final int THUMBCODE = 12;
    public static final int PICKMULTIPLE = 128;
    public static final int IMG1 = 1;
    public static final int IMG2 = 3;
    public static final int IMG3 = 2;
    public static final int IMG4 = 5;
    public static final int VIDEO = 6;
    private MaterialRatingBar ratebar;
    private Uri thumbUri, reelUri;
    private String username, profileUrl, tick , ratebar_st;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    private String thumbUrl, videoUrl;
    private String timestamp;
    private VideoView reel;
    private TextView nametxt, descriptiontxt, towntxt, pinodetxt;
    private TextView uploadtxt, uploadbtn;
    private ArrayList<Uri> imagelist = new ArrayList<Uri>();
    private Uri mutipleuri , uri1,uri2,uri3,uri4;
    private String naman;
    private HashMap<Object, String> hashMap;
    private String name, description, town, pincode;
    private ProgressDialog progressDialog;
    private int uploadcount = 0;
    private String multiUrl, imgurl1, imgurl2, imgurl3,imgurl4;
    private ImageView img1, img2, img3, img4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);
        getSupportActionBar().hide();

        states = findViewById(R.id.states);
        categorysp = findViewById(R.id.category_spinner);
        postbtn = findViewById(R.id.postbtn);

        city = getResources().getStringArray(R.array.india_states);
        category_h = getResources().getStringArray(R.array.category);
        hidetxt = findViewById(R.id.text_hide);
        plusimage = findViewById(R.id.plusimagethumb);
        linearLayout = findViewById(R.id.linearlay);
        nametxt = findViewById(R.id.name_place);
        descriptiontxt = findViewById(R.id.description);
        towntxt = findViewById(R.id.town_cityname);
        pinodetxt = findViewById(R.id.picode);
        reel = findViewById(R.id.video_reel);
        reelIcon = findViewById(R.id.reel_icon);
        reelText = findViewById(R.id.reel_text);
        lreel = findViewById(R.id.lreel);
        img1 = findViewById(R.id.image1);
        img2 = findViewById(R.id.image2);
        img3 = findViewById(R.id.image3);
        img4 = findViewById(R.id.image4);
        ratebar = findViewById(R.id.ratebar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        hashMap = new HashMap<>();


        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        reference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    profileUrl = snapshot.child("ProfileImage").getValue().toString();
                    username = snapshot.child("Name").getValue().toString();
                    tick = snapshot.child("Tick").getValue().toString();

                    Toast.makeText(Upload_data.this, username, Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickImage(IMG1);

            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickImage(IMG2);

            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickImage(IMG3);

            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickImage(IMG4);

            }
        });


//
//        uploadbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                progressDialog.show();
//                uploadmultiple();
//
//
//            }
//        });


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Upload_data.this, "Yes", Toast.LENGTH_SHORT).show();

                hidetxt.setVisibility(View.INVISIBLE);
                plusimage.setVisibility(View.INVISIBLE);


                pickthubimage();
            }
        });

        lreel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reelIcon.setVisibility(View.GONE);
                reelText.setVisibility(View.GONE);
                getvideofromgallery();
            }
        });
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nametxt.getText().toString().trim();
                description = descriptiontxt.getText().toString().trim();
                town = towntxt.getText().toString().trim();
                pincode = pinodetxt.getText().toString().trim();



                checkdetails();

            }
        });


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, city);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        states.setAdapter(arrayAdapter);

        ArrayAdapter catadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, category_h);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorysp.setAdapter(catadapter);
        thumbnailimg = findViewById(R.id.thumbnail_image);


        states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                String state = city[position];

                hashMap.put("State", state);


                // Toast.makeText(Upload_data.this, state, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categorysp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));

                String categoryst = category_h[position];
                hashMap.put("Category", categoryst);

                // Toast.makeText(Upload_data.this, categoryst, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void pickImage(int CODE) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CODE);


    }

    private void checkdetails() {

        if (TextUtils.isEmpty(name)) {

            Toast.makeText(this, "Please Enter Place Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {

            Toast.makeText(this, "Please Enter All details", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(town)) {

            Toast.makeText(this, "Please Enter the name of Town or Nearby Town", Toast.LENGTH_SHORT).show();
        } else {


                uploaddata();




        }

    }

    private void uploaddata() {
        progressDialog.setMessage("Uploading Please Wait");
        progressDialog.show();


        timestamp = String.valueOf(System.currentTimeMillis());
        String filepathname = "Post/" + "post_" + timestamp;

        if (thumbUri != null) {

            uploadimg1();
            uploadimg2();
            uploadimg3();
            uploadimg4();

            Toast.makeText(this, "Posting", Toast.LENGTH_SHORT).show();

            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepathname);
            ref.putFile(thumbUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            thumbUrl = String.valueOf(uri);
                            uploadvideo();


//                           String timestampforvide = String.valueOf(System.currentTimeMillis());
//                           String filepathnameforvideo = "videos/" +"post_" + timestampforvide;
//
//
//                           StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepathnameforvideo);
//                           ref.putFile(reelUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                               @Override
//                               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                                   taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                       @Override
//                                       public void onSuccess(Uri uri) {
//                                           naman = String.valueOf(uri);
//                                           Log.d("Naman",naman);
//
//
//
////
//                                       }
//                                   }).addOnFailureListener(new OnFailureListener() {
//                                       @Override
//                                       public void onFailure(@NonNull Exception e) {
//
//                                       }
//                                   });
//
//                               }
//                           });


                            Log.d("NAman", profileUrl);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            });


        }


    }

    private void uploadvideo() {

        ratebar_st = String.valueOf(ratebar.getRating());





        hashMap.put("PlaceName", name);
        hashMap.put("description", description);
        hashMap.put("town", town);
        hashMap.put("Username", username);
        hashMap.put("profileImage", profileUrl);
        hashMap.put("Tick", tick);
        hashMap.put("Pincode", pincode);
        hashMap.put("ThumbnailUrl", thumbUrl);
        hashMap.put("VideoUrl", "12");
        hashMap.put("Approved", "1");
        hashMap.put("pId", timestamp);
        hashMap.put("plikes", "0");
        hashMap.put("Image1", imgurl1);
        hashMap.put("Image2", imgurl2);
        hashMap.put("image3", imgurl3);
        hashMap.put("image4", imgurl4);
        hashMap.put("uid", firebaseAuth.getCurrentUser().getUid());
        hashMap.put("Rating",ratebar_st);
        hashMap.put("videobyus","1");
        hashMap.put("Lat","0");
        hashMap.put("Long","0");
        //  hashMap.put("MultiImages",multiUrl);


        DatabaseReference refdata = FirebaseDatabase.getInstance().getReference("Posts");
        refdata.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(Upload_data.this, "Success", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Upload_data.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });




        String timestampforvide = String.valueOf(System.currentTimeMillis());
        String filepathnameforvideo = "videos/" + "post_" + timestampforvide;




//


    }

    private void uploadimg4() {

        timestamp = String.valueOf(System.currentTimeMillis());
        String filepathname = "Post/" + "post_" + timestamp+"4";

        if (uri4 != null) {


            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepathname);
            ref.putFile(uri4).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgurl4 = String.valueOf(uri);



//                           String timestampforvide = String.valueOf(System.currentTimeMillis());
//                           String filepathnameforvideo = "videos/" +"post_" + timestampforvide;
//
//
//                           StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepathnameforvideo);
//                           ref.putFile(reelUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                               @Override
//                               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                                   taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                       @Override
//                                       public void onSuccess(Uri uri) {
//                                           naman = String.valueOf(uri);
//                                           Log.d("Naman",naman);
//
//
//
////
//                                       }
//                                   }).addOnFailureListener(new OnFailureListener() {
//                                       @Override
//                                       public void onFailure(@NonNull Exception e) {
//
//                                       }
//                                   });
//
//                               }
//                           });


                            Log.d("NAman", profileUrl);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            });


        }
    }

    private void uploadimg3() {
        timestamp = String.valueOf(System.currentTimeMillis());
        String filepathname = "Post/" + "post_" + timestamp+"2";

        if (uri3 != null) {


            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepathname);
            ref.putFile(uri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                             imgurl3= String.valueOf(uri);



//                           String timestampforvide = String.valueOf(System.currentTimeMillis());
//                           String filepathnameforvideo = "videos/" +"post_" + timestampforvide;
//
//
//                           StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepathnameforvideo);
//                           ref.putFile(reelUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                               @Override
//                               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                                   taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                       @Override
//                                       public void onSuccess(Uri uri) {
//                                           naman = String.valueOf(uri);
//                                           Log.d("Naman",naman);
//
//
//
////
//                                       }
//                                   }).addOnFailureListener(new OnFailureListener() {
//                                       @Override
//                                       public void onFailure(@NonNull Exception e) {
//
//                                       }
//                                   });
//
//                               }
//                           });


                            Log.d("NAman", profileUrl);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            });


        }
    }

    private void uploadimg2() {

        timestamp = String.valueOf(System.currentTimeMillis());
        String filepathname = "Post/" + "post_" + timestamp+"2";

        if (uri2 != null) {


            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepathname);
            ref.putFile(uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgurl2 = String.valueOf(uri);



//                           String timestampforvide = String.valueOf(System.currentTimeMillis());
//                           String filepathnameforvideo = "videos/" +"post_" + timestampforvide;
//
//
//                           StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepathnameforvideo);
//                           ref.putFile(reelUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                               @Override
//                               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                                   taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                       @Override
//                                       public void onSuccess(Uri uri) {
//                                           naman = String.valueOf(uri);
//                                           Log.d("Naman",naman);
//
//
//
////
//                                       }
//                                   }).addOnFailureListener(new OnFailureListener() {
//                                       @Override
//                                       public void onFailure(@NonNull Exception e) {
//
//                                       }
//                                   });
//
//                               }
//                           });


                            Log.d("NAman", profileUrl);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            });


        }
    }

    private void uploadimg1() {

        timestamp = String.valueOf(System.currentTimeMillis());
        String filepathname = "Post/" + "post_" + timestamp+"1";

        if (uri1 != null) {


            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepathname);
            ref.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imgurl1 = String.valueOf(uri);



//                           String timestampforvide = String.valueOf(System.currentTimeMillis());
//                           String filepathnameforvideo = "videos/" +"post_" + timestampforvide;
//
//
//                           StorageReference ref = FirebaseStorage.getInstance().getReference().child(filepathnameforvideo);
//                           ref.putFile(reelUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                               @Override
//                               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                                   taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                       @Override
//                                       public void onSuccess(Uri uri) {
//                                           naman = String.valueOf(uri);
//                                           Log.d("Naman",naman);
//
//
//
////
//                                       }
//                                   }).addOnFailureListener(new OnFailureListener() {
//                                       @Override
//                                       public void onFailure(@NonNull Exception e) {
//
//                                       }
//                                   });
//
//                               }
//                           });


                            Log.d("NAman", profileUrl);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            });


        }




    }

    private void uploadimages() {

        StorageReference imagefolder = FirebaseStorage.getInstance().getReference().child("Image");
        for (uploadcount = 0; uploadcount < imagelist.size(); uploadcount++) {
            Uri individualimage = imagelist.get(uploadcount);

            StorageReference imageName = imagefolder.child("Image" + individualimage.getPathSegments());

            imageName.putFile(individualimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            multiUrl = String.valueOf(uri);

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts").child(timestamp).child("Images").child("Image");

                            databaseReference.setValue(multiUrl);


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

    private void uploadmultiple() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICKMULTIPLE);
    }

    private void getvideofromgallery() {

        reel.setVisibility(View.VISIBLE);

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, VIDEO);

    }

    private void pickthubimage() {
        thumbnailimg.setVisibility(View.VISIBLE);

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, THUMBCODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == THUMBCODE) {


            if (requestCode == THUMBCODE && resultCode == RESULT_OK && data != null) {
                thumbUri = data.getData();

                thumbnailimg.setImageURI(thumbUri);


            }

        } else if (requestCode == VIDEO) {

            if (requestCode == VIDEO && resultCode == RESULT_OK && data != null) ;

            reelUri = data.getData();

            reel.setVideoURI(reelUri);
            reel.start();

        } else if (requestCode == PICKMULTIPLE) {

            if (resultCode == RESULT_OK && data.getClipData() != null) {

                int countClipdata = data.getClipData().getItemCount();

                int currentImageSelected = 0;

                while (currentImageSelected < countClipdata) {

                    mutipleuri = data.getClipData().getItemAt(currentImageSelected).getUri();
                    imagelist.add(mutipleuri);
                    currentImageSelected = currentImageSelected + 1;


                }


            }
        } else if (requestCode == IMG1) {


                if (requestCode == IMG1 && resultCode == RESULT_OK && data != null) {
                    uri1 = data.getData();

                    img1.setImageURI(uri1);


                }


            } else if (requestCode == IMG2) {


            if (requestCode == IMG2 && resultCode == RESULT_OK && data != null) {
                uri2 = data.getData();

                img2.setImageURI(uri2);

            }


            }
            else if (requestCode == IMG3) {


                if (requestCode == IMG3 && resultCode == RESULT_OK && data != null) {
                    uri3 = data.getData();

                    img3.setImageURI(uri3);

                }


                }else if (requestCode == IMG4) {


                    if (requestCode == IMG4 && resultCode == RESULT_OK && data != null) {
                        uri4 = data.getData();

                        img4.setImageURI(uri4);

                    }


                    }else {

                Toast.makeText(this, "Select Some", Toast.LENGTH_SHORT).show();
            }


        }


}