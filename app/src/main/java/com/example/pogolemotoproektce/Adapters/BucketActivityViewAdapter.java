package com.example.pogolemotoproektce.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pogolemotoproektce.Activities.BucketActivity;
import com.example.pogolemotoproektce.Classes.Book;
import com.example.pogolemotoproektce.Classes.BucketListSingleton;
import com.example.pogolemotoproektce.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BucketActivityViewAdapter extends RecyclerView.Adapter<BucketActivityViewAdapter.ViewHolder>{


    private ArrayList<Book> books;
    private Context mContext;
    BucketListSingleton bucketListSingleton;


    public BucketActivityViewAdapter(Context mContext, ArrayList<Book> books) {
        this.books = books;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_layout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        bucketListSingleton = BucketListSingleton.getInstance();

        Glide.with(mContext).asBitmap().load(books.get(position).getUrl()).into(holder.image);
        holder.title.setText(books.get(position).getTitle());
        holder.author.setText(books.get(position).getAuthor());
        holder.price.setText(Double.toString(books.get(position).getPrice())+ " BGN");
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        books.remove(position);
                        notifyItemRemoved(position);
                        /*bucketListSingleton.resetBucket(books);*/
                        ((BucketActivity)mContext).restart(books);

                    }
                });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView title;
        TextView author;
        TextView price;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            image = itemView.findViewById(R.id.bucket_item_image);
            title = itemView.findViewById(R.id.bucket_item_title);
            author = itemView.findViewById(R.id.bucket_item_author);
            price = itemView.findViewById(R.id.bucket_item_price);
            parentLayout = itemView.findViewById(R.id.bucketParentLayout);
        }
    }
}
