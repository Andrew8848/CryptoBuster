package org.cryptobuster.cryptography;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ArrayUtil {
    public static String charsToString(List<Character> list){
        return list.stream().map(String::valueOf).collect(Collectors.joining());
    }

    public static List<Character> toChars(byte[] bytes){
        char[] chars = new String(bytes, StandardCharsets.UTF_8).toCharArray();
        return IntStream.range(0, chars.length).mapToObj(i -> chars[i]).toList();
    }

    public static List<Character> stringToChars(char[] chars){
        return IntStream.range(0, chars.length).mapToObj(i -> chars[i]).toList();
    }

    public static List<Character> stringToChars(String str){
        return str.chars().mapToObj(ch -> (char) ch).toList();
    }

    public static char[] charToPrimitiveChar(List<Character> chars){
        return chars.stream().map(String::valueOf).collect(Collectors.joining()).toCharArray();
    }

    public static byte[] toByte(List<Character> chars){
        return chars.stream().map(String::valueOf).collect(Collectors.joining()).getBytes();
    }
}
