package com.skyrain.stock.portfolio;

public class PortfolioData {
    private String ticker;
    private Integer shares;
    private Float avgPrice;

    public PortfolioData(String ticker, Integer shares, Float avgPrice) {
        this.ticker = ticker;
        this.shares = shares;
        this.avgPrice = avgPrice;
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

    public Float getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Float avgPrice,Integer shares) {
        this.avgPrice = (avgPrice*shares+this.shares*this.avgPrice)/(shares+this.shares);
    }
}
