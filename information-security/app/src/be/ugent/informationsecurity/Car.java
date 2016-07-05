package be.ugent.informationsecurity;

import be.ugent.informationsecurity.models.Bill;
import be.ugent.informationsecurity.models.Booking;
import be.ugent.informationsecurity.server.CarCommunicationModule;
import be.ugent.informationsecurity.server.Server;
import be.ugent.informationsecurity.utils.AES;
import be.ugent.informationsecurity.utils.PublicPrivateKey;
import be.ugent.informationsecurity.utils.Transform;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the Car and all of its functions
 *
 * @author Eveline Hoogstoel, Wouter Pinnoo, Stefaan Vermassen & Titouan Vervack
 *         Group 1
 */
public class Car {

    // 100 ms send delay allowed.
    private static final int SEND_DELAY = 100;
    private int uid;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private SecretKeySpec tempKey;
    private SecretKeySpec tempAppKey;
    private Map<Integer, Booking> bookings;
    private double distance;
    private Date initTime;

    /**
     * Initializes a Car
     * Creates a public private key pair
     *
     * @param uid The id of the car
     */
    public Car(int uid) {
        bookings = new HashMap<>();
        this.uid = uid;
        this.distance = 0;
        this.initTime = new Date();

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the public key of this car
     *
     * @return The publicKey
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Returns the id of this car
     *
     * @return The id of the car
     */
    public int getUID() {
        return uid;
    }

    /**
     * Receives an AES key from the {@code CarCommunicationModule}
     *
     * @param message The message containing the key
     */
    public void receiveAESKey(byte[] message, byte[] signature) {
        decrypt(message, privateKey, Mode.AES_CCM, signature);
    }

    /**
     * Receives an AES key from the {@code SmartphoneApplication}
     *
     * @param message   The message containing the key
     * @param signature The signature of the message
     */
    public void receiveAppAESKey(byte[] message, byte[] signature) {
        decrypt(message, privateKey, Mode.AES_APP, signature);
    }

    /**
     * Receives the booking information and stores it in the memory of the car.
     * 
     * @param message encrypted message
     */
    public void receiveBookingInfo(byte[] message) {
        decrypt(message, privateKey, Mode.CCM, null);
    }

    /**
     * Executes a command if this all the checks pass
     *
     * @param rmsg The message containing the command, booking id and a timestamp (to prevent replay attacks)
     */
    public void execute(byte[] rmsg) {
        decrypt(rmsg, tempAppKey, Mode.COMMAND, null);
    }

    /**
     * Decrypts an message that contains the AES key of the {@code SmartphoneApplication}
     * or the {@code CarCommunicationModule}.
     * It can also decrypt a message that contains a command to execute
     *
     * @param rmsg      The message to decrypt
     * @param key       The key to be used for decryption (an AES key or our own private key
     * @param mode      Decides which kind of message to decrypt (AES or command)
     * @param signature The signature used in AES key exchange, null in the case of command
     */
    private void decrypt(byte[] rmsg, Key key, Mode mode, byte[] signature) {
        long currentTime = new Date().getTime();
        byte[] dmsg = null;

        if (mode == Mode.AES_APP || mode == Mode.AES_CCM) {
            dmsg = PublicPrivateKey.decrypt(rmsg, key);
            System.out.println("CAR: Decrypted message with public key: " + Transform.bytesToHex(dmsg));
        } else if(mode == Mode.CCM){
            try {
                dmsg = AES.decryptToByte(rmsg, tempKey);
                System.out.println("CAR: Decrypted message with AES key: " + Transform.bytesToHex(dmsg));
            } catch (NoSuchPaddingException | BadPaddingException | InvalidKeyException | IllegalBlockSizeException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else { //MODE.COMMAND
            try {
                dmsg = AES.decryptToByte(rmsg, (SecretKeySpec) key);
                System.out.println("CAR: Decrypted message with AES key: " + Transform.bytesToHex(dmsg));
            } catch (NoSuchPaddingException | BadPaddingException | InvalidKeyException | IllegalBlockSizeException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        int buidSize = Integer.SIZE / 8;
        int btimestampSize = Long.SIZE / 8;
        int dmsg2Size = dmsg.length - buidSize - btimestampSize;
        byte[] buid = new byte[buidSize];
        byte[] btimestamp = new byte[btimestampSize];
        byte[] dmsg2 = new byte[dmsg2Size];
        System.arraycopy(dmsg, 0, buid, 0, buidSize);
        System.arraycopy(dmsg, buidSize, btimestamp, 0, btimestampSize);
        System.arraycopy(dmsg, buidSize + btimestampSize, dmsg2, 0, dmsg2Size);
        System.out.println("CAR: Message was: " + Transform.bytesToHex(dmsg2));
        
        if (currentTime - Transform.getLong(btimestamp) <= SEND_DELAY) {
            if(mode == Mode.AES_CCM){
                if (PublicPrivateKey.verify(dmsg, Server.getInstance().getPublicKey(), signature)) {
                    tempKey = new SecretKeySpec(dmsg2, "AES");
                } else {
                    System.err.println("CAR: Signature was forged! Key exchange canceled.");
                }
            }else if(mode == Mode.CCM){
                Booking booking = Booking.fromJSON(new String(dmsg2));
                if (booking != null) {
                    bookings.put(booking.getUid(), booking);
                }
            }
            else {//communication from app
                int bookingUid = Transform.getInt(buid);
                if (bookings.containsKey(bookingUid)) {
                    if (mode == Mode.AES_APP) {
                        if (PublicPrivateKey.verify(dmsg, bookings.get(bookingUid).getPubAppKey(), signature)) {
                            tempAppKey = new SecretKeySpec(dmsg2, "AES");
                        } else {
                            System.err.println("CAR: Signature was forged! Key exchange canceled.");
                        }
                    }else {
                        execute(currentTime, bookingUid, dmsg2);
                    }
                } else {
                    System.err.println("CAR: Booking with UID=" + bookingUid + " is not known in this car.");
                }
            }
        } else {
            System.err.println("CAR: Timestamp is too old, command ignored!");
        }
    }

    /**
     * Checks if we are still allowed to issue commands to the car. If we are the command is executed.
     * If the command that was executed is the {@code Command.LOCK}, billing info is send to the {@code BillingModule}.
     *
     * @param currentTime The current time, used to check if we're still allowed to execute commands
     * @param bookingUid  The booking id
     * @param bcmd        The command to execute
     */
    private void execute(long currentTime, int bookingUid, byte[] bcmd) {
        Booking booking = bookings.get(bookingUid);

        int appUid = booking.getAppUid();
        if (currentTime >= booking.getStart().getTime()
                && currentTime <= booking.getStop().getTime()) {
            String command = Transform.getString(bcmd);
            System.out.println("CAR: User with UID=" + appUid + " executed command=" + command);

            if (SmartphoneApp.Command.UNLOCK.getCommand().equals(command)) {
                System.out.println("Car " + uid + " has been unlocked (booking UID " + bookingUid + ").");
            } else if (SmartphoneApp.Command.START.getCommand().equals(command)) {
                System.out.println("Car " + uid + " has been started (booking UID " + bookingUid + ").");
            } else if (SmartphoneApp.Command.STOP.getCommand().equals(command)) {
                System.out.println("Car " + uid + " has been stopped (booking UID " + bookingUid + ").");
            } else if (SmartphoneApp.Command.LOCK.getCommand().equals(command)) {
                CarCommunicationModule ccm = Server.getInstance().getCarCommunicationModule();
                try {
                    Bill bill = new Bill();
                    bill.setAppUid(appUid);
                    bill.setBookingid(booking.getUid());
                    // Riding 50km/h
                    distance += 50 * 60 * 60 * 1000 * (currentTime - initTime.getTime());
                    bill.setKm(distance);
                    bill.setStartDate(booking.getStart());
                    bill.setEndDate(new Date());
                    byte[] json = bill.toJSON().getBytes();
                    long timeStamp = new Date().getTime();
                    
                    byte[] smsg = ByteBuffer.allocate(json.length
                            + (Integer.SIZE / 8)
                            + (Long.SIZE / 8))
                            .putInt(uid)
                            .putLong(timeStamp)
                            .put(json).array();

                    tempKey = (SecretKeySpec) sendAESkey();
                    byte[] encrypted = AES.encrypt(smsg, tempKey);
                    ccm.receiveBillingInfo(uid, encrypted);

                } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Do a key exchange (AES key) with the server (CarCommunicationModule).
     */
    private SecretKey sendAESkey() throws NoSuchAlgorithmException {
        PublicKey publicKeyServer = Server.getInstance().getPublicKey();

        /*Generate AES key en encrypt with the public key of the server*/
        SecretKey generatedKey = AES.generateKey();
        byte[] secretKey = generatedKey.getEncoded();
        System.out.println("CAR: Generated AES key: " + Transform.bytesToHex(secretKey));
        byte[] msg = ByteBuffer.allocate(
                secretKey.length
                        + (Integer.SIZE / 8)
                        + (Long.SIZE / 8))
                .putInt(this.getUID())
                .putLong(new Date().getTime())
                .put(secretKey)
                .array();

        byte[] encrypted = PublicPrivateKey.encrypt(msg, publicKeyServer);
        System.out.println("CAR: Send (encrypted with public key): " + Transform.bytesToHex(encrypted));
        Server.getInstance().getCarCommunicationModule().receiveAESKey(encrypted, PublicPrivateKey.sign(msg, privateKey));
        return generatedKey;
    }

    /**
     * Decides what message has to be decrypted, an AES message or a command
     */
    private enum Mode {
        AES_APP, AES_CCM, COMMAND, CCM,
    }
}
