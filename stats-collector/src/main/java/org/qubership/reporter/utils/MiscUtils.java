package org.qubership.reporter.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MiscUtils {
    public static String getSha256FromInputStream(InputStream is) throws IOException, NoSuchAlgorithmException {
        byte[] buffer= new byte[8192];
        int count;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (BufferedInputStream bis = new BufferedInputStream(is)) {
            while ((count = bis.read(buffer)) > 0) {
                digest.update(buffer, 0, count);
            }
        }

        byte[] hash = digest.digest();
        return Base64.getEncoder().encodeToString(hash);
    }

    public static String createComplexKey(String repoName, String metricName) {
        return "[" + repoName + "]:[" + metricName + "]";
    }

    public static void sleep(int milliSecs) {
        try {
            Thread.sleep(milliSecs);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Returns X first existed elements from source list.
     * @param source Source list to get elements from
     * @param itemsPerChunk max number of elements to be returned
     * @return
     */
    public static <T> List<T> getChunk(List<T> source, int itemsPerChunk) {
        List<T> result = new ArrayList<>();
        for (int i=0; i<itemsPerChunk; i++) {
            if (i < source.size()) {
                result.add(source.get(i));
            } else  break;
        }

        return result;
    }
}
