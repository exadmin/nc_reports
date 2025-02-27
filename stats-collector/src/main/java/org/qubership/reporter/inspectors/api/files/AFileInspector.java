package org.qubership.reporter.inspectors.api.files;

import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.inspectors.api.ResultSeverity;
import org.qubership.reporter.utils.FileUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public abstract class AFileInspector extends ARepositoryInspector {
    protected abstract FileRequirements getFileRequirements();

    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        FileRequirements fReqs = getFileRequirements();

        File file = Paths.get(pathToRepository, fReqs.getExpectedFileName()).toFile();
        if (!file.exists()) return error("Not found");
        if (!file.isFile()) return error("Not found");

        String defBranch = (String) repoMetaData.get("default_branch");
        String url = (String) repoMetaData.get("html_url");
        String fileURI = url + "/blob/" + defBranch + "/" + file.getName();

        // check sha256 sum
        if (fReqs.getExpSha256CheckSums() != null) {
            try {
                boolean checkIsPassed = false;
                String actSha256 = FileUtils.getSHA256(file.toString());

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
