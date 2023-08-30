package com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Account;

import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle.CurrentValueAPI;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrentAsset {

    private final AccountAPI accountAPI;
    private final CurrentValueAPI currentValueAPI;

    private double cashKRW;                                         // 현금 보유량 (KRW)
    private final Map<String, Double> eachValue = new HashMap<>();  // 보유 중인 코인들의 평가 금액 (시장가 * 보유 수량)

    public CurrentAsset(AccountAPI accountAPI, CurrentValueAPI currentValueAPI) {
        this.accountAPI = accountAPI;
        this.currentValueAPI = currentValueAPI;
    }

    public double getAssetSum() throws IOException {    // 자산의 총 평가 금액

        double sum = 0;
        Map<String, Map<String, Object>> accounts = accountAPI.getAccountData();
        Map<String, Double> currents = currentValueAPI.getCurrentValues();

        for (String Symbol : accounts.keySet()) {
            String marketSymbol = "KRW-" + Symbol;
            Map<String, Object> symbolMap = accounts.get(Symbol);
            if (symbolMap.containsKey("balance")) {
                String balanceStr = (String) symbolMap.get("balance");
                double balance = Double.parseDouble(balanceStr);
                if (currents.containsKey(marketSymbol)) {
                    double currentPrice = currents.get(marketSymbol);
                    double totalValue = balance * currentPrice;
                    eachValue.put(Symbol, totalValue);
                    sum += totalValue;
                }
            }
        }
        String KRWCurrent = (String) accounts.get("KRW").get("balance");
        double KRWBalance = Double.parseDouble(KRWCurrent);
        sum += KRWBalance;
        cashKRW = KRWBalance;

        return sum;
    }

    public double getCashKRW() {
        return cashKRW;
    }

    public Map<String, Double> getEachValue() {
        return eachValue;
    }
}
