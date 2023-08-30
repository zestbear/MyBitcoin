package com.zestbear.bitcoin.mybitcoin.service.UpbitAPI;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@NoArgsConstructor
@Configuration
public class UpbitAPIConfig {

    @Value("${UPBIT_ENCRYPT_KEY}")
    private String KEY;

    @Value("${UPBIT_OPEN_API_ACCESS_KEY}")
    private String ACCESS_KEY;

    @Value("${UPBIT_OPEN_API_SECRET_KEY}")
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
