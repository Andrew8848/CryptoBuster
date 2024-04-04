package org.cryptobuster.cryptography.caesar;

import org.cryptobuster.cryptography.Crypto;
import org.cryptobuster.cryptography.CryptoFromLib;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class AES extends CryptoFromLib implements Crypto {
    public AES() {
        super("AES", CryptoFromLib.AES_KEY_LENGTH, CryptoFromLib.AES_IV_LENGTH);
    }

    @Override
    public List<Character> encrypt(List<Character> data, List<Character> key) throws BadPaddingException {
        String encrypted = null;
        try {
            encrypted = encrypt(getCipherTransformation(), charsToString(data), getKeyFromPassword(charToPrimitiveChar(key)), generateIv(toByte(key)));
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return stringToChars(encrypted);
    }

    @Override
    public List<Character> decrypt(List<Character> data, List<Character> key) throws BadPaddingException {
        String encrypted = null;
        try {
            encrypted = decrypt(getCipherTransformation(), charsToString(data), getKeyFromPassword(charToPrimitiveChar(key)), generateIv(toByte(key)));
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }  catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return stringToChars(encrypted);
    }
}
