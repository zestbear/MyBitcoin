package com.zestbear.bitcoin.mybitcoin.service.Candle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestbear.bitcoin.mybitcoin.domain.Candle.DayCandle;
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

@Service
public class DayCandleService {

    public Map<String, List<DayCandle>> getDayCandleData() {
        String[] coinSymbols = {"BTC", "ETH", "XRP", "ADA", "DOGE", "SOL", "TRX", "DOT", "MATIC", "BCH"};
        String urlTemplate = "https://api.upbit.com/v1/candles/days?market=KRW-%s&count=120&convertingPriceUnit=KRW";
        Map<String, List<DayCandle>> resultMap = new HashMap<>();
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

                DayCandle[] dayCandleData = objectMapper.readValue(result, DayCandle[].class);
                resultMap.put(coinSymbol, Arrays.asList(dayCandleData));
//                for (DayCandle data : dayCandleData) {
//                    System.out.println(objectMapper.writeValueAsString(data));
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }
}