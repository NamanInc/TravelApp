package com.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Adapters.AdapterPost;
import com.Adapters.VideoAdapter;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.travelapp.R;
import com.meghlaxshapplications.travelapp.Upload_reels;
import com.meghlaxshapplications.travelapp.choosetype;
import com.models.ModelPost;
import com.models.ModelVideos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class NotificationFragemt extends Fragment implements Player.EventListener {
    private RecyclerView recyclerView;
    List<ModelVideos> videosList;
    int currentPage=-1;
    LinearLayoutManager layoutManager;
    boolean is_user_stop_video=false;
    VideoAdapter videoAdapter;
    private CardView plus_card;
    private ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification_fragemt, container, false);
//        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.black));

        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.videorecyclerview);
        videosList = new ArrayList<>();
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(false);
        progressBar = view.findViewById(R.id.reelbar);

        plus_card = view.findViewById(R.id.plus_card);

        plus_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), Upload_reels.class);
                startActivity(intent);

            }
        });





//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
//        layoutManager.setStackFromEnd(true);
//        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);
        SnapHelper snapHelper =  new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        Collections.shuffle(videosList);

         videoAdapter = new VideoAdapter(getActivity(),videosList);
        loadpost();




        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //here we find the current item number
                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int page_no=scrollOffset / height;
                if(page_no!=currentPage ){
                    currentPage=page_no;
                    Release_Previous_Player();
                    Set_Player(currentPage);

                }
            }
        });





        return view;



    }

    SimpleExoPlayer privious_player;
    public void Release_Previous_Player(){
        if(privious_player!=null) {
            privious_player.removeListener(this);
            privious_player.release();
        }
    }

    private void loadpost() {

        DatabaseReference ref;

        ref = FirebaseDatabase.getInstance().getReference("TravelReels");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                videosList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelVideos modelPost = ds.getValue(ModelVideos.class);



                    videosList.add(modelPost);

                    Collections.reverse(videosList);
                    Collections.shuffle(videosList);
                    recyclerView.setAdapter(videoAdapter);



















                }










            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });








    }



    public boolean is_fragment_exits(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if(fm.getBackStackEntryCount()==0){
            return false;
        }else {
            return true;
        }

    }

    public void Set_Player(int currentPage){

        try {
            LoadControl loadControl = new DefaultLoadControl.Builder()
                    .setAllocator(new DefaultAllocator(true, 16))
                    .setBufferDurationsMs(1 * 1024, 1 * 1024, 500, 1024)
                    .setTargetBufferBytes(-1)
                    .setPrioritizeTimeOverSizeThresholds(true)
                    .createDefaultLoadControl();


            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), getContext().getResources().getString(R.string.app_name)));

            Log.d("video_url"+currentPage,videosList.get(currentPage).getVideoUrl());

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(videosList.get(currentPage).getVideoUrl()));

       /* MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(proxyUrl));

            Log.d(Variables.tag,item.video_url);
            Log.d(Variables.tag,proxyUrl);*/


            player.prepare(videoSource);

            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            player.addListener(this);


            View layout = layoutManager.findViewByPosition(currentPage);
            final PlayerView playerView = layout.findViewById(R.id.reels_player);
            playerView.setPlayer(player);
             player.setPlayWhenReady(is_visible_to_user);
            privious_player = player;
        }
        catch (Exception e){

        }


    }

    boolean is_visible_to_user;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        is_visible_to_user=isVisibleToUser;

        if(privious_player!=null && (isVisibleToUser && !is_user_stop_video)){
            privious_player.setPlayWhenReady(true);
        }
        else if(privious_player!=null && !isVisibleToUser){
            privious_player.setPlayWhenReady(false);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        is_visible_to_user = true;
        if((privious_player!=null && (!is_user_stop_video)) && !is_fragment_exits() ){
            privious_player.setPlayWhenReady(true);
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(privious_player!=null){
            privious_player.release();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_ENDED:
                Log.i("EventListenerState", "Playback ended!");
                privious_player.setPlayWhenReady(false);
                break;
            case Player.STATE_READY:
                Log.i("EventListenerState", "Playback State Ready!");

                progressBar.setVisibility(View.GONE);
                break;
            case Player.STATE_BUFFERING:
                Log.i("EventListenerState", "Playback buffering");
                progressBar.setVisibility(View.VISIBLE);

                break;
            case Player.STATE_IDLE:

                break;

        }


        }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}