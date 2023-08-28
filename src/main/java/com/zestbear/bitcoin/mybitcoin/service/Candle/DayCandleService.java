package com.zestbear.bitcoin.mybitcoin.service.Candle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestbear.bitcoin.mybitcoin.domain.Candle.DayCandleData;
import okhttp3.OkHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DayCandleService {

    @Cacheable(value = "dayCandles", key = "#root.method.name")
    public CompletableFuture<Map<String, List<DayCandleData>>> getDayCandleData() {
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);

        return CompletableFuture.supplyAsync(() -> {
            String[] coinSymbols = {"BTC", "ETH", "ADA", "DOT", "MATIC"};
            String urlTemplate = "https://api.upbit.com/v1/candles/days?market=KRW-%s&count=21&convertingPriceUnit=KRW";
            Map<String, List<DayCandleData>> resultMap = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            CloseableHttpClient httpClient = HttpClients.createDefault();

            for (String coinSymbol : coinSymbols) {
                String url = String.format(urlTemplate, coinSymbol);
                HttpGet httpGet = new HttpGet(url);

                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    int statusCode = response.getStatusLine().getStatusCode();

                    if (statusCode == 200) {  // 정상적인 응답
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            String result = EntityUtils.toString(entity);
                            DayCandleData[] dayCandleData =
                                    objectMapper.readValue(result, DayCandleData[].class);
                            resultMap.put(coinSymbol, Arrays.asList(dayCandleData));
                        }
                    } else if (statusCode == 429) {  // Too Many Requests
                        System.err.println("Too many API requests for symbol: " + coinSymbol);
                    } else {
                        System.err.println("Unexpected status code: " + statusCode +
                                ", for symbol: " + coinSymbol);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return resultMap;
        });
    }
}
