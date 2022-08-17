package com.skyrain.stock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.highsoft.highcharts.common.hichartsclasses.HICondition;
import com.highsoft.highcharts.common.hichartsclasses.HILabel;
import com.highsoft.highcharts.common.hichartsclasses.HILegend;
import com.highsoft.highcharts.common.hichartsclasses.HILine;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIResponsive;
import com.highsoft.highcharts.common.hichartsclasses.HIRules;
import com.highsoft.highcharts.common.hichartsclasses.HISeries;
import com.highsoft.highcharts.common.hichartsclasses.HISubtitle;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;
import com.skyrain.stock.favorite.FavoriteData;
import com.skyrain.stock.news.NewsAdapter;
import com.skyrain.stock.news.NewsData;
import com.skyrain.stock.news.NewsDialog;
import com.skyrain.stock.peers.PeersAdapter;
import com.skyrain.stock.peers.PeersData;
import com.skyrain.stock.portfolio.PortfolioData;
import com.skyrain.stock.portfolio.TradeDialog;
import com.skyrain.stock.utils.JavaScriptInterface;
import com.skyrain.stock.utils.MyTools;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SubActivity extends AppCompatActivity {

    String gTicker="";
    Float gPrice;
    String gName="";


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gTicker = extras.getString("ticker");
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(gTicker);

        sendDescriptionRequest(gTicker);
        sendLatestPriceRequest(gTicker);
        sendSentimentsRequest(gTicker);
        sendNewsRequest(gTicker);
        sendPeersRequest(gTicker);
        setChart2();
        setChart3();
        setChart4();
        setShowChart();
        setTradeBtn();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub_menu,menu);
        MenuItem star = menu.findItem(R.id.favorite_star);
        ArrayList<FavoriteData> favoriteDataArrayList=getFavoriteDataList();
        if(isTickerInFavorite(gTicker,favoriteDataArrayList)){
            star.setIcon(R.drawable.ic_baseline_star_24);

        }else{
            star.setIcon(R.drawable.ic_baseline_star_border_24);

        }

        star.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                ArrayList<FavoriteData> favoriteDataArrayList=getFavoriteDataList();
                if(isTickerInFavorite(gTicker,favoriteDataArrayList)){
                    star.setIcon(R.drawable.ic_baseline_star_border_24);
                    ArrayList<FavoriteData> newFavoriteDataArrayList=new ArrayList<>();
                    for(FavoriteData fd:favoriteDataArrayList){
                        if(!fd.getTicker().equals(gTicker)){
                            newFavoriteDataArrayList.add(fd);
                        }
                    }
                    setFavoriteDataList(newFavoriteDataArrayList);
                    Toast.makeText(SubActivity.this,gTicker+" is removed from favorites",Toast.LENGTH_SHORT).show();
                }else{
                    star.setIcon(R.drawable.ic_baseline_star_24);
                    FavoriteData favoriteData = new FavoriteData(gTicker, gName);
                    favoriteDataArrayList.add(favoriteData);
                    setFavoriteDataList(favoriteDataArrayList);
                    Toast.makeText(SubActivity.this,gTicker+" is added to favorites",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void sendDescriptionRequest(String ticker){
        RequestQueue requestQueue = Volley.newRequestQueue(SubActivity.this);
        String url="https://stock-gty218-hw8.wl.r.appspot.com/getDescription/"+ticker;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String url=obj.getString("logo");
                    String ticker=obj.getString("ticker");
                    String name=obj.getString("name");

                    TextView tickerView = findViewById(R.id.summary_ticker);
                    TextView nameView = findViewById(R.id.summary_name);
                    ImageView imageView =findViewById(R.id.sub_image);
                    TextView ipoView = findViewById(R.id.sub_ipo);
                    TextView industryView = findViewById(R.id.sub_industry);
                    TextView webView = findViewById(R.id.sub_webUrl);

                    gName=name;
                    tickerView.setText(ticker);
                    nameView.setText(name);
                    ipoView.setText(dateFormat(obj.getString("ipo")));
                    industryView.setText(obj.getString("finnhubIndustry"));
                    webView.setText(obj.getString("weburl"));
                    Picasso.with(SubActivity.this).load(url).into(imageView);
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
    private void sendLatestPriceRequest(String ticker){
        RequestQueue requestQueue = Volley.newRequestQueue(SubActivity.this);
        String url="https://stock-gty218-hw8.wl.r.appspot.com/getLatestPrice/"+ticker;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String price="$"+ MyTools.floatFormat(Float.parseFloat(obj.getString("c")));
                    String change=MyTools.floatFormat(Float.parseFloat(obj.getString("d")))+"("+String. format("%.2f", Float.parseFloat(obj.getString("dp")))+"%)";
                    boolean up=Float.parseFloat(obj.getString("dp"))>0?true:false;
                    Integer flag=up?1:0;
                    gPrice=Float.parseFloat(obj.getString("c"));


                    TextView priceView = findViewById(R.id.summary_price);
                    TextView changeView = findViewById(R.id.summary_change);
                    ImageView trendView = findViewById(R.id.summary_trend);
                    TextView openPriceView = findViewById(R.id.sub_open_price);
                    TextView highPriceView = findViewById(R.id.sub_high_price);
                    TextView lowPriceView = findViewById(R.id.sub_low_price);
                    TextView closePriceView = findViewById(R.id.sub_close);



                    priceView.setText(price);
                    changeView.setText(change);
                    openPriceView.setText("$"+obj.getString("o"));
                    highPriceView.setText("$"+obj.getString("h"));
                    lowPriceView.setText("$"+obj.getString("l"));
                    closePriceView.setText("$"+obj.getString("pc"));
                    if(up){
                        priceView.setTextColor(Color.BLACK);
                        changeView.setTextColor(Color.GREEN);
                        trendView.setImageResource(R.drawable.ic_baseline_trending_up_24);
                    }else{
                        priceView.setTextColor(Color.BLACK);
                        changeView.setTextColor(Color.RED);
                        trendView.setImageResource(R.drawable.ic_baseline_trending_down_24);
                    }
                    //set portfolio
                    setPortfolio();
                    setChart1(flag);

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
    private void sendSentimentsRequest(String ticker){
        RequestQueue requestQueue = Volley.newRequestQueue(SubActivity.this);
        String url="https://stock-gty218-hw8.wl.r.appspot.com/getSentiments/"+ticker;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray redditArr=obj.getJSONArray("reddit");
                    JSONArray twitterArr=obj.getJSONArray("twitter");
                    Integer totalRedditComments=0;
                    Integer posRedditComments=0;
                    Integer negRedditComments=0;
                    Integer totalTwitterComments=0;
                    Integer posTwitterComments=0;
                    Integer negTwitterComments=0;
                    for (int i = 0; i < redditArr.length(); i++) {
                        posRedditComments += Integer.parseInt(redditArr.getJSONObject(i).getString("positiveMention"));
                        totalRedditComments += Integer.parseInt(redditArr.getJSONObject(i).getString("mention"));
                        negRedditComments += Integer.parseInt(redditArr.getJSONObject(i).getString("negativeMention"));
                    }
                    for (int i = 0; i < twitterArr.length(); i++) {
                        posTwitterComments += Integer.parseInt(twitterArr.getJSONObject(i).getString("positiveMention"));
                        totalTwitterComments += Integer.parseInt(twitterArr.getJSONObject(i).getString("mention"));
                        posTwitterComments += Integer.parseInt(twitterArr.getJSONObject(i).getString("negativeMention"));
                    }

                    TextView redditTotalView = findViewById(R.id.reddit_total);
                    TextView redditPosView = findViewById(R.id.reddit_pos);
                    TextView redditNegView = findViewById(R.id.reddit_neg);
                    TextView twitterTotalView = findViewById(R.id.twitter_total);
                    TextView twitterPosView = findViewById(R.id.twitter_pos);
                    TextView twitterNegView = findViewById(R.id.twitter_neg);

                    redditTotalView.setText(totalRedditComments.toString());
                    redditPosView.setText(posRedditComments.toString());
                    redditNegView.setText(negRedditComments.toString());
                    twitterTotalView.setText(totalTwitterComments.toString());
                    twitterPosView.setText(posTwitterComments.toString());
                    twitterNegView.setText(negTwitterComments.toString());


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

    private void sendNewsRequest(String ticker){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date();
        String strCurDate = formatter.format(curDate);
        Date preDate = new Date(curDate.getTime() - 24 * 3600000);
        String strPreDate = formatter.format(preDate);

        RequestQueue requestQueue = Volley.newRequestQueue(SubActivity.this);
        String url="https://stock-gty218-hw8.wl.r.appspot.com/getNews/"+ticker+"/"+strCurDate+"/"+strPreDate;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //set the first image
                    JSONArray jsonArray = new JSONArray(response);
                    TextView firstNewHeadline = findViewById(R.id.first_news_headline);
                    TextView firstNewSource = findViewById(R.id.first_news_source);
                    ImageView firstNewImg = findViewById(R.id.first_news_img);
                    firstNewHeadline.setText(jsonArray.getJSONObject(0).getString("headline"));
                    Long firstNewsCurTime=jsonArray.getJSONObject(0).getLong("datetime");
                    firstNewSource.setText(jsonArray.getJSONObject(0).getString("source")+"   "+getTimeElapsed(firstNewsCurTime*1000));
                    String url=null;
                    url=jsonArray.getJSONObject(0).getString("image");
                    if(url==null || url.equals("")){
                        url="https://static.seekingalpha.com/cdn/s3/uploads/getty_images/823310866/image_823310866.jpg?io=getty-c-w750";
                    }
                    Picasso.with(SubActivity.this).load(url).into(firstNewImg);

                    View firstNewsView=findViewById(R.id.first_news);
                    firstNewsView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            NewsDialog newsDialog = null;
                            try {
                                newsDialog = new NewsDialog(jsonArray.getJSONObject(0).getString("source"), jsonArray.getJSONObject(0).getString("datetime"), jsonArray.getJSONObject(0).getString("headline"), jsonArray.getJSONObject(0).getString("summary"),jsonArray.getJSONObject(0).getString("url"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            newsDialog.show((SubActivity.this).getSupportFragmentManager(), "example");

                        }
                    });
                    // set the rest of the news
                    RecyclerView recyclerView=findViewById(R.id.news_recycler);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SubActivity.this));

                    NewsData[] newsDataList=new NewsData[jsonArray.length()-1];
                    for(int i=1;i< jsonArray.length();i++){
                        String headline=jsonArray.getJSONObject(i).getString("headline");
                        Long newsCurTime=jsonArray.getJSONObject(i).getLong("datetime");
                        String source=jsonArray.getJSONObject(i).getString("source")+"   "+getTimeElapsed(newsCurTime*1000);
                        String imageUrl=null;
                        imageUrl=jsonArray.getJSONObject(i).getString("image");
                        if(imageUrl==null || imageUrl.equals("")){
                            imageUrl="https://static.seekingalpha.com/cdn/s3/uploads/getty_images/823310866/image_823310866.jpg?io=getty-c-w750";
                        }
                        String date=jsonArray.getJSONObject(i).getString("datetime");// unit: s
                        String content=jsonArray.getJSONObject(i).getString("summary");
                        String webUrl=jsonArray.getJSONObject(i).getString("url");
                        NewsData newsData=new NewsData(source,date,headline,content,webUrl,imageUrl,jsonArray.getJSONObject(i).getString("source"));
                        newsDataList[i-1]=newsData;
                    }

                    NewsAdapter newsAdapter=new NewsAdapter(newsDataList,SubActivity.this);
                    recyclerView.setAdapter(newsAdapter);


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

    private void sendPeersRequest(String ticker){
        RequestQueue requestQueue = Volley.newRequestQueue(SubActivity.this);
        String url="https://stock-gty218-hw8.wl.r.appspot.com/getPeers/"+ticker;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    PeersData[] peersDataList=new PeersData[jsonArray.length()];
                    for(int i=0;i< jsonArray.length();i++){
                        peersDataList[i]=new PeersData(jsonArray.getString(i)+",");
                    }

                    RecyclerView peersRecyclerView=findViewById(R.id.peers_recycler);
                    peersRecyclerView.setHasFixedSize(true);
                    peersRecyclerView.setLayoutManager(new LinearLayoutManager(SubActivity.this,LinearLayoutManager.HORIZONTAL,false));


                    PeersAdapter peersAdapter=new PeersAdapter(peersDataList,SubActivity.this);
                    peersRecyclerView.setAdapter(peersAdapter);
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

    private void setTradeBtn(){
        Button tradeBtn = findViewById(R.id.btn_trade);
        tradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TradeDialog tradeDialog = new TradeDialog(gTicker,gName,gPrice,SubActivity.this);
                tradeDialog.show(((AppCompatActivity) SubActivity.this).getSupportFragmentManager(), "Trade Dialog");
            }
        });
    }



    private void setChart1(Integer flag){
        System.out.println(flag);
        WebView webView = findViewById(R.id.sub_web_view_1);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(gTicker,flag), "Android");
        webView.loadUrl("file:///android_asset/chart1.html");
    }
    private void setChart2(){
        WebView webView = findViewById(R.id.sub_web_view_2);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(gTicker,0), "Android");
        webView.loadUrl("file:///android_asset/chart2.html");
    }
    private void setChart3(){
        WebView webView = findViewById(R.id.sub_web_view_3);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(gTicker,0), "Android");
        webView.loadUrl("file:///android_asset/chart3.html");
    }
    private void setChart4(){
        WebView webView = findViewById(R.id.sub_web_view_4);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(gTicker,0), "Android");
        webView.loadUrl("file:///android_asset/chart4.html");
    }

    private void setShowChart(){
        ImageView imageView1 = findViewById(R.id.sub_image_view1);
        imageView1.setClickable(true);
        imageView1.setColorFilter(Color.BLUE);
        imageView1.setBackgroundResource(R.drawable.chart_bottom_line);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView1=findViewById(R.id.sub_image_view1);
                imageView1.setColorFilter(Color.BLUE);
                imageView1.setBackgroundResource(R.drawable.chart_bottom_line);
                ImageView imageView2=findViewById(R.id.sub_image_view2);
                imageView2.setColorFilter(Color.BLACK);
                imageView2.setBackgroundResource(R.drawable.chart_bottom_line2);

                WebView webView1 = findViewById(R.id.sub_web_view_1);
                webView1.setVisibility(View.VISIBLE);
                WebView webView2 = findViewById(R.id.sub_web_view_2);
                webView2.setVisibility(View.INVISIBLE);
            }
        });
        ImageView imageView2 = findViewById(R.id.sub_image_view2);
        imageView2.setClickable(true);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView1=findViewById(R.id.sub_image_view1);
                imageView1.setColorFilter(Color.BLACK);
                imageView1.setBackgroundResource(R.drawable.chart_bottom_line2);
                ImageView imageView2=findViewById(R.id.sub_image_view2);
                imageView2.setColorFilter(Color.BLUE);
                imageView2.setBackgroundResource(R.drawable.chart_bottom_line);


                WebView webView1 = findViewById(R.id.sub_web_view_1);
                webView1.setVisibility(View.INVISIBLE);
                WebView webView2 = findViewById(R.id.sub_web_view_2);
                webView2.setVisibility(View.VISIBLE);
            }
        });
    }

    private String getTimeElapsed(Long preTime){
        Long timeElapsed=(Long)(new Date().getTime()-preTime)/1000;
        if(timeElapsed<60){
            return timeElapsed+" seconds ago";
        }else if(60<=timeElapsed && timeElapsed<3600){
            return timeElapsed/60+" minutes ago";
        }else{
            return timeElapsed/3600+" hours ago";
        }

    }

    private ArrayList<FavoriteData> getFavoriteDataList(){
        ArrayList<FavoriteData>  favoriteDataList=null;
        SharedPreferences sharedPreferences = getSharedPreferences("mp", 0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favoriteDataList", null);
        Type type = new TypeToken<ArrayList<FavoriteData>>() {}.getType();
        favoriteDataList = gson.fromJson(json, type);
        if(favoriteDataList==null){
            favoriteDataList=new ArrayList<>();
        }
        return favoriteDataList;
    }
    private void setFavoriteDataList(ArrayList<FavoriteData> favoriteDataList){
        SharedPreferences sharedPreferences = getSharedPreferences("mp", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favoriteDataList);
        editor.putString("favoriteDataList",json);
        editor.apply();
    }
    private boolean isTickerInFavorite(String ticker,ArrayList<FavoriteData> favoriteDataArrayList){
        if(favoriteDataArrayList==null || favoriteDataArrayList.size()==0){
            return false;
        }
        for(FavoriteData fd: favoriteDataArrayList){
            if(fd.getTicker().equals(ticker)){
                return true;
            }
        }
        return false;
    }

    public void setPortfolio(){
        TextView portfolioShareView =findViewById(R.id.portfolio_shares);
        TextView portfolioAvgView =findViewById(R.id.portfolio_avg);
        TextView portfolioTotalView =findViewById(R.id.portfolio_total);
        TextView portfolioChangeView =findViewById(R.id.portfolio_change);
        TextView portfolioMarketView =findViewById(R.id.portfolio_market_value);


        ArrayList<PortfolioData> portfolioDataList = MyTools.getPortfolioDataList(SubActivity.this);

        for(PortfolioData pd:portfolioDataList){
            if(pd.getTicker().equals(gTicker)){
                portfolioShareView.setText(pd.getShares().toString());
                portfolioAvgView.setText("$"+floatFormat(pd.getAvgPrice()));
                portfolioTotalView.setText("$"+floatFormat(pd.getAvgPrice()*pd.getShares()));
                Float marketValue=gPrice*pd.getShares();
                if(marketValue<(pd.getAvgPrice()*pd.getShares())){
                    portfolioMarketView.setTextColor(Color.RED);
                    portfolioChangeView.setTextColor(Color.RED);
                }else{
                    portfolioMarketView.setTextColor(Color.GREEN);
                    portfolioChangeView.setTextColor(Color.GREEN);
                }
                portfolioMarketView.setText("$"+floatFormat(marketValue));
                portfolioChangeView.setText("$"+floatFormat(marketValue-pd.getShares()*pd.getAvgPrice()));
                return ;
            }
        }
        portfolioShareView.setText("0");
        portfolioAvgView.setText("$0.00");
        portfolioTotalView.setText("$0.00");
        portfolioChangeView.setText("$0.00");
        portfolioMarketView.setText("$0.00");
    }

    public String floatFormat(Float f){
        return String. format("%.2f", f);
    }

    public String dateFormat(String str){
        String[] tmp=str.split("-");
        return tmp[1]+"-"+tmp[2]+"-"+tmp[0];
    }
}