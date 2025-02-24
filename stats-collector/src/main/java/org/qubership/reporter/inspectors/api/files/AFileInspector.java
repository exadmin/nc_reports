package org.qubership.reporter.inspectors.api.files;

import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.InspectorResult;
import org.qubership.reporter.utils.FileUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public abstract class AFileInspector extends ARepositoryInspector {
    protected abstract FileRequirements getFileRequirements();

    @Override
    protected final InspectorResult inspectRepoFolder(String pathToRepository, List<Map<String, Object>> metaData) throws Exception {
        FileRequirements fReqs = getFileRequirements();

        File licenseFile = Paths.get(pathToRepository, fReqs.getExpectedFileName()).toFile();
        if (!licenseFile.exists()) return error("Not found");
        if (!licenseFile.isFile()) return error("Not found");

        // check sha256 sum
        if (fReqs.getExpSha256CheckSums() != null) {
            try {
                boolean checkIsPassed = false;
                String actSha256 = FileUtils.getSHA256(licenseFile.toString());

                for (String expSha256 : fReqs.getExpSha256CheckSums()) {
                    if (actSha256.equals(expSha256)) {
                        checkIsPassed = true;
                        break;
                    }
                }

                if (!checkIsPassed) return error("Unexpected content");
            } catch (Exception ex) {
                ex.printStackTrace();
                return error("Error: " + ex);
            }
        }

        // check minimum size of the file
        if (fReqs.getExpectedMinFileSizeInBytes() != null) {
            long actFileSize = licenseFile.length();
            if (actFileSize < fReqs.getExpectedMinFileSizeInBytes()) return error("Too small");
        }

        // seems all checks are passed
        return ok("");
    }
}
