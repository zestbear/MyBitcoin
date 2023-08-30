package com.zestbear.bitcoin.mybitcoin.service.Execute;

import com.zestbear.bitcoin.mybitcoin.service.Strategy.DeadCross;
import com.zestbear.bitcoin.mybitcoin.service.Strategy.GoldenCross;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Account.CurrentAsset;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle.CurrentValueAPI;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Order.OrderAPI;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


@Service
public class OrderService {

    private final DeadCross deadCross;
    private final GoldenCross goldenCross;
    private final CurrentAsset currentAsset;
    private final OrderAPI orderAPI;
    private final CurrentValueAPI currentValueAPI;

    public OrderService(DeadCross deadCross, GoldenCross goldenCross, CurrentAsset currentAsset, OrderAPI orderAPI, CurrentValueAPI currentValueAPI) {
        this.deadCross = deadCross;
        this.goldenCross = goldenCross;
        this.currentAsset = currentAsset;
        this.orderAPI = orderAPI;
        this.currentValueAPI = currentValueAPI;
    }

    public void sendOrder() throws IOException, NoSuchAlgorithmException {
        String[] coinSymbols = {"BTC", "ETH", "ADA", "DOT", "MATIC"};
        double evalKRW = currentAsset.getAssetSum();                            // 자산 평가 금액
        Map<String, Double> eachValues = currentAsset.getEachValue();           // 보유량 * 시장가
        Map<String, Double> currentValues = currentValueAPI.getCurrentValues(); // 시장가

        for (String key : eachValues.keySet()) {
            System.out.println(key + ": " + eachValues.get(key));
        }
        System.out.println(eachValues.size());
        System.out.println("===============================================");

        for (String symbol : coinSymbols) {
            if (goldenCross.isGoldenCross(symbol)) {
                if (!eachValues.containsKey(symbol)) {
                    String price = "";
                    switch (symbol) {
                        case "BTC", "ETH" -> price = String.valueOf(evalKRW * 0.35);
                        case "ADA" -> price = String.valueOf(evalKRW * 0.1);
                        case "DOT", "MATIC" -> price = String.valueOf(evalKRW * 0.05);
                    }

                    orderAPI.postOrder("bid", "KRW-" + symbol, price, null);
                }
            }

            if (deadCross.isDeadCross(symbol)) {
                if (eachValues.containsKey(symbol)) {
                    double volume = eachValues.get(symbol) / currentValues.get("KRW-" + symbol);

                    orderAPI.postOrder("ask", "KRW-" + symbol, null, String.format("%.8f", volume));
                }
            }
        }
    }
}