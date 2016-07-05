package be.ugent.informationsecurity.server;

import be.ugent.informationsecurity.models.Bill;
import be.ugent.informationsecurity.utils.AES;
import be.ugent.informationsecurity.utils.PublicPrivateKey;

import javax.crypto.SecretKey;
import java.security.*;

/**
 * This class represents the Billing Module and all of its functions
 *
 * @author Eveline Hoogstoel, Wouter Pinnoo, Stefaan Vermassen & Titouan Vervack
 *         Group 1
 */
public class BillingModule {

    public BillingModule() {
    }

    public void processBill(Bill bill) {
        bill.setAmount(10 + 0.35 * bill.getKm());
        System.out.println("BILLINGMODULE: User with UID " + bill.getAppUid() + " billed for the amount of €" + bill.getAmount());
    }
}
