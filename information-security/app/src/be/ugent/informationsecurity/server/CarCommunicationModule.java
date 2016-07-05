package be.ugent.informationsecurity.server;

import be.ugent.informationsecurity.Car;
import be.ugent.informationsecurity.models.Bill;
import be.ugent.informationsecurity.models.Booking;
import be.ugent.informationsecurity.utils.AES;
import be.ugent.informationsecurity.utils.PublicPrivateKey;
import be.ugent.informationsecurity.utils.Transform;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class represents the Car Communication Module and all of its functions
 *
 * @author Eveline Hoogstoel, Wouter Pinnoo, Stefaan Vermassen & Titouan Vervack
 *         Group 1
 */
public class CarCommunicationModule {
    
    private static final int SEND_DELAY = 1000;

    private final int[] carUIDs = new int[]{1, 2, 3};
    private Map<Integer, Car> cars;
    private Map<Integer, SecretKey> carKeys;
    private Map<Integer, SecretKey> carMessageKeys;


    CarCommunicationModule() {
        cars = new HashMap<>();
        carKeys = new HashMap<>();
        carMessageKeys = new HashMap<>();
        for (int uid : carUIDs) {
            cars.put(uid, new Car(uid));
        }
    }

    /**
     * @return the cars
     */
    public Map<Integer, Car> getCars() {
        return cars;
    }

    /**
     * Send given booking information to the given car
     */
    public void sendBookingInfo(Booking booking) throws NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, UnsupportedEncodingException {
        Car tempCar = booking.getCar();
        /*generate and send AES key*/
        SecretKey AESKey = sendAESKey(tempCar);

        /*Encrypt with AES key*/
        byte[] message = booking.toJSON().getBytes();
        long timeStamp = new Date().getTime();
        System.out.println("CARCOMMUNICATIONMODULE: message: " + message);
        
        byte[] smsg2 = ByteBuffer.allocate(message.length
                    + (Integer.SIZE / 8)
                    + (Long.SIZE / 8))
                    .putInt(tempCar.getUID())
                    .putLong(timeStamp)
                    .put(message).array();

        byte[] encrypt = AES.encrypt(smsg2, AESKey);
        System.out.println("CARCOMMUNICATIONMODULE: send (encrypted with AES): " + Transform.bytesToHex(encrypt));
        tempCar.receiveBookingInfo(encrypt);

        System.out.println("--");
    }

    /**
     * Performs the key exchange with the car
     *
     * @return the secret AES key
     * @throws NoSuchAlgorithmException
     */
    private SecretKey sendAESKey(Car tempCar) throws NoSuchAlgorithmException {
        /*Get the public key or the dedicated car*/
        PublicKey publicKeyCar = tempCar.getPublicKey();

        /*Generate AES key and encrypt with the public key of the car*/
        SecretKey generatedKey = AES.generateKey();
        byte[] secretKey = generatedKey.getEncoded();
        System.out.println("CARCOMMUNICATIONMODULE: Generated AES key: " + Transform.bytesToHex(secretKey));
        byte[] msg = ByteBuffer.allocate(
                secretKey.length
                        + (Integer.SIZE / 8)
                        + (Long.SIZE / 8))
                .putInt(tempCar.getUID())
                .putLong(new Date().getTime())
                .put(secretKey)
                .array();

        byte[] encrypted = PublicPrivateKey.encrypt(msg, publicKeyCar);
        System.out.println("CARCOMMUNICATIONMODULE: Send (encrypted with public key): " + Transform.bytesToHex(encrypted));
        tempCar.receiveAESKey(encrypted, PublicPrivateKey.sign(msg, Server.getInstance().getPrivateKey()));

        carKeys.put(tempCar.getUID(), generatedKey);
        return generatedKey;
    }

    /**
     * Receive in process billing info from the car
     */
    public void receiveBillingInfo(int carid, byte[] message) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        long currentTime = new Date().getTime();
        byte[] dmsg = null;
        
        /* Decrypt message */
        SecretKey key = carMessageKeys.get(carid);
        dmsg = AES.decryptToByte(message, key);
        
        int buidSize = Integer.SIZE / 8;
        int btimestampSize = Long.SIZE / 8;
        int dmsg2Size = dmsg.length - buidSize - btimestampSize;
        byte[] buid = new byte[buidSize];
        byte[] btimestamp = new byte[btimestampSize];
        byte[] dmsg2 = new byte[dmsg2Size];
        System.arraycopy(dmsg, 0, buid, 0, buidSize);
        System.arraycopy(dmsg, buidSize, btimestamp, 0, btimestampSize);
        System.arraycopy(dmsg, buidSize + btimestampSize, dmsg2, 0, dmsg2Size);
        System.out.println("CARCOMMUNICATIONMODULE: Decrypted (AES) message: " + Transform.bytesToHex(dmsg2));
       
        if (currentTime - Transform.getLong(btimestamp) <= SEND_DELAY) {
            Bill bill = Bill.fromJSON(new String(dmsg2));
            Server.getInstance().getBillingModule().processBill(bill);
        } else {
            System.err.println("CARCOMMUNICATIONMODULE: Timestamp is too old, message ignored!");
        }
    }

    /**
     * Method to receive the AES key from the car when performing a key exchange
     */
    public boolean receiveAESKey(byte[] message, byte[] signature) {
        long currentTime = new Date().getTime();
        
        byte[] dmsg = PublicPrivateKey.decrypt(message, Server.getInstance().getPrivateKey());
        System.out.println("CARCOMMUNICATIONMODULE: Decrypted (public key) message: " + Transform.bytesToHex(dmsg));

        int buidSize = Integer.SIZE / 8;
        int btimestampSize = Long.SIZE / 8;
        int dmsg2Size = dmsg.length - buidSize - btimestampSize;
        byte[] carUid = new byte[buidSize];
        byte[] btimestamp = new byte[btimestampSize];
        byte[] dmsg2 = new byte[dmsg2Size];
        System.arraycopy(dmsg, 0, carUid, 0, buidSize);
        System.arraycopy(dmsg, buidSize, btimestamp, 0, btimestampSize);
        System.arraycopy(dmsg, buidSize + btimestampSize, dmsg2, 0, dmsg2Size);
        System.out.println("CARCOMMUNICATIONMODULE: Message was: " + Transform.bytesToHex(dmsg2));
        
        if (currentTime - Transform.getLong(btimestamp) <= SEND_DELAY) {
            if (PublicPrivateKey.verify(dmsg, cars.get(Transform.getInt(carUid)).getPublicKey(), signature)) {
                carMessageKeys.put(Transform.getInt(carUid), new SecretKeySpec(dmsg2, "AES"));
            } else {
                System.err.println("CARCOMMUNICATIONMODULE: Signature was forged! Key exchange canceled.");
                return false;
            }
        }
        else{
            System.err.println("CARCOMMUNICATIONMODULE: Timestamp is too old, command ignored!");
            return false;
        }
        
        return true;
    }


}
