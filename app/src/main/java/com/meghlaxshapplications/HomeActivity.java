package com.meghlaxshapplications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.Adapters.ViewPagerAdapter;
import com.Fragments.HomeFragment;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.meghlaxshapplications.travelapp.R;
import com.notification.Token;

public class HomeActivity extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;
    ViewPager rel;
    LottieAnimationView loadinanim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        chipNavigationBar = findViewById(R.id.chip128);
        loadinanim = findViewById(R.id.loadinanim);


        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.pagecolor));


// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(HomeActivity.this,R.color.pagecolor));
        rel = findViewById(R.id.frgment_container);
        getSupportFragmentManager().beginTransaction().replace(R.id.frgment_container,new HomeFragment()).commit();
        chipNavigationBar.setItemSelected(R.id.item_home,true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rel.setVisibility(View.VISIBLE);
                loadinanim.setVisibility(View.GONE);

            }
        },3500);

        setupviewPager();



        bottommenu();




    }

    @Override
    protected void onResume() {


        super.onResume();
    }

    private void setupviewPager() {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager() , FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        rel.setAdapter(viewPagerAdapter);

        rel.setSaveEnabled(false);

        rel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position){

                    case 0:
                        chipNavigationBar.setItemSelected(R.id.item_home,true);
                        break;

                    case 1:
                        chipNavigationBar.setItemSelected(R.id.trending_icon,true);
                        break;

                    case 2:
                        chipNavigationBar.setItemSelected(R.id.item_notification,true);

                        break;

                    case 3:
                        chipNavigationBar.setItemSelected(R.id.item_Posts,true);
                        break;


                    case 4:

                        chipNavigationBar.setItemSelected(R.id.item_save,true);


                        break;




                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }




    private void bottommenu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {


                switch (i){
                    case R.id.item_home:

                        rel.setCurrentItem(0);
                        break;

//                        fragment = new HomeFragment();
//                        break;

                    case R.id.trending_icon:
                        rel.setCurrentItem(1);
                        break;

                    case R.id.item_notification:

                        rel.setCurrentItem(2);
                        break;

                    case R.id.item_Posts:

                        rel.setCurrentItem(3);
                        break;



                    case R.id.item_save:
                        rel.setCurrentItem(4);
                        break;
                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.frgment_container,fragment).commit();

            }
        });


    }
}