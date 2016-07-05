package be.ugent.informationsecurity;

import be.ugent.informationsecurity.server.Server;
import org.apache.http.util.TextUtils;

import java.util.Arrays;
import java.util.Scanner;

/**
 * This class handles all initial interaction with the user and starts the correct modules
 *
 * @author Eveline Hoogstoel, Wouter Pinnoo, Stefaan Vermassen & Titouan Vervack
 *         Group 1
 */
public class Main {

    private SmartphoneApp app = new SmartphoneApp();
    private static final String COMMANDS_TEXT = "Commands: quit; register; list cars; (book|open|close|start|stop) car <carUid>";

    public static void main(String[] args) {
        Main main = new Main();
        main.processInput();
    }

    private void processInput() {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println(COMMANDS_TEXT);
        while (!"quit".equals(input = scanner.nextLine())) {
            processCommand(input);
            System.out.println();
            System.out.println(COMMANDS_TEXT);
        }
        System.out.println("Bye.");
        System.exit(1);
    }

    private void processCommand(String input) {
        if (TextUtils.isEmpty(input)) {
            return;
        }

        if ("register".equals(input)) {
            app.register();
        } else if ("list cars".equals(input)) {
            String cars = String.join(", ", Server.getInstance().getCarCommunicationModule().getCars().keySet().toString());
            System.out.println("Available cars: " + cars);
        } else {
            String[] tmpArr = input.split(" ");
            if (tmpArr.length >= 3) {
                int carUid = Integer.parseInt(Arrays.copyOfRange(tmpArr, 2, tmpArr.length)[0]);

                if (input.startsWith("book car")) {
                    app.bookCar(carUid);
                } else if (input.startsWith("open car")) {
                    app.unlockCar(carUid);
                } else if (input.startsWith("close car")) {
                    app.lockCar(carUid);
                } else if (input.startsWith("start car")) {
                    app.startCar(carUid);
                } else if (input.startsWith("stop car")) {
                    app.stopCar(carUid);
                } else {
                    System.err.println("Unknown command.");
                }
            } else {
                System.err.println("You must provide a car UID.");
            }
        }
    }
}
