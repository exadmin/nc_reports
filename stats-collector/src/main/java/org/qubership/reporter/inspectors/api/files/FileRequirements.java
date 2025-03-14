package org.qubership.reporter.inspectors.api.files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class FileRequirements {
    String expectedFileName;
    Long expectedMinFileSizeInBytes;
    List<String> expSha256CheckSums;
    List<Pattern> regExpsCompiled;
    boolean allowTrim;

    /**
     * References to the file inside git-repository. The file path is relative to the root of the repository.
     * If "/" is used in the begining of the path - it will be trimmed.
     * @param relativeFNameStartingFromRootOfRepository
     */
    public FileRequirements(String relativeFNameStartingFromRootOfRepository) {
        while (relativeFNameStartingFromRootOfRepository.startsWith("/")) relativeFNameStartingFromRootOfRepository = relativeFNameStartingFromRootOfRepository.substring(1);

        this.expectedFileName = relativeFNameStartingFromRootOfRepository;
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

    /**
     * In case file content must contain some string it can be checked by providing reg-exp
     * @param regExpStr
     */
    public void addExpectationByRegExp(String regExpStr) {
        if (regExpsCompiled == null) {
            regExpsCompiled = new ArrayList<>();
        }

        Pattern pattern = Pattern.compile(regExpStr, Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
        regExpsCompiled.add(pattern);
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

    public boolean isAllowTrim() {
        return allowTrim;
    }

    /**
     * If true - then content of file will be read into memory and all white spaces will be removed on the edges.
     * @param allowTrim
     */
    public void setAllowTrim(boolean allowTrim) {
        this.allowTrim = allowTrim;
    }

    public List<Pattern> getRegExpressions() {
        if (regExpsCompiled == null) return null;
        return Collections.unmodifiableList(regExpsCompiled);
    }
}
