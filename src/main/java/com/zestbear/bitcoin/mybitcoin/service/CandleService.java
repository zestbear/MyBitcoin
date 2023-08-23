package com.zestbear.bitcoin.mybitcoin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestbear.bitcoin.mybitcoin.domain.Candle.CandleData;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class CandleService {

    public Map<String, List<CandleData>> getCandleData(String[] coinSymbols) {
        String urlTemplate = "https://api.upbit.com/v1/candles/minutes/10?market=KRW-%s&count=30";
        Map<String, List<CandleData>> resultMap = new HashMap<>();
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

                CandleData[] candleData = objectMapper.readValue(result, CandleData[].class);
                resultMap.put(coinSymbol, Arrays.asList(candleData));
//                for (CandleData data : candleData) {
//                    System.out.println(data.toString());
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resultMap;
    }
}
