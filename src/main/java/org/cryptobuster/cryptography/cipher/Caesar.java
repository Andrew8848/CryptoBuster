package org.cryptobuster.cryptography.cipher;

import org.cryptobuster.cryptography.AlphabetHandler;
import org.cryptobuster.cryptography.ArrayUtil;
import org.cryptobuster.cryptography.Crypto;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;


public class Caesar implements Crypto {

    private static final Charset encoder = StandardCharsets.UTF_8;
    private char[] alphabet;

    public Caesar(String languageAlphabet) {
        switch (languageAlphabet){
            case AlphabetHandler.EN -> this.alphabet = AlphabetHandler.getEnAlphabet();
            case AlphabetHandler.UA -> this.alphabet = AlphabetHandler.getUaAlphabet();
        }
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key) {
        return toBytes(rotate(toChars(data), getShiftedKey(key), alphabet));
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key) {
        char[] reversedAlphabet = getReversedAlphabet(alphabet);
        return toBytes(rotate(toChars(data), getShiftedKey(key), reversedAlphabet));
    }

    public static char[] rotate(char[] data, int key, char[] alphabet) {
        char[] decrypted = new char[data.length];
        IntStream.range(0, data.length)
                .forEach(i -> {
                    int index = indexOf(data[i], alphabet);
                    decrypted[i] = index != -1 ? alphabet[(index + key) % alphabet.length] : data[i];
                });
        return decrypted;
    }

    private char[] getReversedAlphabet(char[] alphabet) {
        char[] reversed = new char[alphabet.length];
        IntStream.range(0, alphabet.length).forEach(i -> reversed[i] = alphabet[alphabet.length - i - 1]);
        return reversed;
    }

    public static int indexOf(char ch, char[] alphabet){
        for (int i = 0; i < alphabet.length; i++){
            if(ch == alphabet[i]) return i;
        }
        return -1;
    }

    private int getShiftedKey(byte[] key) {
        return IntStream.range(0, key.length).map(i -> key[i]).map(Math::abs).sum();
    }

    private char[] toChars(byte[] bytes){
        return new String(bytes, encoder).toCharArray();
    }

    private byte[] toBytes(char[] chars){
        return new String(chars).getBytes(encoder);
    }



}
