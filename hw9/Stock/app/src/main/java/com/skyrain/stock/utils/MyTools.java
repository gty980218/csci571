package com.skyrain.stock.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DateFormatSymbols;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyrain.stock.favorite.FavoriteData;
import com.skyrain.stock.portfolio.PortfolioData;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyTools {

    public static String getFormatDate(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        String[] tmp=strDate.split("-");
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String res=tmp[2]+" "+months[Integer.parseInt(tmp[1])]+' '+tmp[0];
        return res;
    }

    public static ArrayList<PortfolioData> getPortfolioDataList(Context context){
        ArrayList<PortfolioData>  portfolioDataList=null;
        SharedPreferences sharedPreferences = context.getSharedPreferences("mp", 0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("portfolioDataList", null);
        Type type = new TypeToken<ArrayList<PortfolioData>>() {}.getType();
        portfolioDataList = gson.fromJson(json, type);
        if(portfolioDataList==null){
            portfolioDataList=new ArrayList<>();
        }
        return portfolioDataList;
    }

    public static void setPortfolioDataList(ArrayList<PortfolioData> portfolioDataList,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("mp", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(portfolioDataList);
        editor.putString("portfolioDataList",json);
        editor.apply();
    }

    public static boolean isTickerInPortfolio(String ticker,ArrayList<PortfolioData> portfolioDataList){
        if(portfolioDataList==null || portfolioDataList.size()==0){
            return false;
        }
        for(PortfolioData pd: portfolioDataList){
            if(pd.getTicker().equals(ticker)){
                return true;
            }
        }
        return false;
    }

    public static PortfolioData getPortfolioDataByTicker(String ticker,ArrayList<PortfolioData> portfolioDataList){
        for(PortfolioData pd:portfolioDataList){
            if(pd.getTicker().equals(ticker)){
                return pd;
            }
        }
        return null;
    }

    public static void setBalance(Context context,Float amount){
        SharedPreferences mPrefs = context.getSharedPreferences("mp",0);
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putFloat("balance",amount);
        edit.apply();
    }

    public static Float getBalance(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences("mp",0);
        return mPrefs.getFloat("balance",25000);
    }

    public static String floatFormat(Float f){
        return String. format("%.2f", f);
    }


}
