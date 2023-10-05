package com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Order;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zestbear.bitcoin.mybitcoin.config.DecryptionUtils;
import com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.UpbitAPIConfig;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class OrderListAPI {

    private static final String SERVER_URL = "https://api.upbit.com";
    private static final String STATE = "wait";

    private static String ACCESS_KEY;
    private static String SECRET_KEY;

    private final UpbitAPIConfig upbitAPIConfig;

    public OrderListAPI(UpbitAPIConfig upbitAPIConfig) {
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

    private final Queue<String> uuidQueue = new ConcurrentLinkedQueue<>();

    public synchronized void getOrders() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        HashMap<String, String> params = new HashMap<>();
        params.put("state", STATE);

        ArrayList<String> queryElements = new ArrayList<>();
        for (Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

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

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(SERVER_URL + "/v1/orders?" + queryString);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);

            int statusCode=response.getStatusLine().getStatusCode();
            if(statusCode!=200){
                // Handle error
                System.err.println("Error occurred while getting orders: HTTP " + statusCode);
                return;
            }

            HttpEntity entity=response.getEntity();

            String responseStr = EntityUtils.toString(entity, "UTF-8");
            JSONArray jsonArray = new JSONArray(responseStr);

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject orderObject = jsonArray.getJSONObject(i);
                String uuid = orderObject.getString("uuid");
                uuidQueue.add(uuid); // Add UUID to the queue
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Queue<String> getUuidQueue() {
        return uuidQueue;
    }
}
