package org.qubership.reporter.inspectors.api.files;

import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.inspectors.api.ResultSeverity;
import org.qubership.reporter.utils.FileUtils;
import org.qubership.reporter.utils.StrUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AFileInspector extends ARepositoryInspector {
    protected abstract FileRequirements getFileRequirements();

    protected String getReferenceToFileInGitHub(Map<String, Object> repoMetaData) {
        FileRequirements fReqs = getFileRequirements();

        String defBranch = (String) repoMetaData.get("default_branch");
        String url = (String) repoMetaData.get("html_url");
        return url + "/blob/" + defBranch + "/" + fReqs.getExpectedFileName();
    }

    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        FileRequirements fReqs = getFileRequirements();

        File file = Paths.get(pathToRepository, fReqs.getExpectedFileName()).toFile();
        if (!file.exists()) return error("");
        if (!file.isFile()) return error("");

        String fileURI = getReferenceToFileInGitHub(repoMetaData);

        // check sha256 sum
        if (fReqs.getExpSha256CheckSums() != null) {
            try {

                String actSha256;

                String wholeFileContent = FileUtils.readFile(file.toString());

                if (fReqs.isAllowTrim()) {
                    wholeFileContent = wholeFileContent.trim();
                    actSha256 = StrUtils.getSHA256FromString(wholeFileContent);
                } else {
                    actSha256 = FileUtils.getSHA256FromFile(file.toString());
                }

                // check file content for expected check-sum
                {
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

                String errMsg = checkForAllRegExpsOrReturnErrorMsg(wholeFileContent, fReqs.getRegExpressions());
                if (errMsg != null) {
                    return error(errMsg, fileURI);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return error("Error: " + ex);
            }
        }

        // check minimum size of the file
        if (fReqs.getExpectedMinFileSizeInBytes() != null) {
            long actFileSize = file.length();
            if (actFileSize < fReqs.getExpectedMinFileSizeInBytes()) {
                OneMetricResult result = new OneMetricResult(getMetricName(), ResultSeverity.ERROR, "Too small");
                result.setHttpReference(fileURI);
                return result;
            }
        }

        // seems all checks are passed
        return ok("");
    }

    private static String checkForAllRegExpsOrReturnErrorMsg(String content, List<Pattern> regExps) {
        if (regExps == null || regExps.isEmpty()) return null;

        for (Pattern pattern : regExps) {
            Matcher matcher = pattern.matcher(content);
            if (!matcher.find()) return "No required content is found by RegExp = '" + pattern + "'";
        }

        return null;
    }
}
