package org.qubership.reporter.inspectors.impl.workflowfiles;

import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;

import java.util.List;

public class PRConventionalCommitsFileInspector extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "pr-conventional-commits.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        sha256CheckSums.add("0Z+w5ISm3VQ0ldUBInV8VxwaZlIE9Qh7tQlCaqD2oFQ=");
    }

    @Override
    protected String getMetricName() {
        return "WF/ConvCommits";
    }
}
