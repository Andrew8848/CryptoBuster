package org.cryptobuster.cryptography.cipher;


import javax.crypto.BadPaddingException;
import java.util.List;
import java.util.stream.IntStream;

public abstract class CipherTestUA extends CipherTest{

    private String data = "Добрий день світ";
    private String key = "Ключ";


    @Override
    public String getData() {
        return data;
    }

    @Override
    public String getKey() {
        return key;
    }
}
