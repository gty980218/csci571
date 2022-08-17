package com.skyrain.stock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ResponseDelivery;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.highsoft.highcharts.common.hichartsclasses.HICSSObject;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HIColumn;
import com.highsoft.highcharts.common.hichartsclasses.HICondition;
import com.highsoft.highcharts.common.hichartsclasses.HIDataLabels;
import com.highsoft.highcharts.common.hichartsclasses.HILabel;
import com.highsoft.highcharts.common.hichartsclasses.HILegend;
import com.highsoft.highcharts.common.hichartsclasses.HILine;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIResponsive;
import com.highsoft.highcharts.common.hichartsclasses.HIRules;
import com.highsoft.highcharts.common.hichartsclasses.HISeries;
import com.highsoft.highcharts.common.hichartsclasses.HIStackLabels;
import com.highsoft.highcharts.common.hichartsclasses.HISubtitle;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.skyrain.stock.favorite.FavoriteAdapter;
import com.skyrain.stock.favorite.FavoriteData;
import com.skyrain.stock.news.NewsAdapter;
import com.skyrain.stock.portfolio.PortfolioAdapter;
import com.skyrain.stock.portfolio.PortfolioData;
import com.skyrain.stock.utils.MyTools;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.highsoft.highcharts.common.*;
import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.core.HIChartView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    String[] autoComplete=new String[10];
    MenuItem menuItem;
    FavoriteAdapter favoriteAdapter;


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Stock);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView dateView = (TextView)findViewById(R.id.main_date);
        dateView.setText(MyTools.getFormatDate());


        for (int i = 0; i < 10; i++) {
            autoComplete[i]="";
        }
        setInitialFavorites();
        setInitialPortfolio();
        setInitialBalance();
        setFooter();







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        menuItem = menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search...");





        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("ticker",searchView.getQuery().toString());
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sendAutoCompleteRequest(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void sendAutoCompleteRequest(String ticker){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url="https://stock-gty218-hw8.wl.r.appspot.com/autoComplete/"+ticker;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    setAutoComplete(response);
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

    private void setAutoComplete(String response) throws JSONException {
        JSONObject obj = new JSONObject(response);
        JSONArray arr = obj.getJSONArray("result");
        for (int i = 0; i < 10; i++) {
            String str = arr.getJSONObject(i).getString("displaySymbol")+" | "+arr.getJSONObject(i).getString("description");
            autoComplete[i]=str;
        }

        SearchView searchView=(SearchView) menuItem.getActionView();
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        //searchAutoComplete.setBackgroundColor(Color.WHITE);
        //searchAutoComplete.setTextColor(Color.BLACK);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, autoComplete);
        searchAutoComplete.setAdapter(newsAdapter);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("ticker",searchView.getQuery().toString());
                startActivity(intent);
            }
        });
    }

    private void setInitialBalance(){
        SharedPreferences mPrefs = getSharedPreferences("mp",0);
        float balance = mPrefs.getFloat("balance", 25000);
        if(balance==25000){
            SharedPreferences.Editor edit = mPrefs.edit();
            edit.putFloat("balance",25000);
            edit.apply();
        }

    }

    private void setInitialFavorites(){
        ArrayList<FavoriteData> favoriteDataArrayList=getFavoriteDataList();
        FavoriteData[] list=new FavoriteData[favoriteDataArrayList.size()];
        for(int i=0;i<favoriteDataArrayList.size();i++){
            list[i]=favoriteDataArrayList.get(i);
        }
        RecyclerView recyclerView=findViewById(R.id.favorites_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        favoriteAdapter = new FavoriteAdapter(list, MainActivity.this);
        recyclerView.setAdapter(favoriteAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(favoritesCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    ItemTouchHelper.SimpleCallback favoritesCallBack=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN ,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            Integer fromPosition=viewHolder.getAdapterPosition();
            Integer toPosition=target.getAdapterPosition();

            ArrayList<FavoriteData> favoriteDataList = getFavoriteDataList();
            Collections.swap(favoriteDataList,fromPosition,toPosition);
            setFavoriteDataList(favoriteDataList);
            recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            ArrayList<FavoriteData> favoriteDataList = getFavoriteDataList();
            favoriteDataList.remove(viewHolder.getAdapterPosition());
            setFavoriteDataList(favoriteDataList);
            favoriteAdapter.notifyDataSetChanged();
            setInitialFavorites();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            



            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }
    };

    ItemTouchHelper.SimpleCallback portfolioCallBack=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            Integer fromPosition=viewHolder.getAdapterPosition();
            Integer toPosition=target.getAdapterPosition();

            ArrayList<PortfolioData> portfolioDataList = MyTools.getPortfolioDataList(MainActivity.this);
            Collections.swap(portfolioDataList,fromPosition,toPosition);
            MyTools.setPortfolioDataList(portfolioDataList,MainActivity.this);
            recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

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

    private void setInitialPortfolio(){
        TextView balanceView = findViewById(R.id.portfolio_balance);
        TextView cashView = findViewById(R.id.portfolio_cash);
        Float totalCashBack=0.f;
        balanceView.setText("$"+floatFormat(MyTools.getBalance(MainActivity.this)));
        ArrayList<PortfolioData> portfolioDataArrayList=MyTools.getPortfolioDataList(MainActivity.this);
        PortfolioData[] list=new PortfolioData[portfolioDataArrayList.size()];
        for(int i=0;i<portfolioDataArrayList.size();i++){
            PortfolioData portfolioData=portfolioDataArrayList.get(i);
            list[i]=portfolioData;
            totalCashBack+=portfolioData.getShares()*portfolioData.getAvgPrice();
        }
        cashView.setText(floatFormat(totalCashBack+MyTools.getBalance(MainActivity.this)));
        RecyclerView recyclerView=findViewById(R.id.portfolio_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        PortfolioAdapter portfolioAdapter = new PortfolioAdapter(list, MainActivity.this);
        recyclerView.setAdapter(portfolioAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(portfolioCallBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }

    public String floatFormat(Float f){
        return String. format("%.2f", f);
    }

    public void setFooter(){
        TextView footerView = findViewById(R.id.main_footer);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finnhub = new Intent(Intent.ACTION_VIEW);
                finnhub.setData(Uri.parse("https://finnhub.io"));
                startActivity(finnhub);
            }
        });

    }


}