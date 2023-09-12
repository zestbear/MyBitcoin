package com.zestbear.bitcoin.mybitcoin.service.Execute;

import com.zestbear.bitcoin.mybitcoin.service.Strategy.MAComparison;
import com.zestbear.bitcoin.mybitcoin.service.Strategy.RSICalculator;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Account.CurrentAsset;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle.CurrentValueAPI;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Order.OrderAPI;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


@Service
public class OrderService {

    private final CurrentAsset currentAsset;
    private final OrderAPI orderAPI;
    private final CurrentValueAPI currentValueAPI;
    private final MAComparison maComparison;
    private final RSICalculator rsiCalculator;

    public OrderService(CurrentAsset currentAsset, OrderAPI orderAPI, CurrentValueAPI currentValueAPI, MAComparison maComparison, RSICalculator rsiCalculator) {
        this.currentAsset = currentAsset;
        this.orderAPI = orderAPI;
        this.currentValueAPI = currentValueAPI;
        this.maComparison = maComparison;
        this.rsiCalculator = rsiCalculator;
    }

    public void sendOrder() throws IOException, NoSuchAlgorithmException {
        String[] coinSymbols = {"BTC", "ETH", "XRP", "XEM", "NEO", "ETC", "WAVES", "DOGE", "ARK", "SOL", "DOT", "MATIC", "XLM", "DAWN", "SAND"};
        double evalKRW = currentAsset.getAssetSum();                            // 자산 평가 금액
        Map<String, Double> eachValues = currentAsset.getEachValue();           // 보유량 * 시장가
        Map<String, Double> currentValues = currentValueAPI.getCurrentValues(); // 시장가

        for (String symbol : coinSymbols) {
            if (maComparison.isMATiming(symbol).equals("bid") && rsiCalculator.getCalculatedRSI(symbol) < 30) {
                if (!eachValues.containsKey(symbol)) {
                    String price = String.valueOf(10000);
//                    String price = "";
//                    switch (symbol) {
//                        case "BTC", "ETH" -> price = String.valueOf(evalKRW * 0.3);
//                        case "SOL" -> price = String.valueOf(evalKRW * 0.2);
//                        case "DOT", "MATIC" -> price = String.valueOf(evalKRW * 0.05);
//                    }

                    orderAPI.postOrder("bid", "KRW-" + symbol, price, null);
                }
            }

            if (maComparison.isMATiming(symbol).equals("ask") && rsiCalculator.getCalculatedRSI(symbol) > 70) {
                if (eachValues.containsKey(symbol)) {
                    double volume = eachValues.get(symbol) / currentValues.get("KRW-" + symbol);

                    orderAPI.postOrder("ask", "KRW-" + symbol, null, String.format("%.8f", volume));
                }
            }
        }
    }
}