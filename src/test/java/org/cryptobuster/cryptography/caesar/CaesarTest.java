package org.cryptobuster.cryptography.caesar;

import org.cryptobuster.cryptography.Crypto;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaesarTest extends CipherTest{

    @Test
    @Override
    public void crypt() throws BadPaddingException {
        Crypto crypto = new Caesar();
        List<Character> encryptResult = crypto.encrypt(toList(getData()), toList(getKey()));
        String encryptedText = encryptResult.stream().map(String::valueOf).collect(Collectors.joining());

        List<Character> decryptResult = crypto.decrypt(encryptResult, toList(getKey()));
        String decryptedText = decryptResult.stream().map(String::valueOf).collect(Collectors.joining());

        assertEquals(decryptedText, getData());
    }


}