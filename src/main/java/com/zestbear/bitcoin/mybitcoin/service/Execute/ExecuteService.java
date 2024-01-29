package com.zestbear.bitcoin.mybitcoin.service.Execute;

import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.APIService;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Account.CurrentAsset;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Order.CancelOrderAPI;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Order.OrderListAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class ExecuteService {

    private final APIService apiService;
    private final OrderService orderService;
    private final CancelOrderAPI cancelOrderAPI;
    private final OrderListAPI orderListAPI;
    private final CurrentAsset currentAsset;

    public ExecuteService(APIService apiService, OrderService orderService, CancelOrderAPI cancelOrderAPI, OrderListAPI orderListAPI, CurrentAsset currentAsset) {
        this.apiService = apiService;
        this.orderService = orderService;
        this.cancelOrderAPI = cancelOrderAPI;
        this.orderListAPI = orderListAPI;
        this.currentAsset = currentAsset;
    }

    /*
        1. 미체결 주문들이 있다면 모두 불러오기
        2. 미체결 주문들 모두 주문 취소
        3. 모든 API로 모든 정보 새로고침
        4. 매매 실행
     */
    @Scheduled(fixedDelay = 30000)
    public synchronized void execute() throws IOException, NoSuchAlgorithmException {
        
        try {
            orderListAPI.getOrders(); // 미체결 주문 확인
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            log.error("Error updating order list data", e);
            throw new RuntimeException(e);
        }

        cancelOrderAPI.cancelAll(); // 미체결 주문들을 모두 취소 (오류 방지)

        apiService.init(); // 모든 API들의 정보를 업데이트
        currentAsset.getAsset();
        orderService.sendOrder();   // 매매 조건에 맞으면 매매
    }
}