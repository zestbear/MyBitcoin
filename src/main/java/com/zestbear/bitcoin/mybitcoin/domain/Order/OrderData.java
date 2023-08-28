package com.zestbear.bitcoin.mybitcoin.domain.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData {

    private String uuid;
    private String side;
    private String ord_type;
    private String price;
    private String state;
    private String market;
    private String created_at;
    private String volume;
    private String remaining_volume;
    private String reserved_fee;
    private String remaining_fee;
    private String paid_fee;
    private String locked;
    private String executed_volume;
    private int trades_count;

}
