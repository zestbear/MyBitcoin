package com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Order;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zestbear.bitcoin.mybitcoin.config.DecryptionUtils;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.UpbitAPIConfig;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class CancelOrderAPI {

    private static final String SERVER_URL = "https://api.upbit.com";

    private static String ACCESS_KEY;
    private static String SECRET_KEY;

    private final UpbitAPIConfig upbitAPIConfig;
    private final OrderListAPI orderListAPI;

    public CancelOrderAPI(UpbitAPIConfig upbitAPIConfig, OrderListAPI orderListAPI) {
        this.upbitAPIConfig = upbitAPIConfig;
        this.orderListAPI = orderListAPI;
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

    public void cancelAll() {

        Queue<String> uuidQueue = orderListAPI.getUuidQueue();

        while (!uuidQueue.isEmpty()) {
            String uuid = uuidQueue.poll();

            HashMap<String, String> params = new HashMap<>();
            params.put("uuid", uuid);

            ArrayList<String> queryElements = new ArrayList<>();
            for(Map.Entry<String, String> entity : params.entrySet()) {
                queryElements.add(entity.getKey() + "=" + entity.getValue());
            }

            String queryString = String.join("&", queryElements.toArray(new String[0]));

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                md.update(queryString.getBytes("UTF-8"));

                String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

                Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
                String jwtToken = JWT.create()
                        .withClaim("access_key", ACCESS_KEY)
                        .withClaim("nonce", UUID.randomUUID().toString())
                        .withClaim("query_hash", queryHash)
                        .withClaim("query_hash_alg", "SHA512")
                        .sign(algorithm);

                String authenticationToken = "Bearer " + jwtToken;

                HttpClient client = HttpClientBuilder.create().build();
                HttpDelete request = new HttpDelete(SERVER_URL + "/v1/order?" + queryString);
                request.setHeader("Content-Type", "application/json");
                request.addHeader("Authorization", authenticationToken);

                HttpResponse response = client.execute(request);

            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}