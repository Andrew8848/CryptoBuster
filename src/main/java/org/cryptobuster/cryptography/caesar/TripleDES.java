package org.cryptobuster.cryptography.caesar;

import org.cryptobuster.cryptography.Crypto;
import org.cryptobuster.cryptography.CryptoFromLib;

import javax.crypto.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class TripleDES extends CryptoFromLib implements Crypto {

    public TripleDES() {
        super("DESede", CryptoFromLib.DESede_KEY_LENGTH, CryptoFromLib.DESede_IV_LENGTH);
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
        }  catch (IllegalBlockSizeException e) {
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
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return stringToChars(encrypted);
    }
}
