package com.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.meghlaxshapplications.travelapp.R;
import com.models.ModelProducts;

import java.util.List;

public class HomeProAdapter extends RecyclerView.Adapter<HomeProAdapter.MyHolder>{

    Context context;
    List<ModelProducts> productsList;

    private DatabaseReference likesRef;
    private DatabaseReference postRef;
    private FirebaseAuth firebaseAuth;

    boolean mProcessLike = false;


    public HomeProAdapter(Context context, List<ModelProducts> productsList) {
        this.context = context;
        this.productsList = productsList;
        firebaseAuth = FirebaseAuth.getInstance();



    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.homepro,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String price = "₹"+productsList.get(position).getProductPrice();

        holder.productName.setText(productsList.get(position).getProductName());
        holder.productPrice.setText(price);
        holder.productCategory.setText(productsList.get(position).getCategory());



        try {
            Glide.with(context).load(productsList.get(position).getProductImage()).into(holder.productImage);

        } catch (Exception e) {
            e.printStackTrace();
        }




    }






    @Override
    public int getItemCount() {
        return productsList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        ImageView productImage;
        TextView productName, productPrice , productCategory;


        public MyHolder(@NonNull View itemView) {




            super(itemView);

            productCategory = itemView.findViewById(R.id.productCategory);
            productImage = itemView.findViewById(R.id.productImage);
            productPrice = itemView.findViewById(R.id.productprice);
            productName = itemView.findViewById(R.id.producttitle);





        }
    }


}
