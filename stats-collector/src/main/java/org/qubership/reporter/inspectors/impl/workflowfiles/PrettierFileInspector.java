package org.qubership.reporter.inspectors.impl.workflowfiles;

import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;

import java.util.List;

public class PrettierFileInspector extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "prettier.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        sha256CheckSums.add("f5i6XEAxaHH2oaCwWwwNvrJtOG2MOuwe9qUDats0mVs=");
    }

    @Override
    protected String getMetricName() {
        return "WF/Prettier";
    }
}
