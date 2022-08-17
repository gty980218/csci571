package com.skyrain.stock.peers;

public class PeersData {
    String ticker;

    public String getTicker() {
        return ticker;
    }

    public PeersData(String ticker) {
        this.ticker = ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
