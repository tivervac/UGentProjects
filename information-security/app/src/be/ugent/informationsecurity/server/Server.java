package be.ugent.informationsecurity.server;

import be.ugent.informationsecurity.utils.AES;
import be.ugent.informationsecurity.utils.PublicPrivateKey;
import be.ugent.informationsecurity.utils.Transform;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the Server and all of its functions
 *
 * @author Eveline Hoogstoel, Wouter Pinnoo, Stefaan Vermassen & Titouan Vervack
 *         Group 1
 */
public class Server {

    private static Server server;
    public final Map<Integer, PublicKey> users;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private SecretKeySpec initialAESkey;
    private BookingModule bm;
    private CarCommunicationModule cm;
    private BillingModule billingModule;

    public final Map<Integer, SecretKey> userAESKeys;
    private static final int SEND_DELAY = 100;

    private Server() {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeyPair pair = keyGen.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
        bm = new BookingModule();
        cm = new CarCommunicationModule();
        billingModule = new BillingModule();
        this.users = new HashMap<>();
        this.userAESKeys = new HashMap<>();

    }

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }

        return server;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() { //may only be used by the modules on the server
        return privateKey;
    }

    public PublicKey getUserPublicKey(int uid) {
        if (users.containsKey(uid)) {
            return users.get(uid);
        } else {
            System.err.println("No pubapp key foun for user " + uid);
            return null;
        }
    }

    public BillingModule getBillingModule()
    {
        return billingModule;
    }

    public BookingModule getBookingModule() {
        return bm;
    }

    public CarCommunicationModule getCarCommunicationModule() {
        return cm;
    }

    /**
     * Request a userID from the server
     * @param message The temp AES key
     * @return AES(priv_server(UID))
     */
    public byte[] requestUID(byte[] message){
        int uid = users.size();
        System.out.println("Server generated " + uid);
        byte[] decrypted = PublicPrivateKey.decrypt(message, privateKey);
        initialAESkey = new SecretKeySpec(decrypted, "AES");
        byte [] encodedUid = null;
        try {
            encodedUid = AES.encrypt(PublicPrivateKey.encrypt(uid+"", privateKey), initialAESkey);
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException |  UnsupportedEncodingException e) {
            System.err.println("Something went wrong when encrypting UID with AES key in Server");
            e.printStackTrace();
        }
        // 2. server -> app: AES(priv_server(UID))
        System.out.println("Server -> SmartphoneApp: Send msg AES(priv_server(" + uid + "))");
        return encodedUid;
    }

    /**
     * Send the app's public key via AES encryption
     * @param encryptedUid pub_server(UID)
     * @param encryptedPubApp AES(pub_app)
     */
    public void register(byte[] encryptedUid, byte[] encryptedPubApp) {
       try {
            byte[] decryptedUid = PublicPrivateKey.decrypt(encryptedUid, privateKey);
            int uid = Integer.parseInt(new String(decryptedUid, "UTF-8"));
            byte[] decrypted = AES.decryptToByte(encryptedPubApp, initialAESkey);
            X509EncodedKeySpec tmpKeySpec = new X509EncodedKeySpec(decrypted);
            PublicKey pubApp = KeyFactory.getInstance("RSA").generatePublic(tmpKeySpec);
            System.out.println("Server received " + Transform.bytesToHex(pubApp.getEncoded()));
            users.put(uid, pubApp);
            System.out.println("Server has stored the app's public key for " + uid + ", registration is now complete");
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException |  UnsupportedEncodingException | InvalidKeySpecException e) {
            System.err.println("Something went wrong when reconstructing the pubApp");
            e.printStackTrace();
        }

    }

    /**
     * Key exchange procedure, used for booking a car
     * @param msg pub_server(UID||timestamp||AES_to_exchange)
     * @param signature priv_app(UID||timestamp||AES_to_exchange)
     */
    public void executeAESKeyExchange(byte[] msg, byte[] signature){
        //Decrypt msg using public key
        byte[] dmsg = PublicPrivateKey.decrypt(msg, privateKey);
        int buidSize = Integer.SIZE / 8;
        int btimestampSize = Long.SIZE / 8;
        int dmsg2Size = dmsg.length - buidSize - btimestampSize;
        byte[] buid = new byte[buidSize];
        byte[] btimestamp = new byte[btimestampSize];
        byte[] dmsg2 = new byte[dmsg2Size];
        System.arraycopy(dmsg, 0, buid, 0, buidSize);
        System.arraycopy(dmsg, buidSize, btimestamp, 0, btimestampSize);
        System.arraycopy(dmsg, buidSize + btimestampSize, dmsg2, 0, dmsg2Size);

        int uid = Transform.getInt(buid);
        long currentTime = new Date().getTime();
        if (currentTime - Transform.getLong(btimestamp) <= SEND_DELAY) {
            if (users.containsKey(uid)) {
                if(PublicPrivateKey.verify(dmsg, users.get(uid), signature)){
                    userAESKeys.put(uid, new SecretKeySpec(dmsg2, "AES"));
                    System.out.println("SERVER: Received AES key for user " + uid + ": " + Transform.bytesToHex(new SecretKeySpec(dmsg2, "AES").getEncoded()));
                }else{
                    System.err.println("SERVER: Cannot verify identity");
                }

            } else {
                System.err.println("SERVER: User with UID=" + uid + " is unknown.");
            }
        } else {
            System.err.println("SERVER: Timestamp is too old, command ignored!");
        }
    }

}


