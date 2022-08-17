package com.skyrain.stock.news;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.skyrain.stock.R;

import java.util.Calendar;
import java.util.Date;

public class NewsDialog extends AppCompatDialogFragment {

    private String title;
    private String date;
    private String headline;
    private String content;
    private String webUrl;

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public NewsDialog(String title, String date, String headline, String content, String webUrl) {
        this.title = title;
        this.date = date;
        this.headline = headline;
        this.content = content;
        this.webUrl = webUrl;
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View inflate = layoutInflater.inflate(R.layout.news_dialog,null);
        builder.setView(inflate);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView titleView = (TextView) dialog.findViewById(R.id.dialog_title);
        TextView dateView = dialog.findViewById(R.id.dialog_date);
        TextView headlineView = dialog.findViewById(R.id.dialog_headline);
        TextView contentView = dialog.findViewById(R.id.dialog_content);
        ImageView googleView = dialog.findViewById(R.id.dialog_google);
        ImageView twitterView = dialog.findViewById(R.id.dialog_twitter);
        ImageView facebookView = dialog.findViewById(R.id.dialog_facebook);

        googleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getWebUrl()));
                startActivity(intent);
            }
        });
        twitterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tweet = new Intent(Intent.ACTION_VIEW);
                tweet.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + Uri.encode(getHeadline())+"&url="+Uri.encode(getWebUrl())));
                startActivity(tweet);
            }
        });
        facebookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tweet = new Intent(Intent.ACTION_VIEW);
                tweet.setData(Uri.parse("https://www.facebook.com/sharer/sharer.php?u="+Uri.encode(getWebUrl())+"&src=sdkpreparse"));
                startActivity(tweet);
            }
        });



        titleView.setText(getTitle());
        dateView.setText(getFormattedDate(getDate()));
        headlineView.setText(getHeadline());
        contentView.setText(getContent());

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    private String getFormattedDate(String str){
        Long timestamp=Long.parseLong(str)*1000;
        Date date = new Date(timestamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getMonth(calendar.get(Calendar.MONTH))+' '+calendar.get(Calendar.DAY_OF_MONTH)+", "+calendar.get(Calendar.YEAR);
    }

    private String getMonth(Integer i){
        switch (i) {
            case 0:
                return "January";

            case 1:
                return "February";

            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return null;
    }
}
