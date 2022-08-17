package com.skyrain.stock.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.util.ArrayList;

public class JavaScriptInterface {
    String ticker;
    int flag=0;


    public JavaScriptInterface(String ticker,Integer flag) {
        this.ticker = ticker;
        this.flag=flag;
    }
    @JavascriptInterface
    public int getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @JavascriptInterface
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
