package com.zestbear.bitcoin.mybitcoin.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Component
@NoArgsConstructor
public class DecryptionUtils {

    private final String ALGORITHM = "AES";

    public String encrypt(String data, String KEY) throws Exception {
        try {
            Key key = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encVal);
        } catch (Exception e) {
            throw new Exception("Error while encrypting: " + e.toString());
        }
    }

    public String decrypt(String encryptedData, String KEY) throws Exception {
        try {
            Key key = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedValue = Base64.getDecoder().decode(encryptedData.getBytes(StandardCharsets.UTF_8));
            byte[] decValue = c.doFinal(decodedValue);
            return new String(decValue, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new Exception("Error while decrypting: " + e.toString());
        }
    }
}
