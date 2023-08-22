package com.zestbear.bitcoin.mybitcoin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class CandleData {
    @JsonProperty("market")
    private String market;

    @JsonProperty("candle_date_time_utc")
    private String candleDateTimeUtc;

    @JsonProperty("candle_date_time_kst")
    private String candleDateTimeKst;

    @JsonProperty("opening_price")
    private double openingPrice;

    @JsonProperty("high_price")
    private double highPrice;

    @JsonProperty("low_price")
    private double lowPrice;

    @JsonProperty("trade_price")
    private double tradePrice;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("candle_acc_trade_price")
    private double candleAccTradePrice;

    @JsonProperty("candle_acc_trade_volume")
    private double candleAccTradeVolume;

    @JsonProperty("unit")
    private int unit;

    @Override
    public String toString() {
        return "CandleData{" +
                "market='" + market + '\'' +
                ", candleDateTimeUtc='" + candleDateTimeUtc + '\'' +
                ", candleDateTimeKst='" + candleDateTimeKst + '\'' +
                ", openingPrice=" + openingPrice +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", tradePrice=" + tradePrice +
                ", timestamp=" + timestamp +
                ", candleAccTradePrice=" + candleAccTradePrice +
                ", candleAccTradeVolume=" + candleAccTradeVolume +
                ", unit=" + unit +
                '}';
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCandleDateTimeUtc() {
        return candleDateTimeUtc;
    }

    public void setCandleDateTimeUtc(String candleDateTimeUtc) {
        this.candleDateTimeUtc = candleDateTimeUtc;
    }

    public String getCandleDateTimeKst() {
        return candleDateTimeKst;
    }

    public void setCandleDateTimeKst(String candleDateTimeKst) {
        this.candleDateTimeKst = candleDateTimeKst;
    }

    public double getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(double openingPrice) {
        this.openingPrice = openingPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getCandleAccTradePrice() {
        return candleAccTradePrice;
    }

    public void setCandleAccTradePrice(double candleAccTradePrice) {
        this.candleAccTradePrice = candleAccTradePrice;
    }

    public double getCandleAccTradeVolume() {
        return candleAccTradeVolume;
    }

    public void setCandleAccTradeVolume(double candleAccTradeVolume) {
        this.candleAccTradeVolume = candleAccTradeVolume;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
}
