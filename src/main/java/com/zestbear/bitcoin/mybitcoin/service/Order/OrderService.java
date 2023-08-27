package com.zestbear.bitcoin.mybitcoin.service.Order;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.zestbear.bitcoin.mybitcoin.config.DecryptionUtils;
import com.zestbear.bitcoin.mybitcoin.service.Account.UpbitAPIConfig;
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
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {

    private static final String SERVER_URL = "https://api.upbit.com";

    private static String ACCESS_KEY;
    private static String SECRET_KEY;

    @Autowired
    public OrderService(UpbitAPIConfig upbitAPIConfig) throws IOException {
        try {
            ACCESS_KEY = DecryptionUtils.decrypt(upbitAPIConfig.getACCESS_KEY(), upbitAPIConfig.getKEY());
            SECRET_KEY = DecryptionUtils.decrypt(upbitAPIConfig.getSECRET_KEY(), upbitAPIConfig.getKEY());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void postOrder(String[] coinSymbols) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        HashMap<String, String> params = new HashMap<>();
        params.put("market", "KRW-BTC");
        params.put("side", "bid");          // 매수 : bid, 매도 : ask
        params.put("volume", "1");
        params.put("price", "5000");
        params.put("ord_type", "limit");

        ArrayList<String> queryElements = new ArrayList<>();
        for(
                Map.Entry<String, String> entity : params.entrySet()) {
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

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(SERVER_URL + "/v1/orders");
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);
            request.setEntity(new StringEntity(new Gson().toJson(params)));

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            System.out.println(EntityUtils.toString(entity, "UTF-8"));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

}
