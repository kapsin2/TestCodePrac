package com.example.repository;


import com.example.entity.TradeReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeReqRepository extends JpaRepository<TradeReq,Long> {
}
