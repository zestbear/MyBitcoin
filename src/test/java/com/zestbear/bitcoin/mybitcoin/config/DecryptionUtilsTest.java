package com.zestbear.bitcoin.mybitcoin.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DecryptionUtilsTest {

    @Value("${UPBIT_OPEN_API_ACCESS_KEY}")
    private String access;

    @Value("${UPBIT_ENCRYPT_KEY}")
    private String key;

    @Test
    void TestDecryption() throws Exception {
        String decrypted = DecryptionUtils.decrypt(access, key);

        System.out.println(access);
        System.out.println(decrypted);

    }
}