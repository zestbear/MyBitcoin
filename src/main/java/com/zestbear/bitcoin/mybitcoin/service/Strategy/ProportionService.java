package com.zestbear.bitcoin.mybitcoin.service.Strategy;

import com.zestbear.bitcoin.mybitcoin.service.Account.AccountService;
import com.zestbear.bitcoin.mybitcoin.service.Account.CurrentAsset;
import com.zestbear.bitcoin.mybitcoin.service.Candle.CurrentValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ProportionService {

    /*
    계좌에 있는 종목들(KRW 포함)의 비율 반환
     */

    private final AccountService accountService;
    private final CurrentValueService currentValueService;
    private final CurrentAsset currentAsset;

    public Map<String, Double> getProportion() throws IOException {

        /*
        최대 비중 반환 역할
        BTC ETH ADA DOT MATIC
        40% 40% 10% 5%  5%
         */

        Map<String, Double> proportions = new HashMap<>();

        Map<String, Map<String, Object>> accounts = accountService.getAccounts();
        Map<String, Double> currents = currentValueService.getCurrent();
        double current = currentAsset.getCurrentKRWAsset();
        double currentKRW = currentAsset.getCurrentKRW();

//        for (String key : currents.keySet()) {
//            System.out.println(key + " : " + currents.get(key));
//        }

        for (String Symbol : accounts.keySet()) {
            String marketSymbol = "KRW-" + Symbol;
            Map<String, Object> symbolMap = accounts.get(Symbol);
            if (symbolMap.containsKey("balance")) {
                String balanceStr = (String) symbolMap.get("balance");
                double balance = Double.parseDouble(balanceStr);
                if (currents.containsKey(marketSymbol)) {
                    double currentPrice = currents.get(marketSymbol);
                    double totalValue = balance * currentPrice;
                    double percent = totalValue / current * 100;
//                    System.out.println(Symbol + " : " + percent);
                    proportions.put(Symbol, percent);
                }
            }
        }

        double percentofKRW = currentKRW / current * 100;
        proportions.put("KRW", percentofKRW);
//        System.out.println("KRW : " + percentofKRW);


        return proportions;
    }

}
