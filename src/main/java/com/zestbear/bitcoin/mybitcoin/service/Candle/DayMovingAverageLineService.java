package com.zestbear.bitcoin.mybitcoin.service.Candle;

import com.zestbear.bitcoin.mybitcoin.domain.Candle.DayCandle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DayMovingAverageLineService {

    private final DayCandleService dayCandleService;

    public Map<String, Map<Integer, List<Long>>> getMovingAverageLine() {
        Map<String, List<DayCandle>> jsonMap = dayCandleService.getDayCandleData();
        List<Integer> periods = Arrays.asList(5, 10, 20, 60, 120);

        Map<String, Map<Integer, List<Long>>> movingAveragesMap = new HashMap<>();

        for (String coinSymbol : jsonMap.keySet()) {
            try {
                List<DayCandle> sortedDayCandles = new ArrayList<>(jsonMap.get(coinSymbol));
                sortedDayCandles.sort(Comparator.comparing(DayCandle::getCandle_date_time_kst));

                Map<Integer, List<Long>> coinMovingAverages = new HashMap<>();

                for (int period : periods) {
                    if (sortedDayCandles.size() >= period) { // 데이터가 충분한 경우만 처리
                        double sum = sortedDayCandles.subList(sortedDayCandles.size() - period,
                                        sortedDayCandles.size()).stream()
                                .mapToDouble(DayCandle::getTrade_price)
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
