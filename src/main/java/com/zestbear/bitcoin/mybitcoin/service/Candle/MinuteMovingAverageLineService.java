package com.zestbear.bitcoin.mybitcoin.service.Candle;

import com.zestbear.bitcoin.mybitcoin.domain.Candle.MinuteCandle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MinuteMovingAverageLineService {

    private final MinuteCandleService minuteCandleService;

    public Map<String, Double> getLatestPrice() {
        Map<String, List<MinuteCandle>> jsonMap = minuteCandleService.getMinuteCandleData();
        Map<String, Double> latestPrices = new HashMap<>();

        for (String coinSymbol : jsonMap.keySet()) {
            try {
                List<MinuteCandle> sortedMinuteCandles = new ArrayList<>(jsonMap.get(coinSymbol));
                sortedMinuteCandles.sort(Comparator.comparing(MinuteCandle::getCandle_date_time_kst));

                if (!sortedMinuteCandles.isEmpty()) {
                    // Get the latest price of the coin symbol
                    double latestPrice = sortedMinuteCandles.get(sortedMinuteCandles.size() - 1).getTrade_price();
                    latestPrices.put(coinSymbol, latestPrice);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return latestPrices;
    }
}

