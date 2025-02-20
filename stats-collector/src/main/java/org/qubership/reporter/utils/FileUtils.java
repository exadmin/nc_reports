package org.qubership.reporter.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class FileUtils {
    public interface FileAcceptor {
        boolean testFileByName(String shortFileName);
    }

    public static List<String> findAllFilesRecursively(String rootDirName, FileAcceptor fileAcceptor) {
        List<String> result = new ArrayList<>();

        _findAllFilesRecursively(result, new File(rootDirName), fileAcceptor);

        return result;
    }

    private static void _findAllFilesRecursively(List<String> collectedFiles, File dirToScan, FileAcceptor fileAcceptor) {
        TheLogger.debug("Searching additional configuration items at '"+ dirToScan + "'");
        File[] items = dirToScan.listFiles();
        if (items == null) return;

        for (File item : items) {
            TheLogger.debug("    Processing item '" + item + "'");
            if (item.isDirectory() && item.exists()) {
                _findAllFilesRecursively(collectedFiles, item, fileAcceptor);
            } else {
                String longFileName = item.toString();
                String shortFileName = item.getName();
                if (fileAcceptor.testFileByName(shortFileName)) collectedFiles.add(longFileName);
            }
        }
    }

    public static void saveToFile(String content, String fileToWriteInto) throws IOException {
        Path path = Paths.get(fileToWriteInto);
        Files.write(path, content.getBytes());
    }

    public static String getSHA256(String fileName) throws NoSuchAlgorithmException, IOException {
        byte[] buffer= new byte[8192];
        int count;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName))) {
            while ((count = bis.read(buffer)) > 0) {
                digest.update(buffer, 0, count);
            }
        }

        byte[] hash = digest.digest();
        return Base64.getEncoder().encodeToString(hash);
    }
}
