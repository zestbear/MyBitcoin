package com.zestbear.bitcoin.mybitcoin.service.UpbitAPI.Order;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.zestbear.bitcoin.mybitcoin.config.UpbitAPIConfig;
import com.zestbear.bitcoin.mybitcoin.domain.Order;
import com.zestbear.bitcoin.mybitcoin.domain.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class OrderAPI {

    private final UpbitAPIConfig upbitAPIConfig;
    private final OrderRepository orderRepository;

    public OrderAPI(UpbitAPIConfig upbitAPIConfig, OrderRepository orderRepository) {
        this.upbitAPIConfig = upbitAPIConfig;
        this.orderRepository = orderRepository;
    }

    public void postOrder(String orderType, String coinSymbol, String price, String volume) {

        String ACCESS_KEY = upbitAPIConfig.getACCESS_KEY();
        String SECRET_KEY = upbitAPIConfig.getSECRET_KEY();

        HashMap<String, String> params = new HashMap<>();
        params.put("market", coinSymbol);
        if (orderType.equals("bid")) {
            params.put("side", orderType);
            params.put("ord_type", "price");
            params.put("price", price);
            log.info("BID: " + params.get("market") + " " + params.get("price"));
        } else if (orderType.equals("ask")) {
            params.put("side", orderType);
            params.put("ord_type", "market");
            params.put("volume", volume);
            log.info("ASK: " + params.get("market") + " " + params.get("volume"));
        }

        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(queryString.getBytes());

            String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

            Algorithm algorithmHMAC256= Algorithm.HMAC256(SECRET_KEY);
            String jwtToken = JWT.create()
                    .withClaim("access_key", ACCESS_KEY)
                    .withClaim("nonce", UUID.randomUUID().toString())
                    .withClaim ("query_hash" , queryHash)
                    .withClaim ("query_hash_alg" , "SHA512")
                    .sign(algorithmHMAC256);

            String authenticationToken= "Bearer "+jwtToken;
            HttpClient client= HttpClientBuilder.create().build();
            String serverUrl = "https://api.upbit.com";
            HttpPost request=new HttpPost(serverUrl +"/v1/orders");
            request.setHeader ("Content-Type" , "application/json");
            request.addHeader ("Authorization" , authenticationToken);
            request.setEntity(new StringEntity(new Gson().toJson(params)));

            HttpResponse response=client.execute(request);
            HttpEntity entity=response.getEntity();

            String responseJson = EntityUtils.toString(entity, "UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            Order newOrder = objectMapper.readValue(responseJson, Order.class);
            orderRepository.save(newOrder);

            System.out.println(EntityUtils.toString(entity, "UTF-8"));
        } catch(NoSuchAlgorithmException | UnsupportedEncodingException e){
            System.err.println(e.getMessage());
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
