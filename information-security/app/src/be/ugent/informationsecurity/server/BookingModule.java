package be.ugent.informationsecurity.server;

import be.ugent.informationsecurity.Car;
import be.ugent.informationsecurity.models.Booking;
import be.ugent.informationsecurity.utils.AES;
import be.ugent.informationsecurity.utils.PublicPrivateKey;
import be.ugent.informationsecurity.utils.Transform;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * This class represents the Car Communication Module and all of its functions
 *
 * @author Eveline Hoogstoel, Wouter Pinnoo, Stefaan Vermassen & Titouan Vervack
 *         Group 1
 */
public class BookingModule {

    private final List<Booking> bookings;
    private static final int SEND_DELAY = 100;

    public BookingModule() {
        this.bookings = new ArrayList<>();
    }

    private Booking book(int appUid, int carid, Date start, Date stop) {
        Map<Integer, Car> cars = Server.getInstance().getCarCommunicationModule().getCars();
        Car car = cars.get(carid);

        Booking booking = new Booking();
        booking.setStart(start);
        booking.setStop(stop);
        booking.setAppUid(appUid);
        booking.setCar(car);
        booking.setUid();
        booking.setPubAppKey(Server.getInstance().getUserPublicKey(appUid));
        bookings.add(booking);

        try {
            Server.getInstance().getCarCommunicationModule().sendBookingInfo(booking);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return booking;
    }

    /**
     * Reservation of a car, receives msg pub_server(UID)||AES(booking_info)
     * @param encryptedUid pub_server(UID)
     * @param msg AES(booking_info), booking°info inclused carid, startepoch, endepoch and timestamp
     * @return success
     */
    public Booking book(byte[] encryptedUid, byte[] msg){
        byte[] decryptedUid = PublicPrivateKey.decrypt(encryptedUid, Server.getInstance().getPrivateKey());
        try {
            int uid = Integer.parseInt(new String(decryptedUid, "UTF-8"));
            int integerSize = Integer.SIZE/8;
            int longSize = Long.SIZE/8;
            byte[] dmsg = AES.decryptToByte(msg, Server.getInstance(). userAESKeys.get(uid));
            byte[] bCarid = new byte[integerSize];
            byte[] bStartEpoch = new byte[longSize];
            byte[] bEndEpoch = new byte[longSize];
            byte[] bTimeStamp = new byte[longSize];
            System.arraycopy(dmsg, 0, bCarid, 0, integerSize);
            System.arraycopy(dmsg, integerSize, bStartEpoch, 0, longSize);
            System.arraycopy(dmsg, integerSize + longSize, bEndEpoch, 0, longSize);
            System.arraycopy(dmsg, integerSize + longSize*2, bTimeStamp, 0, longSize);
            long currentTime = new Date().getTime();
            if (currentTime - Transform.getLong(bTimeStamp) <= SEND_DELAY) {
                System.out.println("BOOKINGMODULE: Has decrypted the bookinginfo with the temp AES key: user " + uid + " has booked car "  + Transform.getInt(bCarid) + " from " + new Date(Transform.getLong(bStartEpoch)).toString() + " to " + new Date(Transform.getLong(bEndEpoch)));
                return book(uid, Transform.getInt(bCarid), new Date(Transform.getLong(bStartEpoch)), new Date(Transform.getLong(bEndEpoch)));
            }else {
                System.err.println("BOOKINGMODULE: Timestamp is too old, command ignored!");
            }
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }
}
