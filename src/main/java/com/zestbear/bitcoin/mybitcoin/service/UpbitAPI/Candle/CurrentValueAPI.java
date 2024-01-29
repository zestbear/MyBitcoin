package com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Candle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestbear.bitcoin.mybitcoin.domain.CurrentDataDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class CurrentValueAPI {

    private final Map<String, Double> CurrentValues = new ConcurrentHashMap<>();  // 각 코인들의 시장가

    public void getCurrentValueAPI() throws IOException, ExecutionException, InterruptedException, ExecutionException {

        OkHttpClient client = new OkHttpClient();
        String[] coinSymbols = {"BTC", "ETH", "ETC", "SOL", "DOT", "MATIC"};
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (String coin : coinSymbols) {
            String url = String.format("https://api.upbit.com/v1/ticker?markets=KRW-%s", coin);

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("accept", "application/json")
                        .build();

                try (Response response = client.newCall(request).execute()) {

                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();

                        ObjectMapper mapper = new ObjectMapper();
                        CurrentDataDto[] currentDatas = mapper.readValue(responseBody, CurrentDataDto[].class);

                        for (CurrentDataDto currentData : currentDatas) {
                            CurrentValues.put(currentData.getMarket(), currentData.getTrade_price());
                        }
                    } else {
                        System.out.println("Error: " + response.code());
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

    public Map<String, Double> getCurrentValues() {
        return CurrentValues;
    }
}