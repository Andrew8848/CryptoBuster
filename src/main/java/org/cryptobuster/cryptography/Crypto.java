package org.cryptobuster.cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface Crypto {
    public byte[] encrypt(byte[] data, byte[] key) throws BadPaddingException, IllegalArgumentException;

    public byte[] decrypt(byte[] data, byte[] key) throws BadPaddingException, IllegalArgumentException;

}
