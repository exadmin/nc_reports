package org.qubership.reporter.inspectors.api.files;

import java.util.ArrayList;
import java.util.List;

public abstract class AGithubWorkflowFileInspector extends AFileInspector {
    protected abstract String getShortFileNamePlacedInGitHubWorkflowFolder();

    protected abstract void addExpectedSha256Sums(List<String> sha256CheckSums);

    protected abstract void addExpectedContentRegExpressions(List<String> strings);

    @Override
    protected final FileRequirements getFileRequirements() {
        FileRequirements fReqs = new FileRequirements(".github/workflows/" + getShortFileNamePlacedInGitHubWorkflowFolder());

        // add sha256 sums - if set
        List<String> expSha256Sums = new ArrayList<>();
        addExpectedSha256Sums(expSha256Sums);
        if (!expSha256Sums.isEmpty()) {
            for (String next : expSha256Sums) {
                fReqs.addExpectedSha256CheckSum(next);
            }
        }

        List<String> lines = new ArrayList<>();
        addExpectedContentRegExpressions(lines);
        if (!lines.isEmpty()) {
            for (String next : lines) {
                fReqs.addExpectationByRegExp(next);
            }
        }

        return fReqs;
    }

    @Override
    protected final String getMetricDescriptionInMDFormat() {
        return "Checks if './github/workflows/" + getShortFileNamePlacedInGitHubWorkflowFolder() + "' file exists and have expected content";
    }
}
