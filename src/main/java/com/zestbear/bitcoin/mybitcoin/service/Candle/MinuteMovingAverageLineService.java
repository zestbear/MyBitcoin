package com.zestbear.bitcoin.mybitcoin.service.Candle;

import com.zestbear.bitcoin.mybitcoin.domain.Candle.MinuteCandle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MinuteMovingAverageLineService {

    private final MinuteCandleService minuteCandleService;

    public Map<String, Map<Integer, List<Long>>> getMovingAverageLine() {
        Map<String, List<MinuteCandle>> jsonMap = minuteCandleService.getMinuteCandleData();
        List<Integer> periods = Arrays.asList(5, 10, 20, 60, 120);

        Map<String, Map<Integer, List<Long>>> movingAveragesMap = new HashMap<>();

        for (String coinSymbol : jsonMap.keySet()) {
            try {
                List<MinuteCandle> sortedMinuteCandles = new ArrayList<>(jsonMap.get(coinSymbol));
                sortedMinuteCandles.sort(Comparator.comparing(MinuteCandle::getCandle_date_time_kst));

                Map<Integer, List<Long>> coinMovingAverages = new HashMap<>();

                for (int period : periods) {
                    if (sortedMinuteCandles.size() >= period) { // 데이터가 충분한 경우만 처리
                        double sum = sortedMinuteCandles.subList(sortedMinuteCandles.size() - period,
                                        sortedMinuteCandles.size()).stream()
                                .mapToDouble(MinuteCandle::getTrade_price)
                                .sum();

                        // 해당 기간의 이동평균값 리스트를 맵에 추가
                        coinMovingAverages.put(period, Collections.singletonList((long) sum / period));
                    }
                }

                movingAveragesMap.put(coinSymbol, coinMovingAverages);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return movingAveragesMap;
    }
}
