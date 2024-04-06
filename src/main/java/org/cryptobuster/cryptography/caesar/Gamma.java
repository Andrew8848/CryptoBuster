package org.cryptobuster.cryptography.caesar;

import org.cryptobuster.cryptography.ArrayUtil;
import org.cryptobuster.cryptography.Crypto;

import javax.crypto.BadPaddingException;
import java.util.List;
import java.util.stream.IntStream;

public class Gamma implements Crypto {

    @Override
    public byte[] encrypt(byte[] data, byte[] key) {
        List<Character> outputData = encryptDecrypt(ArrayUtil.toChars(data), ArrayUtil.toChars(key));
        return ArrayUtil.charsToString(outputData).getBytes();
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key) {
        List<Character> outputData = encryptDecrypt(ArrayUtil.toChars(data), ArrayUtil.toChars(key));
        return ArrayUtil.charsToString(outputData).getBytes();
    }

    public List<Character> encrypt(List<Character> data, List<Character> key) {
        return encryptDecrypt(data, key);
    }


    public List<Character> decrypt(List<Character> data, List<Character> key) {
        return encryptDecrypt(data, key);
    }

    private static List<Character> encryptDecrypt(List<Character> data, List<Character> key){
        return IntStream
                .range(0, data.size())
                .map(i -> data.get(i) ^ key.get(i % key.size()))
                .mapToObj(i -> (char) i)
                .toList();
    }


}
