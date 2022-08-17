package com.skyrain.stock.portfolio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyrain.stock.R;
import com.skyrain.stock.SubActivity;
import com.skyrain.stock.favorite.FavoriteData;
import com.skyrain.stock.utils.MyTools;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TradeDialog extends AppCompatDialogFragment {

    private String ticker;
    private String name;
    private Integer shares;
    private Float price;
    private Float totalPrice;
    private Context context;

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TradeDialog(String ticker, String name, Float price,Context context) {
        this.ticker = ticker;
        this.name = name;
        this.price = price;
        this.context=context;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Integer getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = shares;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View inflate = layoutInflater.inflate(R.layout.trade_dialog,null);
        builder.setView(inflate);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView titleView = dialog.findViewById(R.id.trade_title);
        TextView priceView = dialog.findViewById(R.id.trade_price);
        TextView tickerView = dialog.findViewById(R.id.trade_ticker);
        TextView sharesView = dialog.findViewById(R.id.trade_shares);
        TextView balanceView = dialog.findViewById(R.id.trade_balance);
        EditText getSharesText = dialog.findViewById(R.id.trade_get_shares);
        TextView totalPriceView = dialog.findViewById(R.id.trade_total_price);
        Button btnBuy = dialog.findViewById(R.id.trade_buy);
        Button btnSell = dialog.findViewById(R.id.trade_sell);

        titleView.setText(getName());
        priceView.setText(getPrice().toString());
        tickerView.setText(getTicker());
        balanceView.setText(floatFormat(MyTools.getBalance(context)));







        getSharesText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str=getSharesText.getText().toString();
                if(str.equals("")){
                    str="0";
                }
                Integer shares = Integer.parseInt(str);
                sharesView.setText(shares.toString());
                totalPrice=shares*getPrice();
                totalPriceView.setText(String.format("%.02f",totalPrice));
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                //set data
                Integer buyShares=0;
                if(!getSharesText.getText().toString().equals("")){
                    buyShares=Integer.parseInt(getSharesText.getText().toString());
                };
                System.out.println(buyShares);
                if(buyShares==null || buyShares<=0){
                    Toast.makeText(context,"Cannot buy non-positive shares",Toast.LENGTH_SHORT).show();
                }else{
                    if(MyTools.getBalance(context)<(getPrice()*buyShares)){
                        Toast.makeText(context,"Not enough money to buy",Toast.LENGTH_SHORT).show();
                    }else{
                        ArrayList<PortfolioData> portfolioDataList = MyTools.getPortfolioDataList(context);
                        PortfolioData portfolioData=null;
                        if(MyTools.isTickerInPortfolio(getTicker(),portfolioDataList)){
                            portfolioData=MyTools.getPortfolioDataByTicker(getTicker(),portfolioDataList);
                            if(portfolioData!=null){
                                portfolioData.setShares(portfolioData.getShares()+buyShares);
                                portfolioData.setAvgPrice(getPrice(),buyShares);
                            }
                        }else{
                            portfolioData = new PortfolioData(getTicker(), buyShares, getPrice());
                            portfolioDataList.add(portfolioData);
                        }
                        MyTools.setBalance(context,MyTools.getBalance(context)-buyShares*getPrice());

                        //set subactivity portfolio
                        MyTools.setPortfolioDataList(portfolioDataList,context);
                        SubActivity parentFragment = (SubActivity) TradeDialog.this.getActivity();
                        parentFragment.setPortfolio();;

                        //set trade dialog success
                        setShares(Integer.parseInt(getSharesText.getText().toString()));
                        TradeSuccessDialog tradeSuccessDialog = new TradeSuccessDialog(getShares(),getTicker(),"bought");
                        tradeSuccessDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "Trade Success Dialog");
                        dialog.dismiss();
                    }
                }
            }
        });
        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set data
                Integer sellShares=0;
                if(!getSharesText.getText().toString().equals("")){
                    sellShares=Integer.parseInt(getSharesText.getText().toString());
                };
                if(sellShares==null ||sellShares<=0){
                    Toast.makeText(context,"Cannot sell non-positive shares",Toast.LENGTH_SHORT).show();
                }else{
                    ArrayList<PortfolioData> portfolioDataList = MyTools.getPortfolioDataList(context);
                    PortfolioData portfolioData=null;
                    if(MyTools.isTickerInPortfolio(getTicker(),portfolioDataList)){
                        portfolioData=MyTools.getPortfolioDataByTicker(getTicker(),portfolioDataList);
                        if(sellShares>portfolioData.getShares()){
                            //error:
                            Toast.makeText(context,"Not enough shares to sell",Toast.LENGTH_SHORT).show();
                        }else{
                            portfolioData.setShares(portfolioData.getShares()-sellShares);
                            if(portfolioData.getShares()<=0){
                                portfolioDataList.remove(portfolioData);
                            }
                            MyTools.setBalance(context,MyTools.getBalance(context)+sellShares*getPrice());
                            //set subactivity portfolio
                            MyTools.setPortfolioDataList(portfolioDataList,context);
                            SubActivity parentFragment = (SubActivity) TradeDialog.this.getActivity();
                            parentFragment.setPortfolio();;
                            //set trade dialog success
                            setShares(Integer.parseInt(getSharesText.getText().toString()));
                            TradeSuccessDialog tradeSuccessDialog = new TradeSuccessDialog(getShares(),getTicker(),"sold");
                            tradeSuccessDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "Trade Success Dialog");
                            dialog.dismiss();
                        }
                    }else{
                        //error: no shares hold with this ticker
                        Toast.makeText(context,"Not shares hold",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return dialog;
    }
    public String floatFormat(Float f){
        return String. format("%.2f", f);
    }
}
