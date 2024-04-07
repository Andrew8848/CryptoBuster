package org.cryptobuster.cryptography.cipher;

import org.cryptobuster.cryptography.Crypto;
import java.util.stream.IntStream;

public class Gamma implements Crypto {

    @Override
    public byte[] encrypt(byte[] data, byte[] key) {
        return encryptDecrypt(data, key);
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key) {
        return encryptDecrypt(data, key);
    }

    private static byte[] encryptDecrypt(byte[] data, byte[] key){
        byte[] bytes = new byte[data.length];
        IntStream.range(0, data.length).forEach(i -> bytes[i] = (byte) (data[i] ^ key[i % key.length]));
        return bytes;
    }
}
