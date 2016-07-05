package be.ugent.informationsecurity.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Provides the functions needed to work with AES
 *
 * @author Eveline Hoogstoel, Wouter Pinnoo, Stefaan Vermassen & Titouan Vervack
 *         Group 1
 */
public class AES {

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        return keyGen.generateKey();
    }

    public static byte[] encrypt(byte[] msg, byte[] key) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(msg);
    }

    public static byte[] encrypt(byte[] msg, SecretKey key) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        return encrypt(msg, key.getEncoded());
    }

    public static byte[] decryptToByte(byte[] cipherText, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(cipherText);
    }

    public static byte[] decryptToByte(byte[] cipherText, SecretKey key) throws NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        return decryptToByte(cipherText, key.getEncoded());
    }

}
