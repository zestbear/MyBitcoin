package com.zestbear.bitcoin.mybitcoin.service.UpbitAPI;

import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Account.AccountAPI;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle.CurrentValueAPI;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle.MinuteCandleAPI;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class APIService {

    private static final Logger logger = LoggerFactory.getLogger(APIService.class);

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
            updateAllData();
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAllData() throws IOException, ExecutionException, InterruptedException {
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                accountAPI.getAccountsAPI();
            } catch (ExecutionException | InterruptedException e) {
                logger.error("Error updating account data", e);
                throw new RuntimeException(e);
            }
        });
        future1.get();

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            try {
                currentValueAPI.getCurrentValueAPI();
            } catch (IOException | ExecutionException | InterruptedException e) {
                logger.error("Error updating current value data", e);
                throw new RuntimeException(e);
            }
        });
        future2.get();

        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> {
            try {
                minuteCandleAPI.getMinuteCandleAPI();
            } catch (ExecutionException | InterruptedException e) {
                logger.error("Error updating minute candle data", e);
                throw new RuntimeException(e);
            }
        });
        future3.get();
    }
}
