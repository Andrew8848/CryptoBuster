package org.cryptobuster.cryptography.caesar;

import org.cryptobuster.cryptography.AlphabetHandler;
import org.cryptobuster.cryptography.ArrayUtil;
import org.cryptobuster.cryptography.Crypto;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;


public class Caesar implements Crypto {

    private final List<Character> alphabet;

    public Caesar(String languageAlphabet) {
        this.alphabet = new AlphabetHandler(languageAlphabet).getAlphabet();
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key) {
        List<Character> dataInChars = ArrayUtil.toChars(data);
        List<Character> outputData = dataInChars.stream().map(getShiftedCharacter(getShiftedKey(key))).toList();;
        return ArrayUtil.charsToString(outputData).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key) {
        List<Character> dataInChars = ArrayUtil.toChars(data);
        List<Character> outputData = dataInChars.stream().map(getReversedCharacter(getShiftedKey(key))).toList();
        return ArrayUtil.charsToString(outputData).getBytes(StandardCharsets.UTF_8);
    }


    private Function<Character, Character> getShiftedCharacter(int key){
        return character -> {
            int index = this.alphabet.indexOf(character);
            return index != -1 ? this.alphabet.get((index + key) % this.alphabet.size()) : character;
        };
    }

    private Function<Character, Character> getReversedCharacter(int key){
        return character -> {
            List<Character> reversedAlphabet = new ArrayList<>(this.alphabet);
            Collections.reverse(reversedAlphabet);
            int index = reversedAlphabet.indexOf(character);
            return index != -1 ? reversedAlphabet.get((index + key) % reversedAlphabet.size()) : character;
        };
    }

    private int getShiftedKey(byte[] key) {
        return IntStream.range(0, key.length).map(i -> key[i]).sum();
    }



}
