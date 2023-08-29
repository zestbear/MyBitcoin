package com.zestbear.bitcoin.mybitcoin.service.Order;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.zestbear.bitcoin.mybitcoin.config.DecryptionUtils;
import com.zestbear.bitcoin.mybitcoin.service.Account.CurrentAsset;
import com.zestbear.bitcoin.mybitcoin.service.Account.UpbitAPIConfig;
import com.zestbear.bitcoin.mybitcoin.service.Candle.CurrentValueService;
import com.zestbear.bitcoin.mybitcoin.service.Strategy.PermissionService;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {

    private final PermissionService permissionService;
    private final CurrentAsset currentAsset;
    private final CurrentValueService currentValueService;

    private static String ACCESS_KEY;
    private static String SECRET_KEY;

    @Autowired
    private UpbitAPIConfig upbitAPIConfig;

    @Autowired
    public OrderService(PermissionService permissionService, CurrentAsset currentAsset, CurrentValueService currentValueService) {
        this.permissionService = permissionService;
        this.currentAsset = currentAsset;
        this.currentValueService = currentValueService;
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

    public void postOrder(String coinSymbol) throws NoSuchAlgorithmException, IOException {

        Map<String, Double> values = currentAsset.getValueforEach();
        double current = currentAsset.getCurrentKRWAsset();
        Map<String, Double> currentValues = currentValueService.getCurrent();

        if (currentValues == null) {
            System.out.println("Failed to retrieve current values");
            return; // Exit the method early
        }


        String action = null;
        try {
            action = permissionService.isSafe(coinSymbol);
        } catch (IOException e) {
            e.printStackTrace();
            return; // if there's an exception, stop here.
        }

        if (action.equals("stay")) {
            return; // Do nothing if we should stay
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("market", "KRW-" + coinSymbol);

        if(action.equals("bid")){
            if (!values.containsKey(coinSymbol)) {
                params.put("side", "bid");
                params.put("ord_type", "price");
                switch (coinSymbol) {
                    case "BTC", "ETH" -> params.put("price", String.valueOf(current * 0.4));
                    case "ADA" -> params.put("price", String.valueOf(current * 0.1));
                    case "DOT", "MATIC" -> params.put("price", String.valueOf(current * 0.05));
                }
                System.out.println("BID: " + params.get("market") + " " + params.get("price"));
            }
        } else if (action.equals("ask")) {
            if (values.containsKey(coinSymbol)) {
                double volume = values.get(coinSymbol) / currentValues.get("KRW-" + coinSymbol);
                params.put("side", "ask");
                params.put("volume", String.format("%.8f", volume));
                params.put("ord_type", "market");
                System.out.println("ASK: " + params.get("market") + " " + params.get("volume"));
                System.out.println("In my wallet: " + values.get(coinSymbol));
            }
        }

        System.out.println("DEFAULT: " + params.get("market"));

        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes());

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        Algorithm algorithmHMAC256= Algorithm.HMAC256(SECRET_KEY);
        String jwtToken= JWT.create()
                .withClaim("access_key", ACCESS_KEY)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim ("query_hash" , queryHash)
                .withClaim ("query_hash_alg" , "SHA512")
                .sign(algorithmHMAC256);

        String authenticationToken= "Bearer "+jwtToken;

        try{
            HttpClient client= HttpClientBuilder.create().build();
            String serverUrl = "https://api.upbit.com";
            HttpPost request=new HttpPost(serverUrl +"/v1/orders");
            request.setHeader ("Content-Type" , "application/json" );
            request.addHeader ("Authorization" , authenticationToken);
            request.setEntity(new StringEntity(new Gson().toJson(params)));

            HttpResponse response=client.execute(request);
            HttpEntity entity=response.getEntity();

            System.out.println(EntityUtils.toString(entity, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}