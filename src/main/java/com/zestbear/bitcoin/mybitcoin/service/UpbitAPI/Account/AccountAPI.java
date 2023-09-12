package com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestbear.bitcoin.mybitcoin.config.DecryptionUtils;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.UpbitAPIConfig;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AccountAPI {

    private static final String SERVER_URL = "https://api.upbit.com";

    private static String ACCESS_KEY;
    private static String SECRET_KEY;

    private final UpbitAPIConfig upbitAPIConfig;

    public AccountAPI(UpbitAPIConfig upbitAPIConfig) {
        this.upbitAPIConfig = upbitAPIConfig;
    }

    @PostConstruct
    public void init() {
        try {
            ACCESS_KEY = DecryptionUtils.decrypt(upbitAPIConfig.getACCESS_KEY(), upbitAPIConfig.getKEY());
            SECRET_KEY = DecryptionUtils.decrypt(upbitAPIConfig.getSECRET_KEY(), upbitAPIConfig.getKEY());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private final Map<String, Map<String, Object>> accountData = new ConcurrentHashMap<>();   // 자산 정보

    public void getAccountsAPI() throws ExecutionException, InterruptedException, ExecutionException {

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        String jwtToken = JWT.create()
                .withClaim("access_key", ACCESS_KEY)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(SERVER_URL + "/v1/accounts");

        request.setHeader("Content-Type", "application/json");
        request.addHeader("Authorization", authenticationToken);

        CompletableFuture<Void> future =
                CompletableFuture.runAsync(() -> {
                    try {
                        HttpResponse response = client.execute(request);
                        HttpEntity entity = response.getEntity();

                        if (entity != null) {
                            String jsonStr =
                                    EntityUtils.toString(entity, StandardCharsets.UTF_8);

                            ObjectMapper mapper =
                                    new ObjectMapper();
                            List<Map<String, Object>> list =
                                    mapper.readValue(
                                            jsonStr,
                                            new TypeReference<List<Map<String, Object>>>() {});

                            accountData.putAll(
                                    list.stream()
                                            .collect(Collectors.toMap(m -> (String)m.get("currency"), m -> m)));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        future.get();  // wait for the async task to complete
    }

    public Map<String, Map<String, Object>> getAccountData() {
        return accountData;
    }
}