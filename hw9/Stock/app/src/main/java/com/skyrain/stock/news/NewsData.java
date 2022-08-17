package com.skyrain.stock.news;

public class NewsData {
    private String source;
    private String date;//timestamp
    private String headline;
    private String content;
    private String webUrl;
    private String url;//image url
    private String title;

    public NewsData(String source, String date, String headline, String content, String webUrl, String url, String title) {
        this.source = source;
        this.date = date;
        this.headline = headline;
        this.content = content;
        this.webUrl = webUrl;
        this.url = url;
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
