package org.cryptobuster.cryptography.caesar;

import org.cryptobuster.cryptography.AlphabetHandler;
import org.cryptobuster.cryptography.Crypto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;


public class Trithemius implements Crypto {

    private final List<Character> alphabet;

    public Trithemius() {
        this.alphabet = new AlphabetHandler(AlphabetHandler.EN).getAlphabet();
    }

    @Override
    public List<Character> encrypt(List<Character> data, List<Character> key) {
        return rotateIncrementAndGetChar(data, getShiftedKey(key), this.alphabet);
    }

    @Override
    public List<Character> decrypt(List<Character> data, List<Character> key) {
        List<Character> reversedAlphabet = new ArrayList<>(this.alphabet);
        Collections.reverse(reversedAlphabet);
        return rotateIncrementAndGetChar(data, getShiftedKey(key), reversedAlphabet);
    }

    private List<Character> rotateIncrementAndGetChar(List<Character> data, int rotationKey, List<Character> alphabet) {
        AtomicInteger i = new AtomicInteger();
        return data.stream().map(getShiftedCharacter(rotationKey, alphabet, i)).toList();
    }

    private Function<Character, Character> getShiftedCharacter(int rotationKey, List<Character> alphabet, AtomicInteger i){
        return character -> {
            int index = alphabet.indexOf(character);
            return index != -1 ? alphabet.get((index + rotationKey + i.getAndIncrement()) % alphabet.size()) : character;
        };
    }

    private int getShiftedKey(List<Character> key) {
        return key.stream().mapToInt(Character::charValue).sum();
    }
}
