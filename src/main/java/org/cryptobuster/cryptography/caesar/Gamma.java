package org.cryptobuster.cryptography.caesar;

import org.cryptobuster.cryptography.Crypto;

import java.util.List;
import java.util.stream.IntStream;

public class Gamma implements Crypto {
    @Override
    public List<Character> encrypt(List<Character> data, List<Character> key) {
        return encriptDecrypt(data, key);
    }

    @Override
    public List<Character> decrypt(List<Character> data, List<Character> key) {
        return encriptDecrypt(data, key);
    }

    private static List<Character> encriptDecrypt(List<Character> data, List<Character> key){
        return IntStream
                .range(0, data.size())
                .map(i -> data.get(i) ^ key.get(i % key.size()))
                .mapToObj(i -> (char) i)
                .toList();
    }
}
