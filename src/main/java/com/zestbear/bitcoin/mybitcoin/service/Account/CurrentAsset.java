package com.zestbear.bitcoin.mybitcoin.service.Account;

import com.zestbear.bitcoin.mybitcoin.service.Candle.CurrentValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CurrentAsset {

    /*
    현재 평가 자산을 KRW로 반환
     */

    private double currentKRW;

    private final Map<String, Double> ValueforEach = new HashMap<>();

    private final AccountService accountService;
    private final CurrentValueService currentValueService;

    public double getCurrentKRWAsset() throws IOException {

        double sum = 0;

        Map<String, Map<String, Object>> accounts = accountService.getAccounts();
        Map<String, Double> currents = currentValueService.getCurrent();

        for (String Symbol : accounts.keySet()) {
            String marketSymbol = "KRW-" + Symbol;
            Map<String, Object> symbolMap = accounts.get(Symbol);
            if (symbolMap.containsKey("balance")) {
                String balanceStr = (String) symbolMap.get("balance");
                double balance = Double.parseDouble(balanceStr);
                if (currents.containsKey(marketSymbol)) {
                    double currentPrice = currents.get(marketSymbol);
                    double totalValue = balance * currentPrice;
                    ValueforEach.put(Symbol, totalValue);
                    sum += totalValue;
                }
            }
        }

        String KRWCurrent = (String) accounts.get("KRW").get("balance");
        double KRWBalance = Double.parseDouble(KRWCurrent);
        sum += KRWBalance;

        currentKRW = KRWBalance;

        return sum;
    }

    public double getCurrentKRW() {
        return currentKRW;
    }

    public Map<String, Double> getValueforEach() {
        return ValueforEach;
    }

}
