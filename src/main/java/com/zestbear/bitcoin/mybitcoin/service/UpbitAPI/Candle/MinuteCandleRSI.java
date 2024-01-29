package com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle;

import com.zestbear.bitcoin.mybitcoin.domain.MinuteCandleDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MinuteCandleRSI {

    private final MinuteCandleAPI minuteCandleAPI;

    public MinuteCandleRSI(MinuteCandleAPI minuteCandleAPI) {
        this.minuteCandleAPI = minuteCandleAPI;
    }

    public Map<String, List<MinuteCandleDto>> getForRSI() {

        Map<String, List<MinuteCandleDto>> minuteCandles = minuteCandleAPI.getMinuteCandles();
        Map<String, List<MinuteCandleDto>> forRSI = new HashMap<>();

        for (String symbol : minuteCandles.keySet()) {
            List<MinuteCandleDto> sortedList =
                    minuteCandles.get(symbol).stream()
                            .sorted((o1, o2) -> o2.getCandle_date_time_kst().compareTo(o1.getCandle_date_time_kst()))
                            .collect(Collectors.toList());
            Collections.reverse(sortedList);

            forRSI.put(symbol, sortedList);
        }

        return forRSI;
    }
}
