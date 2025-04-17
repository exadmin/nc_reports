package org.qubership.reporter.utils;

public class TokenHolder {
    private static String personalTokenValue;

    public static String getPersonalToken() {
        return personalTokenValue;
    }

    public static void setPersonalToken(String personalToken) {
        personalTokenValue = "";

        if (personalToken != null) {
            personalTokenValue = personalToken.trim();
        }
    }
}
