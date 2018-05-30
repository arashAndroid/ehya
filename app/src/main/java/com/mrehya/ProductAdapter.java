package com.mrehya;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by sdfsdfasf on 2/22/2018.
 */




    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

        private Context mContext;
        private List<Product> productList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, count;
            public ImageView thumbnail, overflow;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                count = (TextView) view.findViewById(R.id.count);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

            }
        }


        public ProductAdapter(Context mContext, List<Product> albumList) {
            this.mContext = mContext;
            this.productList = albumList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final Product album = productList.get(position);
            holder.title.setText(album.getTitle());
            holder.count.setText(album.getPrice() + " تومان");

            // loading album cover using Glide library
            Glide.with(mContext)
                    .load(album.getImage())
                    .placeholder(R.drawable.logo_eng)
                    .error(R.drawable.logo_eng)
                    .into(holder.thumbnail);


//            Glide.with(mContext).load(album.getId()).into(holder.thumbnail);

            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(mContext,ShowProduct.class);
                    intent.putExtra("Product", album);
                    mContext.startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return productList.size();
        }
    }

