package com.skyrain.stock.peers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skyrain.stock.MainActivity;
import com.skyrain.stock.R;
import com.skyrain.stock.SubActivity;
import com.skyrain.stock.news.NewsAdapter;
import com.skyrain.stock.news.NewsData;
import com.squareup.picasso.Picasso;

public class PeersAdapter extends RecyclerView.Adapter<PeersAdapter.ViewHolder> {

    PeersData[] peersData;
    Context context;

    public PeersAdapter(PeersData[] peersData, SubActivity activity) {
        this.peersData=peersData;
        this.context=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.peers_item_list,parent,false);
        PeersAdapter.ViewHolder viewHolder=new PeersAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PeersData peersDataList=peersData[position];
        holder.peersView.setText(peersDataList.getTicker());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str=peersDataList.getTicker();
                String ticker=str.substring(0,str.length()-1);
                Intent intent = new Intent(context, SubActivity.class);
                intent.putExtra("ticker",ticker);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return peersData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView peersView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            peersView=itemView.findViewById(R.id.peers_text);
        }
    }
}
