package com.zestbear.bitcoin.mybitcoin.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecryptionUtilsTest {

    @Test
    public void encrypt_and_decrypt() throws Exception {
        final String data = "fasfj14kjraskldn2jhfs213";
        final String key = "9182736450192456";

        String encrypted = DecryptionUtils.encrypt(data, key);
        String decrypred = DecryptionUtils.decrypt(encrypted, key);
        Assertions.assertEquals(data, decrypred);
    }

}