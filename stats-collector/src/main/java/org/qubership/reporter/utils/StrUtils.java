package org.qubership.reporter.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public class StrUtils {
    public static String getSHA256FromString(String str) throws NoSuchAlgorithmException, IOException {
        try (InputStream is = new ByteArrayInputStream(str.getBytes())) {
            return MiscUtils.getSha256FromInputStream(is);
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Replaces content of the string by new one.
     * @param template where to replace
     * @param replacementPrefix start place in the template which will be replaced
     * @param replacementSuffix end place in the template which will be replaces
     * @param replaceWith the new content to be put into template
     * @return new string result
     */
    public static String replaceContent(String template, String replacementPrefix, String replacementSuffix, String replaceWith){
        int cutStart = template.indexOf(replacementPrefix);
        int cutEnd   = template.indexOf(replacementSuffix) + replacementSuffix.length();

        StringBuilder sb = new StringBuilder();
        sb.append(template.substring(0, cutStart));
        sb.append("\n");
        sb.append(replaceWith);
        sb.append("\n");
        sb.append(template.substring(cutEnd));

        return sb.toString();
    }

    /**
     * Returns original string or empty string for null value
     * @param str String
     * @return
     */
    public static String notNull(String str) {
        if (str == null) return "";
        return str;
    }
}
