package com.zestbear.bitcoin.mybitcoin.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DecryptionUtilsTest {

    @Autowired
    private UpbitAPIConfig upbitAPIConfig;

    @Autowired
    private DecryptionUtils decryptionUtils;

    @Test
    void TestDecryption() throws Exception {
        // given
        String access = upbitAPIConfig.getACCESS_KEY(); // 저장될 때 암호화되어 저장됨
        String key = upbitAPIConfig.getKEY();

        // when
        String decrypted = decryptionUtils.decrypt(access, key); // 복호화
        String encrypted = decryptionUtils.encrypt(decrypted, key); // 다시 암호화

        // then
        Assertions.assertEquals(access, encrypted); // 복호화 -> 암호화 과정을 거친 String은 access와 같아야 함
    }
}