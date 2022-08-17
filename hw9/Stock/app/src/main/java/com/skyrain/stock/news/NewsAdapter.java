package com.skyrain.stock.news;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.skyrain.stock.MainActivity;
import com.skyrain.stock.R;
import com.skyrain.stock.SubActivity;
import com.squareup.picasso.Picasso;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    NewsData[] newsData;
    Context context;

    public NewsAdapter(NewsData[] newsData, SubActivity activity) {
        this.newsData=newsData;
        this.context=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.news_item_list,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NewsData newsDataList=newsData[position];
        Picasso.with(context).load(newsDataList.getUrl()).into(holder.newsImage);
        holder.newsHeadline.setText(newsDataList.getHeadline());
        holder.newsSource.setText(newsDataList.getSource());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsDialog newsDialog = new NewsDialog(newsDataList.getTitle(), newsDataList.getDate(), newsDataList.getHeadline(), newsDataList.getContent(), newsDataList.getWebUrl());
                newsDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "News Dialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView newsImage;
        TextView newsSource;
        TextView newsHeadline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage=itemView.findViewById(R.id.news_img);
            newsSource=itemView.findViewById(R.id.news_source);
            newsHeadline=itemView.findViewById(R.id.news_headline);

        }
    }
}
