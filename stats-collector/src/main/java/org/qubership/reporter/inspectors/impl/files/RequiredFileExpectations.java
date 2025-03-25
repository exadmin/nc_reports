package org.qubership.reporter.inspectors.impl.files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class RequiredFileExpectations {
    Long minFileSizeInBytes;
    List<String> expSha256CheckSums;
    List<Pattern> expectedContentRegExpCompiled;
    List<Pattern> restrictedContentRegExpCompiled;
    List<String> oneOfFilePaths;
    boolean allowTrim;

    public RequiredFileExpectations() {
    }

    /**
     * References to the file inside git-repository. The file path is relative to the root of the repository.
     * If "/" is used in the begining of the path - it will be trimmed.
     * @param relativeFNameStartingFromRootOfRepository
     */
    public RequiredFileExpectations(String relativeFNameStartingFromRootOfRepository) {
        addOneOfFilePath(relativeFNameStartingFromRootOfRepository);
    }

    public void setMinFileSizeInBytes(Long minFileSizeInBytes) {
        this.minFileSizeInBytes = minFileSizeInBytes;
    }

    public void addExpectedSha256CheckSum(String sha256) {
        if (expSha256CheckSums == null) {
            expSha256CheckSums = new ArrayList<>();
        }

        expSha256CheckSums.add(sha256);
    }

    /**
     * In case file content must contain some string it can be checked by providing regexp
     * @param regExpStr
     */
    public void addExpectationByRegExp(String regExpStr) {
        if (expectedContentRegExpCompiled == null) {
            expectedContentRegExpCompiled = new ArrayList<>();
        }

        Pattern pattern = Pattern.compile(regExpStr);
        expectedContentRegExpCompiled.add(pattern);
    }

    public void addRestrictedContentRegExp(String regExpStr, Integer compileFlags) {
        if (restrictedContentRegExpCompiled == null) {
            restrictedContentRegExpCompiled = new ArrayList<>();
        }

        if (compileFlags == null) compileFlags = 0;

        Pattern pattern = Pattern.compile(regExpStr, compileFlags);
        restrictedContentRegExpCompiled.add(pattern);
    }

    public Long getMinFileSizeInBytes() {
        return minFileSizeInBytes;
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

    public List<Pattern> getExpectedContentRegExps() {
        if (expectedContentRegExpCompiled == null) return null;
        return Collections.unmodifiableList(expectedContentRegExpCompiled);
    }

    public List<Pattern> getRestrictedContentRegExps() {
        if (restrictedContentRegExpCompiled == null) return null;
        return Collections.unmodifiableList(restrictedContentRegExpCompiled);
    }

    public void addOneOfFilePath(String filePath) {
        if (oneOfFilePaths == null) oneOfFilePaths = new ArrayList<>();

        while (filePath.startsWith("/")) filePath = filePath.substring(1);
        oneOfFilePaths.add(filePath);
    }

    public List<String> getOneOfFilePaths() {
        if (oneOfFilePaths == null) return null;
        return Collections.unmodifiableList(oneOfFilePaths);
    }
}
