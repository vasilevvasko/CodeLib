package com.example.pogolemotoproektce.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pogolemotoproektce.Activities.SingleProductActivity;
import com.example.pogolemotoproektce.Classes.Book;
import com.example.pogolemotoproektce.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HomeActivityHorizontalViewAdapter extends RecyclerView.Adapter<HomeActivityHorizontalViewAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Book> books;


    public HomeActivityHorizontalViewAdapter(Context context, ArrayList<Book> books) {
        this.books = books;
        this.mContext = context;
    }

    private long lastClickTime = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.i("HomeActivityVerticalViewAdapter", "onCreateViewHolder()");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Log.i("HomeActivityVerticalViewAdapter", "onBindViewHolder");


        // so book class
        Glide.with(mContext).asBitmap().load(books.get(position).getUrl()).into(holder.bookImage);
        holder.bookTitle.setText(books.get(position).getTitle());
        holder.bookAuthor.setText((books.get(position).getAuthor()));
        holder.bookPrice.setText(Double.toString(books.get(position).getPrice()) + " BGN");



        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // preventing spam
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();


                Intent intent = new Intent(mContext, SingleProductActivity.class);
                Gson gson = new Gson();
                String jsonString = gson.toJson(books.get(position));
                intent.putExtra("Book", jsonString);
                ((Activity) mContext).startActivityForResult(intent, 1);

            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView bookImage;
        TextView bookTitle;
        TextView bookAuthor;
        TextView bookPrice;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {

            super(itemView);
            bookImage = itemView.findViewById(R.id.image);
            bookTitle = itemView.findViewById(R.id.caption);
            bookAuthor = itemView.findViewById(R.id.autor);
            bookPrice = itemView.findViewById(R.id.price);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
