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
}
