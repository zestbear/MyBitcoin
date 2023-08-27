package com.zestbear.bitcoin.mybitcoin.service.Strategy;

import com.zestbear.bitcoin.mybitcoin.service.Account.AccountService;
import com.zestbear.bitcoin.mybitcoin.service.Candle.CurrentValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ProportionService {

    private final AccountService accountService;
    private final CurrentValueService currentValueService;

    public Map<String, Double> getProportion() throws IOException {

        /*
        최대 비중 설정 역할
        BTC ETH ADA DOT MATIC
        40% 40% 10% 5%  5%
         */

        Map<String, Double> proportions = new HashMap<>();

        Map<String, Map<String, Object>> accounts = accountService.getAccounts();
        Map<String, Double> currents = currentValueService.getCurrent();

        for (String key : currents.keySet()) {
            System.out.println(key + " : " + currents.get(key));
        }

        double sum = 0;

        for (String Symbol : accounts.keySet()) {
            String marketSymbol = "KRW-" + Symbol;
            Map<String, Object> symbolMap = accounts.get(Symbol);
            if (symbolMap.containsKey("balance")) {
                String balanceStr = (String) symbolMap.get("balance");
                double balance = Double.parseDouble(balanceStr);
                if (currents.containsKey(marketSymbol)) {
                    double currentPrice = currents.get(marketSymbol);

                    double totalValue = balance * currentPrice;
                    sum += totalValue;
                }
            }
        }

        String KRWCurrent = (String) accounts.get("KRW").get("balance");
        double KRWBalance = Double.parseDouble(KRWCurrent);
        sum += KRWBalance;

        // 총 평가 자산 출력
        System.out.println(sum);


        return proportions;
    }
}
