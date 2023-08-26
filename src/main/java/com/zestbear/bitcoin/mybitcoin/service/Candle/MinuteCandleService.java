package com.zestbear.bitcoin.mybitcoin.service.Candle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestbear.bitcoin.mybitcoin.domain.Candle.MinuteCandle;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MinuteCandleService {

    public Map<String, List<MinuteCandle>> getMinuteCandleData() {
        String[] coinSymbols = {"BTC", "ETH", "XRP", "ADA", "DOGE", "SOL", "TRX", "DOT", "MATIC", "BCH"};
        String urlTemplate = "https://api.upbit.com/v1/candles/minutes/10?market=KRW-%s&count=120";
        Map<String, List<MinuteCandle>> resultMap = new HashMap<>();
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

                MinuteCandle[] minuteCandleData = objectMapper.readValue(result, MinuteCandle[].class);
                resultMap.put(coinSymbol, Arrays.asList(minuteCandleData));
//                for (MinuteCandle data : minuteCandleData) {
//                    System.out.println(objectMapper.writeValueAsString(data));
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resultMap;
    }
}
