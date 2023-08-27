package com.zestbear.bitcoin.mybitcoin.service.Candle;

import com.zestbear.bitcoin.mybitcoin.domain.Candle.MinuteCandle;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
class MinuteMovingAverageLineServiceTest {

    @Test
    public void printTest() {
        MinuteCandleService service = new MinuteCandleService();
        Map<String, List<MinuteCandle>> result = service.getMinuteCandleData();

        try {
            for (String coinSymbol : result.keySet()) {
                List<MinuteCandle> minuteCandles = result.get(coinSymbol);
                for (MinuteCandle minuteCandle : minuteCandles) {
                    Assertions.assertThat(minuteCandle.getMarket().equals(coinSymbol));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}