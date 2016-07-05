package tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Titouan Vervack
 * Logs results to a pretty file and a pure data file.
 */
public class Logger {

    PrintWriter prettyWriter;
    PrintWriter numWriter;

    public Logger(File prettyFile, File numFile) {
        try {
            prettyWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(prettyFile), "utf-8")));
            numWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(numFile), "utf-8")));
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            System.err.println("Failed to initialise logfiles.");
            System.err.println("Quiting...");
            System.exit(1);
        }
    }

    public void log(String message) {
        prettyWriter.println(message);
        prettyWriter.flush();
    }

    public void logFail(String message) {
        prettyWriter.println("FAIL: " + message);
        prettyWriter.flush();
    }

    public void logPass(String func, double buildTime, double memory, int its, int elements, String ongelijk) {
        prettyWriter.println("PASSED: " + func);
        printLine(prettyWriter, "PASSED: " + func);
        prettyWriter.println(elements + " elements");
        prettyWriter.println("Build in: " + buildTime + "s");
        prettyWriter.println("Used " + memory + "MB");
        prettyWriter.println("Ran " + its + " times");
        prettyWriter.println("Ongelijkmatigheid: " + ongelijk);
        numWriter.println("#" + func);
        numWriter.print("#");
        printLine(numWriter, func);
        numWriter.println(elements + " " + buildTime + " " + memory + " " + its + " " + ongelijk);
        flush();
    }

    public void logPass(String func, double buildTime, double runTime, double memory, int its, int elements) {
        prettyWriter.println("PASSED: " + func);
        printLine(prettyWriter, "PASSED: " + func);
        prettyWriter.println(elements + " elements");
        prettyWriter.println("Build in: " + buildTime + "s");
        prettyWriter.println("Ran in: " + runTime + "s");
        prettyWriter.println("Used " + memory + "MB");
        prettyWriter.println("Ran " + its + " times");
        numWriter.println("#" + func);
        numWriter.print("#");
        printLine(numWriter, func);
        numWriter.println(elements + " " + buildTime + " " + runTime + " " + memory + " " + its);
        flush();
    }

    public void logPass(String func, double runTime, int its, int elements) {
        prettyWriter.println("PASSED: " + func);
        printLine(prettyWriter, "PASSED: " + func);
        prettyWriter.println("Ran in: " + runTime + "s");
        prettyWriter.println("Ran " + its + " times");
        numWriter.println("#" + func);
        numWriter.print("#");
        printLine(numWriter, func);
        numWriter.println(elements + " " + runTime + " " + its);
        flush();
    }

    public void logPass(String func, double runTime, long navigations, long avgnavs, int its, int elements, String ongelijk) {
        prettyWriter.println("PASSED: " + func);
        printLine(prettyWriter, "PASSED: " + func);
        prettyWriter.println(elements + " elements");
        prettyWriter.println("Ran in: " + runTime + "s");
        prettyWriter.println("Ran " + its + " times");
        prettyWriter.println("Used " + navigations + " navigations");
        prettyWriter.println("Averaged to " + avgnavs + " navigations per contains");
        prettyWriter.println("Ongelijkmatigheid: " + ongelijk);
        numWriter.println("#" + func);
        numWriter.print("#");
        printLine(numWriter, func);
        numWriter.println(elements + " " + runTime + " " + navigations + " " + avgnavs + " " + its + " " + ongelijk);
        flush();
    }

    private void printLine(PrintWriter w, String s) {
        for (int i = 0; i < s.length(); i++) {
            w.print("=");
        }
        w.println();
    }

    public void close() {
        prettyWriter.close();
        numWriter.close();
    }

    private void flush() {
        prettyWriter.flush();
        numWriter.flush();
    }
}
