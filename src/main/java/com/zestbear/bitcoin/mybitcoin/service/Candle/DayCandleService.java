package com.zestbear.bitcoin.mybitcoin.service.Candle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestbear.bitcoin.mybitcoin.domain.Candle.DayCandleData;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class DayCandleService {

    public CompletableFuture<Map<String, List<DayCandleData>>> getDayCandleData() {
        return CompletableFuture.supplyAsync(() -> {
            String[] coinSymbols = {"BTC", "ETH", "ADA", "DOT", "MATIC"};
            String urlTemplate = "https://api.upbit.com/v1/candles/days?market=KRW-%s&count=21&convertingPriceUnit=KRW";
            Map<String, List<DayCandleData>> resultMap = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            CloseableHttpClient httpClient = HttpClients.createDefault();

            for (String coinSymbol : coinSymbols) {
                String url = String.format(urlTemplate, coinSymbol);
                String result = "";
                HttpGet httpGet = new HttpGet(url);

                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        result = EntityUtils.toString(entity);
                    }

                    DayCandleData[] dayCandleData = objectMapper.readValue(result, DayCandleData[].class);
                    resultMap.put(coinSymbol, Arrays.asList(dayCandleData));
//                  for (DayCandleData data : dayCandleData) {
//                      System.out.println(objectMapper.writeValueAsString(data));
//                  }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return resultMap;
        });
    }
}