package com.zestbear.bitcoin.mybitcoin.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinuteCandleDto {

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
}