package com.zestbear.bitcoin.mybitcoin.service.Account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zestbear.bitcoin.mybitcoin.config.DecryptionUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.cache.annotation.Cacheable;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private static final String SERVER_URL = "https://api.upbit.com";

    private static String ACCESS_KEY;
    private static String SECRET_KEY;

    private final UpbitAPIConfig upbitAPIConfig;

    public AccountService(UpbitAPIConfig upbitAPIConfig) {
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

    @Cacheable(value = "accounts", key = "#root.method.name")
    public Map<String, Map<String, Object>> getAccounts() throws IOException {
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

        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();

        String jsonStr = EntityUtils.toString(entity, "UTF-8");


        ObjectMapper mapper = new ObjectMapper();
        // JSON string to List of Maps
        List<Map<String,Object>> list =
                mapper.readValue(jsonStr, new TypeReference<List<Map<String,Object>>>(){});

        // Convert to a single map where the keys are the 'currency' values
        Map<String, Map<String,Object>> resultMap =
                list.stream().collect(Collectors.toMap(m -> (String)m.get("currency"), m -> m));

//        for (String currency : resultMap.keySet()) {
//            Map<String, Object> accountInfo = resultMap.get(currency);
//            for (Map.Entry<String, Object> entry : accountInfo.entrySet()) {
//                System.out.println(entry.getKey() + ": " + entry.getValue());
//            }
//            System.out.println("-------------------------");
//        }

        return resultMap;
    }
}