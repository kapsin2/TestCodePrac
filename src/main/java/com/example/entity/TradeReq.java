package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
public class TradeReq {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private Long produectId;


    public TradeReq(Long clientId, Long sellerId,Long produectId) {
        this.clientId = clientId;
        this.sellerId = sellerId;
        this.produectId = produectId;
    }
}