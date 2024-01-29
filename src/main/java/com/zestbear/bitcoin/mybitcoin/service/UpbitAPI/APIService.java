package com.zestbear.bitcoin.mybitcoin.service.UpbitAPI;

import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Account.AccountAPI;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle.CurrentValueAPI;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle.MinuteCandleAPI;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class APIService {

    private final AccountAPI accountAPI;
    private final CurrentValueAPI currentValueAPI;
    private final MinuteCandleAPI minuteCandleAPI;

    @Autowired
    public APIService(AccountAPI accountAPI, CurrentValueAPI currentValueAPI, MinuteCandleAPI minuteCandleAPI) {
        this.accountAPI = accountAPI;
        this.currentValueAPI = currentValueAPI;
        this.minuteCandleAPI = minuteCandleAPI;
    }

    @PostConstruct
    public void init() {
        try {
            updateAccount();
            updateCurrentValue();
            updateMinuteCandle();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        내 계좌의 정보를 불러오는 API
     */
    public synchronized void updateAccount() {
        try {
            accountAPI.getAccountsAPI();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error updating account data", e);
            throw new RuntimeException(e);
        }
    }

    /*
        현재 시세를 얻어오는 API
     */
    public synchronized void updateCurrentValue() {
        try {
            currentValueAPI.getCurrentValueAPI();
        } catch (IOException | ExecutionException | InterruptedException e) {
            log.error("Error updating current value data", e);
            throw new RuntimeException(e);
        }
    }

    /*
        이전 200개의 분봉을 얻어오는 API
     */
    public synchronized void updateMinuteCandle() {
        try {
            minuteCandleAPI.getMinuteCandleAPI();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error updating minute candle data", e);
            throw new RuntimeException(e);
        }
    }
}
