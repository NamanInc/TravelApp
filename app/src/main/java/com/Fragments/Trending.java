package com.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Adapters.AdapterPost;
import com.Adapters.AdapterPostTrending;
import com.Adapters.Adapterrecomandtrending;
import com.Adapters.HomeProAdapter;
import com.Adapters.TravelAdapter;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.travelapp.R;
import com.models.ModelPost;
import com.models.ModelProducts;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Trending extends Fragment {
    private ViewPager2 viewPager2;
    List<ModelPost> postList;
    private RecyclerView trendingRecyclerview;
    LottieAnimationView animationViews;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_trending, container, false);

       viewPager2 = view.findViewById(R.id.viewpagertrending);
       animationViews = view.findViewById(R.id.animationViews);
       trendingRecyclerview = view.findViewById(R.id.trendingRecyclerview);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        trendingRecyclerview.setLayoutManager(linearLayoutManager);

        loadtrendingpost();

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {

               animationViews.setVisibility(View.GONE);

           }
       },3000);

       postList = new ArrayList<>();





       loadPosts();



//




        return view;
    }

    private void loadtrendingpost() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UploadProducts");

//        Query query = reference.orderByChild("Category").equalTo(categry);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelPost modelPost1 = ds.getValue(ModelPost.class);

                    postList.add(modelPost1);

                    Collections.shuffle(postList);

                    AdapterPostTrending adapterPost = new AdapterPostTrending(getContext(),postList);

                    trendingRecyclerview.setAdapter(adapterPost);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void loadPosts() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

//        Query query = reference.orderByChild("Category").equalTo(categry);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelPost modelPost1 = ds.getValue(ModelPost.class);

                    postList.add(modelPost1);



                    TravelAdapter adapterPost = new TravelAdapter(getContext(),postList);

                    Collections.shuffle(postList);

                    viewPager2.setClipToPadding(false);
                    // set padding manually, the more you set the padding the more you see of prev & next page
                    viewPager2.setPadding(40, 0, 40, 0);
                    // sets a margin b/w individual pages to ensure that there is a gap b/w them
                    viewPager2.setPageTransformer(new MarginPageTransformer(2));


                    viewPager2.setAdapter(adapterPost);







                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }

}