package com.zestbear.bitcoin.mybitcoin.service.Strategy;

import com.zestbear.bitcoin.mybitcoin.service.Account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PermissionService {

    private final MovingAverageLineService movingAverageLineService;
    private final AccountService accountService;

    public String isSafe(String coinSymbol) throws IOException {

        Map<String, Map<String, Object>> accounts = accountService.getAccounts();

        String ordSide = movingAverageLineService.OrdSide(coinSymbol);
        if (ordSide.equals("bid")) {
            if (!accounts.containsKey(coinSymbol)) {
                System.out.println("BID for " + coinSymbol);
                return "bid";
            } else {
                return "stay";
            }
        } else if (ordSide.equals("ask")) {
            if (accounts.containsKey(coinSymbol)) {
                System.out.println("ASK for " + coinSymbol);
                return "ask";
            } else {
                return "stay";
            }
        }

        return "stay";
    }
}
