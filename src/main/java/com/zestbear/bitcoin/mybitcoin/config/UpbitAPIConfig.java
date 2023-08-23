package com.zestbear.bitcoin.mybitcoin.config;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@NoArgsConstructor
@Configuration
public class UpbitAPIConfig {

    @Value("${upbit.open.api.key}")
    private String KEY;

    @Value("${upbit.open.api.access.key}")
    private String ACCESS_KEY;

    @Value("${upbit.open.api.secret.key}")
    private String SECRET_KEY;

    public String getACCESS_KEY() {
        return ACCESS_KEY;
    }

    public String getSECRET_KEY() {
        return SECRET_KEY;
    }

    public String getKEY() {
        return KEY;
    }
}
