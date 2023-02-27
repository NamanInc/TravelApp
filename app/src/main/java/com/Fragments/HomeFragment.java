package com.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapters.AdapterPost;
import com.Adapters.AdapterProducts;
import com.Adapters.AdapterUsers;
import com.Adapters.AdapterUsers_verticle;
import com.Adapters.HomeProAdapter;
import com.Weather.WeatherData;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.meghlaxshapplications.CategoryActivity;
import com.meghlaxshapplications.HomeActivity;
import com.meghlaxshapplications.travelapp.MainActivity;
import com.meghlaxshapplications.travelapp.R;
import com.meghlaxshapplications.travelapp.Upload_data;
import com.meghlaxshapplications.travelapp.choosetype;
import com.models.ModelPost;
import com.models.ModelProducts;
import com.models.ModelUsers;
import com.notification.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {
    private ImageView profileimage;
    private TextView gavname;
    private TextView nametxt;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference , ref ,hikingref;
    RecyclerView postRecyclerView , hiking , temples;
    private FirebaseDatabase firebaseDatabase;
    private CardView add;
    private List<ModelPost> postList , hickinglist , templeList;
    private CardView hiking_cat , rafting_cat, camping_cat;
    AdapterPost adapterPost , hickingadapter , templeAdapter;
    private RelativeLayout layout;
    private LottieAnimationView loadhome;
    private RelativeLayout mainrel;
    private RecyclerView homeUsers;
    private ImageView homeImage2,homeImage1;
    private AdapterUsers_verticle adapterUsers;
    private TextView govname;
    private List<ModelUsers> modelUsersList;
    private RecyclerView recyclerviewproducts;
    private List<ModelProducts> productsList;
    private ImageView homeImage4,homeImage3;
    private ImageView darkmodebtn ;
    private TextView wheathertype  , temperaturetxt;
    String url = "https://api.openweathermap.org/data/2.5/weather?lat=12.4637&lon=130.8444&appid=fcae4299299769448808cf69d166d76f";
    private String channel;
    private LottieAnimationView lottieAnimationView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        profileimage = view.findViewById(R.id.profileimage_home);
        nametxt = view.findViewById(R.id.name_namaste);
        mAuth = FirebaseAuth.getInstance();
        add = view.findViewById(R.id.plus_card);
        postRecyclerView = view.findViewById(R.id.postrecyclerview);
        hiking = view.findViewById(R.id.hicking_best);
        hiking_cat = view.findViewById(R.id.hicking_cat);
        rafting_cat = view.findViewById(R.id.rafting_cat);
        layout = view.findViewById(R.id.mainlayuhome);
        homeImage1 = view.findViewById(R.id.homeImage1);
        homeImage2 = view.findViewById(R.id.homeImage2);
        loadhome = view.findViewById(R.id.loadhome);
        temples = view.findViewById(R.id.temple_best);
        homeImage3 = view.findViewById(R.id.homeImage3);
        homeImage4 = view.findViewById(R.id.homeImage4);
        gavname = view.findViewById(R.id.gavname);
        recyclerviewproducts = view.findViewById(R.id.recyclerviewproductshome);
//        mainrel = view.findViewById(R.id.mainrel);
        templeList = new ArrayList<>();
        productsList = new ArrayList<>();
        modelUsersList = new ArrayList<>();
        homeUsers = view.findViewById(R.id.recyclerviewHome_users);
        darkmodebtn = view.findViewById(R.id.darkmodebtn);
        lottieAnimationView = view.findViewById(R.id.type_weather_anim);
        wheathertype = view.findViewById(R.id.weathertype);
        temperaturetxt = view.findViewById(R.id.temperaturetxt);
        govname = view.findViewById(R.id.gavname);

        getWeatherdata();




        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        homeUsers.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerviewproducts.setLayoutManager(linearLayoutManager3);

        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.pagecolor));




        showProducts();





        showUsers();
        loadhomeimage();










        camping_cat = view.findViewById(R.id.camping_cat);
         ref = FirebaseDatabase.getInstance().getReference("Posts");
        hikingref = FirebaseDatabase.getInstance().getReference("Posts");
         hickinglist = new ArrayList<>();


        SharedPreferences shared = getActivity().getSharedPreferences("MODE", MODE_PRIVATE);
         channel = (shared.getString("MODE", ""));

        if (channel.equals("Dark")){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            darkmodebtn.setImageResource(R.drawable.bright);


        }
        else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            darkmodebtn.setImageResource(R.drawable.modedark);
        }



        SharedPreferences sp = this.getActivity().getSharedPreferences("SP_USER",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UID" , FirebaseAuth.getInstance().getCurrentUser().getUid());
        //editor.putString("Mode",)
        editor.apply();

        darkmodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (channel.equals("Dark")){

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                    SharedPreferences sp = getActivity().getSharedPreferences("MODE",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("MODE" , "Light");
                    //editor.putString("Mode",)
                    editor.apply();


                }else {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    SharedPreferences sp = getActivity().getSharedPreferences("MODE",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("MODE" , "Dark");
                    //editor.putString("Mode",)
                    editor.apply();


                }







//                startActivity(new Intent(getContext(),HomeActivity.class));

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());


         camping_cat.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent intent = new Intent(getActivity(), CategoryActivity.class);
                 intent.putExtra("Camping","Camping");
                 startActivity(intent);

             }
         });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Upload_data.class);
                startActivity(intent);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true);
        layoutManager.setStackFromEnd(true);

        LinearLayoutManager hinkinglayoutmanager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true);
        LinearLayoutManager templelayout = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true);
        hinkinglayoutmanager.setStackFromEnd(true);
        temples.setLayoutManager(templelayout);
        templelayout.setStackFromEnd(true);
       // layoutManager.setReverseLayout(true);
        postList = new ArrayList<>();
        postRecyclerView.setLayoutManager(layoutManager);
        hiking.setLayoutManager(hinkinglayoutmanager);

        loadpost();
        loadhiking();
        loadtemples();




        retricvedata();
        // Inflate the layout for this fragment






        return view;


    }

    private void getWeatherdata() {

        String Lat = "0";
        String lon = "0";

        String apiKeys = "fcae4299299769448808cf69d166d76f";


        RequestParams params = new RequestParams();
        params.put("lat",Lat);
        params.put("lon",lon);
        params.put("appid",apiKeys);

        letSdoSomeNetworking(params);



    }

    private void letSdoSomeNetworking(RequestParams params) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url,params,new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                Toast.makeText(getContext(), "Data Fetched", Toast.LENGTH_SHORT).show();

                WeatherData weatherData = WeatherData.fromJson(response);
                updateUri(weatherData);

                //super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });



    }

    private void updateUri(WeatherData weatherData) {
        govname.setText(weatherData.getMcity());
        wheathertype.setText(weatherData.getmWeatherTye());
        temperaturetxt.setText(weatherData.getmTemperature());
        int condition = weatherData.getmCondition();
        temperaturetxt.setText(weatherData.getmTemperature());
        gavname.setText(weatherData.getMcity());


        Toast.makeText(getContext(), weatherData.getmTemperature(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), weatherData.getMcity(), Toast.LENGTH_SHORT).show();



        if (weatherData.getmCondition() == 800){

            lottieAnimationView.setAnimation("sunney.json");



        }
        else if (weatherData.getmCondition()>=300 && condition<=500){
            lottieAnimationView.setAnimation("lightrain.json");




        }
        else if (condition>=500 && condition<=600){
            lottieAnimationView.setAnimation("lightrain.json");




        }
        else  if (condition>=600 && condition<=700){
            lottieAnimationView.setAnimation("snowfall.json");




        }
        else if (condition>=701 && condition<=771){
            lottieAnimationView.setAnimation("fog.json");




        }
       else if (condition>=772 && condition<=800){
            lottieAnimationView.setAnimation("overcast.json");




        }

        else if (condition == 800){
            lottieAnimationView.setAnimation("sunney.json");




        }


        else if (condition>=0 && condition<=300){

            lottieAnimationView.setAnimation("cloudy.json");



        }


//
//        }
       // int resourceID = getResources().getIdentifier(weatherData.getMicon(),"assets",getContext().getPackageName());


    }

    private void showProducts() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UploadProducts");

