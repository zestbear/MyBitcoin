package com.zestbear.bitcoin.mybitcoin.domain.Candle;

public class DayCandle {

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
    private double prev_closing_price;
    private double change_price;
    private double change_rate;
    private double converted_trade_price;

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

    public double getPrev_closing_price() {
        return prev_closing_price;
    }

    public void setPrev_closing_price(double prev_closing_price) {
        this.prev_closing_price = prev_closing_price;
    }

    public double getChange_price() {
        return change_price;
    }

    public void setChange_price(double change_price) {
        this.change_price = change_price;
    }

    public double getChange_rate() {
        return change_rate;
    }

    public void setChange_rate(double change_rate) {
        this.change_rate = change_rate;
    }

    public double getConverted_trade_price() {
        return converted_trade_price;
    }

    public void setConverted_trade_price(double converted_trade_price) {
        this.converted_trade_price = converted_trade_price;
    }
}
