package org.cryptobuster.cryptography.caesar;

import org.cryptobuster.cryptography.Crypto;
import org.cryptobuster.cryptography.CryptoFromLib;

import javax.crypto.*;
import javax.sound.sampled.AudioFormat;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class TripleDES extends CryptoFromLib implements Crypto {

    public TripleDES() {
        super("DESede", CryptoFromLib.DESede_KEY_LENGTH, CryptoFromLib.DESede_IV_LENGTH);
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key) throws BadPaddingException {
        byte[] encrypted;
        try {
            encrypted = encrypt(getCipherTransformation(), data, getKeyFromPassword(toChars(key)), generateIv(key));
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
        return encrypted;
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key) throws BadPaddingException {
        byte[] encrypted;
        try {
            encrypted = decrypt(getCipherTransformation(), data, getKeyFromPassword(toChars(key)), generateIv(key));
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
        return encrypted;
    }
}
