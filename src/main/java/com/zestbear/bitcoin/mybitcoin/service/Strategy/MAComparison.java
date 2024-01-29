package com.zestbear.bitcoin.mybitcoin.service.Strategy;

import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle.MinuteCandleService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MAComparison {

    private final MinuteCandleService minuteCandleService;

    public MAComparison(MinuteCandleService minuteCandleService) {
        this.minuteCandleService = minuteCandleService;
    }

    public String isMATiming(String coinSymbol) {
        Map<String, Map<Integer, Double>> movingAverageLine = null;
        try {
            movingAverageLine = minuteCandleService.getMovingAverageLine().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert movingAverageLine != null;
        Map<Integer, Double> comparisonMap = movingAverageLine.get(coinSymbol);

        if (comparisonMap.get(50) < comparisonMap.get(200)) {
            return "bid";
        } else if (comparisonMap.get(50) > comparisonMap.get(200)) {
            return "ask";
        } else {
            return "stay";
        }
    }
}
