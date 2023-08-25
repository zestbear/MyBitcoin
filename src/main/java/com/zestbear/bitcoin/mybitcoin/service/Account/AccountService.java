package com.zestbear.bitcoin.mybitcoin.service.Account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zestbear.bitcoin.mybitcoin.config.DecryptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class AccountService {

    private static final String SERVER_URL = "https://api.upbit.com";

    private static String ACCESS_KEY;
    private static String SECRET_KEY;

    @Autowired
    public AccountService(UpbitAPIConfig upbitAPIConfig) {
        try {
            ACCESS_KEY = DecryptionUtils.decrypt(upbitAPIConfig.getACCESS_KEY(), upbitAPIConfig.getKEY());
            SECRET_KEY = DecryptionUtils.decrypt(upbitAPIConfig.getSECRET_KEY(), upbitAPIConfig.getKEY());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void getAccounts() throws IOException {
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

        if(response.getStatusLine().getStatusCode() == 200){
            HttpEntity entity = response.getEntity();
            System.out.println(EntityUtils.toString(entity, "UTF-8"));
        } else{
            System.out.println("Error: "+ response.getStatusLine().getStatusCode());
        }

    }
}