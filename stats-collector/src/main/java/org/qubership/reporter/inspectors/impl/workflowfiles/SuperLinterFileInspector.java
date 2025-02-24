package org.qubership.reporter.inspectors.impl.workflowfiles;

import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;

import java.util.List;

public class SuperLinterFileInspector extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "super-linter.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        sha256CheckSums.add("sDiYyYY527ERa/+6s7WBaoVt7PxZIXc2DtcIoQ5rR/E=");
    }

    @Override
    protected String getMetricName() {
        return "WF/SuperLinter";
    }
}
