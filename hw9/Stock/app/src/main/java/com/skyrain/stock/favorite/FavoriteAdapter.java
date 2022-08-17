package com.skyrain.stock.favorite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyrain.stock.R;
import com.skyrain.stock.SubActivity;
import com.skyrain.stock.news.NewsAdapter;
import com.skyrain.stock.news.NewsData;
import com.skyrain.stock.news.NewsDialog;
import com.skyrain.stock.portfolio.PortfolioAdapter;
import com.skyrain.stock.utils.MyTools;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{
    FavoriteData[] favoriteDataList;
    Context context;
    Timer timer;

    public FavoriteAdapter(FavoriteData[] favoriteDataList, Context context) {
        this.favoriteDataList = favoriteDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.favorites_item_list,parent,false);
        FavoriteAdapter.ViewHolder viewHolder=new FavoriteAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        final FavoriteData favoriteData=favoriteDataList[position];
        holder.favoriteTickerView.setText(favoriteData.getTicker());
        holder.favoriteNameView.setText(favoriteData.getName());

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendLatestPriceRequest(favoriteData.getTicker(),holder);
                System.out.println("--------------Refresh Data---------------");
            }
        },1,1000*15);


        holder.favoriteRedirectiew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ticker=favoriteData.getTicker();
                Intent intent = new Intent(context, SubActivity.class);
                intent.putExtra("ticker",ticker);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return favoriteDataList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView favoriteArrowView;
        TextView favoriteTickerView;
        TextView favoriteNameView;
        TextView favoritePriceView;
        TextView favoriteChangeView;
        ImageView favoriteRedirectiew;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteArrowView=itemView.findViewById(R.id.favorite_arrow);
            favoriteTickerView=itemView.findViewById(R.id.favorite_ticker);
            favoriteNameView=itemView.findViewById(R.id.favorite_name);
            favoritePriceView=itemView.findViewById(R.id.favorite_price);
            favoriteChangeView=itemView.findViewById(R.id.favorite_change);
            favoriteRedirectiew=itemView.findViewById(R.id.favorite_redirect);
        }
    }

    private void sendLatestPriceRequest(String ticker,@NonNull FavoriteAdapter.ViewHolder holder){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url="https://stock-gty218-hw8.wl.r.appspot.com/getLatestPrice/"+ticker;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String price="$"+ MyTools.floatFormat(Float.parseFloat(obj.getString("c")));
                    String change=MyTools.floatFormat(Float.parseFloat(obj.getString("d")))+"("+String. format("%.2f", Float.parseFloat(obj.getString("dp")))+"%)";
                    boolean up=Float.parseFloat(obj.getString("dp"))>0?true:false;

                    holder.favoritePriceView.setText(price);
                    holder.favoriteChangeView.setText(change);
                    if(up){
                        holder.favoritePriceView.setTextColor(Color.BLACK);
                        holder.favoriteChangeView.setTextColor(Color.GREEN);
                        holder.favoriteArrowView.setImageResource(R.drawable.ic_baseline_trending_up_24);
                    }else{
                        holder.favoritePriceView.setTextColor(Color.BLACK);
                        holder.favoriteChangeView.setTextColor(Color.RED);
                        holder.favoriteArrowView.setImageResource(R.drawable.ic_baseline_trending_down_24);
                    }
                    //set portfolio

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }
}