//        Query query = reference.orderByChild("Category").equalTo(categry);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                productsList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelProducts modelPost1 = ds.getValue(ModelProducts.class);

                    productsList.add(modelPost1);

                    Collections.shuffle(productsList);

                    HomeProAdapter adapterPost = new HomeProAdapter(getContext(),productsList);

                    recyclerviewproducts.setAdapter(adapterPost);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }

    private void loadhomeimage() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("HomeImages");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String url1 = snapshot.child("image1").getValue().toString();
                    String url2 = snapshot.child("image2").getValue().toString();
                    String url3 = snapshot.child("image3").getValue().toString();
                    String url4 = snapshot.child("image4").getValue().toString();




                    try {

                        Glide.with(getContext()).load(url1).into(homeImage1);
                        Glide.with(getContext()).load(url2).into(homeImage2);
                        Glide.with(getContext()).load(url3).into(homeImage3);
                        Glide.with(getContext()).load(url4).into(homeImage4);

                    } catch (Exception e) {


                        e.printStackTrace();
                    }


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showUsers() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelUsersList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){

                    ModelUsers modelUsers = ds.getValue(ModelUsers.class);

                    if (!modelUsers.getUID().equals(fUser.getUid())){
                        modelUsersList.add(modelUsers);

                    }

                    adapterUsers = new AdapterUsers_verticle(getActivity(),modelUsersList);

                    Collections.shuffle(modelUsersList);

                    homeUsers.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void loadtemples() {

        Query query = ref.orderByChild("Category").equalTo("Temples");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                templeList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelPost modelPost1 = ds.getValue(ModelPost.class);

                    Collections.shuffle(templeList);

                    templeList.add(modelPost1);

                    templeAdapter = new AdapterPost(getActivity(),templeList);
                    temples.setAdapter(templeAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token(token);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mToken);
    }


    private void loadhiking() {

        Query query = ref.orderByChild("Category").equalTo("Hinking");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                hickinglist.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelPost modelPost1 = ds.getValue(ModelPost.class);

                    Collections.shuffle(hickinglist);

                    hickinglist.add(modelPost1);

                    hickingadapter = new AdapterPost(getActivity(),hickinglist);
                    hiking.setAdapter(hickingadapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void loadpost() {


        Query query = ref.orderByChild("Approved").equalTo("2");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    postList.add(modelPost);

                    Collections.shuffle(postList);

                    adapterPost = new AdapterPost(getActivity(),postList);
                    postRecyclerView.setAdapter(adapterPost);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void retricvedata() {

        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("Name").getValue().toString();
                    String profileurl = snapshot.child("ProfileImage").getValue().toString();

                    String withnamsate = "Namaste ! "+name;

                    nametxt.setText(withnamsate);
                    try {

                        Glide.with(HomeFragment.this).load(profileurl).placeholder(R.drawable.profilethub).into(profileimage);


                    }catch (Exception e){


                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onStart() {

        getWeatherdata();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        getWeatherdata();
    }
}