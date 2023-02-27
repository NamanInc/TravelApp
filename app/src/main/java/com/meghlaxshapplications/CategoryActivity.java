package com.meghlaxshapplications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.Adapters.AdapterPost;
import com.Adapters.CategoryPostAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.travelapp.R;
import com.models.ModelPost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryPostAdapter adapterPost;
    private DatabaseReference ref;
    private List<ModelPost> postList;
    private String intentresult;
    private EditText searchtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().hide();



       intentresult = getIntent().getStringExtra("Camping");


        recyclerView = findViewById(R.id.recy);
        searchtext = findViewById(R.id.searchbar);



        ref = FirebaseDatabase.getInstance().getReference("Posts");

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView = findViewById(R.id.recy);

        postList = new ArrayList<>();

        loadpost();


        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {


                if (!TextUtils.isEmpty(s)){
                    searchPost(s.toString());
                }
                else {
                    loadpost();
                }

            }
        });







    }

    private void searchPost(String searchQuery) {




        Query query = ref.orderByChild("Category").equalTo(intentresult);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    if (modelPost.getPlaceName().toLowerCase().contains(searchQuery.toLowerCase()) || modelPost.getDescription().toLowerCase().contains(searchQuery.toLowerCase()) || modelPost.getState().toLowerCase().contains(searchQuery.toLowerCase()) || modelPost.getTown().toLowerCase().contains(searchQuery.toLowerCase())){

                        postList.add(modelPost);
                    }



                    adapterPost = new CategoryPostAdapter(CategoryActivity.this,postList);
                    recyclerView.setAdapter(adapterPost);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(CategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });





    }


    private void loadpost() {




        Query query = ref.orderByChild("Category").equalTo(intentresult);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    postList.add(modelPost);

                    Collections.shuffle(postList);

                    adapterPost = new CategoryPostAdapter(CategoryActivity.this,postList);
                    recyclerView.setAdapter(adapterPost);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(CategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}