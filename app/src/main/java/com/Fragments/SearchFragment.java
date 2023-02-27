package com.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.Adapters.AdapterUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meghlaxshapplications.travelapp.R;
import com.models.ModelUsers;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUsers> usersList;
    EditText searchUsers;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.searchfragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_users);
        searchUsers = view.findViewById(R.id.searchbar_users);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        // Inflate the layout for this fragment

        usersList = new ArrayList<>();

        getallusers();

        searchUsers.addTextChangedListener(new TextWatcher() {
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
                    getallusers();
                }

            }


        });
        return view;
    }

    private void searchPost(String qurrey) {

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){

                    ModelUsers modelUsers = ds.getValue(ModelUsers.class);

                    if (!modelUsers.getUID().equals(fUser.getUid())){


                        if (modelUsers.getName().toLowerCase().contains(qurrey.toLowerCase())){
                            usersList.add(modelUsers);


                        }


                    }

                    adapterUsers = new AdapterUsers(getActivity(),usersList);
                    adapterUsers.notifyDataSetChanged();

                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

    private void getallusers() {

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

                    adapterUsers = new AdapterUsers(getActivity(),usersList);

                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}