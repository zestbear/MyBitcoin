package com.zestbear.bitcoin.mybitcoin.service.Candle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class DayMovingAverageLineServiceTest {

    @Test
    public void printTest() {
//        DayCandleService service = new DayCandleService();
//        CompletableFuture<Map<String, List<DayCandleData>>> result = dayCandleService.getDayCandleData();
//        Map<String, List<DayCandleData>> jsonMap = null;
//        try {
//            jsonMap = result.get();  // 비동기 작업이 완료될 때까지 대기
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            return Collections.emptyMap();  // 오류가 발생하면 빈 맵을 반환
//        }
//
//        try {
//            for (String coinSymbol : result.keySet()) {
//                List<DayCandleData> dayCandles = result.get(coinSymbol);
//                for (DayCandleData dayCandle : dayCandles) {
//                    Assertions.assertThat(dayCandle.getMarket().equals(coinSymbol));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
    }

}