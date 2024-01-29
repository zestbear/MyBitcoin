package com.zestbear.bitcoin.mybitcoin.service.Strategy;

import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Account.CurrentAsset;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle.CurrentValueAPI;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetRatio {

    private final CurrentAsset currentAsset;
    private final CurrentValueAPI currentValueAPI;

    public GetRatio(CurrentAsset currentAsset, CurrentValueAPI currentValueAPI) {
        this.currentAsset = currentAsset;
        this.currentValueAPI = currentValueAPI;
    }

    public double GetNow(String coinSymbol) {
        Map<String, Double> buyPrices = currentAsset.getBuyPrices();            // 보유 중인 코인의 평균매수가
        Map<String, Double> currentValues = currentValueAPI.getCurrentValues(); // 코인의 시장가

        if (currentValues.containsKey("KRW-" + coinSymbol) && buyPrices.containsKey(coinSymbol)) {
            double buyPrice = buyPrices.get(coinSymbol);
            double currentPrice = currentValues.get("KRW-" + coinSymbol);

            return (currentPrice - buyPrice) / buyPrice;
        }
        return 0;
    }
}