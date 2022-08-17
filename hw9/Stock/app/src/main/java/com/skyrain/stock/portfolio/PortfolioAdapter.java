package com.skyrain.stock.portfolio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyrain.stock.R;
import com.skyrain.stock.SubActivity;
import com.skyrain.stock.favorite.FavoriteAdapter;
import com.skyrain.stock.favorite.FavoriteData;
import com.skyrain.stock.utils.MyTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.ViewHolder>{
    PortfolioData[] portfolioDataList;
    Context context;
    Timer timer;

    public PortfolioAdapter(PortfolioData[] portfolioData, Context context) {
        this.portfolioDataList = portfolioData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.portfolio_item_list,parent,false);
        PortfolioAdapter.ViewHolder viewHolder=new PortfolioAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PortfolioData portfolioData=portfolioDataList[position];
        holder.portfolioTickerView.setText(portfolioData.getTicker());
        holder.portfolioSharesView.setText(portfolioData.getShares().toString());
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendLatestPriceRequest(portfolioData.getTicker(), holder);
            }
        },1,1000*15);
        holder.portfolioRedirectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ticker=portfolioData.getTicker();
                Intent intent = new Intent(context, SubActivity.class);
                intent.putExtra("ticker",ticker);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return portfolioDataList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView portfolioArrowView;
        TextView portfolioTickerView;
        TextView portfolioSharesView;
        TextView portfolioPriceView;
        TextView portfolioChangeView;
        ImageView portfolioRedirectView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            portfolioArrowView=itemView.findViewById(R.id.portfolio_arrow);
            portfolioTickerView=itemView.findViewById(R.id.portfolio_ticker);
            portfolioSharesView=itemView.findViewById(R.id.portfolio_shares);
            portfolioPriceView=itemView.findViewById(R.id.portfolio_price);
            portfolioChangeView=itemView.findViewById(R.id.portfolio_change);
            portfolioRedirectView=itemView.findViewById(R.id.portfolio_redirect);
        }
    }
    private void sendLatestPriceRequest(String ticker,@NonNull ViewHolder holder){
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

                    holder.portfolioPriceView.setText(price);
                    holder.portfolioChangeView.setText(change);
                    if(up){
                        holder.portfolioPriceView.setTextColor(Color.BLACK);
                        holder.portfolioChangeView.setTextColor(Color.GREEN);
                        holder.portfolioArrowView.setImageResource(R.drawable.ic_baseline_trending_up_24);
                    }else{
                        holder.portfolioPriceView.setTextColor(Color.BLACK);
                        holder.portfolioChangeView.setTextColor(Color.RED);
                        holder.portfolioArrowView.setImageResource(R.drawable.ic_baseline_trending_down_24);
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
