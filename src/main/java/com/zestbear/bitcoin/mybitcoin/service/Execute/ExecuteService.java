package com.zestbear.bitcoin.mybitcoin.service.Execute;

import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.APIService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class ExecuteService {

    private final APIService apiService;
    private final OrderService orderService;

    @Scheduled(fixedDelay = 600000)
    public void execute() throws IOException, NoSuchAlgorithmException, ExecutionException, InterruptedException {

        apiService.updateAllData();
        orderService.sendOrder();

    }
}