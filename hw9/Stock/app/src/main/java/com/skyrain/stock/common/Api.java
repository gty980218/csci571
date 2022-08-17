package com.skyrain.stock.common;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skyrain.stock.MainActivity;

public class Api {

//    public static String getDescriptionRequest(String ticker, Context activity){
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        String url="https://stock-gty218-hw8.wl.r.appspot.com/getDescription/"+ticker;
//        final String[] result = {null};
//        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                result[0] =response;
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                result[0] ="Error on GetDescription Request!";
//            }
//        });
//        requestQueue.add(stringRequest);
//        return
//    }
}
