package com.zestbear.bitcoin.mybitcoin.domain.Account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderListData {

    private String uuid;
    private String side;
    private String price;
    private String ord_type;
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
    private Integer trades_count;
}
