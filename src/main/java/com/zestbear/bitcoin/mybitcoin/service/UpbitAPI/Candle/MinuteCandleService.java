package com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle;

import com.zestbear.bitcoin.mybitcoin.domain.Candle.MinuteCandleData;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class MinuteCandleService {

    private final MinuteCandleAPI minuteCandleAPI;

    public MinuteCandleService(MinuteCandleAPI minuteCandleAPI) {
        this.minuteCandleAPI = minuteCandleAPI;
    }

    public CompletableFuture<Map<String, Map<Integer, Map<String, Double>>>> getMovingAverageLine() {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, List<MinuteCandleData>> jsonMap;
            try {
                jsonMap = minuteCandleAPI.getMinuteCandles();
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyMap();  // 오류가 발생하면 빈 맵을 반환
            }

            List<Integer> periods = Arrays.asList(5, 10);

            Map<String, Map<Integer, Map<String, Double>>> movingAveragesMap = new HashMap<>();

            for (String coinSymbol : jsonMap.keySet()) {
                try {
                    List<MinuteCandleData> sortedMinuteCandlesData = new ArrayList<>(jsonMap.get(coinSymbol));
                    sortedMinuteCandlesData.sort(Comparator.comparing(MinuteCandleData::getCandle_date_time_kst));

                    Map<Integer, Map<String, Double>> coinMovingAverages = new HashMap<>();

                    for (int period : periods) {
                        if (sortedMinuteCandlesData.size() >= period) { // 데이터가 충분한 경우만 처리

                            double sumCurrent = sortedMinuteCandlesData.subList(sortedMinuteCandlesData.size() - period,
                                            sortedMinuteCandlesData.size()).stream()
                                    .mapToDouble(MinuteCandleData::getTrade_price)
                                    .sum();

                            coinMovingAverages.put(period,new HashMap<>());
                            coinMovingAverages.get(period).put("current",sumCurrent / period);

                            if(sortedMinuteCandlesData.size() >=period+1){
                                double sum2MinAgo = sortedMinuteCandlesData.subList(sortedMinuteCandlesData.size() - period - 1,
                                                sortedMinuteCandlesData.size() - 1).stream()
                                        .mapToDouble(MinuteCandleData::getTrade_price)
                                        .sum();

                                coinMovingAverages.get(period).put("before", sum2MinAgo / period);
                            }
                        }
                        movingAveragesMap.put(coinSymbol, coinMovingAverages);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return movingAveragesMap;
        });
    }
}

