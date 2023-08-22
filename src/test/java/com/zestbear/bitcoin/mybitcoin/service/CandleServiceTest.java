package com.zestbear.bitcoin.mybitcoin.service;

import com.zestbear.bitcoin.mybitcoin.domain.CandleData;
import com.zestbear.bitcoin.mybitcoin.service.CandleService;

import java.util.List;
import java.util.Map;

public class CandleServiceTest {
    public static void main(String[] args) {
        CandleService service = new CandleService();
        String[] coinSymbols = {"BTC", "ETH", "XRP"};
        Map<String, List<CandleData>> result = service.getCandleData(coinSymbols);

        for (Map.Entry<String, List<CandleData>> entry : result.entrySet()) {
            System.out.println("Coin Symbol: " + entry.getKey());
            for (CandleData data : entry.getValue()) {
                System.out.println("    " + data);
                System.out.println();
            }
        }
    }
}
