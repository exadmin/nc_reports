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
                boolean checkIsPassed = false;
                String actSha256;

                if (fReqs.isAllowTrim()) {
                    String wholeFileContent = FileUtils.readFile(file.toString());
                    wholeFileContent = wholeFileContent.trim();
                    actSha256 = StrUtils.getSHA256FromString(wholeFileContent);
                } else {
                    actSha256 = FileUtils.getSHA256FromFile(file.toString());
                }

                for (String expSha256 : fReqs.getExpSha256CheckSums()) {
                    if (actSha256.equals(expSha256)) {
                        checkIsPassed = true;
                        break;
                    }
                }

                if (!checkIsPassed) {
                    OneMetricResult result = new OneMetricResult(getMetricName(), ResultSeverity.WARN, "Unexpected content");
                    result.setHttpReference(fileURI);
                    return result;
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
}
