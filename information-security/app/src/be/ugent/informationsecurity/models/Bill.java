/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ugent.informationsecurity.models;


import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Date;

/**
 * This class represents a bill for a {@code Booking}
 *
 * @author Eveline Hoogstoel, Wouter Pinnoo, Stefaan Vermassen & Titouan Vervack
 *         Group 1
 */
public class Bill {

    private int appUid;
    private int bookingid;
    private double km;
    private double amount;
    private Date startDate;
    private Date endDate;

    public Bill() {
    }

    public static Bill fromJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            return mapper.readValue(json, Bill.class);
        } catch (IOException e) {
            System.err.println("Parsing bill from json failed.");
            e.printStackTrace();
        }
        return new Bill();
    }

    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            return mapper.writeValueAsString(this);
        } catch (IOException e) {
            System.err.println("Parsing bill from json failed.");
            e.printStackTrace();
        }
        return "{}";
    }

    /**
     * @return the appUid
     */
    public int getAppUid() {
        return appUid;
    }

    public void setAppUid(int appUid) {
        this.appUid = appUid;
    }

    /**
     * @return the km
     */
    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }
}
