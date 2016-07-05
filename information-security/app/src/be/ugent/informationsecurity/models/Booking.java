package be.ugent.informationsecurity.models;

import be.ugent.informationsecurity.Car;
import be.ugent.informationsecurity.utils.Transform;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

/**
 * This class represents a booking of a car by a user
 *
 * @author Eveline Hoogstoel, Wouter Pinnoo, Stefaan Vermassen & Titouan Vervack
 *         Group 1
 */
public class Booking {

    // Keeps track of #bookings
    private static int bookingUid;
    private int uid;
    private Date start;
    private Date stop;
    private int appUid;

    @JsonIgnore
    private PublicKey pubAppKey;

    /**
     * This is only used as util property during conversion from object to json and vise versa.
     */
    private String pubAppKeyBytes;

    @JsonIgnore
    private Car car;

    public Booking() {
    }

    public static Booking fromJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            Booking booking = mapper.readValue(json, Booking.class);
            booking.setPubAppKeyFromJSON();
            return booking;
        } catch (IOException e) {
            System.err.println("Parsing Booking from json failed.");
            e.printStackTrace();
        }
        return new Booking();
    }

    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);

        try {
            String json = mapper.writeValueAsString(this);
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
            jsonObject.put("pubAppKeyBytes", Transform.bytesToHex(pubAppKey.getEncoded()));
            return jsonObject.toJSONString();
        } catch (IOException | ParseException e) {
            System.err.println("Parsing Booking from json failed.");
            e.printStackTrace();
        }
        return "{}";
    }

    public int getUid() {
        return uid;
    }

    public void setUid() {
        this.uid = bookingUid++;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStop() {
        return stop;
    }

    public void setStop(Date stop) {
        this.stop = stop;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public PublicKey getPubAppKey() {
        return pubAppKey;
    }

    public void setPubAppKey(PublicKey pubAppKey) {
        this.pubAppKey = pubAppKey;
    }

    public void setPubAppKeyFromJSON() {
        byte[] bytes = Transform.hexToBytes(pubAppKeyBytes);
        X509EncodedKeySpec tmpKeySpec = new X509EncodedKeySpec(bytes);
        try {
            pubAppKey = KeyFactory.getInstance("RSA").generatePublic(tmpKeySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public int getAppUid() {
        return appUid;
    }

    public void setAppUid(int appUid) {
        this.appUid = appUid;
    }
}
