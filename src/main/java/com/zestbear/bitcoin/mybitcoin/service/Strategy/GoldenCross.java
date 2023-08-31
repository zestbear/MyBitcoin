package com.zestbear.bitcoin.mybitcoin.service.Strategy;

import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle.MinuteCandleService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GoldenCross {

    private final MinuteCandleService minuteCandleService;

    public GoldenCross(MinuteCandleService minuteCandleService) {
        this.minuteCandleService = minuteCandleService;
    }

    public Boolean isGoldenCross(String coinSymbol) {
        Map<String, Map<Integer, Map<String, Double>>> movingAverageLine = null;
        try {
            movingAverageLine = minuteCandleService.getMovingAverageLine().get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (movingAverageLine.containsKey(coinSymbol)) {
            Map<Integer, Map<String, Double>> coinTrace = movingAverageLine.get(coinSymbol);
            if (coinTrace.get(5).get("current") > coinTrace.get(10).get("current")) {
                return coinTrace.get(5).get("before") < coinTrace.get(10).get("before");
            }
        }

        return false;
    }
}
