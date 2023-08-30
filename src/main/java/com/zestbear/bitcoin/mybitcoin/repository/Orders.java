package com.zestbear.bitcoin.mybitcoin.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Setter;

@Setter
@Entity
public class Orders {

    @Id
    private String uuid;

    @Column
    private String market;

    @Column
    private String side;

    @Column
    private String volume;

    @Column
    private String price;

    @Column
    private String created_at;

}
