package com.zestbear.bitcoin.mybitcoin.service.Candle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestbear.bitcoin.mybitcoin.domain.Candle.CurrentData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CurrentValueService {

    @Cacheable(value = "currentValues", key = "#root.method.name")
    public Map<String, Double> getCurrent() throws IOException {
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);

        Map<String, Double> returnMap = new HashMap<>();
        OkHttpClient client = new OkHttpClient();

        String[] coinSymbols = {"BTC", "ETH", "ADA", "DOT", "MATIC"};

        for (String coin : coinSymbols) {
            String url = String.format("https://api.upbit.com/v1/ticker?markets=KRW-%s", coin);

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                ObjectMapper mapper = new ObjectMapper();
                CurrentData[] currentDatas = mapper.readValue(responseBody, CurrentData[].class);

                for (CurrentData currentData : currentDatas) {
                    returnMap.put(currentData.getMarket(), currentData.getTrade_price());
                }
            } else {
                System.out.println("Error: " + response.code());
            }
        }

        return returnMap;
    }
}
