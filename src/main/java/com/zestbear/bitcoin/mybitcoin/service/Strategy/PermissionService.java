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

        if (movingAverageLineService.OrdSide(coinSymbol).equals("bid")) {
            if (!accounts.containsKey(coinSymbol)) {
                return "bid";
            } else {
                return "stay";
            }
        } else if (movingAverageLineService.OrdSide(coinSymbol).equals("ask")) {
            if (accounts.containsKey(coinSymbol)) {
                return "ask";
            } else {
                return "stay";
            }
        }

        return "stay";
    }
}
