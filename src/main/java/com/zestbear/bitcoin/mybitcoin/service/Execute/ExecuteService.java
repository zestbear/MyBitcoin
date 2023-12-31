package com.zestbear.bitcoin.mybitcoin.service.Execute;

import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.APIService;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Account.CurrentAsset;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Order.CancelOrderAPI;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Order.OrderListAPI;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class ExecuteService {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteService.class);

    private final APIService apiService;
    private final OrderService orderService;
    private final CancelOrderAPI cancelOrderAPI;
    private final OrderListAPI orderListAPI;

    private final CurrentAsset currentAsset;

    @Scheduled(fixedDelay = 30000)
    public void execute() throws IOException, NoSuchAlgorithmException, ExecutionException, InterruptedException {

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                orderListAPI.getOrders();
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                logger.error("Error updating order list data", e);
                throw new RuntimeException(e);
            }
        });
        future.join();

        cancelOrderAPI.cancelAll(); // 미체결 주문들을 모두 취소 (오류 방지)

        apiService.updateAllData(); // 모든 API들의 정보를 업데이트
        currentAsset.getAsset();
        orderService.sendOrder();   // 매매 조건에 맞으면 매매
    }
}