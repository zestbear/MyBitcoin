package com.zestbear.bitcoin.mybitcoin.domain.Candle;

public class MinuteCandle {

    private String market;
    private String candle_date_time_utc;
    private String candle_date_time_kst;
    private double opening_price;
    private double high_price;
    private double low_price;
    private double trade_price;
    private long timestamp;
    private double candle_acc_trade_price;
    private double candle_acc_trade_volume;
    private int unit;

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCandle_date_time_utc() {
        return candle_date_time_utc;
    }

    public void setCandle_date_time_utc(String candle_date_time_utc) {
        this.candle_date_time_utc = candle_date_time_utc;
    }

    public String getCandle_date_time_kst() {
        return candle_date_time_kst;
    }

    public void setCandle_date_time_kst(String candle_date_time_kst) {
        this.candle_date_time_kst = candle_date_time_kst;
    }

    public double getOpening_price() {
        return opening_price;
    }

    public void setOpening_price(double opening_price) {
        this.opening_price = opening_price;
    }

    public double getHigh_price() {
        return high_price;
    }

    public void setHigh_price(double high_price) {
        this.high_price = high_price;
    }

    public double getLow_price() {
        return low_price;
    }

    public void setLow_price(double low_price) {
        this.low_price = low_price;
    }

    public double getTrade_price() {
        return trade_price;
    }

    public void setTrade_price(double trade_price) {
        this.trade_price = trade_price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getCandle_acc_trade_price() {
        return candle_acc_trade_price;
    }

    public void setCandle_acc_trade_price(double candle_acc_trade_price) {
        this.candle_acc_trade_price = candle_acc_trade_price;
    }

    public double getCandle_acc_trade_volume() {
        return candle_acc_trade_volume;
    }

    public void setCandle_acc_trade_volume(double candle_acc_trade_volume) {
        this.candle_acc_trade_volume = candle_acc_trade_volume;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }


//    @Override
//    public String toString() {
//        return "MinuteCandle{" +
//                "market='" + market + '\'' +
//                ", candleDateTimeUtc='" + candleDateTimeUtc + '\'' +
//                ", candleDateTimeKst='" + candleDateTimeKst + '\'' +
//                ", openingPrice=" + openingPrice +
//                ", highPrice=" + highPrice +
//                ", lowPrice=" + lowPrice +
//                ", tradePrice=" + tradePrice +
//                ", timestamp=" + timestamp +
//                ", candleAccTradePrice=" + candleAccTradePrice +
//                ", candleAccTradeVolume=" + candleAccTradeVolume +
//                ", unit=" + unit +
//                '}';
//    }
}
