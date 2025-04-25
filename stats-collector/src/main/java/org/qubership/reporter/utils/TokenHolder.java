package org.qubership.reporter.utils;

public class TokenHolder {
    private static String personalTokenValue;
    private static String dbFilePath;

    public static String getPersonalToken() {
        return personalTokenValue;
    }

    public static String getDbFilePath() {
        return dbFilePath;
    }

    public static void setPersonalToken(String personalToken) {
        personalTokenValue = "";

        if (personalToken != null) {
            personalTokenValue = personalToken.trim();
        }
    }

    public static void setDbFilePath(String dbFilePath) {
        TokenHolder.dbFilePath = dbFilePath;
    }
}
