package org.cryptobuster.cryptography;

import lombok.Getter;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Getter
public abstract class CryptoFromLib {

    public static final int AES_IV_LENGTH = 16;
    public static final int DES_IV_LENGTH = 8;
    public static final int DESede_IV_LENGTH = DES_IV_LENGTH;

    public static final int AES_KEY_LENGTH = 256;
    public static final int DES_KEY_LENGTH = 64;
    public static final int DESede_KEY_LENGTH = 192;


    private static final String KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 65536;

    private int keyLength;
    private int ivLength;

    private static final byte[] SALT = new byte[]{118, 9, 62, -73, -88, -67, -83, -36};

    private String algorithm;
    private String cipherTransformation;

    private static final Charset encoder = StandardCharsets.UTF_8;

    public CryptoFromLib(String algorithm, int keyLength, int ivLength) {
        this.algorithm = algorithm;
        this.keyLength = keyLength;
        this.ivLength = ivLength;
        this.cipherTransformation = algorithm + "/CBC/PKCS5Padding";
    }

    public SecretKey getKeyFromPassword(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, SALT, ITERATION_COUNT, keyLength);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), this.algorithm);
    }

    public IvParameterSpec generateIv(byte[] passwordAsSeed) {
        byte[] iv = new byte[this.ivLength];
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        random.setSeed(passwordAsSeed);
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static byte[] encrypt(String algorithm, byte[] input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input);
        return Base64.getEncoder()
                .encode(cipherText);
    }

    public static byte[] decrypt(String algorithm, byte[] cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return plainText;
    }

    public static char[] toChars(byte[] bytes){
        return new String(bytes, StandardCharsets.UTF_8).toCharArray();
    }

}
