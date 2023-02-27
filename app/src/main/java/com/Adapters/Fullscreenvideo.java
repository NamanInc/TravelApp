package com.Adapters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
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
import com.meghlaxshapplications.FullPlace;
import com.meghlaxshapplications.travelapp.R;

public class Fullscreenvideo extends AppCompatActivity {
    private boolean fullscreen = false;
    private SimpleExoPlayerView exoplayerview_full;
    private ExoPlayer exoPlayer;

    String vidoeurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreenvideo);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        vidoeurl = intent.getStringExtra("videourl");






        exoplayerview_full = findViewById(R.id.exoplayerview_full);



        videbyus(vidoeurl);




        if (fullscreen){
//            videofull.setImageDrawable(ContextCompat.getDrawable(FullPlace.this,R.drawable.full));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) exoplayerview_full.getLayoutParams();
            params.width  = params.MATCH_PARENT;
            params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
            exoplayerview_full.setLayoutParams(params);
            fullscreen = false;

        }
        else {

//            videofull.setImageDrawable(ContextCompat.getDrawable(FullPlace.this,R.drawable.fullexist));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)exoplayerview_full.getLayoutParams();
            params.width  = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            exoplayerview_full.setLayoutParams(params);
            fullscreen = true;



        }

    }

    private void videbyus(String  videourl) {
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);

            Uri videoUri = Uri.parse(videourl);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoUri,dataSourceFactory,extractorsFactory,null,null);
            exoplayerview_full.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);


        } catch (Exception e) {

            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        exoPlayer.stop();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (exoPlayer!=null){
            pausePlayer();

        }

        super.onPause();
    }


    @Override
    protected void onResume() {
        if (exoPlayer!=null){
          startPlayer();

        }
       // exoPlayer.release();
        super.onResume();
    }

    private void pausePlayer(){
        exoPlayer.setPlayWhenReady(false);
        exoPlayer.getPlaybackState();
    }
    private void startPlayer(){
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.getPlaybackState();
    }
}