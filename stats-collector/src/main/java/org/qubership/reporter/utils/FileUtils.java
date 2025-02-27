package org.qubership.reporter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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


    public static String getSHA256FromFile(String fileName) throws NoSuchAlgorithmException, IOException {
        try (InputStream is = new FileInputStream(fileName)) {
            return MiscUtils.getSha256FromInputStream(is);
        }
    }

    public static String readFile(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes);
    }
}
