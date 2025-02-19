package org.qubership.reporter.utils;

public class TheLogger {
    public static void error(String msg, Exception ... ex) {
        printExceptionStacktrace(ex);

        System.out.println("ERROR: " + msg);
    }

    public static void warn(String msg, Exception ... ex) {
        printExceptionStacktrace(ex);
        System.out.println("WARN : " + msg);
    }

    public static void debug(String msg) {
        System.out.println("DEBUG: " + msg);
    }

    private static void printExceptionStacktrace(Exception ... ex) {
        if (ex == null) return;
        for (Exception next : ex) next.printStackTrace();
    }
}
