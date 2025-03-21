package org.qubership.reporter.inspectors.impl.files;

import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ResultSeverity;
import org.qubership.reporter.utils.FileUtils;
import org.qubership.reporter.utils.StrUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.qubership.reporter.utils.RepoUtils.getReferenceToFileInGitHub;

public abstract class AbstractRequiredFileInspector extends AbstractRepositoryInspector {
    protected abstract RequiredFileExpectations getFileRequirements();

    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) {
        RequiredFileExpectations fReqs = getFileRequirements();
        String filePathToAnalyze = null;
        File file = null;

        // if fReq contains multiple references to files - then select first existed one
        if (!fReqs.getOneOfFilePaths().isEmpty()) {
            for (String nextPath : fReqs.getOneOfFilePaths()) {
                file = Paths.get(pathToRepository, nextPath).toFile();
                if (file.exists() && file.isFile()) {
                    filePathToAnalyze = nextPath;
                    break;
                }
            }
        }

        if (filePathToAnalyze == null) return error("");

        // start analyzing
        String fileURI = getReferenceToFileInGitHub(repoMetaData, filePathToAnalyze);

        try {
            String wholeFileContent = FileUtils.readFile(file.toString());
            if (fReqs.isAllowTrim()) wholeFileContent = wholeFileContent.trim();

            // check sha256 sum
            if (fReqs.getExpSha256CheckSums() != null) {
                String actSha256 = StrUtils.getSHA256FromString(wholeFileContent);

                // check file content for expected check-sum
                boolean checkIsPassed = false;
                for (String expSha256 : fReqs.getExpSha256CheckSums()) {
                    if (actSha256.equals(expSha256)) {
                        checkIsPassed = true;
                        break;
                    }
                }
                if (!checkIsPassed) {
                    return warn("Unexpected content", fileURI);
                }
            }

            String errMsg = checkForExpectedContentOrReturnErrorMsg(wholeFileContent, fReqs.getExpectedContentRegExps(), repoMetaData);
            if (errMsg != null) {
                return error(errMsg, fileURI);
            }

            errMsg = checkForRestrictedContentAndReturnErrMsg(wholeFileContent, fReqs.getRestrictedContentRegExps(), repoMetaData);
            if (errMsg != null) {
                return secError(errMsg, fileURI);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return error("Error: " + ex);
        }

        // check minimum size of the file
        if (fReqs.getMinFileSizeInBytes() != null) {
            long actFileSize = file.length();
            if (actFileSize < fReqs.getMinFileSizeInBytes()) {
                OneMetricResult result = new OneMetricResult(getMetric(), ResultSeverity.ERROR, "Too small");
                result.setHttpReference(fileURI);
                return result;
            }
        }

        // seems all checks are passed
        return ok("");
    }

    protected String checkForExpectedContentOrReturnErrorMsg(String content, List<Pattern> regExps, Map<String, Object> repoMetaData) {
        if (regExps == null || regExps.isEmpty()) return null;

        for (Pattern pattern : regExps) {
            Matcher matcher = pattern.matcher(content);
            if (!matcher.find()) return "No required content is found by RegExp = '" + pattern + "'";
        }

        return null;
    }

    protected String checkForRestrictedContentAndReturnErrMsg(String content, List<Pattern> regExps, Map<String, Object> repoMetaData) {
        if (regExps == null || regExps.isEmpty()) return null;

        for (Pattern pattern : regExps) {
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) return "Restricted pattern: [" + pattern + "]";
        }

        return null;
    }
}
