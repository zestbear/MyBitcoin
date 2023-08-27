package com.zestbear.bitcoin.mybitcoin.service.Candle;

import com.zestbear.bitcoin.mybitcoin.domain.Candle.DayCandleData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DayMovingAverageLineService {

    private final DayCandleService dayCandleService;

    public Map<String, Map<Integer, Double>> getMovingAverageLine() {
        CompletableFuture<Map<String, List<DayCandleData>>> future = dayCandleService.getDayCandleData();
        Map<String, List<DayCandleData>> jsonMap = null;
        try {
            jsonMap = future.get();  // 비동기 작업이 완료될 때까지 대기
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Collections.emptyMap();  // 오류가 발생하면 빈 맵을 반환
        }

        List<Integer> periods = Arrays.asList(5, 10, 21);

        Map<String, Map<Integer, Double>> movingAveragesMap = new HashMap<>();

        for (String coinSymbol : jsonMap.keySet()) {
            try {
                List<DayCandleData> sortedDayCandleData = new ArrayList<>(jsonMap.get(coinSymbol));
                sortedDayCandleData.sort(Comparator.comparing(DayCandleData::getCandle_date_time_kst));

                Map<Integer, Double> coinMovingAverages = new HashMap<>();

                for (int period : periods) {
                    if (sortedDayCandleData.size() >= period) { // 데이터가 충분한 경우만 처리
                        double sum = sortedDayCandleData.subList(sortedDayCandleData.size() - period,
                                        sortedDayCandleData.size()).stream()
                                .mapToDouble(DayCandleData::getTrade_price)
                                .sum();

                        System.out.println(coinSymbol + " (" + period + "-day): " + sum / period);
                        // 해당 기간의 이동평균값을 맵에 추가
                        coinMovingAverages.put(period,sum / period);
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
