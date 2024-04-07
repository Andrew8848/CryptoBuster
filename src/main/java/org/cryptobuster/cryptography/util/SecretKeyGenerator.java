package org.cryptobuster.cryptography.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.Collectors;

public class SecretKeyGenerator {

    private static final char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();

    public static byte[] generateKey(int length){
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return random.ints(length, 0, chars.length)
                .mapToObj(i -> chars[i])
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString().getBytes();
    }

    public static byte[] generateKey(int length, int seed){
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        random.setSeed(seed);
        return random.ints(length, 0, chars.length)
                .mapToObj(i -> chars[i])
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString().getBytes();
    }

//    public static byte[] generateKey(int length, int seed){
//        byte[] key = new byte[length];
//
//        SecureRandom random = null;
//        try {
//            random = SecureRandom.getInstance("SHA1PRNG");
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        random.setSeed(seed);
//        random.nextBytes(key);
//        return key;
//    }
}
