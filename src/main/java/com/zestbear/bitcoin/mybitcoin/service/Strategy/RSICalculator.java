package com.zestbear.bitcoin.mybitcoin.service.Strategy;

import com.zestbear.bitcoin.mybitcoin.dto.MinuteCandleDto;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle.MinuteCandleRSI;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RSICalculator {

    private final MinuteCandleRSI minuteCandleRSI;

    public RSICalculator(MinuteCandleRSI minuteCandleRSI) {
        this.minuteCandleRSI = minuteCandleRSI;
    }

    public Double getCalculatedRSI(String coinSymbol) {

        Map<String, List<MinuteCandleDto>> minuteCandle = minuteCandleRSI.getForRSI();

        if (!minuteCandle.containsKey(coinSymbol)) {
            throw new IllegalArgumentException("No data available for " + coinSymbol);
        }

        List<MinuteCandleDto> candleDataList = minuteCandle.get(coinSymbol);

        double zero = 0;
        List<Double> upList = new ArrayList<>();
        List<Double> downList = new ArrayList<>();
        for (int i = 0; i < candleDataList.size() - 1; i++) {
            double delta = candleDataList.get(i + 1).getTrade_price() - candleDataList.get(i).getTrade_price();

            if (delta > 0) {
                upList.add(delta);
                downList.add(zero);
            } else if (delta < 0) {
                upList.add(zero);
                downList.add(delta * (-1));
            } else {
                upList.add(zero);
                downList.add(zero);
            }
        }

        double Day = 14;
        double a = (double) 1 / (1 + (Day - 1));

        double upEMA = 0;
        if (!upList.isEmpty()) {
            upEMA = upList.get(0);
            if (upList.size() > 1) {
                for (int i = 1; i < upList.size(); i++) {
                    upEMA = (upList.get(i) * a) + (upEMA * (1 - a));
                }
            }
        }

        double downEMA = 0;
        if (!downList.isEmpty()) {
            downEMA = downList.get(0);
            if (downList.size() > 1) {
                for (int i = 1; i < downList.size(); i++) {
                    downEMA = (downList.get(i) * a) + (downEMA * (1 - a));
                }
            }
        }

        double AU = upEMA;
        double AD = downEMA;
        double RS = AU / AD;
        double RSI = 100 - (100 / (1 + RS));

        return RSI;
    }
}