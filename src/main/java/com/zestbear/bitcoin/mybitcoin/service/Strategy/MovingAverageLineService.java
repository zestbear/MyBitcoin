package com.zestbear.bitcoin.mybitcoin.service.Strategy;

import com.zestbear.bitcoin.mybitcoin.service.Candle.CurrentValueService;
import com.zestbear.bitcoin.mybitcoin.service.Candle.DayMovingAverageLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MovingAverageLineService {

    private final DayMovingAverageLineService dayMovingAverageLineService;
    private final CurrentValueService currentValueService;

    public String OrdSide(String coinSymbol) throws IOException {

        Map<String, Double> latestPrice = currentValueService.getCurrent();
        Map<String, Map<Integer, Double>> movingAverageLine = dayMovingAverageLineService.getMovingAverageLine();

        double latest = latestPrice.get(coinSymbol);

        Map<Integer, Double> map = movingAverageLine.get(coinSymbol);

        if (map.containsKey(5) && map.containsKey(10) && map.containsKey(21)) {
            double value5 = map.get(5);
            double value10 = map.get(10);
            double value21 = map.get(21);

            if (value5 > latest && value10 > latest && value21 > latest) {
                return "bid";
            } else if (value5 < latest && value10 < latest && value21 < latest) {
                return "ask";
            }
        }

        return "default";
    }
}
