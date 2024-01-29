package com.zestbear.bitcoin.mybitcoin.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int id;

    @Column(name = "order_name")
    private String uuid;       // 주문의 고유 아이디

    private String side;             // 주문 종류

    private String ord_type;         // 주문 방식

    private String price;            // 주문 당시 화폐 가격

    private String state;            // 주문 상태

    private String market;           // 마켓의 유일키

    private String created_at;       // 주문 생성 시간

    private String volume;           // 사용자가 입력한 주문 양

    private String remaining_volume; // 체결 후 남은 주문 양

    private String reserved_fee;     // 수수료로 예약된 비용

    private String remaining_fee;    // 남은 수수료

    private String paid_fee;         // 사용된 수수료

    private String locked;           // 거래에 사용중인 비용

    private String executed_volume;  // 체결된 양

    private int trades_count;        // 해당 주문에 걸린 체결 수

    void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
