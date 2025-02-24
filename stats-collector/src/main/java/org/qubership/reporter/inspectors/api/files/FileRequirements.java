package org.qubership.reporter.inspectors.api.files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileRequirements {
    String expectedFileName;
    Long expectedMinFileSizeInBytes;
    List<String> expSha256CheckSums;

    public FileRequirements(String expectedFileName) {
        this.expectedFileName = expectedFileName;
    }

    public void setExpectedMinFileSizeInBytes(Long expectedMinFileSizeInBytes) {
        this.expectedMinFileSizeInBytes = expectedMinFileSizeInBytes;
    }

    public void addExpectedSha256CheckSum(String sha256) {
        if (expSha256CheckSums == null) {
            expSha256CheckSums = new ArrayList<>();
        }

        expSha256CheckSums.add(sha256);
    }

    public String getExpectedFileName() {
        return expectedFileName;
    }

    public Long getExpectedMinFileSizeInBytes() {
        return expectedMinFileSizeInBytes;
    }

    public List<String> getExpSha256CheckSums() {
        if (expSha256CheckSums == null) return null;
        return Collections.unmodifiableList(expSha256CheckSums);
    }
}
