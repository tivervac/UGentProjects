package be.ugent.informationsecurity;

import be.ugent.informationsecurity.models.Booking;
import be.ugent.informationsecurity.server.Server;
import be.ugent.informationsecurity.utils.AES;
import be.ugent.informationsecurity.utils.PublicPrivateKey;
import be.ugent.informationsecurity.utils.Transform;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Date;

import static be.ugent.informationsecurity.SmartphoneApp.Command.*;

/**
 * This class represents the SmartphoneApp and all of its functions
 *
 * @author Eveline Hoogstoel, Wouter Pinnoo, Stefaan Vermassen & Titouan Vervack
 *         Group 1
 */
public class SmartphoneApp {

    private int uid;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Booking booking;
    private Server server;

    /**
     * Initializes the Smartphone application
     * Creates a public private key pair
     */
    public SmartphoneApp() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
            server = Server.getInstance();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a message containing the command and sends it to the car.
     * Before creating this message an AES key exchange is done with {@code Car} to allow to securely
     * send the aforementioned message.
     *
     * @param command The command to be executed
     * @param carId   The ID of the car on which we want to execute the command.
     */
    private void executeCommand(Command command, int carId) {
        if (booking == null) {
            System.err.println("ERROR -- booking is null. Book a car first!");
        } else if (booking.getCar() != null && booking.getCar().getUID() == carId) {
            byte[] content = command.getCommand().getBytes();

            SecretKey aesKey = null;
            try {
                aesKey = sendAESKey();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            byte[] emsg = encryptAES(content, aesKey, new Date().getTime(), "message");
            print("Sending message to car");
            booking.getCar().execute(emsg);
        } else {
            System.err.println("ERROR -- You're messing with car " + carId + " while the booking information suggests you're only allowed to car " + booking.getCar().getUID());
        }
    }

    /**
     * Generates an AES key, encrypts it with the public key of the car and signs it with the app's private key.
     * It then sends the message to the car and returns the key so it can be used to encrypt new messages.
     *
     * @return The generated AES key
     * @throws NoSuchAlgorithmException
     */
    private SecretKey sendAESKey() throws NoSuchAlgorithmException {
        SecretKey generatedKey = AES.generateKey();
        byte[] secretKey = generatedKey.getEncoded();
        System.out.println("APP: Generated AES key: " + Transform.bytesToHex(secretKey));

        byte[] msg = ByteBuffer.allocate(
                secretKey.length
                        + (Integer.SIZE / 8)
                        + (Long.SIZE / 8))
                .putInt(booking.getUid())
                .putLong(new Date().getTime())
                .put(secretKey)
                .array();

        byte[] emsg = encryptMsg(msg, booking.getCar().getPublicKey(), "AES");
        booking.getCar().receiveAppAESKey(emsg, PublicPrivateKey.sign(msg, privateKey));

        return generatedKey;
    }


    /**
     * Encrypts a message using the given AES key {@code key}
     *
     * @param smsg      The signed message that has to be encrypted
     * @param key       The AES key
     * @param timeStamp The current timestamp, used to prevent replay attacks
     * @param what      What is being encrypted, only used for prints
     * @return The encrypted message
     */
    private byte[] encryptAES(byte[] smsg, SecretKey key, long timeStamp, String what) {
        try {
            byte[] smsg2 = ByteBuffer.allocate(smsg.length
                    + (Integer.SIZE / 8)
                    + (Long.SIZE / 8))
                    .putInt(booking.getUid())
                    .putLong(timeStamp)
                    .put(smsg).array();
            byte[] emsg = AES.encrypt(smsg2, key);
            print("Encrypting " + what + " with AES key");
            return emsg;
        } catch (BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Encrypts a message using a public key {@code key} and sings it with our own private key
     *
     * @param msg  The message to encrypt
     * @param key  The public key
     * @param what What is being encrypted, used only for prints
     * @return The encrypted message
     */
    private byte[] encryptMsg(byte[] msg, Key key, String what) {
        byte[] emsg = PublicPrivateKey.encrypt(msg, key);
        print("Encrypting " + what + " with public key");

        return emsg;
    }

    /**
     * A utility method that prints a message to stdout
     *
     * @param s The string to print
     */
    private void print(String s) {
        System.out.println("APP: " + s + (s.endsWith(".|!|?") ? "" : "."));
    }

    /**
     * Sends a command to the car to unlock it
     *
     * @param carId The car to unlock
     */
    public void unlockCar(int carId) {
        executeCommand(UNLOCK, carId);
    }

    /**
     * Sends a command to the car to lock it
     *
     * @param carId The car to lock
     */
    public void lockCar(int carId) {
        executeCommand(LOCK, carId);
    }

    /**
     * Sends a command to the car to start it
     *
     * @param carId The car to start
     */
    public void startCar(int carId) {
        executeCommand(START, carId);
    }

    /**
     * Sends a command to the car to stop it
     *
     * @param carId The car to stop
     */
    public void stopCar(int carId) {
        executeCommand(STOP, carId);
    }


    /**
     * Book a car based on the carID
     * @param carUid the ID of the car the user wants to book
     */
    public void bookCar(int carUid) {
        SecretKey generatedKey = null;
        try {
            generatedKey = AES.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] secretKey = generatedKey.getEncoded();
        System.out.println("APP: Generated AES key: " + Transform.bytesToHex(secretKey));
        byte[] msg = ByteBuffer.allocate(secretKey.length
                + (Integer.SIZE / 8)
                + (Long.SIZE / 8))
                .putInt(uid)
                .putLong(new Date().getTime())
                .put(secretKey).array();
        server.executeAESKeyExchange(PublicPrivateKey.encrypt(msg, server.getPublicKey()), PublicPrivateKey.sign(msg, privateKey));

        byte[] bookingInfo = ByteBuffer.allocate((Integer.SIZE / 8)
                + (Long.SIZE / 8)
                + (Long.SIZE / 8)
                + (Long.SIZE / 8))
                .putInt(carUid)
                .putLong(new Date().getTime())
                .putLong(new Date(new Date().getTime() + 99999999L).getTime())
                .putLong(new Date().getTime()).array();
        try {
            booking = server.getBookingModule().book(PublicPrivateKey.encrypt(uid + "", server.getPublicKey()), AES.encrypt(bookingInfo, secretKey));
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.err.println("Something went wrong when encrypting bookingInfo with AES key in SmartphoneApp");
            e.printStackTrace();
        }

        print("Car " + carUid + " has been booked");
    }

    /**
     * Register a new user to the server
     * 1. app -> server: pub_server(AES)
     * 2. server -> app: AES(priv_server(UID))
     * 3. app->server: pub_server(UID)||AES(pub_app)
     */
    public void register() {
        SecretKey generatedKey = null;
        try {
            generatedKey = AES.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] secretKey = generatedKey.getEncoded();
        System.out.println("APP: Generated AES key: " + Transform.bytesToHex(secretKey));
        // 1. app -> server: pub_server(AES)
        System.out.println("SmartphoneApp -> Server: Send msg pub_server(" + Transform.bytesToHex(secretKey) + ")");
        byte[] encryptedUid = server.requestUID(PublicPrivateKey.encrypt(secretKey, server.getPublicKey()));
        try {

            byte[] decryptedUid = PublicPrivateKey.decrypt(AES.decryptToByte(encryptedUid, generatedKey), server.getPublicKey());
            uid = Integer.parseInt(new String(decryptedUid, "UTF-8"));

            System.out.println("SmartphoneApp received UID: " + uid);
            //App has now received the UID, it will now sends its publickey to the server
            //3. app->server: pub_server(UID)||AES(pub_app)
            System.out.println("SmartphoneApp -> Server: Send msg pub_server(" + uid + ")||AES(" + Transform.bytesToHex(publicKey.getEncoded()) + ")");
            server.register(PublicPrivateKey.encrypt(uid + "", server.getPublicKey()), AES.encrypt(publicKey.getEncoded(), secretKey));

        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.err.println("Something went wrong when decrypting UID with AES key in SmartphoneApp");
            e.printStackTrace();
        }
    }


    /**
     * Represents a command that can be send to the car
     */

    public enum Command {
        UNLOCK("unl", "unlock"),
        LOCK("loc", "lock"),
        START("sta", "start"),
        STOP("sto", "stop");

        private final String print;
        private final String command;

        /**
         * Initializes the command
         *
         * @param command The actual command
         * @param print   How the command should be printed
         */
        Command(String command, String print) {
            this.command = command;
            this.print = print;
        }

        /**
         * Returns the command
         *
         * @return The command
         */
        public String getCommand() {
            return command;
        }

        /**
         * Returns a textual representation of what this command does
         *
         * @return The textual representation of what this command does
         */
        @Override
        public String toString() {
            return print;
        }
    }
}
