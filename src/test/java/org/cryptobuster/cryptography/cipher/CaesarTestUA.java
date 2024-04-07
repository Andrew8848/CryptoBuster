package org.cryptobuster.cryptography.cipher;

import org.cryptobuster.cryptography.AlphabetHandler;
import org.cryptobuster.cryptography.ArrayUtil;
import org.cryptobuster.cryptography.Crypto;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaesarTestUA extends CipherTestUA{

    @Test
    @Override
    public void crypt() throws BadPaddingException {
        Crypto crypto = new Caesar(AlphabetHandler.UA);
        byte[] encryptResult = crypto.encrypt(getData().getBytes(StandardCharsets.UTF_8), getKey().getBytes(StandardCharsets.UTF_8));
        String encryptedText = ArrayUtil.toChars(encryptResult).stream().map(String::valueOf).collect(Collectors.joining());

        byte[] decryptResult = crypto.decrypt(encryptResult, getKey().getBytes(StandardCharsets.UTF_8));
        String decryptedText = ArrayUtil.toChars(decryptResult).stream().map(String::valueOf).collect(Collectors.joining());;

        assertEquals(decryptedText, getData());
    }


}