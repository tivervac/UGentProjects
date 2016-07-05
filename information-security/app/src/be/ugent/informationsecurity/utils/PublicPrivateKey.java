package be.ugent.informationsecurity.utils;

import javax.crypto.Cipher;
import java.security.*;

/**
 * Provides the functions needed to work with public-key cryptography
 *
 * @author Eveline Hoogstoel, Wouter Pinnoo, Stefaan Vermassen & Titouan Vervack
 *         Group 1
 */
public class PublicPrivateKey {

    public static final String ALGORITHM = "RSA";

    public static byte[] encrypt(String text, Key key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public static byte[] encrypt(byte[] text, Key key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public static byte[] decrypt(byte[] text, Key key) {
        byte[] dectyptedText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dectyptedText;
    }

    public static byte[] sign(byte[] data, PrivateKey key) {
        Signature signer = null;
        try {
            signer = Signature.getInstance("SHA1withRSA");
            signer.initSign(key);
            signer.update(data);
            return (signer.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verify(byte[] data, PublicKey key, byte[] sig) {
        Signature signer = null;
        try {
            signer = Signature.getInstance("SHA1withRSA");
            signer.initVerify(key);
            signer.update(data);
            return (signer.verify(sig));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
