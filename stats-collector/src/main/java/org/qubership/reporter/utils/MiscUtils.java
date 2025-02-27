package org.qubership.reporter.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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
}
