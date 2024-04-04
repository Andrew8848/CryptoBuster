package org.cryptobuster.cryptography.caesar;

import org.cryptobuster.cryptography.AlphabetHandler;
import org.cryptobuster.cryptography.Crypto;


import java.util.List;
import java.util.function.Function;


public class Caesar implements Crypto {

    private final List<Character> alphabet;

    public Caesar() {
        this.alphabet = new AlphabetHandler(AlphabetHandler.EN).getAlphabet();
    }

    @Override
    public List<Character> encrypt(List<Character> data, List<Character> key) {
        return data.stream().map(getShiftedCharacter(getShiftedKey(key))).toList();
    }

    @Override
    public List<Character> decrypt(List<Character> data, List<Character> key) {
        return data.stream().map(getReversedCharacter(getShiftedKey(key))).toList();
    }

    private Function<Character, Character> getShiftedCharacter(int key){
        return character -> {
            int index = this.alphabet.indexOf(character);
            return index != -1 ? this.alphabet.get(Math.floorMod(index + key, this.alphabet.size())) : character;
        };
    }

    private Function<Character, Character> getReversedCharacter(int key){
        return character -> {
            int index = this.alphabet.indexOf(character);
            return index != -1 ? this.alphabet.get(Math.floorMod(index - key, this.alphabet.size())) : character;
        };
    }

    private int getShiftedKey(List<Character> key) {
        return key.stream().mapToInt(Character::charValue).sum();
    }
}
