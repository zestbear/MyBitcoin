package com.zestbear.bitcoin.mybitcoin.service.Execute;

import com.zestbear.bitcoin.mybitcoin.service.Strategy.GetRatio;
import com.zestbear.bitcoin.mybitcoin.service.Strategy.MAComparison;
import com.zestbear.bitcoin.mybitcoin.service.Strategy.RSICalculator;
import com.zestbear.bitcoin.mybitcoin.service.Strategy.LossRatio;
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
    private final LossRatio lossRatio;
    private final GetRatio getRatio;

    public OrderService(CurrentAsset currentAsset, OrderAPI orderAPI, CurrentValueAPI currentValueAPI, MAComparison maComparison, RSICalculator rsiCalculator, LossRatio lossRatio, GetRatio getRatio) {
        this.currentAsset = currentAsset;
        this.orderAPI = orderAPI;
        this.currentValueAPI = currentValueAPI;
        this.maComparison = maComparison;
        this.rsiCalculator = rsiCalculator;
        this.lossRatio = lossRatio;
        this.getRatio = getRatio;
    }

    public void sendOrder() throws IOException, NoSuchAlgorithmException {
        String[] coinSymbols = {"BTC", "ETH", "ETC", "SOL", "DOT", "MATIC"};
        double cashKRW = currentAsset.getCashKRW();                             // 현금 보유량
        Map<String, Double> eachValues = currentAsset.getEachValue();           // 보유량 * 시장가
        Map<String, Double> currentValues = currentValueAPI.getCurrentValues(); // 시장가

        for (String symbol : coinSymbols) {

//            System.out.println(symbol + ": " + maComparison.isMATiming(symbol));
//            System.out.println(rsiCalculator.getCalculatedRSI(symbol));

            if (!eachValues.containsKey(symbol)) {
                if ((maComparison.isMATiming(symbol).equals("bid") && rsiCalculator.getCalculatedRSI(symbol) < 26) || rsiCalculator.getCalculatedRSI(symbol) < 18) {
                    if (cashKRW >= 25000) {
                        String price = String.valueOf(25000);

                        orderAPI.postOrder("bid", "KRW-" + symbol, price, null);
                    }
                }
            }

            if (eachValues.containsKey(symbol)) {
                if ((maComparison.isMATiming(symbol).equals("ask") && rsiCalculator.getCalculatedRSI(symbol) > 70) || lossRatio.isLoss(symbol) || getRatio.isGet(symbol) || rsiCalculator.getCalculatedRSI(symbol) > 75) {
                    double volume = eachValues.get(symbol) / currentValues.get("KRW-" + symbol);

                    orderAPI.postOrder("ask", "KRW-" + symbol, null, String.format("%.8f", volume));
                }

                if (eachValues.get(symbol) < 40000 && rsiCalculator.getCalculatedRSI(symbol) < 10 && cashKRW >= 25000) {
                    String price = String.valueOf(25000);

                    orderAPI.postOrder("bid", "KRW-" + symbol, price, null);
                }
            }
        }
    }
}