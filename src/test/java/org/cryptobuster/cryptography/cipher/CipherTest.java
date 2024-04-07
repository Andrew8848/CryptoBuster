package org.cryptobuster.cryptography.cipher;


import lombok.Getter;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import java.util.List;
import java.util.stream.IntStream;

public abstract class CipherTest {

    private String data = "Hello Test";
    private String key = "key";

    public abstract void crypt() throws BadPaddingException;

    public static List<Character> toList(String str){
        return toList(str.toCharArray());
    }

    private static List<Character> toList(char[] chars){
        return IntStream.range(0, chars.length).mapToObj(i -> chars[i]).toList();
    }

    public String getData() {
        return data;
    }

    public String getKey() {
        return key;
    }
}
