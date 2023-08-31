package com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestbear.bitcoin.mybitcoin.domain.Candle.MinuteCandleData;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class MinuteCandleAPI {

    private final Map<String, List<MinuteCandleData>> minuteCandles = new ConcurrentHashMap<>();

    public void getMinuteCandleAPI() throws ExecutionException, InterruptedException, ExecutionException {

        String[] coinSymbols = {"BTC", "ETH", "ADA", "DOT", "MATIC"};
        String urlTemplate = "https://api.upbit.com/v1/candles/minutes/60?market=KRW-%s&count=21";
        ObjectMapper objectMapper = new ObjectMapper();
        CloseableHttpClient httpClient = HttpClients.createDefault();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (String coinSymbol : coinSymbols) {
            String url = String.format(urlTemplate, coinSymbol);
            HttpGet httpGet = new HttpGet(url);

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    int statusCode = response.getStatusLine().getStatusCode();

                    if (statusCode == 200) {  // 정상적인 응답
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            String result;
                            try {
                                result = EntityUtils.toString(entity);
                                MinuteCandleData[] minuteCandleData =
                                        objectMapper.readValue(result, MinuteCandleData[].class);
                                minuteCandles.put(coinSymbol, Arrays.asList(minuteCandleData));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else if (statusCode == 429) {  // Too Many Requests
                        System.err.println("Too many API requests for symbol: " + coinSymbol);
                    } else {
                        System.err.println("Unexpected status code: " + statusCode +
                                ", for symbol: " + coinSymbol);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            futures.add(future);

        }

        // allOf 메소드를 사용하여 모든 Future가 완료될 때까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
    }

    public Map<String, List<MinuteCandleData>> getMinuteCandles() {
        return minuteCandles;
    }
}