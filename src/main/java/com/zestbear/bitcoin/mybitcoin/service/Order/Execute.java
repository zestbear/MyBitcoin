package com.zestbear.bitcoin.mybitcoin.service.Order;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.cache.CacheManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class Execute {

    private final OrderService orderService;
    private final CacheManager cacheManager;

    @Scheduled(fixedDelay = 5000)
    public void execute() {
        String[] coinSymbols = {"BTC", "ETH", "ADA", "DOT", "MATIC"};

        for (String sym : coinSymbols) {
            try {
//                System.out.println("Executing: " + sym);
                orderService.postOrder(sym);
            } catch (NoSuchAlgorithmException | IOException e) {
                System.err.println("Failed to post order for symbol: " + sym);
                e.printStackTrace();
            }
        }

        // Invalidate all caches every 5 seconds
        for (String cacheName : cacheManager.getCacheNames()) {
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        }
    }
}